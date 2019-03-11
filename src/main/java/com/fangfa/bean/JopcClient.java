package com.fangfa.bean;

import javafish.clients.opc.AsynchReadAndGroupActivityExample;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用Jopc连接Opc服务器,只支持32位电脑，客户是部署在windows上
*@author  gl
*@date    2018/6/7/007
*@param
*@return
*/
public class JopcClient {

    private final Logger logger = LoggerFactory.getLogger(JopcClient.class);

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
    public List<OpcItem> readOpc(List<String> items) throws Exception {

        AsynchReadAndGroupActivityExample ar = new AsynchReadAndGroupActivityExample();

        logger.debug("建立jopc服务对象");
        JOpc jopc = opcInit();

        logger.debug("建立一个组");
        OpcGroup group = new OpcGroup("Group1", true, 20, 0.0f);

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

        List<OpcItem> opcItemList = new ArrayList<>();
            /**当前时间秒数**/
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 10000) {
                jopc.ping();
                downGroup = jopc.getDownloadGroup();
                if (downGroup != null) {
                    opcItemList.addAll(downGroup.getItems());
                }

                if ((System.currentTimeMillis() - start) >= 6000) {
                    jopc.setGroupActivity(group, false);
                }
                synchronized(ar) {
                    /**间隔50毫秒**/
                    ar.wait(1000);
                }
        }

        jopc.setGroupActivity(group, true);
        jopc.setGroupUpdateTime(group, 100);

        start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 10000) {
                jopc.ping();
                downGroup = jopc.getDownloadGroup();
                if (downGroup != null) {
                    opcItemList.addAll(downGroup.getItems());
                }
                synchronized(ar) {
                    ar.wait(2000);
                }
        }
        logger.debug("断开异步读取");
        jopc.asynch10Unadvise(group);
        coUninitialize();
        return opcItemList;
    }

}

