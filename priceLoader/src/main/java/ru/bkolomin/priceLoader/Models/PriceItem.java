package ru.bkolomin.priceLoader.models;

import java.util.Date;

public class PriceItem {

    private String supplier;
    private Integer rowNumber;
    private String comment;
    private String scode;
    private String vcode;
    private String name;
    private Double price;
    private String stock;
    private Date priceDate;

    public PriceItem() {
    }

    public PriceItem(String supplier, Integer rowNumber, String comment, String scode, String vcode, String name, Double price, String stock, Date priceDate) {
        this.supplier = supplier;
        this.rowNumber = rowNumber;
        this.comment = comment;
        this.scode = scode;
        this.vcode = vcode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.priceDate = priceDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    @Override
    public String toString() {
        return "PriceItem{" +
                "rowNumber=" + rowNumber +
                ", comment='" + comment + '\'' +
                ", s code='" + scode + '\'' +
                ", v code='" + scode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock='" + stock + '\'' +
                '}';
    }
}

