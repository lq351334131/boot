package com.etocrm.sdk.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author qi.li
 * @create 2020/9/17 15:35
 */
public class TxtUtils {
    public void saveToFile(HashMap map){

        try {
            StringBuffer str = new StringBuffer();
            //FileWriter fw = new FileWriter("/opt/sdkserver.txt", true);
            String path=System.getProperty("user.dir")+"/"+getday()+"/"+getHour()+".txt";
            String projectPath=System.getProperty("user.dir");
            // String path2=new File(projectPath).getParent();
            File f=new File(path);
            String directory = path.replace("/"+f.getName(), "");
            File f2=new File(directory);
            //创建文件，一定要先有目录
            //创建目录
            if(!f2.exists())
            {
                f2.mkdirs();
            }
            //创建文件
            if(!f.exists())
            {
                f.createNewFile();
            }
            FileWriter fw= new FileWriter(f,true);
            Set set = map.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                str.append(entry.getKey() + "--" + entry.getValue()+"\r\n");
            }
            fw.write(str.toString());
            fw.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    private String getday(){
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return  returnStr = f.format(date);
    }

    private String getHour(){
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("HH");
        Date date = new Date();
        return returnStr = f.format(date);
    }


}
