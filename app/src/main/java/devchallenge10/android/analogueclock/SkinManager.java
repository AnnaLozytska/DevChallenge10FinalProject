package devchallenge10.android.analogueclock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SkinManager {

    private final Context context;
    private final Resources resources;
    private final SettingsManager mSettingsManager;
    private final List<OnSkinChangedListener> listeners;
    private int clockId;
    private Drawable face;
    private Drawable hourHand;
    private Drawable minuteHand;
    private Drawable secondHand;

    int[] faceSkins = new int[]{R.drawable.clock_face_default,
            R.drawable.clock_face_lathin,
            R.drawable.clock_face_chineese};

    int[] handHourSkins = new int[]{R.drawable.hand_hour_default,
            R.drawable.hand_hour_pink};

    public SkinManager(Context context, int clockId) {
        this.context = context;
        resources = context.getResources();
        mSettingsManager = SettingsManager.getInstance(context);
        listeners = new ArrayList<>(8);
        this.clockId = clockId;
        setSkin(clockId);
    }

    private void setSkin(int clockId) {
        // I would have been so happy to find more beautiful default skins,
        // but bad-bad-bad internet connection ruined my dreams :(
        face = context.getDrawable(R.drawable.clock_face_default);
        hourHand = context.getDrawable(R.drawable.hand_hour_default);
        minuteHand = context.getDrawable(R.drawable.hand_minute_default);
        secondHand = context.getDrawable(R.drawable.hand_second_default);
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

    public int[] getFaceSkins() {
        return faceSkins;
    }

    public int[] getHandSkins() {
        return handHourSkins;
    }

    public interface OnSkinChangedListener {
        void onSkinChanged();
    }
}
