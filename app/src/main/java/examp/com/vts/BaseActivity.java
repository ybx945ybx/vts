package examp.com.vts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by a55 on 2017/11/26.
 */

public class BaseActivity extends RxAppCompatActivity {
    protected Activity       mActivity;
    private   ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }


    public void showProgressDialog(String text) {
        showProgressDialog(text, false);
    }

    public void showProgressDialog(String text, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(mActivity);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }
            mDialog.setMessage(text);
            if (!((Activity) mActivity).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public void showProgressDialog(int resId) {
        showProgressDialog(resId, false);
    }

    public void showProgressDialog(int resId, boolean cancelable) {
        try {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = new ProgressDialog(mActivity);
                mDialog.setCanceledOnTouchOutside(cancelable);
                mDialog.setCancelable(cancelable);
                mDialog.setTitle(null);
            }

            mDialog.setMessage(mActivity.getResources().getString(resId));
            if (!((Activity) mActivity).isFinishing())
                mDialog.show();
        } catch (Exception ex) {
        }
    }

    public boolean isShowing() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                return true;
            }
        } catch (Exception ex) {
        }

        return false;
    }

    public void dismissProgressDialog() {
        try {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
