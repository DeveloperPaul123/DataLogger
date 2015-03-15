package com.devpaul.materialfabmenu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.devpaul.materialfabmenu.utils.ColorUtils;
import com.devpaul.materialfabmenu.utils.RippleGenerator;
import com.devpaul.materialfabmenu.utils.ShadowGenerator;
import com.devpaul.materialfabmenu.utils.ShadowRippleGenerator;

/**
 * Created by Pauly D on 3/5/2015.
 */
public class TestButton extends View {

    private ObjectAnimator shadowAnimator;
    private Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RippleGenerator rippleGenerator;
    private ShadowGenerator shadowGenerator;
    private ShadowRippleGenerator shadowRippleGenerator;

    private static final float MIN_ELEVATION = 0.0f;
    private static final float DEFAULT_ELEVATION = 0.2f;
    private static final float MAX_ELEVATION = 0.92f;
    private static final float MIN_SHADOW_ALPHA = 0.1f;
    private static final float MAX_SHADOW_ALPHA = 0.4f;
    private float elevation, mShadowRadius, minShadowOffset, maxShadowOffset,
            mShadowOffset, maxShadowSize, minShawdowSize, mShadowAlpha;
    private float mSize, cx, cy, buttonSize;
    private int mShadowColor, mButtonColor, shadowColor;

    public TestButton(Context context) {
        super(context);
        init();
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialize everything for this view.
     */
    private void init() {
        maxShadowOffset = getDimension(R.dimen.mat_fab_shadow_offset)*1.5f;
        minShadowOffset = maxShadowOffset/1.5f;
        mShadowOffset = minShadowOffset;
        maxShadowSize = getDimension(R.dimen.mat_fab_shadow_max_radius);
        minShawdowSize = getDimension(R.dimen.mat_fab_shadow_min_radius)/2;
        mShadowRadius = minShawdowSize;
        buttonSize = getDimension(R.dimen.material_floating_button_size);
        mSize = buttonSize + maxShadowSize + mShadowOffset*2;
        mShadowAlpha = MAX_SHADOW_ALPHA;
        mShadowColor = Color.BLACK;
        shadowColor = ColorUtils.getNewColorAlpha(mShadowColor, MIN_SHADOW_ALPHA);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonColor = getResources().getColor(android.R.color.holo_blue_bright);
        mButtonPaint.setColor(mButtonColor);
//        mButtonPaint.setShadowLayer(mShadowRadius, 0.0f, mShadowOffset, Color.BLACK);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(LAYER_TYPE_SOFTWARE, mButtonPaint);
//        }
        shadowAnimator = ObjectAnimator.ofFloat(this, "elevation", MIN_ELEVATION, MAX_ELEVATION);
        elevation = DEFAULT_ELEVATION;
//        setElevation(elevation);

        shadowRippleGenerator = new ShadowRippleGenerator(this, mButtonPaint);
        shadowRippleGenerator.setRippleColor(ColorUtils.getDarkerColor(mButtonColor));
        shadowRippleGenerator.setClipRadius((int) buttonSize/2);
        shadowRippleGenerator.setAnimationDuration(200);
        shadowRippleGenerator.setMaxRippleRadius((int) (0.75f*buttonSize/2));
        shadowRippleGenerator.setMaxShadowOffset(maxShadowOffset);
        shadowRippleGenerator.setMaxShadowSize(maxShadowSize);
        shadowRippleGenerator.setMinShadowOffset(minShadowOffset);
        shadowRippleGenerator.setMinShawdowSize(minShawdowSize);
        shadowRippleGenerator.setAnimationDuration(400);

        rippleGenerator = new RippleGenerator(this);
        rippleGenerator.setEffectColor(ColorUtils.getDarkerColor(mButtonColor));
        rippleGenerator.setHasRippleEffect(true);
        rippleGenerator.setClipRadius((int) buttonSize/2);
        rippleGenerator.setAnimDuration(300);
        rippleGenerator.setMaxAlpha(200);
        rippleGenerator.setRippleSize((int) (0.75f*buttonSize/2));

        shadowGenerator = new ShadowGenerator(this, mButtonPaint);
        shadowGenerator.setMaxShadowOffset(maxShadowOffset);
        shadowGenerator.setMaxShadowSize(maxShadowSize);
        shadowGenerator.setMinShadowOffset(minShadowOffset);
        shadowGenerator.setMinShawdowSize(minShawdowSize);
        shadowGenerator.setAnimationDuration(200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        cx = mSize/2;
        cy = mSize/2;
        rippleGenerator.setIsCircleView(cx, cy, buttonSize/2);
        shadowRippleGenerator.setIsCircleView(cx, cy, buttonSize/2);
        setMeasuredDimension((int) mSize, (int) mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        rippleGenerator.onDraw(canvas);
//        shadowGenerator.onDraw(mButtonPaint);
//        mButtonPaint.setShadowLayer(mShadowRadius, 0.0f, mShadowOffset/2, shadowColor);
        shadowRippleGenerator.onDrawShadow(mButtonPaint);
        canvas.drawCircle(cx, cy, buttonSize / 2, mButtonPaint);
        shadowRippleGenerator.onDrawRipple(canvas);
    }

    /**
     * Flattens the button to have no shadow.
     */
    public void flatten() {
       shadowRippleGenerator.flatten();
    }

    /**
     * Un flatten the view.
     */
    public void unflatten() {
        shadowRippleGenerator.unflatten();
    }

    /**
     * Sets the elevation and changes the parameters of the shadow drawing.
     * @param value the new elevation.
     */
    public void setElevation(float value) {
        this.elevation = value;
        this.mShadowAlpha = (MAX_SHADOW_ALPHA - MIN_SHADOW_ALPHA)*(elevation/MAX_ELEVATION)  + MAX_SHADOW_ALPHA;
        this.mShadowRadius = (maxShadowSize - minShawdowSize)*(elevation/MAX_ELEVATION)  + minShawdowSize;
        this.mShadowOffset = (maxShadowOffset-minShadowOffset)*(elevation/MAX_ELEVATION) + minShadowOffset;
        shadowColor = ColorUtils.getNewColorAlpha(mShadowColor, mShadowAlpha);
        invalidate();
    }

    @Override
    public float getElevation() {
        return elevation;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        shadowGenerator.onTouchEvent(event);
//        rippleGenerator.onTouchEvent(event);
        shadowRippleGenerator.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                elevate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                lower();
                break;
        }
        return true;
    }

    private float getDimension(int id) {
        return getResources().getDimension(id);
    }
}
