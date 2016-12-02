package devchallenge10.android.analogueclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

public class AnalogClockView extends View implements SkinManager.OnSkinChangedListener {

    private SkinManager skinManager;

    private boolean mAttached;
    private boolean mChanged;

    private int mDialWidth;
    private int mDialHeight;

    private float mHour; // hourPosition
    private float mMinutes; // minutePosition
    private float secondPosition; // minutePosition

    private Calendar mCalendar;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = Calendar.getInstance(TimeZone.getTimeZone(tz));
            }
            onTimeChanged();

            invalidate();
        }
    };

    public AnalogClockView(Context context) {
        this(context, null);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        skinManager = new SkinManager(context, getId());
        skinManager.addListener(this);
        mCalendar = Calendar.getInstance();
        mDialWidth = skinManager.getFace().getIntrinsicWidth();
        mDialHeight = skinManager.getFace().getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.0f;
        float vScale = 1.0f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }
        float scale = Math.min(hScale, vScale);
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        int availableWidth = getMeasuredWidth();
        int availableHeight = getMeasuredHeight();
        int centreX = availableWidth / 2;
        int centreY = availableHeight / 2;

        final Drawable dial = skinManager.getFace();
        int faceWidth = dial.getIntrinsicWidth();
        int faceHeight = dial.getIntrinsicHeight();
        boolean scaled = false;
        if (availableWidth < faceWidth || availableHeight < faceHeight) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) faceWidth,
                    (float) availableHeight / (float) faceHeight);
            canvas.save();
            canvas.scale(scale, scale, centreX, centreY);
        }
        if (changed) {
            dial.setBounds(centreX - (faceWidth / 2), centreY - (faceHeight / 2), centreX + (faceWidth / 2), centreY + (faceHeight / 2));
        }
        dial.draw(canvas);
        canvas.save();

        canvas.rotate(mHour / 12.0f * 360.0f, centreX, centreY);
        final Drawable hourHand = skinManager.getHourHand();
        if (changed) {
            faceWidth = hourHand.getIntrinsicWidth();
            faceHeight = hourHand.getIntrinsicHeight();
            hourHand.setBounds(centreX - (faceWidth / 2), centreY - faceHeight, centreX + faceWidth, centreY + faceHeight);
        }
        hourHand.draw(canvas);
        canvas.save();
        canvas.restore();

        canvas.rotate(mMinutes / 60.0f * 360.0f, centreX, centreY);
        final Drawable minuteHand = skinManager.getMinuteHand();
        if (changed) {
            faceWidth = minuteHand.getIntrinsicWidth();
            faceHeight = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(centreX - (faceWidth / 2), centreY - (faceHeight / 2), centreX + (faceWidth / 2), centreY + (faceHeight / 2));
        }
        minuteHand.draw(canvas);
        canvas.save();
        canvas.restore();

        canvas.rotate(secondPosition / 60.0f * 360.0f, centreX, centreY);
        final Drawable secondHand = skinManager.getSecondHand();
        if (changed) {
            faceWidth = secondHand.getIntrinsicWidth();
            faceHeight = secondHand.getIntrinsicHeight();
            secondHand.setBounds(centreX - (faceWidth / 2), centreY - (faceHeight / 2), centreX + (faceWidth / 2), centreY + (faceHeight / 2));
        }
        secondHand.draw(canvas);
        canvas.save();
        canvas.restore();

        if (scaled) {
            canvas.save();
            canvas.restore();
        }
    }

    @Override
    public void onSkinChanged() {
        invalidate();
    }

    private void onTimeChanged() {
        mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);
        mHour = hour + mMinutes / 60.0f;
        mMinutes = minute + second / 60.0f;
        secondPosition = (float) second;
        mChanged = true;
    }
}
