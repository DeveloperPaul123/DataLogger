package com.devpaul.circulartextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.devpaul.materialfabmenu.utils.ColorUtils;

import java.util.Random;




/**
 * Created by Pauly D on 11/13/2014.
 *
 * Simple circle with text in the middle. Just a single letter though.
 */
public class CircularTextView extends View {

    private Paint textPaint;
    private Paint backgroundPaint;

    private int diameter;

    private float mSize;
    private float mPadding;
    private float mTextSize;

    int[] colors;
    float cx, cy, textcx, textcy, dx;
    private String text;

    public CircularTextView(Context context) {
        super(context);
        initialize();
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * Initialize the view.
     */
    private void initialize() {

        if(isInEditMode()) {
            text = "A";
            colors = new int[3];
            colors[0] = getColor(android.R.color.holo_blue_bright);
            colors[1] = getColor(android.R.color.holo_blue_dark);
            colors[2] = getColor(android.R.color.holo_orange_light);
        } else {
            colors = ColorUtils.getRandomColors(20);
        }

        mTextSize = getDimension(R.dimen.circular_text_size);
        mSize = getDimension(R.dimen.circular_text_view_size);
        cx = mSize/2;
        cy = cx;
        mPadding = getDimension(R.dimen.circular_text_view_padding);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(mTextSize);
        textPaint.setColor(getResources().getColor(android.R.color.white));
        textPaint.setAntiAlias(true);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setStrokeWidth(1);
        textPaint.setTextAlign(Paint.Align.CENTER);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(colors[new Random().nextInt(colors.length)]);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * Set the text for the view. Can only be 1 letter.
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        measureTextSize(text);
    }

    /**
     * Get the text for the view.
     * @return String, the text.
     */
    public String getText() {
        return this.text;
    }

    private void measureTextSize(String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        float halfTextSize = mTextSize/2;
        RectF newBounds = new RectF(0.0f, 0.0f, mTextSize, mTextSize);
        newBounds.set(cx-halfTextSize, cy - halfTextSize, cx+halfTextSize, cy+halfTextSize);
        textcx = newBounds.centerX();
        textcy = newBounds.centerY();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(text == null) {
            text = "A";
        }
        if(text.length() != 1) {
            textPaint.setTextSize(mTextSize *0.8f);
        } else {
            textPaint.setTextSize(mTextSize);
        }
        canvas.drawCircle(cx, cy, diameter/2, backgroundPaint);
        canvas.drawText(text, cx, cy + mTextSize/3.0f, textPaint);
    }

    private int getColor(int resId) {
        return getResources().getColor(resId);
    }

    private float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        diameter = (int) (mSize-mPadding);
        cx = (mSize-mPadding)/2;
        cy = (mSize-mPadding)/2;
        setMeasuredDimension((int) mSize, (int) mSize);
    }
}
