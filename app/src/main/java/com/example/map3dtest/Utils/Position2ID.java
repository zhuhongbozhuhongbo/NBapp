package com.example.map3dtest.Utils;

/**
 * Created by 朱宏博 on 2018/7/7.
 */

public class Position2ID {
    public static String p2ID(int position){

        switch (position){
            case 1:
                return "4";
            case 2:
                return "5";
            case 3:
                return "2";
            case 4:
                return "6";
            case 5:
                return "3";
        }
        return "1";

    }

    public static String nplatte2ID(String numberplatte){
        if(numberplatte.equals("豫C 83576")){
            return "4";
        }else if(numberplatte.equals("皖K M1863")){
            return "5";
        }else if(numberplatte.equals("鄂C 9C219")){
            return "6";
        }
        return "0";
    }

    public static String province2ID(String province){
        if(province.equals("江苏")){
            return "太湖隧道项目";
        }

        return "";
    }

 /*   public static String agent2Prj(String agentName){
        if(agentName.equals("豫C 83576")){
            return
        }
    }*/
}
