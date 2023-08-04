package com.example.poshell.db;

import com.example.poshell.model.Cart;
import com.example.poshell.model.Product;
import com.example.poshell.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.poshell.db.PosInMemoryDB.delProductFrom;
@Component
@Primary
public class PosMybatisDB implements PosDB {
    private List<Product> products;

    private List<User> users;

    private SqlSessionFactory sqlSessionFactory;

    private Cart cart;
    private void addProductToSql(@Param("id")Product product){
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        sqlSession.insert("insertProduct",product);
        sqlSession.commit();
        sqlSession.close();
    }
    @Override
    public boolean addProduct(Product product){
        for(Product p: this.products){
            if(p.getId().equals(product.getId())){
                return false;
            }
        }
        if (this.products.add(product)){
            addProductToSql(product);
            return true;
        }
        return false;
    }
    private void delProductFromSql(String id){
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        sqlSession.delete("deleteProduct",id);
        sqlSession.commit();
        sqlSession.close();
    }
    @Override
    public boolean delProductFromList(String id){
        if(delProductFrom(id, this.products)){
            delProductFromSql(id);
            return true;
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

    private PosMybatisDB() throws IOException {
        //1.加载mybatis的核心配置文件，获取SqlSessionFactory
//        System.out.println("You are using MybatisDB!");
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2.获取SqlSession对象，用它执行Sql
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //3.执行sql
        this.products = sqlSession.selectList("product.selectProducts");
        this.cart = new Cart();
        this.users = sqlSession.selectList("user.selectUsers");

        //4.释放
        sqlSession.close();
    }

}
