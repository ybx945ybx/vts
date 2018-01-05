package examp.com.vts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import examp.com.vts.data.LoginChangeEvent;
import examp.com.vts.data.UserBean;
import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;
import examp.com.vts.net.RetrofitFactory;
import examp.com.vts.utils.UserManager;

/**
 * Created by a55 on 2017/11/26.
 */

public class RegisterActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etNickName;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
    }

    private void initView() {
        etUserName = findViewById(R.id.et_username);
        etNickName = findViewById(R.id.et_nickname);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        tvRegister = findViewById(R.id.tv_commit);
    }

    private void initEvent() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUserName.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etNickName.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                    Toast.makeText(mActivity, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                doRegister(etUserName.getText().toString(), etNickName.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void doRegister(String account, String nickName, String password) {
        NetRepository.getInstance().register(account, password, nickName)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean o) {
                        UserManager.getInstance().setUser(o);
                        Logger.d(o.toString());
                        Toast.makeText(mActivity, "注册成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new LoginChangeEvent());
                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();

                        return super.onFailed(e);
                    }
                });


    }
}
