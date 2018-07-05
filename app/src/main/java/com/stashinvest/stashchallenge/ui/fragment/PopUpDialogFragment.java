package com.stashinvest.stashchallenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.presenter.popup.PopupContract;
import com.stashinvest.stashchallenge.ui.presenter.popup.PopupPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PopUpDialogFragment extends DialogFragment implements PopupContract.PopupView {

    private static final String KEY_ID = "id";
    private static final String KEY_URI = "uri";

    @Inject
    GettyImageService gettyImageService;
    @Inject
    GettyImageFactory gettyImageFactory;
    @Inject
    PopupPresenter presenter;

    @BindView(R.id.image_view)
    ImageView mainImageView;
    @BindView(R.id.similar_image_view1)
    ImageView similarImageView1;
    @BindView(R.id.similar_image_view2)
    ImageView similarImageView2;
    @BindView(R.id.similar_image_view3)
    ImageView similarImageView3;
    @BindView(R.id.title_view)
    TextView titleView;

    Unbinder unbinder;


    public static PopUpDialogFragment newInstance(String id, String uri) {
        PopUpDialogFragment fragment = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        presenter.bind(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_popup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id = getArguments().getString(KEY_ID);
        String uri = getArguments().getString(KEY_URI);
        Picasso.with(getContext()).load(uri).into(mainImageView);
        presenter.onDisplayMetadata(id);
        presenter.onDisplaySimilarImages(id);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSimilarImages(String uri1, String uri2, String uri3) {
        Picasso.with(getContext()).load(uri1).into(similarImageView1);
        Picasso.with(getContext()).load(uri2).into(similarImageView2);
        Picasso.with(getContext()).load(uri3).into(similarImageView3);
    }

    @Override
    public void showMetadata(String title) {
        titleView.setText(title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unbind();
    }
}
