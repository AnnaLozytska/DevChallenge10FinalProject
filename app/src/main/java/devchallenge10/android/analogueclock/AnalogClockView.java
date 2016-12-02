package devchallenge10.android.analogueclock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class AnalogClockView extends View implements SkinManager.OnSkinChangedListener {

    private SkinManager skinManager;


    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;


    private boolean mAttached;
    private boolean mChanged;

    public AnalogClockView(Context context) {
        this(context, null);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        skinManager = new SkinManager(context);
        skinManager.addListener(this);
    }

    @Override
    public void onSkinChanged() {
        //TODO
    }
}
