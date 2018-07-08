package com.example.map3dtest.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;

import com.example.nbapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatisticsChartActivity extends AppCompatActivity implements OnChartValueSelectedListener{
    private PieChart mChart;
    private BarChart mBarChart;
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_chart);


        initPieChartView();
        initBarsChartView();
        initLineChartView();
    }

    /**
     * 饼状图
     */
    private void initPieChartView(){
        //饼状图
        mChart = (PieChart)findViewById(R.id.statistics_pie_chart);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);//减速摩擦力系数
        mChart.setCenterText(generateCenterSpannableText());
        mChart.setDrawHoleEnabled(true);//set this to true to draw the pie center empty
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);//Sets the color the transparent-circle should have.
        mChart.setTransparentCircleAlpha(110);//Sets the amount of transparency the transparent circle should have 0 = fully transparent,255 = fully opaque.
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(40, "优秀"));
        entries.add(new PieEntry(20, "满分"));
        entries.add(new PieEntry(20, "及格"));
        entries.add(new PieEntry(20, "不及格"));

        setPieChartData(entries);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);

    }

    /**
     * 饼状图
     */
    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString("刘某人程序员\n我仿佛听到有人说我帅");
        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private void setPieChartData(ArrayList<PieEntry> entries){
        PieDataSet dataSet = new PieDataSet(entries, "三年级一班");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for(int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for(int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        //刷新
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h){

    }

    @Override
    public void onNothingSelected(){

    }

    /**
     * 柱状图BarChart
     */
    private void initBarsChartView(){//ArrayList<BarEntry> entries1, ArrayList<BarEntry> entries2){
        mBarChart = (BarChart)findViewById(R.id.statistics_bar_chart);

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int i = 0; i < 20; i++) {
            // turn your data into Entry objects
            entries1.add(new BarEntry(i, 20 * i));
            entries2.add(new BarEntry(i,10 * i));
        }

        //创建数据集
        BarDataSet dataSet = new BarDataSet(entries1, "BarDataSet1"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet dataSet2 = new BarDataSet(entries2, "BarDataSet2"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置数据显示颜色：柱子颜色
        dataSet2.setColor(Color.RED);
        dataSet2.setBarBorderColor(Color.BLUE);

        float groupSpace=1f;
        float barSpace=0.2f;
        float barWidth=0.45f;

        BarData data = new BarData(dataSet, dataSet2);
        //设置柱子宽度
        data.setBarWidth(barWidth);

        mBarChart.setData(data);//装载数据
        mBarChart.groupBars(0f,groupSpace,barSpace);

        setGeneralStyling(mBarChart);
       /* IMarker marker= new MyMarkView(getContext(),R.layout.makerview);
        mBarChart.setMarker(marker);*/
        mBarChart.setDrawValueAboveBar(true);

        mBarChart.invalidate();//刷新


    }

    /**
     * 柱状图BarChart
     * @param chart
     */
    private void setGeneralStyling(BarChart chart) {
        //设置背景
        chart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //设置图表右下角出现的说明文本,以及说明文本的基本设置。
        //初始化一个Description对象
        Description desc=new Description();
        //设置文本内容
        desc.setText("haha");//(getString(R.string.desc));
        //设置文本大小
        desc.setTextSize(20f);
        //设置文本颜色
        desc.setTextColor(Color.WHITE);
        //设置文本位置，文本右上角相对于（0，0）的坐标位置
        desc.setPosition(400f,400f);
        //引入说明文本
        chart.setDescription(desc);
        //设置图表数据为空时候的文本
        chart.setNoDataText("gaga");//(getString(R.string.Nodata));
        //是否绘制图表背景的网格（开关，如果设置为false那么网格设置都失效）
        chart.setDrawGridBackground(true);
        //设置网格背景颜色
        chart.setGridBackgroundColor(Color.YELLOW);
        //设置边框开关
        chart.setDrawBorders(true);
        //设置边框颜色
        chart.setBorderColor(Color.CYAN);
        //设置边框宽度
        chart.setBorderWidth(20f);
        //设置图表上最大可见绘制值标签的数目。这只需要影响setdrawvalues()时启用。
//        chart.setMaxVisibleValueCount(30);

        //用来描述Y轴的显示，如果设置为true，那么Y轴会显示X变化范围内Y值最大的变化范围
        chart.setAutoScaleMinMaxEnabled(false);
        //设置为true那么图标在放大之后无法进行拖拽，默认为false
        chart.setKeepPositionOnRotation(false);
    }


    /**
     *     折线图
     */
    private void initLineChartView(){
        ArrayList<Entry> values = new ArrayList<>();
        mLineChart = (LineChart) findViewById(R.id.statistics_line_chart);
        for(int i = 0; i < 20; i++){
            float val = (float)(Math.random() * 3) + 3;
            values.add(new Entry(i, val));
        }
        LineDataSet dataSet = new LineDataSet(values,"Label");
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate();

    }

}
