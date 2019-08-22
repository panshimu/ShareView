package com.miaozi.shareview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * created by panshimu
 * on 2019/8/22
 */
public class ShareView extends View {
    private Paint mPaint;
    private int mCurrentShape = 1;
    private final int CIRCLE = 1;
    private final int SQUARE = 2;
    private final int TRIANGLE = 3;
    public ShareView(Context context) {
        this(context,null);
    }

    public ShareView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mCurrentShape == CIRCLE) {
            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth()/2, mPaint);
        }else if(mCurrentShape == SQUARE) {
            mPaint.setColor(Color.GREEN);
            canvas.drawRect(0, 0, getWidth(), getWidth(), mPaint);
        }else if(mCurrentShape == TRIANGLE) {
            mPaint.setColor(Color.RED);
            Path path = new Path();
            path.moveTo(getWidth() / 2, 0);
            path.lineTo((float) (getWidth() / 2 - getWidth() / 4 * Math.sqrt(3)), 3 * getWidth() / 4);
            path.lineTo((float) (getWidth() / 2 + getWidth() / 4 * Math.sqrt(3)), 3 * getWidth() / 4);
            path.close();
            canvas.drawPath(path,mPaint);
        }
    }
    public void exchange(){
        switch (mCurrentShape){
            case CIRCLE:
                mCurrentShape = SQUARE;
                break;
            case SQUARE:
                mCurrentShape = TRIANGLE;
                break;
            case TRIANGLE:
                mCurrentShape = CIRCLE;
                break;
        }
        invalidate();
    }
}
