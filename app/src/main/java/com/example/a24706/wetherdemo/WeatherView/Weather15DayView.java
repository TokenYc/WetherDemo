package com.example.a24706.wetherdemo.WeatherView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.a24706.wetherdemo.R;

/**
 * 一句话功能简述 最近15天天气
 * 功能详细描述
 * http://git.shangxiaban.cn/wangjing/Qianfan_Interface_document/src/master/2.1/%E5%A4%A9%E6%B0%94%E9%A2%84%E6%8A%A5%E6%8E%A5%E5%8F%A3.MD
 *
 * @author 杨晨 on 2017/3/27 10:55
 * @e-mail 247067345@qq.com
 */

public class Weather15DayView extends View {

    private int mPaddingTop = Utils.dp2px(getContext(), 5);
    private int mPaddingBottom = Utils.dp2px(getContext(), 5);
    private int mPaddingLeft = Utils.dp2px(getContext(), 22);
    private int mMarginDayWeather = Utils.dp2px(getContext(), 9);
    private int mMarginWeatherIcon = Utils.dp2px(getContext(), 22);
    private int mMarginTemIcon = Utils.dp2px(getContext(), 21);
    private int mMarginTwoPath = Utils.dp2px(getContext(), 28);

    private int mDayTextColor = Color.parseColor("#666666");
    private int mWeatherTextColor = Color.parseColor("#666666");
    private int mTemperatureTextColor = Color.parseColor("#666666");
    private int mPathColor = Color.parseColor("#cccccc");
    private int mCircleColor = Color.parseColor("#cccccc");

    private Paint mDayTextPaint;
    private Paint mWeatherTextPaint;
    private Paint mTemperaturePaint;
    private Paint mPathPaint;


    private int mIconSize = Utils.dp2px(getContext(), 35);

    public Weather15DayView(Context context) {
        this(context,null);
    }

    public Weather15DayView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }


    public Weather15DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
