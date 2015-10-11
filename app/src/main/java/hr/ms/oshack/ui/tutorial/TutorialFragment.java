package hr.ms.oshack.ui.tutorial;

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

public class TutorialFragment extends Fragment {

    public static String EXTRA_MESSAGE = "ExtraMessage";
    public static String EXTRA_IMAGE = "ExtraImage";

    private String message;
    private @DrawableRes int drawableResId;

    @Bind(R.id.message)
    TextView messageTv;
    @Bind(R.id.image)
    ImageView imageView;

    public static TutorialFragment getInstance(String message, @DrawableRes int drawableResId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        args.putInt(EXTRA_IMAGE, drawableResId);
        TutorialFragment tutorialFragment = new TutorialFragment();
        tutorialFragment.setArguments(args);
        return tutorialFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial, container, false);
        ButterKnife.bind(this, rootView);
        loadFromArgs();
        messageTv.setText(message);
        imageView.setImageResource(drawableResId);
        return rootView;
    }

    private void loadFromArgs() {
        Bundle args = getArguments();
        message = args.getString(EXTRA_MESSAGE);
        drawableResId = args.getInt(EXTRA_IMAGE);
    }

}

