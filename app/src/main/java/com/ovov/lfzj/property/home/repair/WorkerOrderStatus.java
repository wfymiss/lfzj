package com.ovov.lfzj.property.home.repair;

/**
 * Created by kaite on 2018/10/25.
 */

public class WorkerOrderStatus {
    public static String PENDING_DISPATH = "0000";//未派单
    public static String PENDING_RECIEPT = "0200";//未接单
    public static String REPAIR_ING = "001";//维修中
    public static String HAS_REFUSE = "002";//已拒单
    public static String PENDING_CHECK = "02";//待验收
    public static String HAS_COMPLETE = "1";//已完成
    public static String HAS_CANCEL = "2";//已取消
}
