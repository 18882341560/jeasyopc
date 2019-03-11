package com.fangfa.bean;

import javafish.clients.opc.variant.Variant;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Opc {
    private double itemValue;//数据
    private Date timeStamp;//时间

    public Opc (){
        super();
    }
    public Opc(double itemValue,Date timeStamp) {
        this.itemValue = itemValue;
        this.timeStamp = timeStamp;
    }

    public Opc(double itemValue) {
        this.itemValue = itemValue;
    }

    public Opc(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getItemValue() {
        return itemValue;
    }

    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opc opc = (Opc) o;
        return Double.compare(opc.itemValue, itemValue) == 0 &&
                Objects.equals(timeStamp, opc.timeStamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemValue, timeStamp);
    }

    @Override
    public String toString() {
        return "Opc{" +
                "itemValue=" + itemValue +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public void setTimeStamp(String format) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.parse(format);
        this.timeStamp = date;
    }

    public void setItemValue(Variant value) {
        this.itemValue = value.getDouble();
    }
}
