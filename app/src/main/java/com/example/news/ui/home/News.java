package com.example.news.ui.home;

import com.mysql.jdbc.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class News implements Serializable {
    private static final long serialVersionUID = 123456;
    private String ID;
    private String title;
    private String publisher;
    private String publishTime;
    private String contents;
    private String type;
    private ArrayList<String> content_f;

    public News(String ID,String title,String publisher,String date,String contents,String type){
        this.ID = ID;
        this.title = title;
        this.publisher = publisher;
        this.publishTime = date;
        this.contents = contents;
        this.type = type;
        segmentation(contents);
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getContents() {
        return contents;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getContent_f() {
        return content_f;
    }

    public void setContent_f(ArrayList<String> content_f) {
        this.content_f = content_f;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPublishTime(String data) {
        this.publishTime = data;
    }

    public void setContents(String contents) {
        this.contents = contents;
        segmentation(contents);

    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void segmentation(String all){
        if(all!=null){
            String lines[] = all.split("\\r?\\n");
            int i=0;
            ArrayList<String> list = new ArrayList<>();
            while (i<lines.length){
                int n=0;
                ArrayList<String> temp = new ArrayList<>();
                temp.add(lines[i]);
                int len = lines[i].length();
                while(len<100){
                    int a = len;
                    n++;
                    if((i+n)<lines.length)
                        len=lines[i+n].length()+len;
                    else break;
                    temp.add(lines[i+n]);
                    if(len>200 && a!=0){
                        temp.remove(lines[i+n]);
                        n--;
                        break;
                    }
                }
                String str = "";
                for (String item : temp) {
                    // 把列表中的每条数据用逗号分割开来，然后拼接成字符串
                    str += item;
                }
                list.add(str);
                i=i+n+1;

            }
            list.add("已经是最后一页啦，返回新闻主页面阅读下一条新闻吧");
            setContent_f(list);
        }else {
            ArrayList<String> list = new ArrayList<>();
            setContent_f(list);
        }
    }
}
