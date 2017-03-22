package com.example.a24706.wetherdemo.WeatherView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.a24706.wetherdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 24706 on 2017/3/21.
 * 天气预报 24小时温度
 */

public class Weather24HourView extends View {

    private int mLineColor = Color.parseColor("#15bfff");//统一的线颜色
    private int mCircleColorLine = Color.parseColor("#15bfff");//统一的圆颜色
    private int mCircleColorInside = Color.parseColor("#ffffff");//圆内部填充色
    private int mTextColor = Color.parseColor("#666666");//文字颜色
    private int mDashColor = Color.parseColor("#979797");//虚线颜色
    private int mBottomLineColor = Color.parseColor("#dddddd");//底部横线颜色

    private int mStrokeWidth = 4;//折现的线宽度
    private int mBottomStrokeWidth = 2;//底部线宽度

    private int mCircleRadius = 8;//圆的半径
    private int mTextSize = 25;//字体大小

    private int mLengthPerTem = 5;//每度对应的高度
    private int mSpaceLength = 150;//两个小时之间的的距离
    private int mPaddingLeft = 100;//第一个小时与view左边缘的距离
    private int mPaddingBottom = 150;//底部线与view底部的距离
    private int mPathMarginBottomLine=140;//折线的最底部与底部横线的距离
    private int mPaddingTop = 150;//折线的顶部与view顶部的距离
    private int mTemperatureHeight = 300;//折线部分的高度
    private int mMarginBottomLine = 50;//时间与底部横线的距离

    private int maxTemperature = 20;//最高温度
    private int minTemperature = 10;//最低温度

    private Paint mLinePaint;//绘制折线的线条
    private Paint mDashPaint;//绘制虚线的线条
    private Paint mCirclePaint;//绘制小圆圈的线条
    private Paint mTextPaint;//绘制文字
    private Paint mBottomLinePaint;//绘制底部横线

    private Path mWeatherPath;//天气路径path
    private Path mDashPath;//折线path
    private List<WeatherHourEntity> datas;//假数据

    private Bitmap mBitmapSun;


    public Weather24HourView(Context context) {
        this(context, null);
    }

