package hr.ms.oshack.ui.onboarding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import hr.ms.oshack.R;

public class OnboardingActivity extends AppCompatActivity {

    @Bind(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        pager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager()));
    }

    private class OnboardingPagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 4;

        public OnboardingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new OnboardingFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
