package com.Developer.DreamShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;



    // there can be many cartItems can be associated with a same Product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // there can me many cart items in a single cart
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;


    public void setTotalPrice (){
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }

}
