package com.stashinvest.stashchallenge.ui.presenter;

/**
 * Created by jorgereina on 7/2/18.
 */

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showError(String errorMessage);
}
