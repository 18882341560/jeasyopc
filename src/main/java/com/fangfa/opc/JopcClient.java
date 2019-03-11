//package com.fangfa.opc;
//
//import javafish.clients.opc.AsynchReadAndGroupActivityExample;
//import javafish.clients.opc.JOpc;
//import javafish.clients.opc.component.OpcGroup;
//import javafish.clients.opc.component.OpcItem;
//import javafish.clients.opc.exception.Asynch10ReadException;
//import javafish.clients.opc.exception.Asynch10UnadviseException;
//import javafish.clients.opc.exception.CoInitializeException;
//import javafish.clients.opc.exception.ComponentNotFoundException;
//import javafish.clients.opc.exception.ConnectivityException;
//import javafish.clients.opc.exception.CoUninitializeException;
//import javafish.clients.opc.exception.GroupActivityException;
//import javafish.clients.opc.exception.GroupUpdateTimeException;
//import javafish.clients.opc.exception.UnableAddGroupException;
//import javafish.clients.opc.exception.UnableAddItemException;
//
//import java.text.SimpleDateFormat;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * 使用Jopc连接Opc服务器，待封装,支持32位
// *@author  gl
// *@date    2018/6/7/007
// *@param
// *@return
// */
//public class JopcClient {
//
//
//    public static List<OpcGroup> test(List<String> list) throws InterruptedException {
//        AsynchReadAndGroupActivityExample test = new AsynchReadAndGroupActivityExample();
//        List<OpcGroup> opcList = new LinkedList<>();
//        try {
//            /**初始化服务**/
//            JOpc.coInitialize();
//        }
//        catch (CoInitializeException e1) {
//            e1.printStackTrace();
//        }
//        /**建立server对象jopc(IP ,OPCServer名称 ,任意)**/
//        JOpc jopc = new JOpc("localhost", "OSI.DA.1", "JOPC1");
//
//        /**建立一个组((用户组的标识名称),开始活动的组(true 开始 false 不开始) 默认true,刷新组的时间 毫秒,默认 0.0f)**/
//        OpcGroup group = new OpcGroup("group1", true, 20, 0.0f);
//
//        /**name属性表示OPC服务器中的ItemID**/
////        OpcItem item1 = new OpcItem("\\\\10.89.6.6\\T250360EI00000010", true, "");
////        OpcItem item2 = new OpcItem("\\\\10.89.6.6\\T250360CP00000010", true, "");
//
//        /**添加到组**/
////        group.addItem(item1);
////        group.addItem(item2);
//
//        list.forEach( i ->{
//            OpcItem item1 = new OpcItem(i, true, "");
//            group.addItem(item1);
//        });
//
//        jopc.addGroup(group);
//
//        try {
//            /**调用JCustomOpc.connect()连接服务器**/
//            jopc.connect();
//
//            /** 调用JOpc.registerGroups()注册所有的组使用registerGroups()方法注册则OpcItem不用单独注册。如果调用registerGroup(OpcGroup)注册OpcGroup，则还需调用registerItem(OpcGroup,OpcItem)**/
//            jopc.registerGroups();
//
//            /** 调用JOpc.asynch10Read()异步读数据**/
//            jopc.asynch10Read(group);
//
//            OpcGroup downGroup;
//
//            /**当前时间秒数**/
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            long start = System.currentTimeMillis();
//
//            while ((System.currentTimeMillis() - start) < 10000) {
//                jopc.ping();
//                downGroup = jopc.getDownloadGroup();
//                if (downGroup != null) {
//                    System.out.println(downGroup+"===================================");
//                    opcList.add(downGroup);
////                    downGroup.getTimeStamp().getTime());//将获取的时间存进对象
////                    opc.setItemValue(responseItem.getValue());//将获取的VT_R8值value存进对象
//                }
//
////                if ((System.currentTimeMillis() - start) >= 6000) {
////                    jopc.setGroupActivity(group, false);
////                }
//                synchronized(test) {
//                    /**间隔50毫秒**/
//                    test.wait(1000);
//                }
//            }
//
//            // change activity
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
//                    System.out.println(downGroup+"------------------------------");
//                    opcList.add(downGroup);
//                }
//
//                synchronized(test) {
//                    test.wait(2000);
//                }
//            }
//            /**断开异步读取**/
//            jopc.asynch10Unadvise(group);
//            System.out.println("OPC asynchronous reading is unadvise...");
//            System.out.println("OPC 断开异步读取...");
//            /**断开连接**/
//            JOpc.coUninitialize();
//            System.out.println("Program terminated...");
//            System.out.println("断开服务器连接...");
//        }
//        catch (ConnectivityException e) {
//            e.printStackTrace();
//        }
//        catch (UnableAddGroupException e) {
//            e.printStackTrace();
//        }
//        catch (UnableAddItemException e) {
//            e.printStackTrace();
//        }
//        catch (ComponentNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (Asynch10ReadException e) {
//            e.printStackTrace();
//        }
//        catch (Asynch10UnadviseException e) {
//            e.printStackTrace();
//        }
//        catch (GroupUpdateTimeException e) {
//            e.printStackTrace();
//        }
//        catch (GroupActivityException e) {
//            e.printStackTrace();
//        }
//        catch (CoUninitializeException e) {
//            e.printStackTrace();
//        }
//        return opcList;
//    }
//
//}
