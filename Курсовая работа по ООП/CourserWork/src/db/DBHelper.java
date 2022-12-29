package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Models.DeliveryType;
import Models.Order;
import Models.Product;


public class DBHelper {
    private static final String DB_PATH = "C:\\Users\\vital\\OneDrive\\Рабочий стол\\Финал\\Курсовая работа по ООП\\database_.db";
    private static final String CONNECTION_STR = "jdbc:sqlite:";

    private static DBHelper instance = null;

    public static DBHelper getInstance() {
        if (instance == null){
            instance = new DBHelper();
        }

        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STR + DB_PATH);
    }

    public void createTables() {
        try (var connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("PRAGMA foreign_keys=ON");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS deliveryTypes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "name TEXT," +
                            "price REAL" +
                            "days INTEGER)"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS orders (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "clientName TEXT," +
                            "summaryPrice REAL," +
                            "orderDate TEXT," +
                            "deliveryDate TEXT," +
                            "deliveryAddress TEXT," +
                            "deliveryTypeId INTEGER," +
                            "productId INTEGER," +
                            "FOREIGN KEY (productId) REFERENCES products(id) ON DELETE SET NULL," +
                            "FOREIGN KEY (deliveryTypeId) REFERENCES deliveryTypes(id) ON DELETE SET NULL)"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS products (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "name TEXT," +
                            "description TEXT," +
                            "weight REAL," +
                            "count INTEGER," +
                            "price REAL)"
            );
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int getLastOrderId(){
        return getLastIdByTable("'orders'");
    }

    public int getLastProductId(){
        return getLastIdByTable("'products'");
    }

    private int getLastIdByTable(String table){
        try (var connection = this.getConnection()){
            String sql = "SELECT seq FROM sqlite_sequence WHERE name=" + table;

            PreparedStatement statement = connection.prepareStatement(sql);

            var result = statement.executeQuery();

            return result.getInt(1);
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return 0;
    }

    public void finishOrder(Order order){
        try (var connection = this.getConnection()){
            String sql = "UPDATE orders SET isFinished = 1 WHERE id = ? ";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, order.getId());

            statement.execute();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public List<Order> findOrderByClientName(String clientName){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM orders WHERE clientName LIKE '%" + clientName + "%'";

            List<Order> orders = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(sql);
            var result = statement.executeQuery();

            while (result.next()){
                var dateTime = result.getString("deliveryDate").split(" ");

                orders.add(
                        new Order(
                                result.getInt("id"),
                                result.getString("clientName"),
                                result.getFloat("summaryPrice"),
                                result.getString("orderDate"),
                                dateTime[0], dateTime[1],
                                result.getString("deliveryAddress"),
                                getDeliveryTypeById(result.getInt("deliveryTypeId")),
                                result.getBoolean("isFinished"), getProductById(result.getInt("productId"))
                        )
                );
            }

            return orders;
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public void addOrder(Order order){
        try (var connection = this.getConnection()){
            String sql = "INSERT INTO " +
                    "orders(clientName, summaryPrice, orderDate, deliveryDate, deliveryAddress, deliveryTypeId, productId) " +
                    "VALUES(?,?,?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, order.getClientName());
            statement.setObject(2, order.getSummaryPrice());
            statement.setObject(3, order.getOrderDate());
            statement.setObject(4, (order.getDeliveryDate() + " " + order.getDeliveryTime()));
            statement.setObject(5, order.getDeliveryAddress());
            statement.setObject(6, order.getDeliveryType().getId());
            statement.setObject(7, order.getProduct().getId());

            statement.execute();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void addProduct(Product product){
        try (var connection = this.getConnection()){
            String sql = "INSERT INTO " +
                    "products(name, description, count, price, weight) " +
                    "VALUES(?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, product.getName());
            statement.setObject(2, product.getDescription());
            statement.setObject(3, product.getCount());
            statement.setObject(4, product.getPrice());
            statement.setObject(5, product.getWeight());


            statement.execute();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void addDeliveryType(DeliveryType deliveryType){
        try (var connection = this.getConnection()){
            String sql = "INSERT INTO " +
                    "deliveryTypes(name, price) " +
                    "VALUES(?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, deliveryType.getName());
            statement.setObject(2, deliveryType.getPrice());

            statement.execute();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public Product getProductById(int id){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM products WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, id);
            var result = statement.executeQuery();

            return new Product(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("description"),
                    result.getFloat("weight"),
                    result.getInt("count"),
                    result.getFloat("price")
            );
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public DeliveryType getDeliveryTypeById(int id){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM deliveryTypes WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, id);
            var result = statement.executeQuery();

            return new DeliveryType(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getFloat("price"));
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public Order getOrderById(int id){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM orders " +
                    "WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, id);
            var result = statement.executeQuery();

            var dateTime = result.getString("deliveryDate").split(" ");

            return new Order(
                    result.getInt("id"),
                    result.getString("clientName"),
                    result.getFloat("summaryPrice"),
                    result.getString("orderDate"),
                    dateTime[0], dateTime[1],
                    result.getString("deliveryAddress"),
                    getDeliveryTypeById(result.getInt("deliveryTypeId")),
                    result.getBoolean("isFinished"), getProductById(result.getInt("productId"))
            );
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public List<Order> getAllOrders(){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM orders o " +
                    "LEFT JOIN deliveryTypes dt " +
                    "ON o.deliveryTypeId = dt.id WHERE o.isFinished = 0";

            List<Order> orders = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(sql);
            var result = statement.executeQuery();

            while (result.next()){
                var dateTime = result.getString("deliveryDate").split(" ");

                orders.add(
                        new Order(
                                result.getInt("id"),
                                result.getString("clientName"),
                                result.getFloat("summaryPrice"),
                                result.getString("orderDate"),
                                dateTime[0], dateTime[1],
                                result.getString("deliveryAddress"),
                                new DeliveryType(
                                        result.getInt("id"),
                                        result.getString("name"),
                                        result.getFloat("price")),
                                result.getBoolean("isFinished"), getProductById(result.getInt("productId"))
                        )
                );
            }

            return orders;
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public void editProduct(Product product, int id){
        try (var statement = this.getConnection().prepareStatement(
                "UPDATE products" +
                        " SET name = ?, description = ?, weight = ?," +
                        " count = ?, price = ? " +
                        " WHERE id = ?")) {
            statement.setObject(1, product.getName());
            statement.setObject(2, product.getDescription());
            statement.setObject(3, product.getWeight());
            statement.setObject(4, product.getCount());
            statement.setObject(5, product.getPrice());
            statement.setObject(6, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editOrder(Order order, int id){
        try (var statement = this.getConnection().prepareStatement(
                "UPDATE orders" +
                        " SET clientName = ?, summaryPrice = ?, orderDate = ?," +
                        " deliveryDate = ?, deliveryAddress = ?, deliveryTypeId = ?," +
                        " productId = ?" +
                        " WHERE id = ?")) {
            statement.setObject(1, order.getClientName());
            statement.setObject(2, order.getSummaryPrice());
            statement.setObject(3, order.getOrderDate());
            statement.setObject(4, (order.getDeliveryDate() + " " + order.getDeliveryTime()));
            statement.setObject(5, order.getDeliveryAddress());
            statement.setObject(6, order.getDeliveryType().getId());
            statement.setObject(7, order.getProduct().getId());
            statement.setObject(8, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Product> getAllProducts(){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM products";

            List<Product> products = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(sql);
            var result = statement.executeQuery();

            while (result.next()) {
                products.add(
                        new Product(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("description"),
                                result.getFloat("weight"),
                                result.getInt("count"),
                                result.getFloat("price")
                        )
                );
            }

            return products;

        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public List<DeliveryType> getAllDeliveryTypes(){
        try (var connection = this.getConnection()){
            String sql = "SELECT * FROM deliveryTypes";

            List<DeliveryType> types = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(sql);
            var result = statement.executeQuery();

            while (result.next()) {
                types.add(
                        new DeliveryType(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getFloat("price"))
                );
            }

            return types;

        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public void deleteOrderById(int id){
        try (var connection = this.getConnection()){
            String sql = "DELETE FROM orders WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void deleteDeliveryTypeById(int id){
        try (var connection = this.getConnection()){
            String sql = "DELETE FROM deliveryTypes WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public void deleteProductById(int id){
        try (var connection = this.getConnection()){
            String sql = "DELETE FROM products WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

}