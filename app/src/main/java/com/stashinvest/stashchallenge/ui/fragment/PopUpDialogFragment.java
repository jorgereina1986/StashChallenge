package com.stashinvest.stashchallenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.api.model.Metadata;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopUpDialogFragment extends DialogFragment {

    private static final String KEY_ID = "id";
    private static final String KEY_URI = "uri";
    private static final String TAG = PopUpDialogFragment.class.getSimpleName() + ": ";

    @Inject
    GettyImageService gettyImageService;
    @Inject
    GettyImageFactory gettyImageFactory;

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.similar_image_view1)
    ImageView similarImageView1;
    @BindView(R.id.similar_image_view2)
    ImageView similarImageView2;
    @BindView(R.id.similar_image_view3)
    ImageView similarImageView3;
    @BindView(R.id.title_view)
    TextView titleView;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;

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
        Picasso.with(getContext()).load(uri).into(imageView);
        imageMetadata(id);
        similarImages(id);
    }

    private void imageMetadata(String id) {
//        progressBar.setVisibility(View.VISIBLE);
        Call<MetadataResponse> call = gettyImageService.getImageMetadata(id);
        call.enqueue(new Callback<MetadataResponse>() {
            @Override
            public void onResponse(Call<MetadataResponse> call, Response<MetadataResponse> response) {

                if (response.isSuccessful()) {
                    List<Metadata> metadataList = response.body().getMetadata();
                    titleView.setText(metadataList.get(0).getTitle());
                } else {
                    //todo show error
                    Toast.makeText(getContext(), "Metadata Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MetadataResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void similarImages(String id) {
//        progressBar.setVisibility(View.VISIBLE);
        Call<ImageResponse> call = gettyImageService.getSimilarImages(id);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {

                if (response.isSuccessful()) {
                    List<ImageResult> images = response.body().getImages();

                    if (images.size() >= 3) {
                        setImagesToIv(images.get(0).getThumbUri(), similarImageView1);
                        setImagesToIv(images.get(1).getThumbUri(), similarImageView2);
                        setImagesToIv(images.get(2).getThumbUri(), similarImageView3);
                    } else if (images.size() == 2) {
                        setImagesToIv(images.get(0).getThumbUri(), similarImageView1);
                        setImagesToIv(images.get(1).getThumbUri(), similarImageView2);
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView3);

                    } else if (images.size() == 1) {
                        setImagesToIv(images.get(0).getThumbUri(), similarImageView1);
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView2);
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView3);
                    } else {
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView1);
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView2);
                        Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(similarImageView3);
                    }

                } else {
                    //todo - show error
                    Toast.makeText(getContext(), "Similar images response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setImagesToIv(String imageUri, ImageView similarImageView) {
        Picasso.with(getContext()).load(imageUri).into(similarImageView);
    }
}
