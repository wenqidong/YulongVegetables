package com.dong.java.entity;

/**
 * @author wenqi
 * @date 2021/2/24 22:20
 */
public class GoodEntity {

    /**
     * 序号
     */
    private int number;

    /**
     * 名称及规格
     */
    private String name;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private int amount;

    /**
     * 单价
     */
    private Float price;

    /**
     * 总价
     */
    private Float total;

    /**
     * 备注
     */
    private String memo;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
