package com.dong.java.entity;

import java.util.Objects;

/**
 * @author wenqi
 * @date 2021/2/28 21:51
 */
public class PriceEntity {

    public PriceEntity(String name, String price) {
        this.name = name;
        this.price = price;
    }

    private String name;

    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceEntity that = (PriceEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
