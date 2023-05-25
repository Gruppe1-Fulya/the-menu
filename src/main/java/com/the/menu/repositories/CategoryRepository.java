package com.the.menu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.the.menu.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
