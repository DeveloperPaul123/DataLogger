package com.devpaul.materialfabmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pauly D on 2/21/2015.
 */
public class MaterialFloatingActionButtonMenu extends ViewGroup {


    private Paint circlePaint;
    private Paint buttonPlusPaint;
    private int circleColor, buttonColor, windowWidth, windowHeight;

    private MaterialFloatingActionButton mAddButton;

    private enum State {OPEN, CLOSED};
    private State mCurState = State.CLOSED;

    /**
     * Animator for expanding the circle menu.
     */
    private ObjectAnimator circleShowAnimator, circleCloseAnimator, degreeOffsetAnimator;

    private AnimatorSet mExpandAnimation;
    private AnimatorSet mCollapseAnimation;
    private AnimatorSet showButtonsSet;
    private AnimatorSet hideButtonsSet;

    private int mButtonsCount;
    private float cx, cy, menuRadius, maxRadius, startAngle, degreeOffset;

    private float[] interceptDownEvents = new float[2];
    private float[] lastEventPoints = new float[2];

    private boolean isScrolling =false;
    private boolean isAddButton = false;
    private boolean mShowAllChildren;

    private int mSlop, minFlingVel, maxFlingVel;

    private List<Animator> expands;
    private List<Animator> collapses;

    private MaterialFABMenuListener mListener;

