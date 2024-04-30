package com.example.ecommerce.sales.catalog;

import com.example.ecommerce.sales.catalog.category.Categories;
import com.example.ecommerce.sales.catalog.category.Category;
import com.example.ecommerce.sales.catalog.category.CategoryId;

/**
 * Find Categories use-case.
 */
public interface FindCategories {

    /**
     * Lists all categories.
     *
     * @return all categories
     */
    Categories all();

    /**
     * Finds a category by the category ID.
     *
     * @param id the category ID
     * @return the category
     */
    Category byId(CategoryId id);
}
