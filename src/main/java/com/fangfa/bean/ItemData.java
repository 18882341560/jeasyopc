package com.fangfa.bean;

import java.io.Serializable;

/**
 * @author green
 * @date 2018/10/29/029
 */
public class ItemData implements Serializable {
    private String serverTagName;
    private String date; // item 里的时间
    private String value;
    private String createDate; // 此次数据创建的时间

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getServerTagName() {
        return serverTagName;
    }

    public void setServerTagName(String serverTagName) {
        this.serverTagName = serverTagName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ItemData() {
    }
}
