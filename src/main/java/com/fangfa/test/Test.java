package com.fangfa.test;

import com.fangfa.bean.JopcClient;
import com.fangfa.opc.ImportExcel;
import com.fangfa.opc.OpcTest3;
import com.fangfa.util.JDBCUtil;
import com.fangfa.util.Task;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author green
 * @date 2018/11/18/018
 */
public class Test {

    public static void main(String[] args) throws Exception {

        List<String> tagList = ImportExcel.getItemsAll();

        while (true) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            String date = dateTimeFormatter.format(localDateTime);
          String querySql = "select * from jl_OpcTables";
          List<Map<String, Object>> list = JDBCUtil.executeQuery(querySql);
            boolean flag = false;
        for (Map<String,Object> map:list) {
             String tableName =(String) map.get("tableName");
             String d = tableName.replace("jl_opc_","");
             if(d.equals(date)){
                 flag = true;
                 break;
             }
        }
        if(!flag){
            String tableName = "jl_opc_"+date;
            String createTableSql =
                    "CREATE TABLE "+tableName+" (\n" +
                    "  [id] int  IDENTITY(1,1) NOT NULL,\n" +
                    "  [serverTagName] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,\n" +
                    "  [date] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,\n" +
                    "  [value] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,\n" +
                    "  [createDate] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,\n" +
                    "  [tagName] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL\n" +
                    ");";

            JDBCUtil.executeUpdate(createTableSql);
            // 把表名称加入表中
            String s = "insert into jl_OpcTables (tableName,date) values (?,?)";
            JDBCUtil.executeUpdate(s,tableName,date);
        }

            /*正式*/

            MasterJopc masterJopc = new MasterJopc();
            JOpc jOpc = masterJopc.opcInit();
            List<OpcItem> opcItemList = masterJopc.readOpc(tagList,jOpc);

            /*正式*/

            //这里有重复的数据，需要去重
            Map<String,OpcItem> map = new HashMap<>();
            opcItemList.forEach(o->{
                map.put(o.getItemName(),o);
            });

            List<OpcItem> itemList = new ArrayList<>();

            System.out.println("开始保存数据");
            map.forEach((k,v)->{
                itemList.add(v);
            });

           itemList.parallelStream()
                   .forEach(Test::save);
            Thread.sleep(10000);
            System.out.println("opc完成一次");
        }

    }



    private static void save(OpcItem opcItem){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String date = dateTimeFormatter.format(localDateTime);
        String opcTableName = "jl_opc_"+date;
        String createDate = OpcTest3.getLocalDateTimeByYYYYMMDDHHmmss();
        String sql = "insert into "+opcTableName+" (serverTagName,date,value,createDate,tagName) values (?,?,?,?,?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tagName = opcItem.getItemName().replace("\\\\10.89.6.6\\","");
        try {
            JDBCUtil.executeUpdate(sql,opcItem.getItemName(),simpleDateFormat.format(opcItem.getTimeStamp().getTime()),
                    opcItem.getValue().toString(),createDate,tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
