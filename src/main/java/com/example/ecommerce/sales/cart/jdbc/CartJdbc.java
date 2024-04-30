package com.example.ecommerce.sales.cart.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.ecommerce.common.primitives.Money;
import com.example.ecommerce.common.primitives.Quantity;
import com.example.ecommerce.sales.cart.Cart;
import com.example.ecommerce.sales.cart.CartId;
import com.example.ecommerce.sales.cart.item.CartItem;
import com.example.ecommerce.sales.cart.item.ProductId;
import com.example.ecommerce.sales.cart.item.Title;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
final public class CartJdbc implements Cart {
    private final @NonNull CartId id;

    private final @NonNull JdbcTemplate jdbcTemplate;

    private List<CartItem> items;

    @Override
    public CartId id() {
        return id;
    }

    @Override
    public List<CartItem> items() {
        if (items == null) {
            items = jdbcTemplate.queryForList(
                            "SELECT product_id, title, price, quantity FROM cart_items " +
                                    "WHERE cart_id = ?", id.value())
                    .stream()
                    .map(this::toCartItem)
                    .collect(Collectors.toList());
        }
        return items;
    }

    private CartItem toCartItem(Map<String, Object> entry) {
        return new CartItem(
                new ProductId(entry.get("product_id")),
                new Title((String) entry.get("title")),
                new Money(((BigDecimal) entry.get("price")).floatValue()),
                new Quantity((Integer) entry.get("quantity")));
    }

    @Override
    public boolean hasItems() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(cart_id) FROM cart_items " +
                        "WHERE cart_id = ?", Integer.class, id.value()) > 0;
    }
    //如果購物車中已經存在相同的項目，則使用 SQL UPDATE 語句更新購物車中該項目的數量，以達到增加數量的效果。
    //如果購物車中不存在相同的項目，則使用 SQL INSERT 語句將新項目插入購物車。
    @Override
    public void add(CartItem toAdd) {
        if (hasItem(toAdd)) {
            jdbcTemplate.update(
                    "UPDATE cart_items SET quantity = quantity + ? " +
                            "WHERE cart_id = ? AND product_id = ? AND title = ? AND price = ?",
                    toAdd.quantity().value(), id.value(),
                    toAdd.productId().value(), toAdd.title().value(), toAdd.unitPrice().value());
        } else {
            jdbcTemplate.update(
                    "INSERT INTO cart_items VALUES (?, ?, ?, ?, ?)",
                    toAdd.productId().value(), toAdd.title().value(), toAdd.unitPrice().value(),
                    toAdd.quantity().value(), id.value());
        }
    }
    //調用 hasItem 方法檢查購物車中是否已經存在要添加的項目 toAdd。
    private boolean hasItem(CartItem item) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(product_id) FROM cart_items " +
                        "WHERE cart_id = ? AND product_id = ? AND title = ? AND price = ?", Integer.class,
                id.value(), item.productId().value(), item.title().value(), item.unitPrice().value()) > 0;
    }

    @Override
    public void remove(ProductId productId) {
        jdbcTemplate.update(
                "DELETE FROM cart_items WHERE product_id = ? AND cart_id = ?",
                productId.value(), id.value());
    }

    @Override
    public void empty() {
        jdbcTemplate.update("DELETE FROM cart_items WHERE cart_id = ?", id.value());
    }
}
