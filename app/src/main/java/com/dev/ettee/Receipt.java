package com.dev.ettee;

import java.util.Date;

public class Receipt {

    int id;
    Date purcharseDate = null;
    Double amount;
    String paymentMethod = null;
    String name;
    String commentary = null;
    Boolean pinned = false;
    User user = null;
    Shop shop = null;
    Category category = null;

    public Receipt(String name, Date date, Shop shop, Double amount, String commentary) {
        super();
        this.id = 1;
        this.purcharseDate = date;
        this.amount = amount;
        this.paymentMethod = "Carte bleue";
        this.name = name;
        this.commentary = commentary;
        this.pinned = false;
        this.user = new User();
        this.shop = shop;
        this.category = new Category(1, "TEST");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPurcharseDate() {
        return purcharseDate;
    }

    public void setPurcharseDate(Date purcharseDate) {
        this.purcharseDate = purcharseDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
