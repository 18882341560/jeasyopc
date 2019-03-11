package com.fangfa.util;

import com.fangfa.opc.OpcTest3;
import javafish.clients.opc.component.OpcItem;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author green
 * @date 2018/11/18/018
 */
public class Task implements Runnable {

   private OpcItem opcItem;

   public Task(OpcItem opcItem){
       this.opcItem = opcItem;
   }

    @Override
    public void run() {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            String date = dateTimeFormatter.format(localDateTime);
            String opcTableName = "jl_opc_"+date;
            String createDate = OpcTest3.getLocalDateTimeByYYYYMMDDHHmmss();
            String sql = "insert into "+opcTableName+" (serverTagName,date,value,createDate,tagName) values (?,?,?,?,?)";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tagName = opcItem.getItemName().replace("\\\\10.89.6.6\\","");
            JDBCUtil.executeUpdate(sql,opcItem.getItemName(),simpleDateFormat.format(opcItem.getTimeStamp().getTime()),
            opcItem.getValue().toString(),createDate,tagName);
    }

}
