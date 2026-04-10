package ph.edu.dlsu.greencanyonairbnb.model;

public class Property {
    private int id;
    private String propertyName;
    private double price;
    private String propertyAddress;
    private String imageUrl;
    private String propertyDescription;

    public Property (int id, String propertyName, double price, String propertyAddress, String imageUrl, String propertyDescription) {
        this.id = id;
        this.propertyName = propertyName;
        this.price = price;
        this.propertyAddress = propertyAddress;
        this.imageUrl = imageUrl;
        this.propertyDescription = propertyDescription;
    }

    public String getPropertyName() {return propertyName;}
    public double getPrice() {return price;}
    public String getPropertyAddress() {return propertyAddress;}
    public String getImageUrl() {return imageUrl;}
    public String getPropertyDescription() {return propertyDescription;}

}
