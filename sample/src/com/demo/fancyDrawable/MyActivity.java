package com.demo.fancyDrawable;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import com.ivonhoe.drawable.*;

public class MyActivity extends Activity {

    private ColorfulDrawable mColorfulDrawable;

    private ImageView mGridImage;
    private ImageView mStreakImage;
    private ImageView mBallsLineImage;
    private ImageView mTextLineImage;
    private ImageView mBallsCircleImage;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadDrawables();
    }

    private void loadDrawables() {
        /**
         *
         * */
        mGridImage = (ImageView) findViewById(R.id.gridImage);
        GridDrawable gridDrawable = new GridDrawable();
        Drawable itemDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        for (int i = 0; i < 10; i++) {
            gridDrawable.addDrawable(itemDrawable);
        }
        int size = getResources().getDimensionPixelSize(R.dimen.itemSize);
        int padding = getResources().getDimensionPixelOffset(R.dimen.itemPadding);
        gridDrawable.setPadding(padding, padding, padding, padding);
        gridDrawable.setDefaultChildSize(size, size);
        mGridImage.setImageDrawable(gridDrawable);

        /**
         *
         * */
        Drawable streakDrawable = new StreakDrawable();
        mStreakImage = (ImageView) findViewById(R.id.streakImage);
        mStreakImage.setImageDrawable(streakDrawable);

        /**
         *
         * */
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            mColorfulDrawable = new ColorfulDrawable();
            actionBar.setBackgroundDrawable(mColorfulDrawable);
        }

        /**
         *
         * */
        Drawable ballsDrawable = new BallsLineDrawable();
        mBallsLineImage = (ImageView) findViewById(R.id.ballsLineImage);
        mBallsLineImage.setImageDrawable(ballsDrawable);

        /**
         *
         * */
        Drawable textDrawable = new BallsLineDrawable();
        ((AtomDrawable) textDrawable).setAtomText("LOADING");
        Paint paint = new Paint();
        paint.setDither(false);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(5);
        paint.setTextSize(40);
        paint.setColor(Color.BLUE);
        ((AtomDrawable) textDrawable).setPaint(paint);
        mTextLineImage = (ImageView) findViewById(R.id.textLineImage);
        mTextLineImage.setImageDrawable(textDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startDrawableAnimation(mColorfulDrawable);
        startDrawableAnimation(mStreakImage.getDrawable());
        startDrawableAnimation(mBallsLineImage.getDrawable());
        startDrawableAnimation(mTextLineImage.getDrawable());
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopDrawableAnimation(mColorfulDrawable);
        stopDrawableAnimation(mStreakImage.getDrawable());
        stopDrawableAnimation(mBallsLineImage.getDrawable());
        stopDrawableAnimation(mTextLineImage.getDrawable());
    }

    private void startDrawableAnimation(Drawable drawable) {
        if (drawable instanceof FancyDrawable) {
            ((FancyDrawable) drawable).start();
        }
    }

    private void stopDrawableAnimation(Drawable drawable) {
        if (drawable instanceof FancyDrawable) {
            ((FancyDrawable) drawable).stop();
        }
    }
}
