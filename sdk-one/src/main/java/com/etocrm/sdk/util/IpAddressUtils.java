package com.etocrm.sdk.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author qi.li
 * @create 2020/12/2 16:45
 */
public class IpAddressUtils {

    private static AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 通过IPws查询
     * @param ip
     * @return
     */
    public static Map<String,String> getIpWs126Net2(String ip){
        Map<String, String> ipMap = new LinkedHashMap<String, String>();
        try{
            String res = HttpUtils.httpRequest(String.format("http://ip.ws.126.net/ipquery?ip=%s", ip),"GBK");
            if(StringUtils.isNotBlank(res)){
                res = res.substring(res.indexOf("localAddress") + 13, res.length());
                JSONObject jsonObject = new JSONObject(res);
                String city = jsonObject.getString("city");
              //  String area = jsonObject.getString("area");
                String province = jsonObject.getString("province");
                if(city.equals(province)){
                    ipMap.put("province",province);
                    ipMap.put("city",city);
                    return ipMap;
                }else{
                    ipMap.put("province",province);
                    ipMap.put("city",city);
                    return ipMap;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ipMap.put("province","l");
        ipMap.put("city","l");
        return ipMap;
    }

    public static String getIpWs126Net(String ip){

        try{
            Map<String, String> ipMap = new LinkedHashMap<String, String>();
            String res = HttpUtils.httpRequest(String.format("http://ip.ws.126.net/ipquery?ip=%s", ip),"GBK");
            if(StringUtils.isNotBlank(res)){
                res = res.substring(res.indexOf("localAddress") + 13, res.length());
                JSONObject jsonObject = new JSONObject(res);
                String city = jsonObject.getString("city");
                //  String area = jsonObject.getString("area");
                String province = jsonObject.getString("province");
                if(city.equals(province)){
                    ipMap.put("city",city);
                    return city;
                }else{
                    ipMap.put("province",province);
                    ipMap.put("city",city);
                    return province+city;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过IPAPI查询
     * @param ip
     * @return
     */
    public static String getIpAPI(String ip){
        try{
            String res = HttpUtils.httpRequest(String.format("http://ip-api.com/json/%s?lang=zh-CN", ip));
            if(!StringUtils.isEmpty(res)){
                JSONObject json = new JSONObject(res);
                String status = json.getString("status");
                if ("success".equals(status)) {
                    String regionName = json.getString("regionName");
                    String city = json.getString("city");
                    if(regionName.equals(city)){
                        return city;
                    }
                    return regionName + city;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * whois.pconline.com.cn  太平洋IP为最优选择
     * @param ip
     * @return
     */
    public static String getWhoisPconLineIp(String ip){
        try{
            String res = HttpUtils.httpRequest(String.format("http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true", ip),"GBK");
            if(!StringUtils.isEmpty(res)){
                JSONObject jsonObject = new JSONObject(res);
                String addr = jsonObject.getString("addr");
                return addr;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getAddressByIp(String ip){
        String addr = null;
        addr = getWhoisPconLineIp(ip);
        if(Objects.isNull(addr)){
            addr = getIpAPI(ip);
            if(Objects.isNull(addr)){
                addr = getIpWs126Net(ip);
                if(Objects.isNull(addr)){
                    return null;
                }
            }
        }
        return addr;
    }


    public static boolean isInnerIP(String ipAddress){
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum,aBegin,aEnd) || isInner(ipNum,bBegin,bEnd) || isInner(ipNum,cBegin,cEnd) || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    /**
     * 判断IP是否为内网IP
     * @param ipAddress
     * @return
     */
    private static long getIpNum(String ipAddress) {
        String [] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    private static boolean isInner(long userIp,long begin,long end){
        return (userIp>=begin) && (userIp<=end);
    }


    /**
     * 外部调用
     * @param ip
     * @return
     */
    public static String getAddressByIP(String ip){
        if(isInnerIP(ip)){
            return "内网IP";
        }else{
            return getAddressByIp(ip);
        }
    }




}
