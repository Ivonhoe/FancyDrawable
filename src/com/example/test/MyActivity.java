package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.support.view.progressbar.SmoothProgressBar;
import com.support.view.widget.core.AnimationListView;
import com.support.view.widget.control.AnimationController;
import com.support.view.widget.core.ViewPagerDecorator;
import com.support.view.widget.sample.*;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements View.OnClickListener {

    private AnimationListView listView1;
    private AnimationListView listView2;
    private SmoothProgressBar smoothProgressBar;
    private Button progressStartButton;
    private Button progressStopButton;

    private View view1, view2, view3;
    private ViewPagerDecorator viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private PagerTabStrip pagerTabStrip;
    private List<View> viewList;
    private List<String> titleList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
    }

    private void initView() {
        viewPager = (ViewPagerDecorator) findViewById(R.id.viewpager);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSpacing(50);

        view1 = findViewById(R.layout.layout1);
        view2 = findViewById(R.layout.layout2);
        view3 = findViewById(R.layout.layout3);

        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.layout1, null);
        view2 = lf.inflate(R.layout.layout2, null);
        view3 = lf.inflate(R.layout.layout3, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("ProgressBar");
        titleList.add("ListView");
        titleList.add("三");

        setupSmoothProgressBar();
        setupAnimationListView();
    }

    private void setupSmoothProgressBar() {
        smoothProgressBar = (SmoothProgressBar) view1.findViewById(R.id.smoothProgress);
        progressStartButton = (Button) view1.findViewById(R.id.start);
        progressStopButton = (Button) view1.findViewById(R.id.stop);

        progressStartButton.setOnClickListener(this);
        progressStopButton.setOnClickListener(this);
    }

    private void setupAnimationListView() {
        listView1 = (AnimationListView) view2.findViewById(R.id.listView1);
        listView1.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        listView1.attachAnimators(new RightInAnimation());

        listView2 = (AnimationListView) view3.findViewById(R.id.listView1);
        listView2.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        listView2.attachAnimators(new ScaleInAnimation());

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

        };

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                AnimationController.getInstance(getApplicationContext()).onPageScrolling(v, i1);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 0) {
                    AnimationController.getInstance(getApplicationContext()).onPageScrollEnd();
                }

            }
        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
            "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
            "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
            "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
            "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
            "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
            "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
            "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {
            smoothProgressBar.progressiveStart();
        } else if (v.getId() == R.id.stop) {
            smoothProgressBar.progressiveStop();
        }
    }
}
