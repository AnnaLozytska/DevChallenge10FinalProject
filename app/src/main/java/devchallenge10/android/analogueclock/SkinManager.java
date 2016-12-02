package devchallenge10.android.analogueclock;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class SkinManager {


    private final Context mContext;
    private final Resources mResources;
    private final SettingsManager mSettingsManager;

    private final List<OnSkinChangedListener> listeners;


    public SkinManager(Context context) {
        mContext = context;
        mResources = context.getResources();
        mSettingsManager = SettingsManager.getInstance(context);
        listeners = new ArrayList<>(8);
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
