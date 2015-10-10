package hr.ms.oshack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ms.oshack.R;
import hr.ms.oshack.ui.onboarding.OnboardingActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_btn)
    public void startOnboarding() {
        finish();
        Intent i = new Intent(this, OnboardingActivity.class);
        startActivity(i);
    }

}
