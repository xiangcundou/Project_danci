package com.cycycd.admin.project_danci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Admin on 2017/9/11.
 *
 */

public class NetThread extends Thread {
    private String word;
    private final String key="cycycd123";
    private String url;
    public void setWord(String w)
    {
        this.word=w;
    }
    public NetThread(String u)
    {
        this.url=u;
    }
    //发送http请求  将结果存入全局变量中
    private void methodGet()
    {
        String content="word="+word+"&key="+key;
        try {
            URL httpurl=new URL(url+"?"+content);
            HttpURLConnection conn=(HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb=new StringBuffer();
            String str;
            while((str=reader.readLine())!=null)
            {
                sb.append(str);
            }
            Global.putNetRes(sb.toString());
        } catch (MalformedURLException e) {
            Global.putNetRes("{'code':'-1','tran':'0','phonetic':'0','exp':'0'}");
        } catch (IOException e) {
            Global.putNetRes("{'code':'-1','tran':'0','phonetic':'0','exp':'0'}");
        }
    }
    @Override
    public void run()
    {
        methodGet();
    }
}
