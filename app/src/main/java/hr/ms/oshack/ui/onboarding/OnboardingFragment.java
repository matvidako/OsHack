package hr.ms.oshack.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hr.ms.oshack.R;

public class OnboardingFragment extends Fragment {

    public static String EXTRA_MESSAGE = "ExtraMessage";
    public static String EXTRA_IMAGE = "ExtraImage";
    public static String EXTRA_FAKE_FAB = "ExtraFakeFab";

    private String message;
    private @DrawableRes int drawableResId;
    private @DrawableRes int fakeFabResId;

    @Bind(R.id.message)
    TextView messageTv;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.fake_fab)
    ImageView fakeFab;

    public static OnboardingFragment getInstance(String message, @DrawableRes int drawableResId, @DrawableRes int fakeFabResId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        args.putInt(EXTRA_IMAGE, drawableResId);
        args.putInt(EXTRA_FAKE_FAB, fakeFabResId);
        OnboardingFragment onboardingFragment = new OnboardingFragment();
        onboardingFragment.setArguments(args);
        return onboardingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_onboarding, container, false);
        ButterKnife.bind(this, rootView);
        loadFromArgs();
        messageTv.setText(message);
        imageView.setImageResource(drawableResId);

        if(fakeFabResId != 0) {
            fakeFab.setImageResource(fakeFabResId);
        }

        return rootView;
    }

    private void loadFromArgs() {
        Bundle args = getArguments();
        message = args.getString(EXTRA_MESSAGE);
        drawableResId = args.getInt(EXTRA_IMAGE);
        fakeFabResId = args.getInt(EXTRA_FAKE_FAB);
    }

}
