package devchallenge10.android.analogueclock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SkinManager {

    private final Context context;
    private final Resources mResources;
    private final SettingsManager mSettingsManager;
    private final List<OnSkinChangedListener> listeners;
    private int clockId;
    private Drawable face;
    private Drawable hourHand;
    private Drawable minuteHand;
    private Drawable secondHand;


    public SkinManager(Context context, int clockId) {
        this.context = context;
        mResources = context.getResources();
        mSettingsManager = SettingsManager.getInstance(context);
        listeners = new ArrayList<>(8);
        this.clockId = clockId;
        setSkin(clockId);
    }

    private void setSkin(int clockId) {
        face = context.getDrawable(R.drawable.clock_face);
        hourHand = context.getDrawable(R.drawable.hand_hour);
        minuteHand = context.getDrawable(R.drawable.hand_minute);
        secondHand = context.getDrawable(R.drawable.hand_second);
    }

    public Drawable getFace() {
        return face;
    }

    public void setFace(Drawable face) {
        this.face = face;
    }

    public Drawable getHourHand() {
        return hourHand;
    }

    public void setHourHand(Drawable hourHand) {
        this.hourHand = hourHand;
    }

    public Drawable getMinuteHand() {
        return minuteHand;
    }

    public void setMinuteHand(Drawable minuteHand) {
        this.minuteHand = minuteHand;
    }

    public Drawable getSecondHand() {
        return secondHand;
    }

    public void setSecondHand(Drawable secondHand) {
        this.secondHand = secondHand;
    }

    public void addListener(OnSkinChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnSkinChangedListener listener) {
        listeners.remove(listener);
    }

    public interface OnSkinChangedListener {
        void onSkinChanged();
    }
}
