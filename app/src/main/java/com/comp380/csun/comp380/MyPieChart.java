package com.comp380.csun.comp380;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by David on 3/15/2015.
 */
public class MyPieChart extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] valueDegree;
    private String[] names;
    private RectF rectf;
    private int width;
    private int height;
    private float temp;


    public MyPieChart(Context context, float[] values, String[] names) {
        super(context);
        valueDegree = values;
        this.names = names;
        setupRectF();
    }

    private void setupRectF(){
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        rectf = new RectF(width/7, width/7, height/4, height/4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offsetY = 80;
        temp = 0;

        // 0 Light Blue, 1 Light Green, 2 Yellow, 3 Orange, 4 Magenta
        int[] colors = {Color.rgb(51, 255, 255),
                Color.rgb(51, 255, 51),
                Color.rgb(255, 255, 0),
                Color.rgb(255, 128, 0),
                Color.rgb(255, 51, 255)};


        for (int i = 0; i < names.length && !names[i].equals(""); i++) {
            if (i == 0) {
                paint.setColor(colors[i]);
                canvas.drawArc(rectf, 0, valueDegree[i], true, paint);
                paint.setTextSize(40);
                canvas.drawText(names[i], 0, 80, paint);
            } else {
                temp += valueDegree[i - 1];
                paint.setColor(colors[i]);
                canvas.drawArc(rectf, temp, valueDegree[i], true, paint);
                paint.setTextSize(40);
                canvas.drawText(names[i], 0, offsetY += 50, paint);
            }
        }
    }
}