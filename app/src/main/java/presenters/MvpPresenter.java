package presenters;

import views.MvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

}