package examp.com.vts;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import examp.com.vts.adapter.RVBaseAdapter;
import examp.com.vts.adapter.RVBaseHolder;
import examp.com.vts.data.ImgBean;
import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;
import examp.com.vts.utils.DisplayUtils;
import examp.com.vts.view.ImgPreviewPopupWindow;

public class AboutUsActivity extends BaseActivity {
    private RecyclerView          rycvImgs;
    private ImgPreviewPopupWindow imgPreviewPopupWindow;
    private ImageView             ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rycvImgs = findViewById(R.id.rycv_img);
        rycvImgs.setLayoutManager(new GridLayoutManager(mActivity, 3));
        RVBaseAdapter mAdapter = new RVBaseAdapter<ImgBean>(mActivity, new ArrayList<>(), R.layout.pic_list_item) {
            @Override
            public void bindView(RVBaseHolder holder, ImgBean o) {
                SimpleDraweeView          img = holder.getView(R.id.iv_pic);
                LinearLayout.LayoutParams lp  = (LinearLayout.LayoutParams) img.getLayoutParams();
                lp.width = (DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 60)) / 3;
                lp.height = lp.width;
                img.setLayoutParams(lp);
                img.setImageURI(o.path);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgPreviewPopupWindow = new ImgPreviewPopupWindow(mActivity, o.path);
                        imgPreviewPopupWindow.showOrDismiss(rycvImgs);
                    }
                });
            }

        };
        rycvImgs.setAdapter(mAdapter);

        NetRepository.getInstance().picList()
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<ArrayList<ImgBean>>() {
                    @Override
                    public void onSuccess(ArrayList<ImgBean> o) {
                        mAdapter.setmDatas(o);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgPreviewPopupWindow != null && imgPreviewPopupWindow.isShowing()) {
            imgPreviewPopupWindow.showOrDismiss(rycvImgs);
        }
    }
}
