package com.stashinvest.stashchallenge.ui.presenter.main;

import android.view.View;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.fragment.PopUpDialogFragment;
import com.stashinvest.stashchallenge.ui.presenter.BasePresenter;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    MainContract.MainView mainView;

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
        view.hideProgress();

        if (response.isSuccessful()) {
            List<ImageResult> images = response.body().getImages();
            updateImages(images);
        } else {
            view.hideProgress();
        }
    }

    private void updateImages(List<ImageResult> images) {
        List<BaseViewModel> viewModels = new ArrayList<>();
        int i = 0;
        for (ImageResult imageResult : images) {
            viewModels.add(gettyImageFactory.createGettyImageViewModel(i++, imageResult, this::onImageLongPress));
        }

        mainView.showSearchedImages(viewModels);
    }

    @Override
    public void onFailure(Call<ImageResponse> call, Throwable t) {

    }

    @Override
    public void onImageSearched(String searchQuery) {
        view.showProgress();
        Call<ImageResponse> call = gettyImageService.searchImages(searchQuery);
        call.enqueue(this);
    }

    @Override
    public void onPopUpLaunched() {

    }

    private void getImages(String searchQuery) {

    }

    private void launchPopUp() {

        PopUpDialogFragment popUpDialogFragment = PopUpDialogFragment.newInstance();

    }

    public void onImageLongPress(String id, String uri) {
        //todo - implement new feature
        view.showError("Hello Test");
    }
}
