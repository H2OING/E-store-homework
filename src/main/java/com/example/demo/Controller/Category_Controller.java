package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Service.Category_Service;
import com.example.demo.Service.Product_Service;

import javax.validation.Valid;

import com.example.demo.Service.Web_User_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
public class Category_Controller {

    @Autowired
    Category_Service categoryService;
    @Autowired
    Product_Service productService;
    @Autowired
    Web_User_Service webUserService;

    @GetMapping("/categories")
    public String getAllCategories(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        if(webUserService.isLoggedIn() && webUserService.isCustomer()){
            Collection<Product> products = webUserService.getLoggedInWebUser().getCart().getProducts();
            model.addAttribute("cartProducts", products);
        }
        return "categories";
    }

    @GetMapping(value = "/category/{id}")
    public String getCategory(@PathVariable(name = "id") Long id, Model model){
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("products", productService.getProductsByCategoryId(category));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category";
    }

    @GetMapping (value = "/admin/categories")
    public String adminGetCategories(Model model)
    {
        model.addAttribute("object", categoryService.getAllCategories());
		return "category-show-all";
    }

    @GetMapping ("/admin/category/add")
public String getAddCategory (Category category){
	return "category-create";
}


@PostMapping ("/admin/category/add")
public String postAddCategory (@Valid Category category, BindingResult result)
{
	if (!result.hasErrors()) {
		if(categoryService.createCategory(category))
		return "redirect:/admin/categories";
		else 
			return "redirect:/error";
	}
	else {
		return"category-create";
	}
}

	

    /*
    @PutMapping(value = "/admin/category/update/{id}")
    public String updateCategory(@PathVariable(name = "id") Long id, @Valid Category category){
        categoryService.updateCategory(id, category);
        return "updateCategory";
    }
    */

    @GetMapping("/admin/category/update/{id}")
    public String updatecategory(@PathVariable(name = "id") Long id, Model model){
        try{
            model.addAttribute("category", categoryService.getCategoryById(id));
            return "category-update";
        }
        catch(Exception e){
            return "error";
        }
    }
    @PostMapping("/admin/category/update/{id}")
    public String postUpdatecategory(@PathVariable(name = "id") Long id,@Valid Category category, BindingResult result, Model model, RedirectAttributes redirAttrs){
        
        if(!result.hasErrors()){
            if(categoryService.updateCategory(id, category)){
                //return "redirect:/admin/category/" + id;
                redirAttrs.addFlashAttribute("success", "Category updated!");
                return "redirect:/admin/categories";}
            else{
                return "redirect:/error";}
        }
        else{
            model.addAttribute("category", categoryService.getCategoryById(id));
            return"category-update";
        }

    }

    @RequestMapping(value = "/admin/category/delete/{id}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCategory(@PathVariable(name = "id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
