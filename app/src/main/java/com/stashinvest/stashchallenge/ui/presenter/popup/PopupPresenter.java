package com.stashinvest.stashchallenge.ui.presenter.popup;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.api.model.Metadata;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PopupPresenter extends BasePresenter<PopupContract.PopupView>
        implements PopupContract.Presenter {


    @Inject
    GettyImageService gettyImageService;

    PopupContract.PopupView popupView;

    @Inject
    public PopupPresenter() {
    }

    public void bind(PopupContract.PopupView popupView) {
        this.popupView = popupView;
    }

    public void unbind() {
        popupView = null;
    }

    @Override
    public void onDisplaySimilarImages(String id) {
        similarImages(id);
    }

    @Override
    public void onDisplayMetadata(String id) {
        imageMetadata(id);
    }

    private void imageMetadata(String id) {
        Observable<MetadataResponse> metadataResponseObservable =
                gettyImageService
                        .getImageMetadata(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        metadataResponseObservable.subscribe(new Observer<MetadataResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MetadataResponse metadataResponse) {
                List<Metadata> metadataList = metadataResponse.getMetadata();
                popupView.showMetadata(metadataList.get(0).getTitle());

            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void similarImages(String id) {
        Observable<ImageResponse> imageResponseObservable =
                gettyImageService
                        .getSimilarImages(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

        imageResponseObservable.subscribe(new Observer<ImageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ImageResponse imageResponse) {
                List<ImageResult> images = imageResponse.getImages();
                if (images.size() >= 3) {
                    popupView.showSimilarImages(
                            images.get(0).getThumbUri(),
                            images.get(1).getThumbUri(),
                            images.get(2).getThumbUri());
                } else if (images.size() == 2) {
                    popupView.showSimilarImages(
                            images.get(0).getThumbUri(),
                            images.get(1).getThumbUri(),
                            null);

                } else if (images.size() == 1) {
                    popupView.showSimilarImages(
                            images.get(0).getThumbUri(),
                            null,
                            null);
                } else {
                    popupView.showSimilarImages(null, null, null);
                }



            }

            @Override
            public void onError(Throwable e) {
                //toast
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
