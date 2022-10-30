package com.example.demo.Controller;

import com.example.demo.Model.*;
import com.example.demo.Service.*;

import javax.validation.Valid;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class Bill_Controller {
    @Autowired
    Bill_Service billService;
    @Autowired
    Web_User_Service webUserService;
    @Autowired
    Customer_Order_Service customerOrderService;
    @Autowired
    Product_Service productService;
    @Autowired
    Cart_Service cartService;

    CreditCardValidation creditCardValidationGlobal = new CreditCardValidation();

    @GetMapping("/admin/bills")
    public String getAllBills(Model model){
        model.addAttribute("bills", billService.getAllBills());
        return "bills";
    }
    @GetMapping(value = "/user/checkout")
    public String getBillingForm(Model model){
        Web_User user = webUserService.getLoggedInWebUser();
        model.addAttribute("order", new Customer_Order(Order_Status.HOLD, new Date(), null, user.getCart().toStringProducts(), user.getCart()));
        model.addAttribute("cart", webUserService.getLoggedInWebUser().getCart());
        model.addAttribute("creditCard", creditCardValidationGlobal);
        model.addAttribute("paypal", new PaypalValidation());
        return "checkout";
    }

    @GetMapping(value = "/admin/bill/{id}")
    public String getBill(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("bill", billService.getBillById(id));
        return "bill";
    }

    @PostMapping("/user/checkout")
    public String createCustomerOrderBill(Model model, @ModelAttribute("cart") Cart cart, @ModelAttribute("bill") Bill bill,
                                          @ModelAttribute("creditCard") @Valid CreditCardValidation creditCardValidation, BindingResult resultCreditCard,
                                          @ModelAttribute("paypal") @Valid PaypalValidation paypalValidation, BindingResult resultPaypal,
                                          RedirectAttributes redirectAttributes){
        Web_User loggedInUser = webUserService.getLoggedInWebUser();
        if(resultCreditCard.hasErrors() || resultPaypal.hasErrors()){
            model.addAttribute("order", new Customer_Order(Order_Status.HOLD, new Date(), null, loggedInUser.getCart().toStringProducts(), loggedInUser.getCart()));
            model.addAttribute("cart", loggedInUser.getCart());
            return "checkout";
        } else {

            Collection<Product> orderProducts =loggedInUser.getCart().getProducts();
            HashMap<Long, Integer> productOrderQuantities = new HashMap<>();
            countProductAmount(productOrderQuantities, orderProducts);
            Pair<Boolean, String> flagAndErrorMessage = notEnoughInStock(productOrderQuantities);

            if(flagAndErrorMessage.getValue0()){
                redirectAttributes.addFlashAttribute("errorMessage", flagAndErrorMessage.getValue1());
                return "redirect:/user/cart?error";
            } else{
                subtractInStock(productOrderQuantities);
            }

            Customer_Order order = new Customer_Order(Order_Status.NEW, new Date(), null, loggedInUser.getCart().toStringProducts(), cart);

            bill.setOrder(order);
            bill.setPaymentDate(new Date());
            if(creditCardValidation.getFlag()){
                bill.setPaymentMethod(Payment_Method.CREDIT_CARD);
            } else{
                bill.setPaymentMethod(Payment_Method.PAYPAL);
            }

            order.setBill(bill);
            billService.createBill(bill);

            customerOrderService.createCustomerOrder(order);
            clearCart(loggedInUser);
            return "redirect:/";
        }
    }

    private void countProductAmount(HashMap<Long, Integer> productOrderQuantities, Collection<Product> orderProducts){
        for (Product product:
                orderProducts) {
            if(!productOrderQuantities.containsKey(product.getIdP())){
                productOrderQuantities.put(product.getIdP(), 1);
                continue;
            }
            productOrderQuantities.put(product.getIdP(), productOrderQuantities.get(product.getIdP()) + 1);
        }
    }

    private Pair<Boolean, String> notEnoughInStock(HashMap<Long, Integer> productOrderQuantities){
        for (Map.Entry<Long, Integer> entry:
             productOrderQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer amount = entry.getValue();
            Product product = productService.getProductById(productId);
            if(product.getQuantity() < amount){
                return Pair.with(true, String.format("You are trying to order %d amount of %s, when there is only %d in stock", amount, product.getName(), product.getQuantity()));
            }
        }
        return Pair.with(false, "");
    }

    private void subtractInStock(HashMap<Long, Integer> productOrderQuantities){
        for (Map.Entry<Long, Integer> entry:
             productOrderQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer amount = entry.getValue();
            Product product = productService.getProductById(productId);
            product.setQuantity(product.getQuantity() - amount);
        }
    }

    private void clearCart(Web_User loggedInUser){
        Cart cart = loggedInUser.getCart();
        List<Product> cartProducts = new ArrayList<>(cart.getProducts());
        for (Product product:
             cartProducts) {
            cart.removeProduct(product);
            cart.setTotal(cart.getTotal().subtract(product.getPrice()));
            product.removeCart(cart);
            productService.updateProduct(product.getIdP(), product);
        }
        cartService.updateCart(cart.getIdCart(), cart);

    }

    @PostMapping(value = "/admin/createBill")
    public String createBill(@Valid Bill bill, BindingResult result){
    	if(!result.hasErrors()) {
        billService.createBill(bill);
        return "redirect:/bills";
    	}else {
    		return "error-page";
    	}
    }
    
    @GetMapping(value = "/admin/createBill")
    public String getCreatedBill(Model model, Bill bill) {
    	Bill bills = new Bill();
    	model.addAttribute("createdBill", bills);
    	return "createBill";
    }

    @PutMapping(value = "/admin/updateBill/{id}")
    public String updateBill(@PathVariable(name = "id") Long id,@Valid Bill bill, BindingResult result){
        if(!result.hasErrors()) {
    	billService.updateBill(id, bill);
        return "redirect:/bills";
        }else {
        	return "error-page";
        }
    }
    
    @GetMapping(value = "/admin/updateBill/{id}")
    public String getUpdatedBill(@PathVariable(name = "id") Long id,Model models) {
    	models.addAttribute("updatedBill", billService.getBillById(id));
		return "updateBill";
    }

    @RequestMapping(value = "/admin/deleteBill/{id}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteBill(@PathVariable(name = "id") Long id, Model model){
    	try {
        model.addAttribute("DeletedBill", billService.deleteBill(id));
        return "bills";
    	} catch (Exception e) {
    		
    	return "redirect:/error-page";
		
    	}
    }
}
