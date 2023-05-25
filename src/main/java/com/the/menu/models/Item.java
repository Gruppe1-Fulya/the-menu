package com.the.menu.models;

import java.io.Serializable;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = Category.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    public Item() {
    }

    public Item(Category category, String name, double price) {
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
