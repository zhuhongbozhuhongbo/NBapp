package chihane.jdaddressselector.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import chihane.jdaddressselector.R;
/**
 * 注意布局文件date_pick和time_pick，他们中有的控件引用了PickerView.java
 */

public class TimePickerActivity extends AppCompatActivity {
    private TextView mtime_select;//启动时间选择器的按钮
    private TextView mdate_select;
    private int saved_year = 2018;
    private int saved_month = 4;
    private int saved_day = 1;
    private int saved_hour = 0;
    private int saved_minute = 0;
    private int saved_second = 0;

    private String dateText = "2018-4-1";
    private String timeText = "00:00:00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /*   setContentView(R.layout.activity_chart);
        initDate();
        initTime();*/

    }


  /*  private void initDate() {//dialogpicker
        mdate_select = (TextView) findViewById(R.id.date_select);
        mdate_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickDatePopupWindow pickDatePopupWindow = new PickDatePopupWindow(TimePickerActivity.this, saved_year, saved_month, saved_day);
                pickDatePopupWindow.setDatePickListener(new PickDatePopupWindow.DatePickListener() {
                    @Override
                    public void onPickDate(Integer year, Integer month, Integer day) {
                        dateText = String.format("%d-%02d-%02d", year, month, day);
                        saved_day = day;
                        saved_month = month;
                        saved_year = year;
                        Log.d("haha", dateText);
                        mdate_select.setText(dateText);

                    }
                });
                pickDatePopupWindow.showAtLocation(mdate_select, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void initTime(){
        mtime_select = (TextView) findViewById(R.id.time_select);
        mtime_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给出一个默认值或者是回去mTimeContent中的String格式化

                PickTimePopupWindow pickTimePopupWindow = new PickTimePopupWindow(TimePickerActivity.this, saved_hour, saved_minute, saved_second);
                pickTimePopupWindow.setDatePickListener(new PickTimePopupWindow.DatePickListener() {
                    @Override
                    public void onPickDate(Integer hour, Integer minute, Integer second) {
                        timeText = String.format("%d-%02d-%02d", hour, minute, second);
                        saved_second = second;
                        saved_minute = minute;
                        saved_hour = hour;
                        Log.d("haha", timeText);
                        mtime_select.setText(timeText);

                    }
                });
                pickTimePopupWindow.showAtLocation(mdate_select, Gravity.BOTTOM, 0, 0);
            }
        });
    }*/
}
