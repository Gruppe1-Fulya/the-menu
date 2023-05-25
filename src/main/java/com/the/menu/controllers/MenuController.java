package com.the.menu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.the.menu.repositories.CategoryRepository;
import com.the.menu.repositories.ItemRepository;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private CategoryRepository categories;

    @Autowired
    private ItemRepository items;

    @GetMapping
    public String getMenu(Model model) {
        model.addAttribute("categories", categories.findAll());
        return "menu";
    }

    @GetMapping("/{id}")
    public String getCategory(@PathVariable(name = "id", required = true) int id, Model model) {
        var category = categories.findById(id).get();
        var categoryItems = items.findAllByCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("items", categoryItems);
        return "category";
    }
}
