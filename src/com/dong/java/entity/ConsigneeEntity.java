package com.dong.java.entity;

import java.util.Objects;

/**
 * @author wenqi
 * @date 2021/2/28 21:54
 */
public class ConsigneeEntity {

    public ConsigneeEntity(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    private String name;

    private String address;

    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsigneeEntity that = (ConsigneeEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone);
    }
}
