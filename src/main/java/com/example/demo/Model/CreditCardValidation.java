package com.example.demo.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Getter
@Setter
public class CreditCardValidation {

    @Pattern(regexp = "^5[1-5][0-9]{14}|^(222[1-9]|22[3-9]\\\\d|2[3-6]\\\\d{2}|27[0-1]\\\\d|2720)[0-9]{12}$", message = "Invalid input for credit card number")
    private String number;

    @Pattern(regexp = "^[\\p{Lu}\\p{M}][\\p{L}\\p{M},.'-]+(?: [\\p{L}\\p{M},.'-]+)*$", message = "Invalid input for name")
    private String ownerName;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$", message = "Invalid input for date")
    private String date;

    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid input for cvv code")
    private String cvvCode;

    private boolean flag;
    private String flagString;

    public CreditCardValidation(String number, String ownerName, String date, String cvvCode) {
        this.number = number;
        this.ownerName = ownerName;
        this.date = date;
        this.cvvCode = cvvCode;
    }

    public CreditCardValidation() {
        flag = true;
    }

    public boolean getFlag(){
        return flag;
    }

    public void setFlagString(String value){
        this.flagString = value;
        if(value.equalsIgnoreCase("on")){
            this.flag = true;
        } else if(value.equalsIgnoreCase("off")){
            this.flag = false;
        }
        System.out.println(flag);
    }

    public String getFlagString(){
        return this.flagString;
    }


}
