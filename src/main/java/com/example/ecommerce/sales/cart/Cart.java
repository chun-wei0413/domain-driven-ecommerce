package com.example.ecommerce.sales.cart;

import com.example.ecommerce.sales.cart.item.CartItem;
import com.example.ecommerce.sales.cart.item.ProductId;

import java.util.List;

/**
 * Cart entity.
 */

public interface Cart {

    CartId id();

    List<CartItem> items();

    boolean hasItems();

    void add(CartItem toAdd);

    void remove(ProductId productId);

    void empty();
}