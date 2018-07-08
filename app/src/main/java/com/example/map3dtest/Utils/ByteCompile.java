package com.example.map3dtest.Utils;
//:

import java.util.Base64;

/**
 * Created by HeRui on 2018/4/7.
 */
public class ByteCompile {


    /**
     * to convert a byte to boolean
     * caution: only a byte!
     * @param  b: the data in form of byte
     * @return true if the byte is positive, false when the byte is negative
     */

    public static boolean byte2Boolean(byte b){

        if(b>0)
        {
            return true;
        }else
        {
            return false;
        }

    }
    /**
     * to convert  bytes in terms of "yyyy-MM-dd"
     * @param  yearByte: the byte containing information of
     * @return true if the byte is positive, false when the byte is negative
     */
    public  static String byte2Date(byte yearByte,byte monthByte,byte dayByte){

        int yearInt = 2000 + (int)(yearByte & 0xff);
        int monthInt = (int)(monthByte & 0xff);
        int dayInt = (int)(dayByte & 0xff);
        String date = ""+yearInt+"-"+monthInt+"-"+dayInt;
        return date;
    }

    public  static String byte2Time(byte hourByte,byte minuteByte,byte secondByte){

        int hourInt = (int)(hourByte & 0xff);
        int minuteInt = (int)(minuteByte & 0xff);
        int secondInt = (int)(secondByte & 0xff);
        String time = ""+hourInt+":"+minuteInt+":"+secondInt;
        return time;
    }

    public static String byte2Id(byte b1,byte b2,byte b3,byte b4,byte b5,byte b6){

        long i = (long) ((((b6 & 0xff) << 40)
                | ((b5 & 0xff) << 32)
                | ((b4 & 0xff) << 24)
                | ((b3 & 0xff) << 16)
                | ((b2 & 0xff) << 8)
                | ((b1 & 0xff) << 0)));
        return ""+i;
    }

    public static  String byte2GPS(byte b1,byte b2,byte b3,byte b4,byte b5,byte b6,byte b7, byte b8){

        String encoded = new String();
        byte[] bs = new byte[]{b1,b2,b3,b4,b5,b6,b7,b8};
        return  encoded = Base64.getEncoder().encodeToString(bs);


    }

    public static int byte2Int(byte b1, byte b2,byte b3 ,byte b4) {
        return   b4 & 0xFF |
                (b3 & 0xFF) << 8 |
                (b2 & 0xFF) << 16 |
                (b1 & 0xFF) << 24;
    }

}