    public Weather24HourView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Weather24HourView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mockData();
        mLengthPerTem = getLengthPerTem();
        mWeatherPath = new Path();
        mDashPath = new Path();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=2;
        mBitmapSun = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sun,options);
        initLinePaint();
        initDashPaint();
        initCirclePaint();
        initTextPaint();
        initBottomLinePaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWeatherPath.reset();
        //这里按顺序绘制防止错误覆盖
        drawPath(canvas);
        drawIconDash(canvas);
        drawOther(canvas);//绘制温度，时间，和小圆
        drawBottomLine(canvas);
    }


    /**
     * 初始化底部横线paint
     */
    private void initBottomLinePaint() {
        mBottomLinePaint = new Paint();
        mBottomLinePaint.setColor(mBottomLineColor);
        mBottomLinePaint.setStrokeWidth(mBottomStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mBottomLinePaint.setAntiAlias(true);
    }


    /**
     * 初始化文字paint
     */
    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
    }

    /**
     * 初始化圆形paint
     */
    private void initCirclePaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColorLine);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
    }

    /**
     * 初始化Path线条paint
     */
    private void initLinePaint() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mStrokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
    }

    /**
     * 初始化虚线paint
     */
    private void initDashPaint() {
        mDashPaint = new Paint();
        mDashPaint.setColor(mDashColor);
        mDashPaint.setStrokeWidth(mStrokeWidth);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setAntiAlias(true);
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
        Log.d("weather", "lengthPerTem=====>" + mTemperatureHeight / (max - min));
        return mTemperatureHeight / (max - min);
    }

    private void mockData() {
        datas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            WeatherHourEntity weatherHourEntity = new WeatherHourEntity();
            int tem = 20+random.nextInt(5);
            Log.d("weather", "tem=====>" + tem);
            weatherHourEntity.setTemperature(tem);
            weatherHourEntity.setWeather_code("" + random.nextInt(4));
            if (i < 10) {
                if (i == 0) {
                    weatherHourEntity.setTime("现在");
                } else {
                    weatherHourEntity.setTime("0" + i + ":00");
                }
            } else {
                weatherHourEntity.setTime(i + ":00");
            }
            datas.add(weatherHourEntity);
        }
        datas.get(0).setWeather_code("0");
        datas.get(1).setWeather_code("0");
        datas.get(2).setWeather_code("1");
    }

    /**
     * 绘制天气折线图
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (i == 0) {
                int x = mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                mWeatherPath.moveTo(x, y);
            } else {
                int x = i * mSpaceLength + mPaddingLeft;
                int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
                mWeatherPath.lineTo(x, y);
            }
        }
        canvas.drawPath(mWeatherPath, mLinePaint);
    }

    /**
     * 绘制圆，时间，温度
     * @param canvas
     */
    private void drawOther(Canvas canvas) {
        for (int i = 0; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            int x = i * mSpaceLength + mPaddingLeft;
            int y = mPaddingTop + (maxTemperature - entity.getTemperature()) * mLengthPerTem;
            drawSingleCircle(canvas, x, y);
            drawTime(canvas, entity.getTime(), i);
            drawTemperature(canvas, entity.getTemperature(), i);
        }
    }

    /**
     * 绘制天气图标和虚线
     * @param canvas
     */
    private void drawIconDash(Canvas canvas) {
        int startIndex=0;
        String weatherCode=datas.get(0).getWeather_code();

        int centerLeftIndex;
        int centerRightIndex;
        int centerLeftTemperature;
        int centerRightTemperature;
        int centerTemperature;
        int x;
        int y;
        for (int i = 1; i < datas.size(); i++) {
            WeatherHourEntity entity = datas.get(i);
            if (!weatherCode.equals(entity.getWeather_code())||i==datas.size()-1){
                x= (int) (mPaddingLeft+(startIndex+(float)(i-startIndex)/2)*mSpaceLength);
                if ((i-startIndex)%2==0){
                    centerTemperature=datas.get(startIndex+(i-startIndex)/2).getTemperature();
                    Log.d("weather", "center xxx tem=====>" +centerTemperature+"  i==>"+i+"  startIndex===>"+startIndex);
                    y=mPaddingTop+(maxTemperature-centerTemperature)*mLengthPerTem+((centerTemperature-minTemperature)*mLengthPerTem+mPathMarginBottomLine)/2;
                }else{
                    centerLeftIndex=startIndex+(i-startIndex)/2;
                    centerRightIndex=centerLeftIndex+1;
                    centerLeftTemperature=datas.get(centerLeftIndex).getTemperature();
                    centerRightTemperature=datas.get(centerRightIndex).getTemperature();
                    centerTemperature=centerLeftTemperature + (centerRightTemperature - centerLeftTemperature) / 2;
//                    Log.d("weather", "center tem=====>" +(centerLeftTemperature + (float) (centerRightTemperature - centerLeftTemperature) / 2));
                    y = mPaddingTop+(maxTemperature-centerTemperature)*mLengthPerTem+((centerTemperature-minTemperature)*mLengthPerTem+mPathMarginBottomLine)/2;
                }
                canvas.drawBitmap(mBitmapSun,x-25,y-25,null);
                if (startIndex==0) {
                    drawDash(canvas, datas.get(startIndex).getTemperature(), startIndex);
                }
                drawDash(canvas,entity.getTemperature(),i);
                startIndex=i;
                weatherCode=entity.getWeather_code();
            }
        }
    }

    /**
     * 绘制底部横线
     * @param canvas
     */
    private void drawBottomLine(Canvas canvas) {
        canvas.drawLine(mPaddingLeft,  mTemperatureHeight + mPaddingTop+mPathMarginBottomLine,
                mPaddingLeft + (datas.size() - 1) * mSpaceLength, mTemperatureHeight + mPaddingTop+mPathMarginBottomLine, mBottomLinePaint);
    }

    /**
     * 绘制单个时间
     * @param canvas
     * @param time
     * @param index
     */
    private void drawTime(Canvas canvas, String time, int index) {
        int x = index * mSpaceLength + mPaddingLeft;
        int y = mTemperatureHeight + mPaddingTop + mMarginBottomLine+mPathMarginBottomLine;
        canvas.drawText(time, x - 30, y, mTextPaint);
    }

    /**
     * 绘制单个温度
     * @param canvas
     * @param temperature
     * @param index
     */
    private void drawTemperature(Canvas canvas, int temperature, int index) {
        if (index == 0) {
            int x = mPaddingLeft;
            int y = mPaddingTop + (maxTemperature - temperature) * mLengthPerTem;
            canvas.drawText(temperature + "°", x - 10, y - 30, mTextPaint);
        } else {
            int x = index * mSpaceLength + mPaddingLeft;
            int y = mPaddingTop + (maxTemperature - temperature) * mLengthPerTem;
            canvas.drawText(temperature + "°", x - 10, y - 30, mTextPaint);
        }
    }


    /**
     * 绘制单个折线
     * @param canvas
     * @param temperature
     * @param index
     */
    private void drawDash(Canvas canvas,int temperature,int index) {
        int x = index * mSpaceLength + mPaddingLeft;
        int y = mPaddingTop + (maxTemperature - temperature) * mLengthPerTem;
        mDashPath.reset();
        mDashPath.moveTo(x, y);
        mDashPath.lineTo(x, mTemperatureHeight + mPaddingTop+mPathMarginBottomLine);
        canvas.drawPath(mDashPath, mDashPaint);
    }

    /**
     * 绘制单个圆
     * @param canvas
     * @param x
     * @param y
     */
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
        setMeasuredDimension(mSpaceLength * datas.size(), mTemperatureHeight + mPaddingTop +mPathMarginBottomLine+ mPaddingBottom);
    }


}
