package com.example.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.support.widget.progressbar.SmoothProgressBar;
import org.ivonhoe.supportlib.R;

import java.util.ArrayList;

/**
 * Created by ivonhoe on 2014/12/24.
 */
public class ProgressBarLayout extends LinearLayout implements View.OnClickListener {

    private ArrayList<SmoothProgressBar> smoothProgressBars = new ArrayList<SmoothProgressBar>();

    private Button progressStartButton;
    private Button progressStopButton;

    public ProgressBarLayout(Context context) {
        super(context);
    }

    public ProgressBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setupSmoothProgressBar();
    }

    private void setupSmoothProgressBar() {
        SmoothProgressBar smoothProgressBar1 = (SmoothProgressBar) findViewById(R.id.smoothProgress1);
        SmoothProgressBar smoothProgressBar2 = (SmoothProgressBar) findViewById(R.id.smoothProgress2);
        SmoothProgressBar smoothProgressBar3 = (SmoothProgressBar) findViewById(R.id.smoothProgress3);
        SmoothProgressBar smoothProgressBar4 = (SmoothProgressBar) findViewById(R.id.smoothProgress4);
        SmoothProgressBar smoothProgressBar5 = (SmoothProgressBar) findViewById(R.id.smoothProgress5);

        smoothProgressBars.add(smoothProgressBar1);
        smoothProgressBars.add(smoothProgressBar2);
        smoothProgressBars.add(smoothProgressBar3);
        smoothProgressBars.add(smoothProgressBar4);
        smoothProgressBars.add(smoothProgressBar5);

        progressStartButton = (Button) findViewById(R.id.start);
        progressStopButton = (Button) findViewById(R.id.stop);

        progressStartButton.setOnClickListener(this);
        progressStopButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start) {
            progressiveStart();
        } else if (view.getId() == R.id.stop) {
            progressiveStop();
        }
    }

    private void progressiveStart() {
        for (SmoothProgressBar progressBar : smoothProgressBars) {
            progressBar.progressiveStart();
        }
    }

    private void progressiveStop() {
        for (SmoothProgressBar progressBar : smoothProgressBars) {
            progressBar.progressiveStop();
        }
    }
}
