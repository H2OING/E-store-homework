package com.example.demo.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "Customer_Order")
@Entity
public class Customer_Order {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="IdCO")
    @Setter(value = AccessLevel.NONE)
    private long idCO;
    @Column(name = "status")
    private Order_Status status;
    @Column(name = "ordered_date")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date orderedDate;
    @Column(name = "shipped_date")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date shippedDate;

    @Column(name = "ordered_products")
    private String orderedProducts;
    /*
    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "idCart")
    @Cascade(CascadeType.ALL)
    private Cart cart;
     */
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @Cascade(CascadeType.ALL)
    private Cart cart;

    @OneToOne(mappedBy = "order")
    private Bill bill;
    
    public Customer_Order(Order_Status status,Date orderedDate,Date shippedDate, String orderedProducts, Cart cart) {
    	this.status = status;
    	this.orderedDate = orderedDate;
    	this.shippedDate = shippedDate;
        this.orderedProducts = orderedProducts;
    	this.cart = cart;
    }
}


