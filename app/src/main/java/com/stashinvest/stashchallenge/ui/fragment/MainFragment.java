package com.stashinvest.stashchallenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.presenter.main.MainContract;
import com.stashinvest.stashchallenge.ui.presenter.main.MainPresenter;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.util.SpaceItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class MainFragment extends Fragment implements MainContract.MainView {
    @Inject
    ViewModelAdapter adapter;
    @Inject
    GettyImageService gettyImageService;
    @Inject
    GettyImageFactory gettyImageFactory;
    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.search_phrase)
    EditText searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindDimen(R.dimen.image_space)
    int space;

    Unbinder unbinder;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        mainPresenter.attachView(this);
        mainPresenter.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        searchView.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainPresenter.onImageSearched(searchView.getText().toString());
                return true;
            }
            return false;
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space, space, space, space));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void showSearchedImages(List<BaseViewModel> viewModels) {
        adapter.setViewModels(viewModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showPopUp(String id, String uri) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        PopUpDialogFragment popUpDialogFragment = PopUpDialogFragment.newInstance(id, uri);
        popUpDialogFragment.show(fragmentTransaction, "popupdialog");
    }

    @Override
    public void onDestroy() {
        mainPresenter.unbind();
        mainPresenter.detachView();
        super.onDestroy();
    }
}
