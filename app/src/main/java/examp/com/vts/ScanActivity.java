package examp.com.vts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private TextView     tvAboutUs;
    private TextView     tvCourse;
    private TextView     tvFollow;
    private TextView     tvCooperation;
    private TextView     tvClause;
    private TextView     tvProtocols;
    private TextView     tvfeedback;
    private TextView     tvAppointment;
    private ImageView    ivDrawerLayoutBack;

    private ImageView ivBack;
    private ImageView ivBottomCenter;
    private ImageView ivBottomLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        intView();
        initEvent();
    }

    private void intView() {
        drawerLayout = findViewById(R.id.drawer_layout);

        tvAboutUs = findViewById(R.id.tv_about_us);
        tvCourse = findViewById(R.id.tv_course);
        tvFollow = findViewById(R.id.tv_follow);
        tvCooperation = findViewById(R.id.tv_cooperation);
        tvClause = findViewById(R.id.tv_clause);
        tvProtocols = findViewById(R.id.tv_protocols);
        tvfeedback = findViewById(R.id.tv_feedback);
        tvAppointment = findViewById(R.id.tv_appointment);
        ivDrawerLayoutBack = findViewById(R.id.iv_DrawerLayout_back);

        ivBack = findViewById(R.id.iv_back);
        ivBottomCenter = findViewById(R.id.iv_bottom_btn);
        ivBottomLeft = findViewById(R.id.iv_bottom_left);
    }

    private void initEvent() {
        tvAboutUs.setOnClickListener(this);
        tvCourse.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvCooperation.setOnClickListener(this);
        tvClause.setOnClickListener(this);
        tvProtocols.setOnClickListener(this);
        tvfeedback.setOnClickListener(this);
        tvAppointment.setOnClickListener(this);

        ivBottomLeft.setOnClickListener(this);
        ivBottomCenter.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivDrawerLayoutBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_bottom_btn:
                Intent intentTakePhoto = new Intent(ScanActivity.this, TakePhoteActivity.class);
                startActivity(intentTakePhoto);
                break;
            case R.id.iv_bottom_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.tv_about_us:    // 关于我们
                Intent intent = new Intent(ScanActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_course:     // 教程
                Intent intent1 = new Intent(ScanActivity.this, CourseActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_follow:    // 关注微信
                Intent intent2 = new Intent(ScanActivity.this, FollowActivity.class);
                startActivity(intent2);
                break;
//            case R.id.tv_cooperation:
//                Intent intent3 = new Intent(ScanActivity.this, CooperationActivity.class);
//                startActivity(intent3);
//                break;
            case R.id.tv_clause:  // 使用条款
                Intent intent4 = new Intent(ScanActivity.this, ClauseActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_protocols:  // 隐私协议
                Intent intent5 = new Intent(ScanActivity.this, ProtocolsActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_feedback:   // 意见反馈
                Intent intent6 = new Intent(ScanActivity.this, FeedbackActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_appointment:    // 预约
                Intent intent7 = new Intent(ScanActivity.this, AppointmentActivity.class);
                startActivity(intent7);
                break;
            case R.id.iv_DrawerLayout_back:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

        }
    }
}
