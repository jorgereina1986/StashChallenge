package com.stashinvest.stashchallenge.ui.presenter;

import android.support.annotation.NonNull;

/**
 * Created by jorgereina on 7/2/18.
 */

public class BasePresenter<V> {

    protected V view;

    public final void attachView(@NonNull V view) {
        this.view = view;
    }

    public final void detachView() {
        view = null;
    }

    /**
     * Check if the view is attached.
     * This checking is only necessary when returning from an asynchronous call
     */
    protected final boolean isViewAttached() {
        return view != null;
    }
}
