package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Service.Cart_Service;
import com.example.demo.Service.Category_Service;
import com.example.demo.Service.Product_Service;
import com.example.demo.Service.Web_User_Service;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

@Controller
public class Product_Controller {

    @Autowired
    Product_Service productService;
    @Autowired
    Category_Service categoryService;

    @Autowired
    Cart_Service cartService;

    @Autowired
    Web_User_Service webUserService;

    //@GetMapping("/home")
    @GetMapping
    public String getAllProducts(Model model){

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        if(webUserService.isLoggedIn()){
            Collection<Product> products = webUserService.getLoggedInWebUser().getCart().getProducts();
            model.addAttribute("cartProducts", products);
        }
        return "home";
    }

    @GetMapping("/admin/product")
    public String selectAllProductsAdmin(Model model){
        model.addAttribute("object", productService.getAllProducts());
        return "product-show-all";
    }

    @GetMapping(value = "/product/{id}")
    public String getProduct(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        if(webUserService.isLoggedIn() && webUserService.isCustomer()){
            Collection<Product> products = webUserService.getLoggedInWebUser().getCart().getProducts();
            model.addAttribute("cartProducts", products);
        }
        return "product";
    }

    @RequestMapping(value = "/image/{productId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProductImage(@PathVariable("productId") Long productId) throws IOException {
        byte[] imageContent = productService.getProductById(productId).getPicture();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @GetMapping ("/admin/product/add")
    public String getAddProduct (Model model){
        Collection<Category> listCategory = categoryService.getAllCategories(); 
        
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", listCategory);
        return "product-create";
    }


    @PostMapping ("/admin/product/add")
    public String postAddProduct (@Valid Product product, BindingResult result, @ModelAttribute("product") Product product2, Model model)
    {
        if (!result.hasErrors()) {
            if(productService.createProduct(product))
            return "redirect:/admin/product";
            else 
                return "redirect:/error";
        }
        else {
            Collection<Category> listCategory = categoryService.getAllCategories(); 
            model.addAttribute("listCategory", listCategory);
            return"product-create";
        }
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable(name = "id") Long id, Model model){
        try{
            Collection<Category> listCategory = categoryService.getAllCategories(); 
            
            model.addAttribute("product", productService.getProductById(id));
            model.addAttribute("listCategory", listCategory);
            model.addAttribute("pictureFile", null);

            return "product-update";
        }
        catch(Exception e){
            return "error";
        }
    }

    @PostMapping(value = "/admin/product/update/{id}")
    public String updateProduct(@PathVariable(name = "id") Long id,@Valid Product product, BindingResult result, Model model){
        
        if(!result.hasErrors()){
            if(productService.updateProduct(id, product)){
                return "redirect:/admin/product";}
            else{
                return "redirect:/error";}
        }
        else{
            model.addAttribute("product", productService.getProductById(id));
            return"product-update";
        }
            
    }

    @RequestMapping(value = "/admin/product/delete/{id}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteProduct(@PathVariable(name = "id") Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }

}
