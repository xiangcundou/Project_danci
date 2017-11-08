package com.cycycd.admin.project_danci;

/**
 * Created by Admin on 2017/10/16.
 */

public class Global {
    //网络抓取的JSON
    private static String net_res;
    public static String getNetRes()
    {
        return net_res;
    }
    public static void putNetRes(String input)
    {
        net_res=input;
    }


    //临时变量

    //是否进行过成功的搜索
    public static boolean sersuccMark=false;

    //数据段
    public static String en;
    public static String tran;
    public static String yb;
    public static String exp;

    //是否被收藏
    public static boolean isCollect;

    //位置
    public static int locate;
}
