package com.cycycd.admin.project_danci;

/**
 * Created by Administrator on 2017/10/20.
 */

public class WordCard {
    public String word;
    public String tran;
    public String yb;
    public String exp;
    //构造 默认为无收藏
    public WordCard(String s1,String s2,String s3,String s4)
    {
        word=s1;
        tran=s2;
        yb=s3;
        exp=s4;
    }
}
