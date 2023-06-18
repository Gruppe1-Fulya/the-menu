package com.the.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.the.menu.models.Category;
import com.the.menu.models.Item;
import com.the.menu.repositories.CategoryRepository;
import com.the.menu.repositories.ItemRepository;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin")
class MenuApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    /*
     * Create a category with name 'başlangıç'
     */
    @Test
    void testCreateCategory() throws Exception {
        String categoryName = "başlangıç";

        Optional<Category> category = categoryRepository.findByName(categoryName);
        assertEquals(false, category.isPresent(), "Category should not exists");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                .param("name", categoryName))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        category = categoryRepository.findByName(categoryName);
        assertEquals(true, category.isPresent(), "Category should exists");
    }

    /*
     * Create an item with name 'yayla' which belongs to 'çorba' category
     */
    @Test
    void testCreateItem() throws Exception {
        String itemName = "yayla";
        int itemPrice = 80;

        Optional<Category> category = categoryRepository.findByName("çorba");
        assertEquals(true, category.isPresent(), "Category should exists");

        int categoryId = category.get().getId();
        Optional<Item> item = itemRepository.findByName(itemName);
        assertEquals(false, item.isPresent(), "Item should not exists");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/" + categoryId)
                .param("id", Integer.toString(categoryId))
                .param("name", itemName)
                .param("price", Integer.toString(itemPrice)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/" + categoryId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/" + categoryId));

        item = itemRepository.findByName(itemName);
        assertEquals(true, item.isPresent(), "Item should exists");
    }

    /*
     * Delete category 'tatlı'
     */
    @Test
    void testDeleteCategory() throws Exception {
        String categoryName = "tatlı";

        Optional<Category> category = categoryRepository.findByName(categoryName);
        assertEquals(true, category.isPresent(), "Category should exists");
        int categoryId = category.get().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/category/" + categoryId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        category = categoryRepository.findByName(categoryName);
        assertEquals(false, category.isPresent(), "Category should not exists");
    }

    /*
     * Delete item 'domates' which belongs to 'çorba' category
     */
    @Test
    void testDeleteItem() throws Exception {
        String itemName = "domates";

        Optional<Category> category = categoryRepository.findByName("çorba");
        assertEquals(true, category.isPresent(), "Category should exists");

        Optional<Item> item = itemRepository.findByName(itemName);
        assertEquals(true, item.isPresent(), "Item should exists");
        int categoryId = category.get().getId();
        int itemId = item.get().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/item/" + itemId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/" + categoryId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/" + categoryId));

        item = itemRepository.findByName(itemName);
        assertEquals(false, item.isPresent(), "Item should not exists");
    }

    /*
     * Change the name of category 'yemek' to 'yemekler'
     */
    @Test
    void testRenameCategory() throws Exception {
        String oldName = "yemek";
        String newName = "yemekler";

        Optional<Category> category = categoryRepository.findByName(oldName);
        assertEquals(true, category.isPresent(), "Category should exists");
        int categoryId = category.get().getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/category/" + categoryId)
                .param("name", newName))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/" + categoryId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/" + categoryId));

        category = categoryRepository.findByName(oldName);
        assertEquals(false, category.isPresent(), "Category should not exists");

        category = categoryRepository.findByName(newName);
        assertEquals(true, category.isPresent(), "Category should exists");
    }

    /*
     * Double the price of the item 'su'
     */
    @Test
    void testChangeItemPrice() throws Exception {
        String itemName = "su";

        Optional<Item> item = itemRepository.findByName(itemName);
        assertEquals(true, item.isPresent(), "Item should exists");

        Category category = item.get().getCategory();
        int categoryId = category.getId();
        int itemId = item.get().getId();
        double itemPrice = item.get().getPrice();
        double newPrice = itemPrice * 2;

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/item/" + itemId)
                .param("name", itemName)
                .param("price", Double.toString(newPrice)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/" + categoryId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/" + categoryId));

        double price = itemRepository.findByName(itemName).get().getPrice();
        assertEquals(newPrice, price, "Price values should be equal");
    }
}
