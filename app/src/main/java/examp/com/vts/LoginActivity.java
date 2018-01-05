package examp.com.vts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import examp.com.vts.data.LoginChangeEvent;
import examp.com.vts.data.UserBean;
import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;
import examp.com.vts.utils.UserManager;

public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        tvLogin = findViewById(R.id.tv_commit);
    }

    private void initEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAccount.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(etAccount.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void doLogin(String account, String password) {
        NetRepository.getInstance().login(account, password)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean o) {
                        UserManager.getInstance().setUser(o);

                        Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
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
