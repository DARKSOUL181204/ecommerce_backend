package com.Developer.DreamShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


//@Getter
//@Setter
//@RequiredArgsConstructor
//@NoArgsConstructor
//@Entity


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor   // optional, helps create Category(id, name, products)
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
