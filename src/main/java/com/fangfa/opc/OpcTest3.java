package com.fangfa.opc;

import com.fangfa.util.JDBCUtil;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpcTest3 {

    public static void main(String[] args) throws Exception{
        try {
//        List<String> list = ImportExcel.getItemsAll();
//        JopcClient jopcClient = new JopcClient();
//        List<OpcItem> opcItems = jopcClient.readOpc(list);
            // 模拟得到opcItem
            List<OpcItem> opcItems = new ArrayList<>();
            OpcItem opcItem = new OpcItem("\\\\10.89.6.6\\B250043C100000010",true,"");
            Variant itemValue = new Variant(6.0);
            opcItem.setValue(itemValue);
            opcItems.add(opcItem);
            // 模拟完
            // 有重复的数据，去重
            Map<String,OpcItem> map = new HashMap();
            for (OpcItem opc:opcItems) {
                map.put(opc.getItemName(),opc);
            }

            String createDate = OpcTest3.getLocalDateTimeByYYYYMMDDHHmmss();
            String sql = "insert into jl_OpcData (serverTagName,date,value,createDate,tagName) values (?,?,?,?,?)";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (OpcItem opc : map.values()) {
                String tagName = opc.getItemName().replace("\\\\10.89.6.6\\","");
                JDBCUtil.executeUpdate(sql,opc.getItemName(),simpleDateFormat.format(opc.getTimeStamp().getTime()),
                        opc.getValue().toString(),createDate,tagName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLocalDateTimeByYYYYMMDDHHmmss() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(localDateTime);
    }
}
