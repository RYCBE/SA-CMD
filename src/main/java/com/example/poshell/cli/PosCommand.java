package com.example.poshell.cli;

import com.example.poshell.biz.PosService;
import com.example.poshell.model.Cart;
import com.example.poshell.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class PosCommand {

    private PosService posService;

    @Autowired
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @ShellMethod(value = "List Products", key = "pd")
    public String products() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Product product : posService.products()) {
            stringBuilder.append("\t").append(++i).append("\t").append(product).append("\n");
        }
        return stringBuilder.toString();
    }

    @ShellMethod(value = "New Cart", key = "n")
    public String newCart() {
        return posService.newCart() + " OK";
    }

    @ShellMethod(value = "Add a Product to Cart(by its PD)", key = "a")
    public String addToCart(String productId, int amount) {
        if (posService.add(productId, amount)) {
            return posService.getCart().toString();
        }
        return "Wrong ID of products!";
    }

    @ShellMethod(value = "Print all Products in cart", key = "l")
    public String printProducts() {
        return posService.getCart().toString();
    }

    @ShellMethod(value = "Set a Product's amount in Cart(by its PD)", key = "s")
    public String setProduct(String productId, int amount) {
        if (posService.set(productId, amount)) {
            return posService.getCart().toString();
        }
        return "ERROR";
    }
    @ShellMethod(value = "Delete a Product in Cart(by its PD)", key = "d")
    public String delProduct(String productId, int amount) {
        if (posService.del(productId, amount)) {
            return posService.getCart().toString();
        }
        return "ERROR";
    }

    @ShellMethod(value = "Get the total price of your Products", key = "t")
    public String total(){
        Cart c = posService.getCart();
        return String.valueOf(posService.total(c));
    }

    @ShellMethod(value = "Check out", key = "c")
    public String check(){
        posService.checkout(posService.getCart());
        return "You have checked out!";
    }

    @ShellMethod(value = "Check out with your Vip account!", key = "cv")
    public String checkVip(String username, String pwd){
        if(posService.checkVip(posService.getCart(),username,pwd)){
            return "You have checked out with your Vip account!";
        }
        return "Wrong username or password!";
    }

    @ShellMethod(value = "Generate a Product in Product list(ID,Product,Price)",key = "ad")
    public String addProductToList(String id, String name, double price){
        Product p = new Product(id,name,price);
        if(posService.addProduct(p)){
            return "Successfully add a Product into list!";
        }
        return "Error! Maybe there is a same ProductID or the list is full!";
    }

    @ShellMethod(value = "Delete a Product in Product list(ID or Product)",key = "dd")
    public String delProductFromList(String idOrName){
        if(posService.delProductFromList(idOrName)){
            return "Successfully delete a Product from list!";
        }
        return "Error! There is not such a Product!";
    }

}
