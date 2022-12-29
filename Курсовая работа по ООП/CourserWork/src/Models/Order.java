package Models;

import java.time.LocalDate;

public class Order {
    private int id;

    private final String clientName;
    private float summaryPrice;
    private final LocalDate orderDate;
    private final LocalDate deliveryDate;
    private final String deliveryTime;
    private final String deliveryAddress;
    private final DeliveryType deliveryType;
    private final boolean isFinished;

    private final Product product;

    public Order(int id, String clientName, float summaryPrice, String orderDate,
                 String deliveryDate, String deliveryTime, String deliveryAddress, DeliveryType deliveryType, boolean isFinished, Product product) {
        this.id = id;
        this.clientName = clientName;
        this.summaryPrice = summaryPrice;
        this.orderDate = LocalDate.parse(orderDate);
        this.deliveryDate = LocalDate.parse(deliveryDate);
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
        this.deliveryType = deliveryType;
        this.isFinished = isFinished;
        this.product = product;
    }

    public Order(String clientName, LocalDate orderDate,
                 LocalDate deliveryDate, String deliveryTime, String deliveryAddress, DeliveryType deliveryType, boolean isFinished, Product product) {
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
        this.deliveryType = deliveryType;
        this.isFinished = isFinished;
        this.product = product;

        this.summaryPrice += deliveryType.getPrice() + product.getPrice();

    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public float getSummaryPrice() {
        return summaryPrice;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
