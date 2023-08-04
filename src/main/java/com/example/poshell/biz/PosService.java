package com.example.poshell.biz;

import com.example.poshell.model.Cart;
import com.example.poshell.model.Product;

import java.util.List;

public interface PosService {
    public Cart getCart();

    public Cart newCart();

    public void checkout(Cart cart);

    public double total(Cart cart);

    public boolean add(Product product, int amount);

    public boolean add(String productId, int amount);

    public boolean set(String productId, int amount);
    public boolean del(String productId, int amount);

    public boolean checkVip(Cart cart, String username, String pwd);

    public List<Product> products();
    public boolean addProduct(Product product);

    boolean delProductFromList(String idOrName);
}
