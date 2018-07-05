package com.stashinvest.stashchallenge.ui.presenter.main;

import com.stashinvest.stashchallenge.ui.presenter.BaseView;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;

import java.util.List;

/**
 * Created by jorgereina on 7/2/18.
 */

public interface MainContract {

    interface Presenter {

        void onImageSearched(String searchQuery);
        void onPopUpLaunched();

        void onImageLongPress(String id, String uri);
    }

    interface MainView extends BaseView {

        void showSearchedImages(List<BaseViewModel> imageResponseList);
        void showPopUp(String id, String uri);

    }
}
