package com.stashinvest.stashchallenge.ui.presenter;

/**
 * Created by jorgereina on 7/2/18.
 */

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showUnauthorizedError();

    void showEmpty();

    void showError(String errorMessage);

//    void showMessageLayout(boolean show);
}
