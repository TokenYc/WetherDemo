package com.example.a24706.wetherdemo.WeatherView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 24706 on 2017/3/21.
 */

public class WeatherView extends View {

    private int mLineColor = Color.parseColor("#15bfff");//统一的线颜色
    private int mCircleColorLine = Color.parseColor("#15bfff");//统一的圆颜色
    private int mCircleColorInside = Color.parseColor("#ffffff");
    private int mTextColor = Color.parseColor("#666666");
    private int mBottomLineColor = Color.parseColor("#dddddd");

    private int mStrokeWidth = 4;//统一的线宽度
    private int mBottomStrokeWidth = 2;

    private int mCircleRadius = 8;
    private int mTextSize = 25;

    private int mLengthPerTem = 5;//每度对应的高度
    private int mSpaceLength = 150;//两个小时之间的的距离
    private int mPaddingLeft = 100;//第一个小时与view左边缘的距离
    private int mPaddingTop = 150;
    private int mTotalHeight = 400;
    private int mMarginBottomLine=50;

    private int maxTemperature = 20;//最高温度
    private int minTemperature = 10;//最低温度

    private Paint mLinePaint;//绘制实现的线条
    private Paint mDashPaint;//绘制虚线的线条
    private Paint mCirclePaint;//绘制小圆圈的线条
    private Paint mTextPaint;//绘制文字
    private Paint mBottomLinePaint;

    private Path mWeatherPath;
    private Path mDashPath;
    private List<WeatherHourEntity> datas;//假数据


    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mockData();
        mLengthPerTem = getLengthPerTem();
        mWeatherPath = new Path();
        mDashPath = new Path();
        initLinePaint();
        initDashPaint();
        initCirclePaint();
        initTextPaint();
        initBottomLinePaint();
    }

    private void initBottomLinePaint() {
        mBottomLinePaint = new Paint();
        mBottomLinePaint.setColor(mBottomLineColor);
        mBottomLinePaint.setStrokeWidth(mBottomStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mBottomLinePaint.setAntiAlias(true);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
    }

    private void initCirclePaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColorLine);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
    }

    private void initLinePaint() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mStrokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
    }

    private void initDashPaint() {
        mDashPaint = new Paint();
        mDashPaint.setColor(mTextColor);
        mDashPaint.setStrokeWidth(mStrokeWidth);
        mDashPaint.setStyle(Paint.Style.STROKE);
//        mDashPaint.setAntiAlias(true);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mDashPaint.setPathEffect(effects);
    }

    /**
     * 获取每度对应的长度
     *
     * @return
     */
    private int getLengthPerTem() {
        int min = 1000;
        int max = -1000;
        for (WeatherHourEntity entity : datas) {
            int tem = entity.getTemperature();
            if (tem < min) {
                min = tem;
            } else if (tem > max) {
                max = tem;
            }
        }
        maxTemperature = max;
        minTemperature = min;
        Log.d("weather", "lengthPerTem=====>" + 400 / (max - min));
        return mTotalHeight / (max - min);
    }

    private void mockData() {
        datas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            WeatherHourEntity weatherHourEntity = new WeatherHourEntity();
            int tem = random.nextInt(30);
            Log.d("weather", "tem=====>" + tem);
            weatherHourEntity.setTemperature(tem);
            weatherHourEntity.setWeather_code("" + i);
            if (i < 10) {
                if (i==0) {
                    weatherHourEntity.setTime("现在");
                }else{
                    weatherHourEntity.setTime("0" + i + ":00");
                }
            } else {
                weatherHourEntity.setTime(i + ":00");
            }
            datas.add(weatherHourEntity);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWeatherPath.reset();
        //这里按顺序绘制防止错误覆盖
        drawPath(canvas);
        drawCircle(canvas);
        drawTemperature(canvas);
        drawTime(canvas);
        drawBottomLine(canvas);
    }

    private void drawBottomLine(Canvas canvas) {
        canvas.drawLine(mPaddingLeft, mTotalHeight + mPaddingTop, mPaddingLeft+(datas.size() - 1) * mSpaceLength, mTotalHeight + mPaddingTop, mBottomLinePaint);
    }

    private void drawTime(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (i == 0) {
                int x = mPaddingLeft;
                int y = mTotalHeight + mPaddingTop+mMarginBottomLine;
                canvas.drawText(entity.getTime(), x-20, y, mTextPaint);
            } else {
                int x = i * mSpaceLength+mPaddingLeft;
                int y = mTotalHeight + mPaddingTop+mMarginBottomLine;
                canvas.drawText(entity.getTime(), x-30, y, mTextPaint);
            }
        }
    }

    private void drawTemperature(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (i == 0) {
                int x = mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                canvas.drawText(entity.getTemperature() + "°", x - 10, y - 30, mTextPaint);
            } else {
                int x = i * mSpaceLength+mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                canvas.drawText(entity.getTemperature() + "°", x - 10, y - 30, mTextPaint);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (i == 0) {
                int x = mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                drawSingleCircle(canvas, x, y);
            } else {
                int x = i * mSpaceLength+mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                drawSingleCircle(canvas, x, y);
            }
        }
    }

    private void drawPath(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (i == 0) {
                int x = mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                drawDash(x,y,canvas);
                mWeatherPath.moveTo(x, y);
            } else {
                int x = i * mSpaceLength+mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                drawDash(x,y,canvas);
                mWeatherPath.lineTo(x, y);
            }
        }
        canvas.drawPath(mWeatherPath, mLinePaint);
    }

    private void drawDash(int x, int y, Canvas canvas) {
        mDashPath.reset();
        mDashPath.moveTo(x,y);
        mDashPath.lineTo(x,mTotalHeight + mPaddingTop);
        canvas.drawPath(mDashPath,mDashPaint);
    }

    private void drawSingleCircle(Canvas canvas, int x, int y) {
        //先画实心圆，再画圆的边
        mCirclePaint.setColor(mCircleColorInside);
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, mCircleRadius, mCirclePaint);

        mCirclePaint.setColor(mCircleColorLine);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, mCircleRadius, mCirclePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mSpaceLength * datas.size(), 1000);
    }
}
