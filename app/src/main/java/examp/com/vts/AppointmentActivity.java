package examp.com.vts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;

public class AppointmentActivity extends BaseActivity {

    private ImageView ivBack;

    private EditText etName;
    private EditText etPhone;
    private TextView tvCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        initView();
        initEvent();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        tvCommit = findViewById(R.id.tv_commit);
    }

    private void initEvent() {
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    Toast.makeText(mActivity, "请先输入电话", Toast.LENGTH_SHORT).show();
                    return;
                }

                doCommit(etName.getText().toString(), etPhone.getText().toString());
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void doCommit(String name, String phone) {
        NetRepository.getInstance().saveOrder(name, phone)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(mActivity, "预约成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }
}
