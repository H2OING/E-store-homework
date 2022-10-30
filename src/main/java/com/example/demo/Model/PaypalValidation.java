package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PaypalValidation {

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "Invalid input for email")
    @Size(min = 5, max = 30)
    private String email;

    public PaypalValidation(){

    }
}
