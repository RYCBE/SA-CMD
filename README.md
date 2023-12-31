# POS in Shell

The demo shows a simple POS system with command line interface. 

To run

```shell
mvn clean spring-boot:run
```

Currently, it implements three commands which you can see using the `help` command.

```shell
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.4)
 
shell:>help
AVAILABLE COMMANDS

Built-In Commands
       help: Display help about available commands
       stacktrace: Display the full stacktrace of the last error.
       clear: Clear the shell screen.
       quit, exit: Exit the shell.
       history: Display or save the history of previously run commands
       version: Show version info
       script: Read and execute commands from a file.

Pos Command
       dd: Delete a Product in Product list(ID or Product)
       a: Add a Product to Cart(by its PD)
       c: Check out
       ad: Generate a Product in Product list(ID,Product,Price)
       cv: Check out with your Vip account!
       s: Set a Product's amount in Cart(by its PD)
       d: Delete a Product in Cart(by its PD)
       t: Get the total price of your Products
       pd: List Products
       l: Print all Products in cart
       n: New Cart
```



## 分层结构

软件体系结构中，三层架构模式可谓经典。接下来结合本次的作业谈一谈对他的理解。

### 展示层

展示层负责处理所有的界面展示以及交互逻辑。

本次作业中，这一层的解决方案是SpringShell，最终用于负责和用户交互的是命令行界面。命令行界面并不适合与用户进行交互，比较具有交互性的应当是带有用户界面GUI的交互方式。

### 业务层

业务层负责处理请求对应的业务，其中负责了对数据层的一些组合，以及逻辑的处理。

本次作业中，业务层与展示层对接的接口由 `PosService.java` 提供，默认提供了一个实现方案`PosServiceImp.java`，不过并不完善。

### 数据层

数据层应当负责底层的数据持久化工作，本次作业提供了一个实现方案`PosInMemoryDB`也即将数据先存到内存中，这并不复合实际而仅仅是一种模拟。并且在程序再一次启动时，原先保存的数据会丢失。

本次作业中，我简单地学习了jdbc的相关知识，参考了其上的MyBatis框架的一些做法，实现了与mysql进行交互，从而达到数据真正地持久化。



我们用本次我对于`dd`命令实现进行一个解读。

输入`pd`命令，打印商品列表（该商品列表从本地数据库读取得到）

```
shell:>pd
        1       PD2     macbook 21000.0
        2       PD3     ak47    2700.0
        3       PD4     m4a1    3100.0
        4       PD5     awp     4750.0
        5       PD6     tec9    900.0
        6       PD7     uuuu    777.0
```

输入`dd PD5`（删除商品）

```
shell:>dd PD5
Successfully add a Product into list!
shell:>pd
        1       PD2     macbook 21000.0
        2       PD3     ak47    2700.0
        3       PD4     m4a1    3100.0
        4       PD6     tec9    900.0
        5       PD7     uuuu    777.0
```

SpringShell在得到`ad`命令之后，`PosCommand`中调用``delProductFromList``方法，而PosCommand（展示层）类中有一个类成员PosService（业务层），因此`PosCommand.delProductFromList`其中必然要调用业务层的方法去处理逻辑。

```java
//PosCommand.java
@ShellMethod(value = "Delete a Product in Product list(ID or Product)",key = "dd")
public String delProductFromList(String idOrName){
    //这里调用了业务层的方法去处理逻辑
    if(posService.delProductFromList(idOrName)){
        return "Successfully delete a Product from list!";
    }
    return "Error! There is not such a Product!";
}
```

业务层负责去处理逻辑，由于这个功能需要持久化地去操作数据库，所以必须交给数据层去操作

```java
//PosServiceImp.java
@Override
public boolean delProductFromList(String idOrName){
    return posDB.delProductFromList(idOrName);
}
```

数据层进行删除，并且进行持久化地工作

```java
//PosMybatisDB.java
@Override
public boolean delProductFromList(String id){
    if(delProductFrom(id, this.products)){
    	//在本地地Mysql中删除
        delProductFromSql(id);
        return true;
    }
    return false;
}

private void delProductFromSql(String id){
    SqlSession sqlSession = this.sqlSessionFactory.openSession();
    sqlSession.delete("deleteProduct",id); //这里需要添加Mapper映射文件，具体过程参考了MyBatis的入门教程
    sqlSession.commit(); //需要进行事务的提交，不然数据库中无法观察到变化
    sqlSession.close();
}
```

这之后我们打开本地的数据库:

<img src="./ReadMePng/image-20230312210732594.png" alt="image-20230312210732594" style="zoom:80%;" />

可以看到数据确实被持久化了。整个命令的流程就从上到下打通了。

