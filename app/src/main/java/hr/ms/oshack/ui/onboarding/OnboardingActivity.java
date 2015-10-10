package hr.ms.oshack.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ms.oshack.R;
import hr.ms.oshack.storage.PrefsManager;
import hr.ms.oshack.ui.MapsActivity;

public class OnboardingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.start_btn)
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        pager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        indicator.setViewPager(pager);
        onPageSelected(0);
        PrefsManager.onOnboardingSeen(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 2) {
            indicator.setVisibility(View.GONE);
            startBtn.setVisibility(View.VISIBLE);
        } else {
            indicator.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @OnClick(R.id.start_btn)
    public void startMainActivity() {
        finish();
        Intent i = new Intent(this, MapsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private class OnboardingPagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 3;
        String[] titles;
        int[] drawables = {R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3};
        int[] fakeFabs = {R.drawable.komarac_fab, 0, R.drawable.boca_fab};

        public OnboardingPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.onboarding_messages);
        }

        @Override
        public Fragment getItem(int position) {
            return OnboardingFragment.getInstance(titles[position], drawables[position], fakeFabs[position]);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
