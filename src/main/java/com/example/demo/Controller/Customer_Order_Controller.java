package com.example.demo.Controller;

import com.example.demo.Model.Customer_Order;
import com.example.demo.Model.Order_Status;
import com.example.demo.Model.Web_User;
import com.example.demo.Service.Customer_Order_Service;

import javax.validation.Valid;

import com.example.demo.Service.Web_User_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class Customer_Order_Controller {

    @Autowired
    Customer_Order_Service customerOrderService;
    @Autowired
    Web_User_Service webUserService;

    @GetMapping("PlaceholderMapping3")
    public String getAllCustomerOrders(Model model){
        model.addAttribute("customerOrders", customerOrderService.getAllCustomerOrders());
        return "customerOrders";
    }

    @GetMapping(value = "asdasdasdasdasd")
    public String getCustomerOrder(Model model){
        //model.addAttribute("customerOrder", customerOrderService.getCustomerOrderByCart(webUserService.getLoggedInWebUser().getCart()));
        return "customerOrder";
    }

    @PostMapping(value = "/user/createOrder")
    public String createCustomerOrder(){
        Web_User loggedInUser = webUserService.getLoggedInWebUser();
        Customer_Order order = new Customer_Order(Order_Status.HOLD, new Date(), null, loggedInUser.getCart().toStringProducts(), loggedInUser.getCart());
        customerOrderService.createCustomerOrder(order);
        return "redirect:/user/checkout";
    }

    @PutMapping(value = "PlaceholderMapping5")
    public String updateCustomerOrder(@PathVariable(name = "id") Long id,@Valid Customer_Order customerOrder){
        customerOrderService.updateCustomerOrder(id, customerOrder);
        return "updateCustomerOrder";
    }

    @RequestMapping(value = "PlaceholderMapping6", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCustomerOrder(@PathVariable(name = "id") Long id){
        customerOrderService.deleteCustomerOrder(id);
        return "deleteCustomerOrder";
    }
}
