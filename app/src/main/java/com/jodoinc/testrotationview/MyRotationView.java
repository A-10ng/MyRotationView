package com.jodoinc.testrotationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * description ：类的作用
 * author : LongSh1z
 * email : 2674461089@qq.com
 * date : 2020/9/10 20:04
 */
public class MyRotationView extends View {

    private Context context;
    private float diceRadius = 50f;                 //占星的半径
    private float instance = 50f;                   //占星离中心的距离
    private float selectedRadius = diceRadius * 1.5f;             //选中的占星的背景半径
    private String diceColor = "#564099";           //占星的颜色
    private String selectedColor = "#FFF1C2";       //选中的占星的背景颜色
    private Paint txtPaint;                         //数字的画笔
    private float txtSize = 25f;                    //数字的大小
    private Paint dicePaint;                        //占星的画笔
    private Paint selectedPaint;                    //选中的占星的背景的画笔
    private float mWidth;                           //自定义View的宽度
    private float mHeight;                          //自定义View的高度
    private int desIndex = 0;
    private int index = 0;
    private ArrayList<Runnable> runnables = new ArrayList<>();
    private Rect txtRect;

    public MyRotationView(Context context) {
        super(context);
        init(context);
    }

    public MyRotationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRotationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        dicePaint = new Paint();
        dicePaint.setAntiAlias(true);
        dicePaint.setColor(Color.parseColor(diceColor));

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(txtSize);

        txtRect = new Rect();

        selectedPaint = new Paint();
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(Color.parseColor(selectedColor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        instance = mWidth / 2 - selectedRadius;

        canvas.translate(mWidth / 2 , mHeight / 2);

        drawInstantState(canvas,index);
    }

    private void drawInstantState(Canvas canvas, int index) {
        canvas.rotate(15f);
        if (index % 12 == 0){
            canvas.drawCircle(instance,0,selectedRadius,selectedPaint);
        }
        canvas.drawCircle(instance,0,diceRadius,dicePaint);
        canvas.rotate(-90f);

        txtPaint.getTextBounds("1",0,1,txtRect);
        int textHeight = txtRect.bottom - txtRect.top;
        canvas.drawText("1",0,instance + textHeight / 2f,txtPaint);
        canvas.rotate(90f);

        for (int i = 1; i < 12; i++) {
            canvas.rotate(30f);

            if (i == index % 12){
                canvas.drawCircle(instance,0,selectedRadius,selectedPaint);
            }

            canvas.drawCircle(instance,0,diceRadius,dicePaint);
            canvas.rotate(-90f);

            txtPaint.getTextBounds("1",0,1,txtRect);
            int textHeight1 = txtRect.bottom - txtRect.top;
            canvas.drawText(String.valueOf(i+1),0,instance + textHeight1 / 2f,txtPaint);

            canvas.rotate(90f);
        }

        if (this.index != desIndex){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            };
            postDelayed(runnable,500);
            runnables.add(runnable);

            this.index++;
        }
    }

    public void startAnimation(int desIndex){
        for (int i = 0; i < runnables.size(); i++) {
            removeCallbacks(runnables.get(i));
        }
        this.desIndex = desIndex;
        this.index = 0;
        invalidate();
    }

    public void stopAnimation(){
        this.desIndex = this.index;
    }
}
