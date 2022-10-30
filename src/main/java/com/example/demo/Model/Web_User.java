package com.example.demo.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@Table(name = "Web_User")
@Entity
public class Web_User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="IdUser")
    @Setter(value = AccessLevel.NONE)
    private long idUser;
    @Column(name = "name")
    @NotNull
    @Pattern(regexp = "[A-ZŽĶĻŅČĢŠĪĀĒŪ]{1}[a-zžšķļņģčīāūē\\s]+", message = "Invalid input for name")
    @Size(min = 2, max = 30 ,message = "Invalid input length for name")
    private String name;
    @Column(name = "surname")
    @NotNull
    @Pattern(regexp = "[A-ZŽĶĻŅČĢŠĪĀĒŪ]{1}[a-zžšķļņģčīāūē\\s]+", message = "Invalid input for surname")
    @Size(min = 2, max = 30 ,message = "Invalid input length for surname")
    private String surname;
    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;
    @Column(name = "address")
    @NotNull
    private String address;



    @Column(name = "email", unique = true)
    @Pattern (regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,10}$", message = "Invalid input for email")
    @Size(min=5, max=30)
    @NotNull
    private String email;
    
   // private String session;

    @Column(name = "password")
    //@Pattern (regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    //atleast 1 capital letter, 1 number and 1 speacial character (Password must be atleast 8 character)
    @NotNull
    private String password;
    @Column(name = "role")
    //@NotNull
    private Role role;
    @OneToOne(mappedBy = "webUser")
    private Cart cart;

    public Web_User (String name, String surname, String email, String phoneNumber, String address, String password, Role role) {
        this.name = name;
        this.surname = surname;
    	  this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    	this.password = password;
    	this.role = role;
    }

    //for the hash
    public void setPasswordHashed(String password){
      setPassword(new BCryptPasswordEncoder().encode(password));
    }

    //check if raw password matches with hashed
    public boolean checkPassword(String password){
      return new BCryptPasswordEncoder().matches(password, this.password);
    }
}
