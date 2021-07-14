package com.etocrm.sdk.util;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @Author qi.li
 * @create 2020/12/2 18:20
 */
public class IpLocationTool{

    private static String IP_FILE = "qqwry.dat"; // 纯真IP数据库名

    private static IPLocation loc = new IPLocation(); // 为提高效率而采用的临时变量
    private static Map<String, IPLocation> cache = new HashMap<String, IPLocation>(); // 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
    private static final int IP_RECORD_LENGTH = 7;
    private static final byte REDIRECT_MODE_1 = 0x01;
    private static final byte REDIRECT_MODE_2 = 0x02;
    private static long ipBegin;
    private static long ipEnd;
    private static byte[] buf = new byte[100];;
    private static byte[] b4 = new byte[4];;
    private static byte[] b3 = new byte[3];
    private static RandomAccessFile ipFile;
    static {
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/qqwry.dat");
            File file = classPathResource.getFile();
            //IpLocationTool.class.getClassLoader().getResource(IP_FILE);
            //InputStream inoput=IpLocationTool.class.getClassLoader().getResourceAsStream(IP_FILE);
         //   File qqWryDataFile = new File(IpLocationTool.class.getClassLoader().getResource(IP_FILE).getFile());
            ipFile = new RandomAccessFile(file, "r");
            ipBegin = readLong4(0);
            ipEnd = readLong4(4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取城市
     *
     * @param ip
     * @return
     */
    public static Map<String,String> getCity(String ip) {
        //return getLoc(ip).getCountry().replaceAll("省", "-").replaceAll("市", "");
        Map<String,String> locaMap =new HashMap<String, String>();
        locaMap.put("province",getLoc(ip).getCountry().replaceAll("省", ""));
        locaMap.put("city",getLoc(ip).getCountry().replaceAll("市", ""));
        return locaMap;
    }
    public static String getCity2(String ip) {
        return getLoc(ip).getCountry().replaceAll("省", "-").replaceAll("市", "");
    }

    /**
     * 获取IP地址
     *
     * @param ip
     * @return
     */
    public static IPLocation getLoc(String ip) {
        IPLocation result = cache.get(ip);
        if (result == null) {
            result = getIPLocation(getIpByteArrayFromString(ip));
        }
        return result;
    }

    private static IPLocation getIPLocation(byte[] ip) {
        IPLocation info = null;
        long offset = locateIP(ip);
        if (offset != -1)
            info = getIPLocation(offset);
        if (info == null) {
            info = new IPLocation();
            info.setCountry("");
            info.setArea("");
        }
        return info;
    }

    // ===================================================================================================================================================
    // ===================================================================================================================================================
    // ===================================================================================================================================================
    public static class IPLocation {
        private String country = "";
        private String area = "";

        public IPLocation getCopy() {
            IPLocation ret = new IPLocation();
            ret.country = country;
            ret.area = area;
            return ret;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        protected void setArea(String area) {
            if (area.trim().equals("CZ88.NET")) {
                this.area = "";
            } else {
                this.area = area;
            }
        }
    }

    private static long readLong4(long offset) {

        long ret = 0;
        try {
            ipFile.seek(offset);
            ret |= (ipFile.readByte() & 0xFF);
            ret |= ((ipFile.readByte() << 8) & 0xFF00);
            ret |= ((ipFile.readByte() << 16) & 0xFF0000);
            ret |= ((ipFile.readByte() << 24) & 0xFF000000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    private static long readLong3(long offset) {
        long ret = 0;
        try {
            ipFile.seek(offset);
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    private static long readLong3() {
        long ret = 0;
        try {
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    private static void readIP(long offset, byte[] ip) {
        try {
            ipFile.seek(offset);
            ipFile.readFully(ip);
            byte temp = ip[0];
            ip[0] = ip[3];
            ip[3] = temp;
            temp = ip[1];
            ip[1] = ip[2];
            ip[2] = temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0)
                return r;
        }
        return 0;
    }

    private static int compareByte(byte b1, byte b2) {
        if ((b1 & 0xFF) > (b2 & 0xFF))
            return 1;
        else if ((b1 ^ b2) == 0)
            return 0;
        else
            return -1;
    }

    private static long locateIP(byte[] ip) {
        long m = 0;
        int r;
        readIP(ipBegin, b4);
        r = compareIP(ip, b4);
        if (r == 0)
            return ipBegin;
        else if (r < 0)
            return -1;
        for (long i = ipBegin, j = ipEnd; i < j;) {
            m = getMiddleOffset(i, j);
            readIP(m, b4);
            r = compareIP(ip, b4);
            // log.debug(Utils.getIpStringFromBytes(b));
            if (r > 0)
                i = m;
            else if (r < 0) {
                if (m == j) {
                    j -= IP_RECORD_LENGTH;
                    m = j;
                } else
                    j = m;
            } else
                return readLong3(m + 4);
        }
        m = readLong3(m + 4);
        readIP(m, b4);
        r = compareIP(ip, b4);
        if (r <= 0)
            return m;
        else
            return -1;
    }

    private static long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / IP_RECORD_LENGTH;
        records >>= 1;
        if (records == 0)
            records = 1;
        return begin + records * IP_RECORD_LENGTH;
    }

    private static IPLocation getIPLocation(long offset) {
        try {
            ipFile.seek(offset + 4);
            byte b = ipFile.readByte();
            if (b == REDIRECT_MODE_1) {
                long countryOffset = readLong3();
                ipFile.seek(countryOffset);
                b = ipFile.readByte();
                if (b == REDIRECT_MODE_2) {
                    loc.setCountry(readString(readLong3()));
                    ipFile.seek(countryOffset + 4);
                } else
                    loc.setCountry(readString(countryOffset));
                loc.setArea(readArea(ipFile.getFilePointer()));
            } else if (b == REDIRECT_MODE_2) {
                loc.setCountry(readString(readLong3()));
                loc.setArea(readArea(offset + 8));
            } else {
                loc.setCountry(readString(ipFile.getFilePointer() - 1));
                loc.setArea(readArea(ipFile.getFilePointer()));
            }
            return loc;
        } catch (IOException e) {
            return null;
        }
    }

    private static String readArea(long offset) throws IOException {
        ipFile.seek(offset);
        byte b = ipFile.readByte();
        if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
            long areaOffset = readLong3(offset + 1);
            if (areaOffset == 0)
                return "";
            else
                return readString(areaOffset);
        } else
            return readString(offset);
    }

    private static String readString(long offset) {
        try {
            ipFile.seek(offset);
            int i;
            for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile.readByte())
                ;
            if (i != 0)
                return getString(buf, 0, i, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }

}
