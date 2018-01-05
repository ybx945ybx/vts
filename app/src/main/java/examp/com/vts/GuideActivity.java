package examp.com.vts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import examp.com.vts.data.LoginChangeEvent;

public class GuideActivity extends BaseActivity {

    private TextView tvLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        EventBus.getDefault().register(this);

        initView();
        initEvent();

    }

    private void initView() {
        tvLogin = findViewById(R.id.tv_login);
        tvRegister = findViewById(R.id.tv_register);
    }

    private void initEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "登录 ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "注册 ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GuideActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe
    public void onLoginChangeEvent(LoginChangeEvent event) {
//        Toast.makeText(mActivity, "接收到了登录状态变化¬", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
