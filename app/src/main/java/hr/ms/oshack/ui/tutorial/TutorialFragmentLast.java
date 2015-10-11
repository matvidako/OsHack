package hr.ms.oshack.ui.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ms.oshack.R;

public class TutorialFragmentLast extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_last, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.cancel)
    public void onCancel() {
        getActivity().finish();
    }

    @OnClick(R.id.take_photo)
    public void onTakePhoto() {
        takePhoto();
    }

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 1);
    }

}
