package com.jerry.sweetcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jerry.sweetcamera.album.AlbumHelper;
import com.jerry.sweetcamera.album.ImageItem;
import com.jerry.sweetcamera.util.BitmapUtils;
import com.jerry.sweetcamera.util.TakePhotoEvent;
import com.jerry.sweetcamera.util.UpPicSuccessEvent;
import com.jerry.sweetcamera.widget.SquareCameraContainer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 自定义相机的activity
 *
 * @author jerry
 * @date 2015-09-01
 */
public class CameraActivity extends Activity {
    public static final String TAG = "CameraActivity";

    private CameraManager mCameraManager;

    private TextView m_tvFlashLight, m_tvCameraDireation;
    private SquareCameraContainer mCameraContainer;
    private ImageButton           m_ibRecentPic;

    private int mFinishCount = 2;   //finish计数   当动画和异步任务都结束的时候  再调用finish方法
    AlbumHelper helper;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        EventBus.getDefault().register(this);
        mCameraManager = CameraManager.getInstance(this);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {
        m_tvFlashLight = (TextView) findViewById(R.id.tv_flashlight);
        m_tvCameraDireation = (TextView) findViewById(R.id.tv_camera_direction);
        mCameraContainer = (SquareCameraContainer) findViewById(R.id.cameraContainer);
        m_ibRecentPic = (ImageButton) findViewById(R.id.ib_recentpic);
    }

    void initData() {
        mCameraManager.bindOptionMenuView(m_tvFlashLight, m_tvCameraDireation);
        //        mCameraContainer.setImagePath(getIntent().getStringExtra(PATH_OUTIMG));
        mCameraContainer.bindActivity(this);

        //todo  获取系统相册中的一张相片
        List<ImageItem> list = helper.getImagesList();
        if (list != null && list.size() != 0) {
            m_ibRecentPic.setImageBitmap(BitmapUtils.createCaptureBitmap(list.get(0).imagePath));
            m_ibRecentPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            m_ibRecentPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到系统相册
                    Intent intent = new Intent(Intent.ACTION_DEFAULT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(intent);
                }
            });
        } else {
            //设置默认图片
            m_ibRecentPic.setImageResource(R.drawable.selector_camera_icon_album);
        }
    }

    void initListener() {
        if (mCameraManager.canSwitch()) {
            m_tvCameraDireation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_tvCameraDireation.setClickable(false);
                    mCameraContainer.switchCamera();

                    //500ms后才能再次点击
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            m_tvCameraDireation.setClickable(true);
                        }
                    }, 500);
                }
            });
        }

        m_tvFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraContainer.switchFlashMode();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraContainer != null) {
            mCameraContainer.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraContainer != null) {
            mCameraContainer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraManager.unbinding();
        mCameraManager.releaseActivityCamera();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //在创建前  释放相机
    }

    /**
     * 对一些参数重置
     */
    public void rest() {
        mFinishCount = 2;
    }

    /**
     * 退出按钮点击
     */
    public void onExitClicked(View view) {
        onBackPressed();
    }

    /**
     * 照相按钮点击
     */
    public void onTakePhotoClicked(View view) {
        mCameraContainer.takePicture();
    }

    /**
     * 提交finish任务  进行计数  都在main Thread
     */

    /**
     * 照完照片 提交
     */
    public void postTakePhoto(String imagePath) {
        //        mFinishCount--;
        //        if (mFinishCount < 0) mFinishCount = 2;
        //        if (mFinishCount == 0) {
        //            setResult(RESULT_OK);
        //            finish();
        //        }


        //        mCameraManager.releaseActivityCamera();

        Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();

        EventBus.getDefault().post(new TakePhotoEvent(imagePath));
        //        Observable.timer(2, TimeUnit.SECONDS)
        //                .subscribeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Action1<Long>() {
        //                    @Override
        //                    public void call(Long isFirstLauch) {
        //                        Intent intent = new Intent(SplashActivity.this, UserManager.getInstance().isLogin() ? MainActivity.class : GuideActivity.class);
        //                        SplashActivity.this.startActivity(intent);
        //                        SplashActivity.this.finish();
        //                    }
        //                });
        //        Bitmap bitmap = SweetApplication.CONTEXT.getCameraBitmap();

        //        if (bitmap != null) {
        //            m_ibRecentPic.setImageBitmap(bitmap);
        //        }


    }

    @Subscribe
    public void onUpPicSuccessEvnt(UpPicSuccessEvent event){
//        Toast.makeText(this, "收到上传完图片的回调", Toast.LENGTH_SHORT).show();

        //                                Intent intent = new Intent(CameraActivity.this, MusicActivity.class);
//                                intent.putExtra("music", event.mp3);
//                                startActivity(intent);
                                finish();

    }

}
