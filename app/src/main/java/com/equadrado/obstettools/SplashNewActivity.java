package com.equadrado.obstettools;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.os.SystemClock.sleep;

public class SplashNewActivity extends AppCompatActivity {
    private final String TAG = "SplashNewActivity";
    Bitmap bitmapOriginal, bitmapBlur;
    ImageView back;
    TextView appTitle;
    LinearLayout layoutBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.back2);
        bitmapBlur = bitmapOriginal;
        appTitle = (TextView) findViewById(R.id.appTitle);
        back = (ImageView) findViewById(R.id.imageSplash);
        layoutBanner = (LinearLayout) findViewById(R.id.layoutBanner);

        startBlur();
    }

    public void startBlur() {
        ValueAnimator anim = ValueAnimator.ofFloat (1.0f, 5.0f);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean alpha = false;
            boolean banner = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                bitmapBlur = createBitmap_ScriptIntrinsicBlur(bitmapBlur, val );
                back.setImageBitmap(bitmapBlur);
                if (val > 4.9f) {
                    callMain();
                } else if ((val >= 4f) && !(alpha) ) {
                    alpha = true;
                    startAlpha();
                } else if ((val >= 2f) && !(banner) ) {
                    banner = true;
                    showBanner();
                }
            }
        });

        anim.setDuration(4000);
        anim.start();
    }

    private void startAlpha() {

        ValueAnimator anim = ValueAnimator.ofFloat (1.0f, 0.75f);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                back.setAlpha(val);
            }
        });

        anim.setDuration(1000);
        anim.start();
    }

    private void showBanner(){
        layoutBanner.setVisibility(View.VISIBLE);
    }

    private void callMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Bitmap createBitmap_ScriptIntrinsicBlur(Bitmap src, float r) {

        //Radius range (0 < r <= 25)
        if(r <= 0){
            r = 0.1f;
        }else if(r > 25){
            r = 25.0f;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(this);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, src);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }

}
