package com.example.poshell.db;

import com.example.poshell.model.Cart;
import com.example.poshell.model.Product;
import com.example.poshell.model.User;

import java.util.List;

public interface PosDB {

    public List<Product> getProducts();

    public Cart saveCart(Cart cart);

    public Cart getCart();

    public List<User> getUsers();

    public Product getProduct(String productId);

    public boolean addProduct(Product product);

    boolean delProductFromList(String idOrName);
}
