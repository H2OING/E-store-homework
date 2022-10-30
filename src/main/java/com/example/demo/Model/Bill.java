package com.example.demo.Model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Bill")
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="IdBi")
    @Setter(value = AccessLevel.NONE)
    private long idBi;
    //@NotNull
    @Column(name = "payment_method")
    private Payment_Method paymentMethod;
    @Column(name = "payment_date")
    @NotNull
    private Date paymentDate;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "invoice_number")
    private long invoiceNumber;
   
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "idCO")
    private Customer_Order order;
    
    public Bill(Payment_Method paymentMethod,Date paymentDate,long invoiceNumber,Customer_Order order) {
    	this.paymentMethod = paymentMethod;
    	this.paymentDate = paymentDate;
    	this.invoiceNumber = invoiceNumber;
    	this.order = order;
    }
}
