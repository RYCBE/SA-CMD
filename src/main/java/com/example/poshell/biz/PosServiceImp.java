package com.example.poshell.biz;

import com.example.poshell.db.PosDB;
import com.example.poshell.model.Cart;
import com.example.poshell.model.Item;
import com.example.poshell.model.Product;
import com.example.poshell.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PosServiceImp implements PosService {

    private PosDB posDB;

    @Autowired
    public void setPosDB(PosDB posDB) {
        this.posDB = posDB;
    }

    @Override
    public Cart getCart() {
        return posDB.getCart();
    }

    @Override
    public Cart newCart() {
        return posDB.saveCart(new Cart());
    }

    @Override
    public void checkout(Cart cart) {
        posDB.saveCart(new Cart());
    }
    @Override
    public boolean checkVip(Cart cart, String username, String pwd) {
        List<User> users = posDB.getUsers();
        for(User u:users){
            if(u.getUsername().equals(username) && u.getPwd().equals(pwd)){
                posDB.saveCart(new Cart());
                return true;
            }
        }
        return false;
    }

    @Override
    public double total(Cart cart) {
        return cart.getTotal();
    }

    @Override
    public boolean add(Product product, int amount) {
        return addByProductId(product, amount);
    }

    private boolean addByProductId(Product product, int amount) {
        if (product == null) return false;
        Cart c = this.getCart();
        List<Item> cc = c.getItems();
        for(int i=0;i<cc.size();i++){
            Item t = cc.get(i);
            if (t.getProduct().toString().equals(product.toString())){
                Item tmpItem = new Item(product,t.getAmount()+amount);
                cc.set(i,tmpItem);
                return true;
            }
        }
        c.addItem(new Item(product, amount));
        return true;
    }

    @Override
    public boolean add(String productId, int amount) {
        Product product = posDB.getProduct(productId);
        return addByProductId(product, amount);
    }
    @Override
    public boolean set(String productId, int amount){
        Product product = posDB.getProduct(productId);
        if (product == null) return false;

        Cart c = this.getCart();
        List<Item> cc = c.getItems();
        for(int i=0;i<cc.size();i++){
            if (cc.get(i).getProduct().toString().equals(product.toString())){
                Item tmpItem = new Item(product,amount);
                cc.set(i,tmpItem);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean del(String productId, int amount){
        Product product = posDB.getProduct(productId);
        if (product == null) return false;

        Cart c = this.getCart();
        List<Item> cc = c.getItems();
        for(int i=0;i<cc.size();i++){
            Item t = cc.get(i);
            if (t.getProduct().toString().equals(product.toString())){
                int oldAmount = t.getAmount();
                if(oldAmount>=amount){
                    Item tmpItem = new Item(product,oldAmount-amount);
                    cc.set(i,tmpItem);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Product> products() {
        return posDB.getProducts();
    }

    @Override
    public boolean addProduct(Product product){
        return posDB.addProduct(product);
    }
    @Override
    public boolean delProductFromList(String idOrName){
        return posDB.delProductFromList(idOrName);
    }
}
