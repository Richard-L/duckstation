package com.github.stenzek.duckstation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class TouchscreenControllerButtonView extends View {
    private Drawable mUnpressedDrawable;
    private Drawable mPressedDrawable;
    private boolean mPressed = false;
    private int mButtonCode = -1;
    private String mButtonName = "";
    private ButtonStateChangedListener mListener;

    public interface ButtonStateChangedListener
    {
        void onButtonStateChanged(TouchscreenControllerButtonView view, boolean pressed);
    }


    public TouchscreenControllerButtonView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public TouchscreenControllerButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public TouchscreenControllerButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TouchscreenControllerButtonView, defStyle, 0);

        if (a.hasValue(R.styleable.TouchscreenControllerButtonView_unpressedDrawable)) {
            mUnpressedDrawable = a.getDrawable(R.styleable.TouchscreenControllerButtonView_unpressedDrawable);
            mUnpressedDrawable.setCallback(this);
        }

        if (a.hasValue(R.styleable.TouchscreenControllerButtonView_pressedDrawable)) {
            mPressedDrawable = a.getDrawable(R.styleable.TouchscreenControllerButtonView_pressedDrawable);
            mPressedDrawable.setCallback(this);
        }

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the example drawable on top of the text.
        Drawable drawable = mPressed ? mPressedDrawable : mUnpressedDrawable;
        if (drawable != null) {
            drawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            drawable.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final boolean oldState = mPressed;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                mPressed = true;
                invalidate();

                if (mListener != null && !oldState)
                    mListener.onButtonStateChanged(this, true);

                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            {
                mPressed = false;
                invalidate();

                if (mListener != null && oldState)
                    mListener.onButtonStateChanged(this, false);

                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public boolean isPressed()
    {
        return mPressed;
    }

    public String getButtonName() {
        return mButtonName;
    }

    public void setButtonName(String buttonName) {
        mButtonName = buttonName;
    }

    public int getButtonCode()
    {
        return mButtonCode;
    }

    public void setButtonCode(int code)
    {
        mButtonCode = code;
    }

    public Drawable getPressedDrawable() {
        return mPressedDrawable;
    }

    public void setPressedDrawable(Drawable pressedDrawable) {
        mPressedDrawable = pressedDrawable;
    }

    public Drawable getUnpressedDrawable() {
        return mUnpressedDrawable;
    }

    public void setUnpressedDrawable(Drawable unpressedDrawable) {
        mUnpressedDrawable = unpressedDrawable;
    }

    public void setButtonStateChangedListener(ButtonStateChangedListener listener)
    {
        mListener = listener;
    }
}
