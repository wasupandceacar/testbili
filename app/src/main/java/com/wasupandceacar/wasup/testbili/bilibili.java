package com.wasupandceacar.wasup.testbili;

import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class bilibili extends AppCompatActivity {

    String imgSrc=null;

    //判断整数
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public void download(String avstring) {
        String avurl="http://www.bilibili.com/video/av"+avstring+"/";
        Document document= null;
        try {
            Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
            document = Jsoup.connect(avurl).get();
            //获取所有的img标签
            Elements elements = document.getElementsByTag("img");
            imgSrc = elements.first().attr("abs:src");
            if(imgSrc==null){
                Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            }else {
                //下载图片
                OutputStream output = null;
                URL url = new URL(imgSrc);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String SDCard = Environment.getExternalStorageDirectory() + "";
                String pathName = SDCard + "/BiliSnapshot/" + avstring + imgSrc.substring(imgSrc.length() - 4);//文件存储路径
                File file = new File(pathName);
                InputStream input = conn.getInputStream();
                if (file.exists()) {
                    System.out.println("exits");
                    return;
                } else {
                    String dir = SDCard + "/BiliSnapshot";
                    new File(dir).mkdir();//新建文件夹
                    file.createNewFile();//新建文件
                    output = new FileOutputStream(file);
                    //读取大文件
                    int i = 0;
                    while ((i = input.read()) != -1) {
                        output.write(i);
                    }
                    input.close();
                    output.flush();
                }
                Toast.makeText(this, "已下载到" + SDCard + "/BiliSnapshot", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "此视频不存在或无写入权限（请授权）", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadPic(View view) throws IOException {
        EditText av=(EditText)findViewById(R.id.avedit);
        String avstring=av.getText().toString();
        if(isNumeric(avstring)){
            download(avstring);
        }else{
            Toast.makeText(this, "请输入av号（纯数字）", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bilibilicatcher);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }
}
