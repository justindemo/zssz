package com.xytsz.xytsz.global;


import com.xytsz.xytsz.R;
import com.xytsz.xytsz.net.NetUrl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by admin on 2017/1/11.
 *
 * 基础设施
 */
public class Data {
    /**
     * 当前处置状态
     */
    public static final String[] phaseIndaciton = {"审核", "下派", "处置", "验收", "验收通过", "审核未通过"};

    /*
    * 处置等级*/
    public static String[] grades = new String[]{"一般病害", "应急抢险", "违章施工", "日常养护", "社会举报", "网格上报"};
    public static String[] departments = new String[]{"巡查一科", "巡查二科", "养护一段", "养护二段", "监控室","临时工"};

    //病害名称
    public static String[] pbname = new String[]{"一类病害", "二类病害", "三类病害"};




}
