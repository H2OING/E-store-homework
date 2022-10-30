package com.example.demo.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Category")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="IdCat")
    @Setter(value = AccessLevel.NONE)
    private long idCat;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private Collection<Product> products = new ArrayList<Product>();

    public Category (String name,String description) {
    	this.name = name;
    	this.description = description;
    }
 

}
