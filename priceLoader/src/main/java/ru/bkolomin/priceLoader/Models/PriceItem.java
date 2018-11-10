package ru.bkolomin.priceLoader.Models;

public class PriceItem {

    private String supplier;
    private Integer rowNumber;
    private String comment;
    private String code;
    private String name;
    private Double price;
    private String stock;
    private Double price22325552;

    public PriceItem(String supplier, Integer rowNumber, String comment, String code, String name, Double price, String stock) {
        this.supplier = supplier;
        this.rowNumber = rowNumber;
        this.comment = comment;
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;//++**
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "PriceItem{" +
                "rowNumber=" + rowNumber +
                ", comment='" + comment + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock='" + stock + '\'' +
                '}';
    }
}
