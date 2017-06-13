package com.example.lenovo.hutu;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by  Mr.Robot on 2017/3/26.
 * zhouchatain@gmail.com
 * GitHub:https://github.com/TheSadFrog
 */

public class ChartsActivity extends Activity {

    private LineChartView mChart;
    private Map<String,Long> table = new TreeMap<>();
    private LineChartData mData;
    private List<PointValue> values = new ArrayList<PointValue>();
    public List<ItemBean> allDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_view);
        mChart = (LineChartView) findViewById(R.id.line_chart);
        mData = new LineChartData();
        //allDate = (List<ItemBean>) getIntet().getSerializableExtra("cost_list");
        generateValues(allDate);
        generateData();
    }

    private void generateData() {
        int indexX = 0;
        for(Long value:table.values()){
            values.add(new PointValue(indexX, value));
            indexX++;
        }
        Line line = new Line(values).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLORS[1]);
        lines.add(line);
        mData.setLines(lines);
        mChart.setLineChartData(mData);
        mChart.setInteractive(true);
        mChart.setZoomType(ZoomType.HORIZONTAL);
        mChart.setMaxZoom((float) 2);//最大方法比例
        mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(mChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        mChart.setCurrentViewport(v);
    }

    private void generateValues(List<ItemBean> all) {
        if(all.size()> 0){
            for (int i = 0; i < all.size(); i++) {
                ItemBean costBean = all.get(i);
                String costDate = costBean.ItemDate;
                long costMoney = Long.parseLong(costBean.ItemCost);
                if(!table.containsKey(costDate)){
                    table.put(costDate,costMoney);
                }else {
                    long originMoney =table.get(costDate) + costMoney;
                    table.put(costDate,originMoney);
                }
            }
        }
    }
}