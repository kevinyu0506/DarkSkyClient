package presenters;

import views.LoginMvpView;

/**
 * Created by Kevin on 2018/3/14.
 */

public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void startLogin(String emailId);

}