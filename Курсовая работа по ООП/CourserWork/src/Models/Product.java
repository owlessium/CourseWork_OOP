package Models;

import java.util.Objects;

public class Product {
    private int id;

    private final String name;
    private final String description;
    private final float weight;
    private int count;
    private final float price;

    public Product(int id, String name, String description, float weight, int count, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.count = count;
        this.price = price;
    }

    public Product(String name, String description, float weight, int count, float price) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.count = count;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Float.compare(product.weight, weight) == 0 && count == product.count && Float.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, weight, count, price);
    }
}
