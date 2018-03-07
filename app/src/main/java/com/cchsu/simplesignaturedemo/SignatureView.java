package com.cchsu.simplesignaturedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cchsu on 2017/10/24.
 */

class SignatureView extends View {

    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    public SignatureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        init();
    }

    public SignatureView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);  //畫筆的顏色
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(5f); //畫筆的粗細
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawPath(mPath, mPaint);
    }

    private float cur_x, cur_y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cur_x = eventX;
                cur_y = eventY;
                mPath.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                myDraw(event); //減鋸齒描繪
                mPath.quadTo(cur_x, cur_y, eventX, eventY);

                cur_x = eventX;
                cur_y = eventY;
                break;

            default:
                return false;
        }
        invalidate();
        return true;
    }

    /**
     * 繪圖時減鋸齒操作
     * @param event
     */
    private void myDraw(MotionEvent event) {
        int historySize = event.getHistorySize();
        for(int i=0; i<historySize; i++) {
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            mPath.quadTo(cur_x, cur_y, historicalX, historicalY);
        }
    }

    /**
     * 清除描繪
     */
    public void clear() {
        mPath.reset();
        invalidate();
    }
}
