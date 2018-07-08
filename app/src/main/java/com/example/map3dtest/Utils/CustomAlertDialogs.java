package com.example.map3dtest.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.map3dtest.charts.ChartActivitySec;
import com.example.map3dtest.tables.ProjectSheetActivity;

/**
 * Created by 朱宏博 on 2018/7/7.
 */

public class CustomAlertDialogs {
    public static String[] choiceItems = {"数据图表", "轨迹重现"};

    public static void simple(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("查询类型")
                //.setMessage("对话框")//注：若开启message则不能显示选择项
                .setCancelable(true)
                .setSingleChoiceItems(choiceItems, 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Log.d("haha", "选中了" + which);
                        switch (which){
                            case 0:
                                Intent intent =new Intent(context,ChartActivitySec.class);
                                //startActivity(intent);

                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
