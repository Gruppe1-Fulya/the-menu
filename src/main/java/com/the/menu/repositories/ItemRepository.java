package com.the.menu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.the.menu.models.Category;
import com.the.menu.models.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByCategory(Category category);

    Optional<Item> findByName(String name);
}
