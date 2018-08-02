package com.stashinvest.stashchallenge.ui.presenter.main;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.presenter.BasePresenter;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends BasePresenter<MainContract.MainView>
        implements MainContract.Presenter, Callback<ImageResponse> {
    @Inject
    ViewModelAdapter adapter;
    @Inject
    GettyImageFactory gettyImageFactory;
    @Inject
    GettyImageService gettyImageService;

    private MainContract.MainView mainView;

    @Inject
    public MainPresenter() {
    }

    public void bind(MainContract.MainView mainView) {
        this.mainView = mainView;
    }

    public void unbind() {
        mainView = null;
    }

    @Override
    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
        mainView.hideProgress();

        if (response.isSuccessful()) {
            List<ImageResult> images = response.body().getImages();
            updateImages(images);
        } else {
            view.showError(response.code()+"");
        }
    }

    private void updateImages(List<ImageResult> images) {
        List<BaseViewModel> viewModels = new ArrayList<>();
        int i = 0;
        for (ImageResult imageResult : images) {
            viewModels.add(gettyImageFactory.createGettyImageViewModel(i++, imageResult, this::onImageLongPress));
        }
        view.showSearchedImages(viewModels);
    }

    @Override
    public void onFailure(Call<ImageResponse> call, Throwable t) {
        view.showError(t.getMessage());
    }

    @Override
    public void onImageSearched(String searchQuery) {
        view.showProgress();

        Observable<ImageResponse> imageResponseObservable = gettyImageService.searchImages(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        imageResponseObservable.subscribe(new Observer<ImageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ImageResponse imageResponse) {
                    List<ImageResult> images = imageResponse.getImages();
                    updateImages(images);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.getMessage());
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void onImageLongPress(String id, String uri) {
        mainView.showPopUp(id, uri);
    }
}
