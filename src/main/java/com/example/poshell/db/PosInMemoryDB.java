package com.example.poshell.db;

import com.example.poshell.model.Cart;
import com.example.poshell.model.Product;
import com.example.poshell.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PosInMemoryDB implements PosDB {
    private List<Product> products = new ArrayList<>();

    private List<User> users = new ArrayList<>();

    private Cart cart;
    @Override
    public boolean addProduct(Product product){
        for(Product p: this.products){
            if(p.getId().equals(product.getId())){
                return false;
            }
        }
        return this.products.add(product);
    }
    @Override
    public boolean delProductFromList(String idOrName){
        return delProductFrom(idOrName, this.products);
    }

    static boolean delProductFrom(String id, List<Product> products) {
        for(int i = 0; i< products.size(); ++i){
            if(products.get(i).getId().equals(id)){
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Cart saveCart(Cart cart) {
        this.cart = cart;
        return this.cart;
    }

    @Override
    public Cart getCart() {
        return this.cart;
    }

    @Override
    public Product getProduct(String productId) {
        for (Product p : getProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    private PosInMemoryDB() {

        this.products.add(new Product("PD1", "iPhone 13", 9000));
        this.products.add(new Product("PD2", "MacBook Pro", 30000));
        this.products.add(new Product("PD3","AK47",2700));
        this.products.add(new Product("PD4","M4A1",3100));
        this.products.add(new Product("PD5","AWP",4750));

        this.cart = new Cart();

        this.users.add(new User("admin","000"));
    }

}
