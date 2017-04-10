package com.iflytek.voicedemo;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created by Kathy.Tian on 17/4/5.
 */

public class PieGraph {
    public GraphicalView getGraphicalView(Context context, int largeSlide, int mediumSlide, int tinySlide ){
        CategorySeries series=new CategorySeries("Voice Demo Pie");
        int[] portions={largeSlide,mediumSlide,tinySlide};
        String[] seriesName=new String[]{"Positive","Neutral","Negative"};
        int numSlides=3;
        for(int i=0;i<numSlides;i++){
            series.add("Mood "+ seriesName[i], portions[i]);

        }
        DefaultRenderer defaultRenderer=new DefaultRenderer();
        SimpleSeriesRenderer simpleSeriesRenderer=null;
        int color_po=0xFFDF9085;
        int color_neu=0xFF0F8EC5;
        int color_neg=0xFF66D4B7;

        int[] colors={color_po, color_neu, color_neg};
        for(int i=0;i<numSlides;i++){
            simpleSeriesRenderer=new SimpleSeriesRenderer();
            simpleSeriesRenderer.setColor(colors[i]);
            defaultRenderer.addSeriesRenderer(simpleSeriesRenderer);
            defaultRenderer.setZoomEnabled(false);
            defaultRenderer.setPanEnabled(false);

        }

        return ChartFactory.getPieChartView(context,series,defaultRenderer);

    }

}
