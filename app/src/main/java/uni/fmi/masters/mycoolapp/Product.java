package uni.fmi.masters.mycoolapp;

import android.app.Instrumentation;

public class Product {
    private int ID;
    private String name;
    private double price;
    private int quantity;
    private String number;
    private String barcode;

    public Product(String name, double price, int quantity, String number, String barcode) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.number = number;
        this.barcode = barcode;
    }

    public  Product() { }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
