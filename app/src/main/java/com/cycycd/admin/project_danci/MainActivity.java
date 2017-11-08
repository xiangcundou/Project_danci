package com.cycycd.admin.project_danci;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<WordCard> mWord=new ArrayList<>();
    ImageView col;
    EditText input;
    CardView searchbt,resCard,cshow;
    TextView cn,en,yb,exp;
    WordAdapter wAdapter;
    DrawerLayout op;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input=(EditText) findViewById(R.id.search_area);
        searchbt=(CardView)findViewById(R.id.search_bt);
        cn=(TextView) findViewById(R.id.cn_text);
        en=(TextView) findViewById(R.id.en_text);
        yb=(TextView) findViewById(R.id.yb_text);
        exp=(TextView) findViewById(R.id.ex_text);
        resCard=(CardView) findViewById(R.id.rescard);
        col=(ImageView) findViewById(R.id.collect);
        //判断是否为竖屏 如果是则设置监听事件
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            cshow=(CardView)findViewById(R.id.collectshowbt);
            //弹出收藏按钮
            cshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    op=(DrawerLayout) findViewById(R.id.opview);
                    op.openDrawer(GravityCompat.START);
                }
            });
        }

        //初始化 重载 加载DB等
        if(mWord.size()==0)
        {
            initWord();
        }
        if(Global.sersuccMark)
        {
            reloadcard();
        }
        RecyclerView rcv=(RecyclerView) findViewById(R.id.wordlist);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        rcv.setLayoutManager(lm);
        wAdapter=new WordAdapter(mWord);
        //适配器点击事件
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            op=(DrawerLayout) findViewById(R.id.opview);
            wAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    final WordCard w=mWord.get(position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            en.setText(w.word);
                            cn.setText(w.tran);
                            yb.setText(w.yb);
                            exp.setText(w.exp);
                            col.setImageResource(R.drawable.ic_like_x);
                            if(resCard.getVisibility()==View.INVISIBLE)
                            {
                                resCard.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    Global.isCollect=true;
                    Global.en=w.word;
                    Global.tran=w.tran;
                    Global.yb=w.yb;
                    Global.exp=w.exp;
                    if(!Global.sersuccMark)
                    {
                        Global.sersuccMark=true;
                    }
                    Global.locate=position;
                    op.closeDrawer(GravityCompat.START);
                }
            });
        }
        else
        {
            wAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    final WordCard w=mWord.get(position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            en.setText(w.word);
                            cn.setText(w.tran);
                            yb.setText(w.yb);
                            exp.setText(w.exp);
                            col.setImageResource(R.drawable.ic_like_x);
                            if(resCard.getVisibility()==View.INVISIBLE)
                            {
                                resCard.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    Global.isCollect=true;
                    Global.en=w.word;
                    Global.tran=w.tran;
                    Global.yb=w.yb;
                    Global.exp=w.exp;
                    if(!Global.sersuccMark)
                    {
                        Global.sersuccMark=true;
                    }
                    Global.locate=position;
                }
            });
        }
        rcv.setAdapter(wAdapter);
        //搜索按钮点击
        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮时 关闭输入法
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
                {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
                }
                //判断网络连接
                if (isNetworkConnected(MainActivity.this))
                {
                    getRes();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //彩蛋
        searchbt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(input.getText().toString().equals("cycycd"))
                {
                    Toast.makeText(MainActivity.this,"cheers!",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //收藏
        col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordCard w=new WordCard(Global.en,Global.tran,Global.yb,Global.exp);
                if(!Global.isCollect)
                {

                    collectWord(w,col);
                    Global.isCollect=true;
                }
                else
                {
                    discollectWord(w,col);
                    Global.isCollect=false;
                }
            }
        });
    }
    //搜索获取结果
    private void getRes()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetThread t=new NetThread("http://www.cycycd.com/api/youdao");
                t.setWord(input.getText().toString());
                t.start();
                try {
                    t.join();
                    //解析数据
                    Gson gs=new Gson();
                    final JsonRes res=gs.fromJson(Global.getNetRes(),JsonRes.class);
                    if(res.code.equals("1"))
                    {
                        int i=searWord(input.getText().toString());
                        if(i!=-1)
                        {
                            Global.locate=i;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                en.setText(input.getText().toString());
                                cn.setText(res.tran);
                                yb.setText(res.phonetic);
                                exp.setText(res.exp);
                                if(resCard.getVisibility()==View.INVISIBLE)
                                {
                                    resCard.setVisibility(View.VISIBLE);
                                }
                                if (Global.isCollect)
                                {
                                    col.setImageResource(R.drawable.ic_like_x);
                                }
                                else
                                {
                                    col.setImageResource(R.drawable.ic_like);
                                }
                            }
                        });
                        Global.en=input.getText().toString();
                        Global.tran=res.tran;
                        Global.yb=res.phonetic;
                        Global.exp=res.exp;
                        if(!Global.sersuccMark)
                        {
                            Global.sersuccMark=true;
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

    }
    private void reloadcard()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                en.setText(Global.en);
                cn.setText(Global.tran);
                yb.setText(Global.yb);
                exp.setText(Global.exp);
                if(resCard.getVisibility()==View.INVISIBLE)
                {
                    resCard.setVisibility(View.VISIBLE);
                }
                if (Global.isCollect)
                {
                    col.setImageResource(R.drawable.ic_like_x);
                }
                else
                {
                    col.setImageResource(R.drawable.ic_like);
                }
            }
        });
    }
    //插入语句
    private void insertWord(WordCard w)
    {
        Sqlexec sql=new Sqlexec(this,"wordbook.db",null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("word",w.word);
        values.put("tran",w.tran);
        values.put("yb",w.yb);
        values.put("exp",w.exp);
        db.insert("Word",null,values);
    }
    //收藏语句
    private void collectWord(WordCard w, ImageView v)
    {
        //插入数据库
        insertWord(w);
        //心形改变
        v.setImageResource(R.drawable.ic_like_x);
        //toast提示
        Snackbar.make(v,"收藏成功",Snackbar.LENGTH_SHORT).show();
        Global.isCollect=true;
        mWord.add(w);
        wAdapter.notifyItemInserted(mWord.size()-1);
        Global.locate=mWord.size()-1;
    }

    //取消收藏
    private void discollectWord(WordCard w,ImageView v)
    {
        //心形改变
        v.setImageResource(R.drawable.ic_like);
        //删除数据
        deleteWord(w);
        Global.isCollect=false;
        Snackbar.make(v,"取消收藏",Snackbar.LENGTH_SHORT).show();
        mWord.remove(Global.locate);
        wAdapter.notifyItemRemoved(Global.locate);
    }
    //删除语句
    private void deleteWord(WordCard w)
    {
        Sqlexec sql=new Sqlexec(this,"wordbook.db",null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        db.delete("Word","word=?",new String[]{w.word});
    }
    //初始化数据
    private void initWord()
    {
        Sqlexec sql=new Sqlexec(this,"wordbook.db",null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        Cursor cus=db.rawQuery("select * from Word",null);
        if(cus.moveToFirst())
        {
            do{
                String a=cus.getString(cus.getColumnIndex("word"));
                String b=cus.getString(cus.getColumnIndex("tran"));
                String c=cus.getString(cus.getColumnIndex("yb"));
                String d=cus.getString(cus.getColumnIndex("exp"));
                mWord.add(new WordCard(a,b,c,d));
            }while(cus.moveToNext());
        }
        cus.close();
    }
    private int searWord(String w)
    {
        int res=-1;
        Sqlexec sql=new Sqlexec(this,"wordbook.db",null,1);
        SQLiteDatabase db=sql.getWritableDatabase();
        Cursor cus=db.rawQuery("select * from Word where word=?",new String[]{w});
        if(cus.moveToFirst())
        {
            Global.isCollect=true;
            for(int i = 0 ; i < mWord.size() ; i++) {
                if(w.equals(mWord.get(i).word))
                {
                    res=i;
                    break;
                }
            }
            cus.close();
            return res;
        }
        else
        {
            Global.isCollect=false;
            cus.close();
            return -1;
        }
    }
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if(info != null && info.isAvailable()){
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //WiFi网络
                return true;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //移动网络
                return true;
            } else {
                //网络错误
                return false;
            }
        }else{
            //网络错误
            return false;
        }
    }
}
