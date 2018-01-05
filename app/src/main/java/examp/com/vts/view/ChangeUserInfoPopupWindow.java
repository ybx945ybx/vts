package examp.com.vts.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import examp.com.vts.R;
import examp.com.vts.net.NetRepository;
import examp.com.vts.utils.UserManager;

/**
 * Created by a55 on 2017/11/29.
 */

public class ChangeUserInfoPopupWindow extends PopupWindow {

    @BindView(R.id.llyt_center_view) LinearLayout     llytCenterView;
    @BindView(R.id.iv_close)         ImageView        ivClose;
    @BindView(R.id.iv_add_avator)    SimpleDraweeView ivAddAvator;
    @BindView(R.id.et_nickName)      EditText         etNickName;
    @BindView(R.id.tv_commit)        TextView         tvCommit;

    private Activity               mActivity;
    private LayoutInflater         mInflater;
    private View                   mContentView;
    private View                   mParentView;
    private OnChangeAvatarListener mOnChangeAvatarListener;
    private OnCommitListener mOnCommitListener;

//    private String avatorBase64;

    public ChangeUserInfoPopupWindow(Activity activity, String imgUrl) {
        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.change_userinfo_popu_window, null);
        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //        mTvChangeAvatar.setVisibility(canChagneAvatar ? View.VISIBLE : View.GONE);
        //        int screenWidth = DisplayUtils.getScreenWidth(mActivity);
        //        setWidth(screenWidth);
        //        setHeight(DisplayUtils.getScreenHeight(mActivity));
        //        setFocusable(true);
        //        setOutsideTouchable(true);
        //        setBackgroundDrawable(new BitmapDrawable());
        //        if (fromRefund == 1) {              //  等于1的时候是退款申请单详情页的 图片设成match_parent
        //            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //            mImgAvatar.setLayoutParams(lp);
        //        }
        //        update();

        //        Glide.with(mActivity)
        //                .load(UpyUrlManager.getUrl(imgUrl, screenWidth))
        //                .placeholder(R.mipmap.ic_default_square_large)
        //                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        //                .dontAnimate()
        //                .into(mImgAvatar);

        ivClose.setOnClickListener(v -> showOrDismiss(mParentView));
        // 更换头像监听
        ivAddAvator.setOnClickListener(v -> {
            if (mOnChangeAvatarListener != null) {
                mOnChangeAvatarListener.onChange();
            }
        });

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnCommitListener != null){
                    mOnCommitListener.onCommit(etNickName.getText().toString());
                }

            }
        });

        renderUI();
    }

    private void renderUI() {
    }


    public void setAvator(String avator) {
//    public void setAvator(String avatorBase64, String avator) {
//        this.avatorBase64 = avatorBase64;
        ivAddAvator.setImageURI(avator.contains("file:") ? avator : "file:///" + avator);
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
            llytCenterView.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            llytCenterView.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }


    public void setOnChangeAvatarListener(OnChangeAvatarListener onChangeAvatarListener) {
        mOnChangeAvatarListener = onChangeAvatarListener;
    }

    public interface OnChangeAvatarListener {
        void onChange();
    }

    public void setCommitListener(OnCommitListener onCommitListener) {
        mOnCommitListener = onCommitListener;
    }

    public interface OnCommitListener {
        void onCommit(String nickname);
    }
}
