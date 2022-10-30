package com.example.demo.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@Table(name = "Cart")
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="IdCart")
    @Setter(value = AccessLevel.NONE)
    private long idCart;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "is_empty")
    private boolean isEmpty;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "IdUser")
    private Web_User webUser;

    @OneToMany(mappedBy = "cart")
    @ToString.Exclude
    private Collection<Customer_Order> orders = new ArrayList<Customer_Order>();
    
    @ManyToMany(mappedBy = "carts")
    private Collection<Product> products = new ArrayList<Product>();

    public void addProduct(Product product) {
    	products.add(product);
    }

    public Cart (BigDecimal total, boolean isEmpty,Web_User webUser,Collection<Product> products) {
    	this.total = total;
    	this.isEmpty = isEmpty;
    	this.webUser = webUser;
    	this.products = products;
    	
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public String toStringProducts(){
        StringBuilder builder = new StringBuilder();
        for (Product product:
             this.products) {
            builder.append(String.format("%s: %.2f€\n", product.getName(), product.getPrice()));
        }
        builder.append(String.format("Total: %.2f€", this.total));
        return builder.toString();
    }
}
