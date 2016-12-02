package devchallenge10.android.analogueclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AnalogClockView extends View implements SkinManager.OnSkinChangedListener {

    private SkinManager skinManager;

    private boolean isAttached;
    private boolean isChanged;

    private int faceWidth;
    private int faceHeight;

    private float hourPosition;
    private float minutePosition;
    private float secondPosition;

    private CountDownTimer secondsTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(1)) {
        @Override
        public void onTick(long l) {
            onTimeChanged();
            invalidate();
        }

        @Override
        public void onFinish() {

        }
    };

    private Calendar currentTime;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                currentTime = Calendar.getInstance(TimeZone.getTimeZone(tz));
            }
            onTimeChanged();
            secondsTimer.cancel();
            secondsTimer.start();
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
        faceWidth = skinManager.getFace().getIntrinsicWidth();
        faceHeight = skinManager.getFace().getIntrinsicHeight();
        onTimeChanged();
        secondsTimer.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isAttached) {
            isAttached = true;
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
        if (isAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            isAttached = false;
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
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < faceWidth) {
            hScale = (float) widthSize / (float) faceWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < faceHeight) {
            vScale = (float )heightSize / (float) faceHeight;
        }
        float scale = Math.min(hScale, vScale);
        setMeasuredDimension(resolveSizeAndState((int) (faceWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (faceHeight * scale), heightMeasureSpec, 0));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isChanged = true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = isChanged;
        if (changed) {
            isChanged = false;
        }
        int availableWidth = getMeasuredWidth();
        int availableHeight = getMeasuredHeight();
        int centreX = availableWidth / 2;
        int centreY = availableHeight / 2;

        final Drawable dial = skinManager.getFace();
        int faceWidth = dial.getIntrinsicWidth();
        int faceHeight = dial.getIntrinsicHeight();
        int saveCount;

        scaleIfNeeded(canvas, availableWidth, availableHeight, centreX, centreY, faceWidth, faceHeight);
        if (changed) {
            dial.setBounds(centreX - (faceWidth / 2), centreY - (faceHeight / 2), centreX + (faceWidth / 2), centreY + (faceHeight / 2));
        }
        dial.draw(canvas);
        saveCount = canvas.save();
        canvas.restoreToCount(saveCount);

        final Drawable hourHand = skinManager.getHourHand();
        int elementWidth = hourHand.getIntrinsicWidth();
        int elementHeight = hourHand.getIntrinsicHeight();
        scaleIfNeeded(canvas, faceWidth / 3, faceHeight / 3, centreX, centreY, elementWidth, elementHeight);
        hourHand.setBounds(centreX - (elementWidth / 2), centreY - elementHeight, centreX + (elementWidth / 2), centreY);
        canvas.rotate(hourPosition / 12.0f * 360.0f, centreX, centreY);
        hourHand.draw(canvas);
        canvas.restoreToCount(saveCount);

        final Drawable minuteHand = skinManager.getMinuteHand();
        elementWidth = minuteHand.getIntrinsicWidth();
        elementHeight = minuteHand.getIntrinsicHeight();
        scaleIfNeeded(canvas, faceWidth / 3, faceHeight / 3, centreX, centreY, elementWidth, elementHeight);
        minuteHand.setBounds(centreX - (elementWidth / 2), centreY - elementHeight, centreX + (elementWidth / 2), centreY);
        canvas.rotate(minutePosition / 60.0f * 360.0f, centreX, centreY);
        minuteHand.draw(canvas);
        canvas.restoreToCount(saveCount);

        final Drawable secondHand = skinManager.getSecondHand();
        elementWidth = secondHand.getIntrinsicWidth();
        elementHeight = secondHand.getIntrinsicHeight();
        scaleIfNeeded(canvas, faceWidth / 3, faceHeight / 3, centreX, centreY, elementWidth, elementHeight);
        secondHand.setBounds(centreX - (elementWidth / 2), centreY - elementHeight, centreX + (elementWidth / 2), centreY);
        canvas.rotate(secondPosition / 60.0f * 360.0f, centreX, centreY);
        secondHand.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    private boolean scaleIfNeeded(Canvas canvas, int availableWidth, int availableHeight,
                                  int centreX, int centreY, int elementWidth, int elementHeight) {
        if (availableWidth < elementWidth || availableHeight < elementHeight) {
            float scale = Math.min((float) availableWidth / (float) elementWidth,
                    (float) availableHeight / (float) elementHeight);
            canvas.save();
            canvas.scale(scale, scale, centreX, centreY);
            return true;
        }
        return false;
    }

    @Override
    public void onSkinChanged() {
        invalidate();
    }

    private void onTimeChanged() {
        currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR);
        int minute = currentTime.get(Calendar.MINUTE);
        int second = currentTime.get(Calendar.SECOND);
        hourPosition = hour + minutePosition / 60.0f;
        minutePosition = minute + second / 60.0f;
        secondPosition = (float) second;
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }
}
