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
import examp.com.vts.utils.SPUtils;
import examp.com.vts.utils.UserManager;

public class FeedbackActivity extends BaseActivity {

    private EditText  etFeedBack;
    private TextView  tvCommit;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
        initEvent();

    }

    private void initView() {
        etFeedBack = findViewById(R.id.et_content);
        tvCommit = findViewById(R.id.tv_commit);
        ivBack = findViewById(R.id.iv_back);
    }

    private void initEvent() {
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFeedBack.getText().toString())){
                    Toast.makeText(mActivity, "请先输入您宝贵的建议", Toast.LENGTH_SHORT).show();
                    return;
                }

                docommit(etFeedBack.getText().toString());
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void docommit(String s) {
        NetRepository.getInstance().saveOpinion(s)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(mActivity, "提交成功，感谢您对我们的支持", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }
}
