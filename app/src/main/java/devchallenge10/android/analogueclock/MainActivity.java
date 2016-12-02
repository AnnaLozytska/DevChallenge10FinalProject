package devchallenge10.android.analogueclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    AnalogClockView clockView;
    SkinManager skinManager;
    LinearLayout faceContainer;
    LinearLayout handsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockView = (AnalogClockView) findViewById(R.id.clockView);
        faceContainer = (LinearLayout) findViewById(R.id.faceSkinsContainer);
        handsContainer = (LinearLayout) findViewById(R.id.handSkinsContainer);
        skinManager = clockView.getSkinManager();
        int[] faces = skinManager.getFaceSkins();
        int[] hands = skinManager.getHandSkins();

        for (final int faceId : faces) {
            ImageView faceView = new ImageView(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.view_size), ViewGroup.LayoutParams.WRAP_CONTENT);
            faceView.setLayoutParams(params);
            faceView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            faceView.setImageResource(faceId);
            faceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skinManager.setFace(faceId);
                }
            });
            faceContainer.addView(faceView);
        }

        for (final int handsId : hands) {
            ImageView handsView = new ImageView(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.view_size), ViewGroup.LayoutParams.WRAP_CONTENT);
            handsView.setLayoutParams(params);
            handsView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            handsView.setImageResource(handsId);
            handsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skinManager.setHourHand(handsId);
                }
            });
            handsContainer.addView(handsView);
        }
    }
}
