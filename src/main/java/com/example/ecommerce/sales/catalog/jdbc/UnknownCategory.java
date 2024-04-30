package com.example.ecommerce.sales.catalog.jdbc;

import com.example.ecommerce.sales.catalog.category.Category;
import com.example.ecommerce.sales.catalog.category.CategoryId;
import com.example.ecommerce.sales.catalog.category.Title;
import com.example.ecommerce.sales.catalog.category.Uri;

import lombok.ToString;

/**
 * Null object implementation for Category entity.
 */
@ToString
final class UnknownCategory implements Category {

    @Override
    public CategoryId id() {
        return new CategoryId(0);
    }

    @Override
    public Uri uri() {
        return new Uri("unknown");
    }

    @Override
    public Title title() {
        return new Title("unknown category");
    }

    @Override
    public void changeTitle(Title title) {
        // do nothing
    }
}
