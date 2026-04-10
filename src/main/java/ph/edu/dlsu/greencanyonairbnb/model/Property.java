package ph.edu.dlsu.greencanyonairbnb.model;

public class Property {
    private int id;
    private String propertyName;
    private double price;
    private String propertyAddress;

    public Property (int id, String propertyName, double price, String propertyAddress) {
        this.id = id;
        this.propertyName = propertyName;
        this.price = price;
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyName() {return propertyName;}
    public double getPrice() {return price;}
    public String getPropertyAddress() {return propertyAddress;}

}
