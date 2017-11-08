package com.cycycd.admin.project_danci;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类
 * Created by Administrator on 2017/10/19.
 */

public class Sqlexec extends SQLiteOpenHelper {
    private static final String CREATE_WORD="create table Word("
        +"word text primary key,"
        +"tran text,"
        +"yb text,"
        +"exp text)";
    Context con;
    public Sqlexec(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);
        con=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_WORD);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion )
    {
        //升级
    }
}
