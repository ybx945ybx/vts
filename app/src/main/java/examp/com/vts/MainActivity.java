package examp.com.vts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jerry.sweetcamera.CameraActivity;
import com.jerry.sweetcamera.util.TakePhotoEvent;
import com.jerry.sweetcamera.util.UpPicSuccessEvent;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import examp.com.vts.data.UserBean;
import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;
import examp.com.vts.utils.FileUtils;
import examp.com.vts.utils.ImageUtil;
import examp.com.vts.utils.Md5FileNameGenerator;
import examp.com.vts.utils.UserManager;
import examp.com.vts.view.ChangeUserInfoPopupWindow;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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

    private ImageView ivBottomLeft;

    private ImageView        ivAdd;
    private SimpleDraweeView ivAvator;
    private TextView         tvName;

    private ImageView         ivAboutUs;
    private LinearLayout      llytSCan;
    private ArrayList<String> mPermissionList;// 权限列表
    private long clickTime = 0;                                // 第一次点击的时间
    private ChangeUserInfoPopupWindow changePopupWindow;
    private static final int    REQUEST_CODE_CHOOSE = 100;
//    private              String mAvatarBase64       = "";
    private              String mAvatar             = "";
    private              String picUrl             = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        getPermission();
        initVars();
        intView();
        initEvent();
        initData();
    }

    private void initVars() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        picUrl = FileUtils.saveBitmap(this, bitmap, new Md5FileNameGenerator().generate("share"));
        bitmap.recycle();
    }

    private void getPermission() {
        mPermissionList = new ArrayList<>();
        mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        mPermissionList.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mPermissionList.size(); i++) {
                if (checkSelfPermission(mPermissionList.get(i)) != PackageManager.PERMISSION_GRANTED) {
                    list.add(mPermissionList.get(i));
                }
            }

            switch (list.size()) {
                case 0:
                    break;
                case 1:
                    String[] permisses1 = {list.get(0)};
                    requestPermissions(permisses1, 100);
                    break;
                case 2:
                    String[] permisses2 = {list.get(0), list.get(1)};
                    requestPermissions(permisses2, 100);
                    break;
                case 3:
                    String[] permisses3 = {list.get(0), list.get(1), list.get(2)};
                    requestPermissions(permisses3, 100);
                    break;

            }

        }
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

        ivBottomLeft = findViewById(R.id.iv_bottom_left);

        ivAdd = findViewById(R.id.iv_add);
        ivAvator = findViewById(R.id.iv_avator);
        tvName = findViewById(R.id.tv_name);

        ivAboutUs = findViewById(R.id.iv_about_us);
        llytSCan = findViewById(R.id.llyt_scan);
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
        ivDrawerLayoutBack.setOnClickListener(this);

        ivAboutUs.setOnClickListener(this);
        llytSCan.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

    }

    private void initData() {
        UserBean userBean = UserManager.getInstance().getUser();
        ivAvator.setImageURI(userBean.headpic);
        tvName.setText(userBean.nickname);

    }

    /**
     * 选择头像
     */
    private void selectPic() {
        Matisse.from(MainActivity.this)

                .choose(MimeType.ofAll(), false)
                .countable(true)
                .maxSelectable(1)
                //                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));

            if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() > 0) {
                mAvatar = Matisse.obtainPathResult(data).get(0);
//                mAvatarBase64 = getUserAvatarBase64(Matisse.obtainPathResult(data).get(0));
//                Logger.d(mAvatarBase64);
                changePopupWindow.setAvator(Matisse.obtainPathResult(data).get(0));
//                changePopupWindow.setAvator(mAvatarBase64, Matisse.obtainPathResult(data).get(0));
                //                ImageLoaderUtils.showLocationImage(avatarPickResult.contains("file:") ? avatarPickResult : "file:///" + avatarPickResult, ivAvatar);
            }
        }
    }

    /**
     * 获取头像Base64编码
     *
     * @param avatar 头像本地url
     * @return 头像Base64编码
     */
    private String getUserAvatarBase64(String avatar) {
        Bitmap bmAvatar = ImageUtil.getSmallBitmap(avatar, 400, 400);
        return ImageUtil.compressImageBase64(bmAvatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:

                changePopupWindow = new ChangeUserInfoPopupWindow(MainActivity.this, "");
                changePopupWindow.setOnChangeAvatarListener(new ChangeUserInfoPopupWindow.OnChangeAvatarListener() {
                    @Override
                    public void onChange() {
                        selectPic();
                        //                        HaiUriMatchUtils.matchUri(getActivity(), uri);
                    }
                });
                changePopupWindow.setCommitListener(new ChangeUserInfoPopupWindow.OnCommitListener() {
                    @Override
                    public void onCommit(String nickname) {
                        if (TextUtils.isEmpty(nickname)){
                            Toast.makeText(mActivity, "请填写昵称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(mAvatar)){
                            Toast.makeText(mActivity, "请上传头像", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        showProgressDialog("正在更新信息");
                        try {
                            File file = null;
                            if (!TextUtils.isEmpty(mAvatar)) {
                                file = new File(mAvatar);
                                //                        Log.i(TAG, file.toString());
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                            }
//                            else {
//                                file = new File(picUrl);
//                                //                        Log.i(TAG, file.toString());
//                                if (!file.exists()) {
//                                    file.createNewFile();
//                                }
//                            }

                            NetRepository.getInstance()
                                    .uploadHeadPic(file, nickname, UserManager.getInstance().getUserId())
                                    .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                                    .subscribe(new DefaultSubscriber<UserBean>() {
                                        @Override
                                        public void onSuccess(UserBean o) {
                                            ivAvator.setImageURI(o.headpic);
                                            tvName.setText(o.nickname);
                                            //                                        UserBean userBean = UserManager.getInstance().getUser();
                                            //                                        userBean.headpic =
                                            //userBean.headpic = o.headpic;
                                            //userBean.nickname = o.nickname;
                                            UserManager.getInstance().setUser(o);
                                            dismissProgressDialog();
                                            changePopupWindow.showOrDismiss(ivAboutUs);
                                        }

                                        @Override
                                        public void onFinish() {

                                        }

                                        @Override
                                        public boolean onFailed(Throwable e) {
                                            dismissProgressDialog();
                                            return super.onFailed(e);
                                        }
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                            //                            Log.i(TAG, "jpeg保存失败");
                        }
                    }
                });
                changePopupWindow.showOrDismiss(ivAboutUs);
                break;
            case R.id.iv_about_us:
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.llyt_scan:
                //                Intent intent1 = new Intent(MainActivity.this, TakePhoteActivity.class);
                Intent intent1 = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_bottom_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.tv_about_us:    // 关于我们
                Intent intent11 = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent11);
                break;
            case R.id.tv_course:     // 教程
                Intent intent12 = new Intent(MainActivity.this, CourseActivity.class);
                startActivity(intent12);
                break;
            case R.id.tv_follow:    // 关注微信
                Intent intent2 = new Intent(MainActivity.this, FollowActivity.class);
                startActivity(intent2);
                break;
            //            case R.id.tv_cooperation:
            //                Intent intent3 = new Intent(ScanActivity.this, CooperationActivity.class);
            //                startActivity(intent3);
            //                break;
            case R.id.tv_clause:  // 使用条款
                Intent intent4 = new Intent(MainActivity.this, ClauseActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_protocols:  // 隐私协议
                Intent intent5 = new Intent(MainActivity.this, ProtocolsActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_feedback:   // 意见反馈
                Intent intent6 = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_appointment:    // 预约
                Intent intent7 = new Intent(MainActivity.this, AppointmentActivity.class);
                startActivity(intent7);
                break;
            case R.id.iv_DrawerLayout_back:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            Toast.makeText(this, "再点一次退出Vts", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    @Subscribe
    public void onTakePhotoSuccess(TakePhotoEvent takePhotoEvent) {
//        Toast.makeText(mActivity, takePhotoEvent.imagePath, Toast.LENGTH_SHORT).show();
        String imagepath = takePhotoEvent.imagePath;
        BufferedOutputStream bos = null;
        Bitmap               bm  = null;

        try {
            File file = new File(imagepath);
            if (!file.exists()) {
                file.createNewFile();
            }

            // 压缩图片

            bm = BitmapFactory.decodeFile(imagepath);
            bos = new BufferedOutputStream(new FileOutputStream(file));

                            int width  = bm.getWidth();
                            int height = bm.getHeight();
                            // 设置想要的大小
                            int newWidth  = 600;
                            int newHeight = 600;
                            // 计算缩放比例
                            float scaleWidth  = ((float) newWidth) / width;
                            float scaleHeight = ((float) newHeight) / height;
                            // 取得想要缩放的matrix参数
                            Matrix matrix = new Matrix();
                            matrix.postScale(scaleWidth, scaleHeight);
                            // 得到新的图片
                            bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                                    true);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

            FileOutputStream fos = new FileOutputStream(imagepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();


//            upPic(file);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.flush();
                bos.close();
                bm.recycle();

                File file = new File(imagepath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                // 上传照片到服务器
                upPic(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void upPic(File picture) {
        //        showProgressDialog("正在上传图片...");
        NetRepository.getInstance().uploadpic(picture)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        String mp3 = o.toString();

                        EventBus.getDefault().post(new UpPicSuccessEvent(mp3));
                        //                        dismissProgressDialog();
                        Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                        intent.putExtra("music", mp3);
                        startActivity(intent);
//                        finish();

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        //                        dismissProgressDialog();
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new UpPicSuccessEvent(""));

                        //
                        //                        //todo  删掉
//                                                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
//                                                intent.putExtra("music", "http://39.106.32.139/mp3/1.mp3");
//                                                startActivity(intent);
//                        finish();

                        return super.onFailed(e);
                    }
                });
    }
}
