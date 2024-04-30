package com.example.ecommerce.sales.catalog.jdbc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.example.ecommerce.sales.catalog.category.Categories;
import com.example.ecommerce.sales.catalog.category.Category;
import com.example.ecommerce.sales.catalog.category.CategoryId;
import com.example.ecommerce.sales.catalog.category.Title;
import com.example.ecommerce.sales.catalog.category.Uri;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JDBC implementation of Categories collection.
 */
@RequiredArgsConstructor
final class CategoriesJdbc implements Categories {
    private final @NonNull String query;
    private final @NonNull List<Object> queryParams;

    private final @NonNull JdbcTemplate jdbcTemplate;

    public CategoriesJdbc(@NonNull String query, @NonNull Object queryParam, @NonNull JdbcTemplate jdbcTemplate) {
        this(query, List.of(queryParam), jdbcTemplate);
    }

    public CategoriesJdbc(@NonNull String query, @NonNull JdbcTemplate jdbcTemplate) {
        this(query, List.of(), jdbcTemplate);
    }

    @Override
    public Stream<Category> stream() {
        return jdbcTemplate.queryForList(query.concat(" ORDER BY 1"), queryParams.toArray())
                .stream()
                .map(this::toCategory);
    }
//用於將資料庫查詢結果的每一行映射為一個 Category 物件。
    private Category toCategory(Map<String, Object> entry) {
        return new CategoryJdbc(
                new CategoryId(entry.get("id")),
                new Uri((String) entry.get("uri")),
                new Title((String) entry.get("title")),
                jdbcTemplate);
    }

}
