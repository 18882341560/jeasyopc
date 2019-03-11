package com.fangfa.util;

import com.fangfa.bean.Opc;
import javafish.clients.opc.AsynchReadAndGroupActivityExample;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.*;

import java.text.SimpleDateFormat;

public class OpcUtil {
    public Opc opcUtil(String groupNmae, String itemId) {
        AsynchReadAndGroupActivityExample test = new AsynchReadAndGroupActivityExample();
        Opc opc = new Opc();//实例化opc对象
        try {
            /**初始化服务**/
            JOpc.coInitialize();
        } catch (CoInitializeException e1) {
            e1.printStackTrace();
        }
        /**建立server对象jopc(IP ,OPCServer名称 ,任意)**/
        JOpc jopc = new JOpc("localhost", "OSI.DA.1", "JOPC1");
        /**建立一个组((用户组的标识名称),开始活动的组(true 开始 false 不开始) 默认true,刷新组的时间 毫秒,默认 0.0f)**/
        OpcGroup group = new OpcGroup(groupNmae, true, 20, 0.0f);
        group.getItems();

        /**name属性表示OPC服务器中的ItemID**/
        OpcItem item = new OpcItem(itemId, true, "");
        /**添加到组**/
        group.addItem(item);
        jopc.addGroup(group);
        try {
            /**调用JCustomOpc.connect()连接服务器**/
            jopc.connect();
            System.out.println("OPC client is connected...");
            System.out.println("OPC 服务器连接成功...");
            /** 调用JOpc.registerGroups()注册所有的组使用registerGroups()方法注册则OpcItem不用单独注册。如果调用registerGroup(OpcGroup)注册OpcGroup，则还需调用registerItem(OpcGroup,OpcItem)**/
            jopc.registerGroups();
            System.out.println("OPC groups are registered...");
            System.out.println("OPC groups 注册成功...");
            /** 调用JOpc.asynch10Read()异步读数据**/
            jopc.asynch10Read(group);
            System.out.println("OPC asynchronous reading is applied...");
            System.out.println("OPC 正在异步读取数据...");
//            synchronized(test) {
//                /**间隔50毫秒**/
//                test.wait(50);
//            }
            OpcGroup downGroup;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                OpcItem responseItem = jopc.synchReadItem(group, item);

                opc.setTimeStamp(sdf.format(responseItem.getTimeStamp().getTime()));//将获取的时间存进对象
                opc.setItemValue(responseItem.getValue());//将获取的VT_R8值value存进对象

                System.out.println("Opc对象封装的时间：" + opc.getTimeStamp());//从opc对象里取出时间
                System.out.println("Opc对象封装的值：" + opc.getItemValue());//从opc对象里取出value值
                //System.out.println("这是封装在对象里面的时间和value数据："+opc.toString());
            } catch (SynchReadException e) {
                e.printStackTrace();
            } catch (Exception E) {//
                E.printStackTrace();
            }
            /**当前时间秒数**/
//            long start = System.currentTimeMillis();
//            while ((System.currentTimeMillis() - start) < 10000) {
//                jopc.ping();
//                downGroup = jopc.getDownloadGroup();
//                if (downGroup != null) {
//                    System.out.println("这是数据："+downGroup);
//
//                    //Opc opc = new Opc();
//
//                }
//
//                if ((System.currentTimeMillis() - start) >= 6000) {
//                    jopc.setGroupActivity(group, false);
//                }
//            }

            // change activity
//            jopc.setGroupActivity(group, true);
//
//            // change updateTime
//            jopc.setGroupUpdateTime(group, 100);
//
//            start = System.currentTimeMillis();
//            while ((System.currentTimeMillis() - start) < 10000) {
//                jopc.ping();
//                downGroup = jopc.getDownloadGroup();
//                if (downGroup != null) {
//                    System.out.println("这是数据："+downGroup.toString());
//                }
//            }
            /**断开异步读取**/
            jopc.asynch10Unadvise(group);
            System.out.println("OPC asynchronous reading is unadvise...");
            System.out.println("OPC 断开异步读取...");
            /**断开连接**/
            JOpc.coUninitialize();
            System.out.println("Program terminated...");
            System.out.println("断开服务器连接...");
        } catch (ConnectivityException e) {
            e.printStackTrace();
        } catch (UnableAddGroupException e) {
            e.printStackTrace();
        } catch (UnableAddItemException e) {
            e.printStackTrace();
        } catch (ComponentNotFoundException e) {
            e.printStackTrace();
        } catch (Asynch10ReadException e) {
            e.printStackTrace();
        } catch (Asynch10UnadviseException e) {
            e.printStackTrace();
        }
//        catch (GroupUpdateTimeException e) {
//            e.printStackTrace();
//        }
//        catch (GroupActivityException e) {
//            e.printStackTrace();
//        }
        catch (CoUninitializeException e) {
            e.printStackTrace();
        }
        return opc;
    }
}
