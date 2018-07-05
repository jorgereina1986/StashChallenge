package com.stashinvest.stashchallenge.ui.presenter.popup;

import com.stashinvest.stashchallenge.ui.presenter.BaseView;

/**
 * Created by jorgereina on 7/5/18.
 */

public interface PopupContract {

    interface Presenter {

        void onDisplaySimilarImages(String id);

        void onDisplayMetadata(String id);
    }

    interface PopupView extends BaseView {

        void showSimilarImages(String uri1, String uri2, String uri3);

        void showMetadata(String title);
    }
}
