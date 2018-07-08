package chihane.jdaddressselector.picker;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import chihane.jdaddressselector.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 朱宏博 on 2017/12/1.
 */

public class PickTimePopupWindow extends PopupWindow implements PickerView.onSelectListener{

    public interface DatePickListener{
        void onPickDate(Integer year, Integer month, Integer day);
    }

    PickTimePopupWindow.DatePickListener mPickListener;
    Activity mParentActivity;

    PickerView mYearPickerView;
    PickerView mMonthPickerView;
    PickerView mDayPickerView;



    public PickTimePopupWindow(Activity activity, Integer year, Integer month, Integer day) {
        mParentActivity = activity;

        if (initView(year, month, day)) {
            refreshDayData(year, month, day);
        }
    }

    public void setDatePickListener(PickTimePopupWindow.DatePickListener datePickListener) {
        mPickListener = datePickListener;
    }

    protected boolean initView(Integer year, Integer month, Integer day) {
        LayoutInflater mInflater = LayoutInflater.from(mParentActivity);
        View view = mInflater.inflate(R.layout.time_pick, null, true);
        this.setContentView(view);// 设置显示的视图
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置窗体的宽度
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置窗体的高度
        this.setFocusable(true);// 设置窗体可点击
        this.setOutsideTouchable(true);

        mYearPickerView = (PickerView)view.findViewById(R.id.pv_year);
        mMonthPickerView = (PickerView)view.findViewById(R.id.pv_month);
        mDayPickerView = (PickerView)view.findViewById(R.id.pv_day);

        if (mYearPickerView == null || mMonthPickerView == null || mDayPickerView == null) {
            return false;
        }

        // int thisYear = new Date().getHours();
        List<Pair<String, Object>> yearList = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            yearList.add(new Pair<String, Object>(String.valueOf(i), i));
        }
        mYearPickerView.setData(yearList);
        mYearPickerView.setOnSelectListener(this);
        mYearPickerView.setSelected(year);


        List<Pair<String, Object>> monthList = new ArrayList<>();
        for (int i = 0; i <= 59 ; i++) {
            monthList.add(new Pair<String, Object>(String.valueOf(i), i));
        }
        mMonthPickerView.setData(monthList);
        mMonthPickerView.setOnSelectListener(this);
        mMonthPickerView.setSelected(month);


        TextView btnClose = (TextView) view.findViewById(R.id.pick_pop_close);
        if (btnClose != null) {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        TextView btnComplete = (TextView) view.findViewById(R.id.pick_pop_complete);
        if (btnComplete != null) {
            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPickListener != null) {
                        mPickListener.onPickDate((Integer)mYearPickerView.getCurrentData(), (Integer)mMonthPickerView.getCurrentData(), (Integer)mDayPickerView.getCurrentData());
                    }
                    dismiss();
                }
            });
        }

        return true;
    }

    protected void refreshDayData(Integer year, Integer month, Integer day) {
        final Integer bigMonth[] = {1, 3, 5, 7, 8, 10, 12};
        final Integer littleMonth[] = {4, 6, 9, 11};

        int daysOfMonth = 30;
        if (month == 2) {
            if (isLeapYear(year)) {
                daysOfMonth = 29;
            } else {
                daysOfMonth = 28;
            }
        } else {
            List<Integer> bigMonthList = Arrays.asList(bigMonth);
            List<Integer> littleMonthList = Arrays.asList(littleMonth);

            if (bigMonthList.contains(month)) {
                daysOfMonth = 31;
            }else if (littleMonthList.contains(month)) {
                daysOfMonth = 30;
            }
        }

        List<Pair<String, Object>> dayList = new ArrayList<>();
        //   for (int i = 1; i <= daysOfMonth; i++) {
        for (int i = 0; i <= 50; i++) {
            dayList.add(new Pair<String, Object>(String.valueOf(i), i));
        }
        mDayPickerView.setData(dayList);
        mDayPickerView.setSelected(0);
    }

    protected boolean isLeapYear(Integer year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        }
        return  false;
    }


    @Override
    public void onSelect(String text, Object data) {
        refreshDayData((Integer) mYearPickerView.getCurrentData(), (Integer) mMonthPickerView.getCurrentData(), (Integer) mDayPickerView.getCurrentData());
    }
}
