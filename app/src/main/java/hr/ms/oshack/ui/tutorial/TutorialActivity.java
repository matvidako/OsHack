package hr.ms.oshack.ui.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ms.oshack.R;

public class TutorialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        pager.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        indicator.setViewPager(pager);
        onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @OnClick(R.id.close)
    public void onClose() {
        finish();
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 5) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 6;
        String[] titles;
        int[] drawables = {R.drawable.tutorial_1, R.drawable.tutorial_2, R.drawable.tutorial_3, R.drawable.tutorial_4};

        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.tutorial_messages);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new TutorialFragmentFirst();
            } else if(position == 5) {
                return new TutorialFragmentLast();
            }
            position--;
            return TutorialFragment.getInstance(titles[position], drawables[position]);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        Toast.makeText(TutorialActivity.this, getString(R.string.trap_success), Toast.LENGTH_SHORT).show();
    }
}

