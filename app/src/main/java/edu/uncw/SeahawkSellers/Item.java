package edu.uncw.SeahawkSellers;

class Item {

    private String title;
    private String description;
    private Integer price;
    private String seller;

    // No-argument constructor is required to support conversion of Firestore document to POJO
    public Item() {}

    // All-argument constructor is required to support conversion of Firestore document to POJO
    public Item(String title, String description, Integer price, String seller) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getSeller(){
        return seller;
    }
}
