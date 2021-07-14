package com.etocrm.sdk.util;

import java.util.ArrayList;
import java.util.List;

public class PageTime {

    public  static  String  twentys="20S以下";

    public  static  String  sixtys="20-60S";

    public  static  String  two="1-2min";

    public  static  String  five="2-5min";

    public  static  String  twenty="10-20min";

    public  static  String  thirty="20-30min";

    public  static  String  forty="30-40min";

    public  static  String  sixty="40-60min";

    public  static  String  oh="1-2h";

    public  static  String  th="2h以上";


    public static   List<String> getPageTime(){
        List<String> list=new ArrayList<>();
        list.add(PageTime.twentys);
        list.add(PageTime.sixtys);
        list.add(PageTime.two);
        list.add(PageTime.five);
        list.add(PageTime.twenty);
        list.add(PageTime.thirty);
        list.add(PageTime.forty);
        list.add(PageTime.sixty);
        list.add(PageTime.oh);
        list.add(PageTime.th);
        return list;
    }

}
