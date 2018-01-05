package examp.com.vts.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import examp.com.vts.R;
import examp.com.vts.utils.DisplayUtils;

/**
 * Created by a55 on 2017/11/30.
 */

public class ImgPreviewPopupWindow extends PopupWindow {

    @BindView(R.id.centerView) SimpleDraweeView mImgAvatar;

    private Activity               mActivity;
    private LayoutInflater         mInflater;
    private View                   mContentView;
    private View                   mParentView;

    public ImgPreviewPopupWindow(Activity activity, String imgUrl) {
        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.img_preview_popupwindow, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        int screenWidth = DisplayUtils.getScreenWidth(mActivity);
        setWidth(screenWidth);
        setHeight(DisplayUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        update();

        mImgAvatar.setImageURI(imgUrl);

        mContentView.setOnClickListener(v -> showOrDismiss(mParentView));
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;
        mParentView = parent;
        long duration = 250;
        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.00f, 0.1f, 1.00f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            mImgAvatar.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            mImgAvatar.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }

}
