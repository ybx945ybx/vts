package examp.com.vts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.jzvd.JZVideoPlayerStandard;
import examp.com.vts.data.UserBean;
import examp.com.vts.utils.UserManager;

public class CourseActivity extends BaseActivity {
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private SimpleDraweeView      ivAvator;
    private TextView              tvName;
    private ImageView             ivBack;
    private ImageView             ivPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ivAvator = findViewById(R.id.iv_avator);
        tvName = findViewById(R.id.tv_name);

        UserBean userBean = UserManager.getInstance().getUser();
        if (userBean != null) {
            ivAvator.setImageURI(userBean.headpic);
            tvName.setText(userBean.nickname);
        }

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//                jzVideoPlayerStandard.thumbImageView.setImageURI("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");

        ivPlay = findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jzVideoPlayerStandard.startVideo();
                ivPlay.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (jzVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jzVideoPlayerStandard.releaseAllVideos();
    }
}