    private OnClickListener childListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mListener != null) {
                mListener.onChildClicked((int) v.getTag(), v);
            }
        }
    };

    /**
     * Listener interface for this view.
     */
    private static interface MaterialFABMenuListener {
        /**
         * Called when a child is clicked.
         * @param position, the position of the child in the view.
         * @param child the view child.
         */
        public void onChildClicked(int position, View child);

        /**
         * Called when the menu opens.
         */
        public void onExpanded();

        /**
         * Called when the menu closes.
         */
        public void onCollapsed();
    }

    /**
     * Animator listener for the hiding buttons animations.
     */
    private Animator.AnimatorListener hideButtonsListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mCurState = State.CLOSED;
            requestLayout();
            mCollapseAnimation.start();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * Animator listener for the expanding menu listener.
     */
    private Animator.AnimatorListener expandMenuListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            showButtonsSet.start();

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * Animator listener for the showing buttons animator.
     */
    private Animator.AnimatorListener showButtonsListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mCurState = State.OPEN;
            requestLayout();
            mAddButton.animate().cancel();
            mAddButton.animate().setDuration(150).rotation(45.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddButton.flatten();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animator.AnimatorListener collapseMenuListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mAddButton.animate().cancel();
            mAddButton.animate().setDuration(150).rotation(-0.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddButton.unflatten();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    public MaterialFloatingActionButtonMenu(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialFloatingActionButtonMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Initialize the view and fields.
     * @param context the context of this view.
     * @param attrs the attribute set of this view.
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialFloatingActionButtonMenu);
        circleColor = a.getColor(R.styleable.MaterialFloatingActionButtonMenu_mat_fab_menu_color,
                getColor(android.R.color.holo_blue_dark));
        mShowAllChildren = a.getBoolean(R.styleable.MaterialFloatingActionButtonMenu_mat_fab_menu_showChildrenAlways, false);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        buttonColor = getColor(android.R.color.holo_blue_dark);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);

        menuRadius = 1f;

        circleShowAnimator = ObjectAnimator.ofFloat(this, "menuRadius", maxRadius);
        circleCloseAnimator = ObjectAnimator.ofFloat(this, "menuRadius", 1.0f);
        degreeOffsetAnimator = ObjectAnimator.ofFloat(this, "degreeOffset", 0.0f);

        OvershootInterpolator interpolator = new OvershootInterpolator();

        mExpandAnimation = new AnimatorSet().setDuration(150);
        mExpandAnimation.setInterpolator(interpolator);
        mExpandAnimation.addListener(expandMenuListener);

        mCollapseAnimation = new AnimatorSet().setDuration(100);
        mCollapseAnimation.addListener(collapseMenuListener);

        showButtonsSet = new AnimatorSet().setDuration(100);
        showButtonsSet.setInterpolator(interpolator);
        showButtonsSet.addListener(showButtonsListener);

        hideButtonsSet = new AnimatorSet().setDuration(50);
        hideButtonsSet.addListener(hideButtonsListener);

        setWillNotDraw(false);
        createAddButton(context);

        assignOnClickListeners();

        interceptDownEvents[0] = 0.0f;
        interceptDownEvents[1] = 0.0f;

        lastEventPoints[0] = 0.0f;
        lastEventPoints[1] = 0.0f;

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mSlop = configuration.getScaledTouchSlop();
        minFlingVel = configuration.getScaledMinimumFlingVelocity();
        maxFlingVel = configuration.getScaledMaximumFlingVelocity();

        startAngle = 225f;

        expands = new ArrayList<>();
        collapses = new ArrayList<>();

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        windowWidth = metrics.widthPixels;
        windowHeight = metrics.heightPixels;
    }

    /**
     * Assigns on click listeners for all the child views.
     */
    private void assignOnClickListeners() {
        int count = getChildCount();
        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if(child == mAddButton) {
                continue;
            }
            child.setTag(i);
            child.setOnClickListener(childListener);
        }
    }

    /**
     * Sets a new {@link com.devpaul.materialfabmenu.MaterialFloatingActionButtonMenu.MaterialFABMenuListener}
     * for this view.
     * @param listener the listener.
     */
    public void setMenuListener(MaterialFABMenuListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Creates the main button.
     * @param context
     */
    private void createAddButton(Context context) {
        mAddButton = new MaterialFloatingActionButton(getContext());
        mAddButton.setId(R.id.fab_expand_menu_button);
        mAddButton.setButtonColor(buttonColor);
        mAddButton.setButtonPressedColor(circleColor);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        addView(mAddButton, super.generateDefaultLayoutParams());
    }


    /**
     * Toggle the menu open or closed.
     */
    private void toggle() {
        mCollapseAnimation.cancel();
        mExpandAnimation.cancel();
        showButtonsSet.cancel();
        hideButtonsSet.cancel();

        if(mCurState == State.OPEN) {
            hideButtonsSet.start();
        } else if(mCurState == State.CLOSED) {
            mExpandAnimation.start();
        }
    }

    private void open(boolean animate) {
        if(animate) {
            if(mCurState == State.OPEN) hideButtonsSet.start();
        } else {
            //TODO open menu without animation.
        }
    }

    private void close(boolean animate) {
        if(animate) {
            if (mCurState == State.CLOSED) mExpandAnimation.start();
        } else {
            //TODO close menu without animation.
        }

    }

    /**
     * Sets up the animations for the buttons collapsing and closing.
     */
    private void setUpAnimations() {

        circleShowAnimator = ObjectAnimator.ofFloat(this, "menuRadius", maxRadius);
        circleCloseAnimator = ObjectAnimator.ofFloat(this, "menuRadius", 1.0f);

        mExpandAnimation.play(circleShowAnimator);
        mCollapseAnimation.play(circleCloseAnimator);

        if(expands.size()>0 && collapses.size() >0) {
            showButtonsSet.playSequentially(expands);
            hideButtonsSet.playSequentially(collapses);
        }
    }
    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, menuRadius, circlePaint);
    }

    private void resetChildrenPositions() {
        this.startAngle = 225.0f;
        requestLayout();
    }

    public void setDegreeOffset(float degreeOffset) {
        this.degreeOffset = degreeOffset;
        //TODO figure out how to translate the offset to an angular velocity.
    }

    public float getDegreeOffset() {
        return degreeOffset;
    }

    public void setMenuRadius(float radius) {

        this.menuRadius = radius;
        if(radius == maxRadius) {
            if(mListener != null) {
                mListener.onExpanded();
            }
        } else if(radius == 1.0f) {
            if(mListener != null) {
                mListener.onCollapsed();
            }
        }
        invalidate();
    }

    public float getMenuRadius() {
        return this.menuRadius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View v = getChildAt(i);
            if(v == mAddButton) {

            } else {
                try{
                    final MaterialFABMenuItem item = (MaterialFABMenuItem) v;
                } catch (ClassCastException e) {
                    throw new ClassCastException("Children can only be MaterialFABMenuItems");
                }
            }
        }
        int size = (int) (0.60f * Math.min(windowWidth, windowHeight));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if(!isScrolling) {
            expands.clear();
            collapses.clear();
        }

        int childCount = getChildCount();
        //start above the main button.

        float halfAddButtonHeight = mAddButton.getMeasuredHeight()/2;
        float halfAddButtonWidth = mAddButton.getMeasuredWidth()/2;

        float cx = getMeasuredWidth() - mAddButton.getPaddingRight() - halfAddButtonWidth;
        float cy = getMeasuredHeight() - mAddButton.getPaddingBottom() - halfAddButtonHeight;

        final int addLeft = (int) (cx - halfAddButtonWidth);
        final int addTop = (int) (cy - halfAddButtonHeight);
        final int addRight = (int) (cx + halfAddButtonWidth);
        final int addBottom = (int) (cy + halfAddButtonHeight);

        mAddButton.layout(addLeft, addTop, addRight, addBottom);

        maxRadius = Math.min(cx, cy) * 10.0f/12.0f;

        if(isInEditMode()) {
            menuRadius = maxRadius;
        }

        this.cx = cx;
        this.cy = cy;

        for(int i = 0; i<childCount; i++) {

            final View child = getChildAt(i);
            if(child == mAddButton) {
                continue;
            }

            final int halfChildHeight = child.getMeasuredHeight()/2;
            final int halfChildWidth = child.getMeasuredWidth()/2;

            final int x;
            final int y;

            float radius = 0.45f * maxRadius + getDimension(R.dimen.material_button_spacing);

            float angleSpacing = calculateAngleSpacing(radius, halfChildWidth);

            x = (int) cx + (int) (radius * Math.cos(Math.toRadians(startAngle - (i - 1)*angleSpacing)));
            y = (int) cy + (int) (radius * Math.sin(Math.toRadians(startAngle - (i - 1)*angleSpacing)));

            final int left = x - halfChildWidth;
            final int top = y - halfChildHeight;
            final int bottom = y + halfChildHeight;
            final int right = x + halfChildHeight;

            child.layout(left, top, right, bottom);

            child.setScaleY(isOpen() ? 1:0);
            child.setScaleX(isOpen() ? 1:0);
            MaterialFabLayoutParams layoutParams = (MaterialFabLayoutParams) child.getLayoutParams();
            if(isOnScreen(child) && !isScrolling) layoutParams.setViewTarget(child);
        }

        if(!isScrolling) setUpAnimations();
        invalidate();
    }

    /**
     * Checks if a view is on screen.
     * @param child the view child to check the position of.
     * @return true if the child is visible, false otherwise.
     */
    private boolean isOnScreen(View child) {

        float imageMargin = getDimension(R.dimen.material_item_margin);
        if(child.getLeft() + imageMargin > windowWidth) {
            return false;
        } else if(child.getTop() + imageMargin > windowHeight) {
            return false;
        } else if(child.getBottom() - imageMargin < 0) {
            return false;
        } else if(child.getRight() - imageMargin < 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isOpen() {
        return isInEditMode() || mCurState == State.OPEN;
    }

    /**
     * Calculates the angle spacing required to get a spacing of 16 dp between buttons.
     * This is based on the equation a^2 + b^2 - 2abcos(th) = c^2
     * @param radius the radius to the child.
     * @return the spacing between the children.
     */
    private float calculateAngleSpacing(float radius, float halfChildSize) {
        float desiredSpace = getDimension(R.dimen.material_button_spacing) + 2*halfChildSize;
        float cosRatio = ((desiredSpace*desiredSpace) - 2* (radius * radius))/(-2 * radius * radius);
        float radians = (float) Math.acos(cosRatio);
        return (float) Math.toDegrees(radians);
    }

    @Override
    protected void onFinishInflate() {
        bringChildToFront(mAddButton);
        mButtonsCount = getChildCount();
        super.onFinishInflate();
    }

    /**
     * Gets a dimension.
     * @param id, the id of the dimension.
     * @return the dimension in pixels.
     */
    private float getDimension(int id) {
        return getResources().getDimension(id);
    }

    private static OvershootInterpolator interpolator = new OvershootInterpolator();

    public class MaterialFabLayoutParams extends LayoutParams {

        private ObjectAnimator expandAnimator;
        private ObjectAnimator collapseAnimator;

        private PropertyValuesHolder xScaleIn = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f);
        private PropertyValuesHolder yScaleIn = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f);
        private PropertyValuesHolder xScaleOut = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.0f);
        private PropertyValuesHolder yScaleOut = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.0f);

        private boolean animationsSet;

        public MaterialFabLayoutParams(LayoutParams source) {
            super(source);

        }

        public void setViewTarget(View view) {
            expandAnimator = ObjectAnimator.ofPropertyValuesHolder(view, xScaleIn, yScaleIn);
            collapseAnimator = ObjectAnimator.ofPropertyValuesHolder(view, xScaleOut, yScaleOut);

            expandAnimator.setInterpolator(interpolator);
            expandAnimator.setDuration(60);
            collapseAnimator.setDuration(60);
            if(!animationsSet) {
                expands.add(expandAnimator);
                collapses.add(collapseAnimator);

                animationsSet = true;
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MaterialFabLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MaterialFabLayoutParams(super.generateLayoutParams(p));
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MaterialFabLayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();

        switch(action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                interceptDownEvents[0] = ev.getRawX();
                interceptDownEvents[1] = ev.getRawY();
                isScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isScrolling) {
                    return true;
                }
                isScrolling = checkIfScrolling(interceptDownEvents, ev);
                interceptDownEvents[0] = ev.getRawX();
                interceptDownEvents[1] = ev.getRawY();
                break;
            case MotionEvent.ACTION_DOWN:
                Log.i("MENU", "Down");
                interceptDownEvents[0] = ev.getRawX();
                interceptDownEvents[1] = ev.getRawY();

                for(int i = 0; i < getChildCount(); i++) {
                    Rect buttonRect = getBoudingRect(getChildAt(i));
                    if(buttonRect.contains((int) ev.getX(), (int) ev.getY())) {
                        isAddButton = true;
                        break;
                    } else {
                        isAddButton = false;
                    }
                }
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastEventPoints[0] = event.getX() - cx;
                lastEventPoints[1] = cy - event.getY();
                isScrolling = false;
                requestLayout();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isScrolling) {
                    isScrolling = checkIfScrolling(lastEventPoints, event);
                    break;
                }

                if(isScrolling) {
                    float x = event.getRawX() - cx;
                    float y = cy - event.getRawY();
                    float[] center = new float[]{cx, cy};

                    if(lastEventPoints[0] == 0.0 && lastEventPoints[1]== 0.0) {
                        lastEventPoints[0] = x;
                        lastEventPoints[1] = y;
                        break;
                    }

                    float[] newEventPoints = new float[]{x, y};
                    float newRadius = getDistance(center, newEventPoints);
                    float oldRadius = getDistance(center, lastEventPoints);
                    float pointDistance = getDistance(lastEventPoints, newEventPoints);
                    float newAngle =  calculateAngleBetweenPoints(newRadius, oldRadius, pointDistance);
                    int scrollDirection = getScrollDirection(lastEventPoints, newEventPoints);

                    if(mCurState == State.OPEN && !Float.isNaN(newAngle)) {
                        startAngle += newAngle * scrollDirection;
                        Log.i("MENU", "" + newAngle);
                        if(startAngle > 360.0f) {
                            startAngle = startAngle - 360.0f;
                        }
                        requestLayout();
                    }

                    lastEventPoints[0] = x;
                    lastEventPoints[1] = y;

                }

                break;
        }
        return true;
    }

    /**
     * Checks to see if the user is currently scrolling the menu.
     * @param ev the current motion event.
     * @return boolean, true if scrolling, false otherwise.
     */
    private boolean checkIfScrolling(float[] lastPoints, MotionEvent ev) {
        float[] point = new float[2];
        point[0] = ev.getRawX();
        point[1] = ev.getRawY();

        float delta = getDistance(lastPoints, point);
        Log.i("MENU", "Change: " + delta + " Slop : " + mSlop);
        if(Math.abs(delta) > mSlop) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the bounding rectangle of a view.
     * @param childView the view to get the rect of.
     * @return a {@code Rect} object, bounding the childview.
     */
    private Rect getBoudingRect(View childView) {
        return new Rect(childView.getLeft(), childView.getTop(),
                childView.getRight(), childView.getBottom());
    }

    /**
     * Figures out the direction of the scroll based on the old touch point and then current one.
     * @param startPoint the start point.
     * @param endPoint the end point.
     * @return an integer indicating if the scroll is to the left or right.
     */
    private int getScrollDirection(float[] startPoint, float[] endPoint) {
        float xdif = endPoint[0] - startPoint[0];
        float ydif = endPoint[1] - startPoint[1];
        if((xdif < 0 && ydif < 0) || (xdif == 0.0 && ydif < 0) || (xdif < 0 && ydif == 0.0f)) {
            //scroll left
            return -10;
        } else if((xdif > 0 && ydif > 0)|| (xdif == 0.0f && ydif >0) || (xdif == 0.0f && ydif > 0)) {
            //scroll right
            return 10;
        } else {
            return 0;
        }
    }

    /**
     * Gets the distance between two points.
     * @param startPoint the start point.
     * @param endPoint the end point.
     * @return distance, the distance in pixels.
     */
    private float getDistance(float[] startPoint, float[] endPoint) {
        float x1 = startPoint[0];
        float x2 = endPoint[0];

        float y1 = startPoint[1];
        float y2 = endPoint[1];

        float ydif = y2 -y1;
        float xdif = x2 - x1;

        float aSq = ydif*ydif;
        float bSq = xdif*xdif;
        return (float) Math.sqrt(aSq + bSq);
    }

    /**
     * Calculates the angle between two points. Looks at the radius to the old and new points and then the distance between the
     * two points.
     * @param newRadius the radius from the center of the main button to the new point.
     * @param oldRadius the radius from the center of the main button to the old point.
     * @param distanceBetween the distance between the two points.
     * @return an angle, in degrees.
     */
    private float calculateAngleBetweenPoints(float newRadius, float oldRadius, float distanceBetween) {

        float cosRatio = ((distanceBetween * distanceBetween)
                - newRadius*newRadius - oldRadius*oldRadius)/(-2 * newRadius * oldRadius);

        float radians = (float) Math.acos(cosRatio);
        return (float) Math.toDegrees(radians);
    }

    /**
     * Gesture listener for this view.
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        //TODO
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
