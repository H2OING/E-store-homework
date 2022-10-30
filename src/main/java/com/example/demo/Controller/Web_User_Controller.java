package com.example.demo.Controller;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Product;
import com.example.demo.Model.Role;
import com.example.demo.Model.Web_User;
import com.example.demo.Service.Cart_Service;
import com.example.demo.Service.Category_Service;
import com.example.demo.Service.Web_User_Service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;

@Controller
public class Web_User_Controller {
    @Autowired
    Web_User_Service webUserService;
    @Autowired
    Category_Service categoryService; //For navigation bar
    @Autowired
    Cart_Service cartService;

    @GetMapping("/admin/webuser")
    public String getAllWebUsers(Model model){
        model.addAttribute("webUser", webUserService.getAllWebUsers());
        return "webUser-show-all";
    }

    @GetMapping(value = "/admin/webuser/{id}")
    public String getWebUser(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("webUser", webUserService.getWebUserById(id));
        return "webUser";
    }


    @GetMapping(value = "/user/accountDetails")
    public String getLoggedInUserDetails(Model model){
        model.addAttribute("webUser", webUserService.getLoggedInWebUser());
        model.addAttribute("categories", categoryService.getAllCategories());
        if(webUserService.isLoggedIn() && webUserService.isCustomer()){
            Collection<Product> products = webUserService.getLoggedInWebUser().getCart().getProducts();
            model.addAttribute("cartProducts", products);
        }
        return "accountDetails";
    }


    @GetMapping(value = "/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("webUser", new Web_User());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "registration";
    }

    @PostMapping(value = "/register")
    public String createWebUser(@ModelAttribute("webUser") @Valid Web_User webUser, BindingResult result){
        try {
            if (result.hasErrors()) {
                return "registration";
            } else {
                webUserService.createWebUser(webUser);
                return "redirect:/register?success";
            }
        } catch (Exception e){
            return "redirect:/register?error";
        }
    }

    @PutMapping(value = "PlaceholderMapping10")
    public String updateWebUser(@PathVariable(name = "id") Long id, @Valid Web_User webUser){
        webUserService.updateWebUser(id, webUser);
        return "updateWebUser";
    }

    @GetMapping("/admin/webuser/update/{id}")
    public String updateWebUser(@PathVariable(name = "id") Long id, Model model){
        try{
            model.addAttribute("webUser", webUserService.getWebUserById(id));
            return "webUser-update";
        }
        catch(Exception e){
            return "error";
        }
    }
    @PostMapping("/admin/webuser/update/{id}")
    public String postUpdateWebUser(@PathVariable(name = "id") Long id,@Valid Web_User webUser, BindingResult result, Model model){
        
        if(!result.hasErrors()){
            if(webUserService.updateWebUser(id, webUser))
                //return "redirect:/admin/webuser/" + id;
                return "redirect:/admin/webuser";
            else
                return "redirect:/error";
        }
        else{
            model.addAttribute("webUser", webUserService.getWebUserById(id));
            return"webUser-update";
        }
    }

    @RequestMapping(value = "/admin/webuser/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteWebUser(@PathVariable(name = "id") Long id, Model model){
        Web_User user = webUserService.getWebUserById(id);
        
        cartService.deleteCart(user.getCart().getIdCart());
        webUserService.deleteWebUser(id);
            //model.addAttribute("webUser", webUserService.getAllWebUsers());
        return "redirect:/admin/webuser";
    }
}
