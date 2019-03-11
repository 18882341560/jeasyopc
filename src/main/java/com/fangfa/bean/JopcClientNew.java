package com.fangfa.bean;

import com.fangfa.util.JDBCUtil;
import com.fangfa.util.Task;
import javafish.clients.opc.AsynchReadAndGroupActivityExample;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 使用Jopc连接Opc服务器,只支持32位电脑，客户是部署在windows上
*@author  gl
*@date    2018/6/7/007
*@param
*@return
*/
public class JopcClientNew {

    private final Logger logger = LoggerFactory.getLogger(JopcClientNew.class);

    /**
     * 初始化opc服务
    *@author  gl
    *@date    2018/6/23/023
    *@param
    *@return
    */
    public JOpc opcInit(){
        logger.debug("初始化服务");
        JOpc.coInitialize();
        logger.debug("建立jopc服务对象");
        JOpc jopc = new JOpc("localhost", "OSI.DA.1", "JOPC1");
        return jopc;
    }

    /**
     * 断开连接
    *@author  gl
    *@date    2018/6/23/023
    *@param
    *@return
    */
    public void coUninitialize(){
        logger.debug("断开连接");
        JOpc.coUninitialize();
    }

    /**
    *@author  gl
    *@date    2018/6/23/023
    *@param    items:所有item的集合
    *@return   有数据的OpcItem集合对象
    */
    public void readOpc(List<String> items) throws Exception {

        AsynchReadAndGroupActivityExample ar = new AsynchReadAndGroupActivityExample();

        logger.debug("建立jopc服务对象");
        JOpc jopc = opcInit();

        logger.debug("建立一个组");
        OpcGroup group = new OpcGroup("group1", true, 20, 0.0f);

        logger.debug("将所有的item添加到组中");
        items.forEach( s -> {
            OpcItem item1 = new OpcItem(s, true, "");
            group.addItem(item1);
        });
        jopc.addGroup(group);

        logger.debug("调用JCustomOpc.connect()连接服务器");
        jopc.connect();

        logger.debug("调用JOpc.registerGroups()注册所有的组使用registerGroups()方法注册则OpcItem不用单独注册。如果调用registerGroup(OpcGroup)注册OpcGroup，则还需调用registerItem(OpcGroup,OpcItem)");
        jopc.registerGroups();

        logger.debug("调用JOpc.asynch10Read()异步读数据");
        jopc.asynch10Read(group);

        OpcGroup downGroup;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
            /**当前时间秒数**/
        try {
            while (true) {
                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
                String date = dateTimeFormatter.format(localDateTime);
                String querySql = "select *  from jl_OpcTables";
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
                            "CREATE TABLE [dbo].["+tableName+"] (\n" +
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
                    jopc.ping();
                    downGroup = jopc.getDownloadGroup();
                    if (downGroup != null) {
                       List<OpcItem> opcItemList = downGroup.getItems();
                        for (OpcItem opcItem:opcItemList) {
                            Task task = new Task(opcItem);
                            executor.execute(task);
                        }
                    }
                logger.debug("等待一分钟开始");
                    synchronized(ar) {
                        /**间隔1分钟**/
                        ar.wait(60000);
                    }
                logger.debug("等待一分钟结束");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 简单的做个日志
            FileWriter fw = new FileWriter("D://opc.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(e.getMessage()+"\r\n ");
            bw.close();
            fw.close();
        }
    }

}

