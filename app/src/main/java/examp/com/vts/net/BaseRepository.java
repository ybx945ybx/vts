package examp.com.vts.net;

import android.accounts.NetworkErrorException;
import android.content.Context;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by a55 on 2017/11/2.
 */

public class BaseRepository {

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable) {
        return this.transform(observable, null, false);
    }

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable, Context context, boolean needSystemTime) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(result -> {
                    if (result == null) {
                        return Observable.error(new NetworkErrorException());
                    } else {
                        if (result.status == 1) { // 成功
                            return Observable.just(result.data);
                        } else { // 错误
                            return Observable.error(ErrorException.createException(result.status, result.msg));
                        }
                    }
                });
    }
}

