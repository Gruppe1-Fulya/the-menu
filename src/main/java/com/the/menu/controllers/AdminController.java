package com.the.menu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.the.menu.models.Category;
import com.the.menu.models.Item;
import com.the.menu.repositories.CategoryRepository;
import com.the.menu.repositories.ItemRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public String getAdminPage(Model model) {
        var categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin";
    }

    @GetMapping("/{id}")
    public String getCategoryPage(@PathVariable(name = "id", required = true) int id, Model model) {
        var category = categoryRepository.findById(id).get();
        var items = itemRepository.findAllByCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("items", items);
        return "admin_category";
    }

    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String createItem(@PathVariable(name = "id", required = true) int id, @ModelAttribute Item item) {
        var category = categoryRepository.findById(id).get();
        item.setCategory(category);
        itemRepository.save(item);
        return "redirect:/admin/" + id;
    }

    @DeleteMapping("/category/{id}")
    public String deleteCategory(@PathVariable(name = "id", required = true) int id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable(name = "id", required = true) int id) {
        var item = itemRepository.findById(id).get();
        var category = item.getCategory();
        itemRepository.deleteById(id);
        return "redirect:/admin/" + category.getId();
    }

    @PatchMapping("/category/{id}")
    public String updateCategory(@PathVariable(name = "id", required = true) int id,
            @ModelAttribute Category category) {
        var original = categoryRepository.findById(id).get();
        original.setName(category.getName());
        categoryRepository.save(original);
        return "redirect:/admin/" + id;
    }

    @PatchMapping("/item/{id}")
    public String updateItem(@PathVariable(name = "id", required = true) int id, @ModelAttribute Item item) {
        var original = itemRepository.findById(id).get();
        var category = original.getCategory();
        original.setName(item.getName());
        original.setPrice(item.getPrice());
        itemRepository.save(original);
        return "redirect:/admin/" + category.getId();
    }
}
