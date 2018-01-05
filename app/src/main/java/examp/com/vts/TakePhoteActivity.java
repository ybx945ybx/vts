package examp.com.vts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import examp.com.vts.net.DefaultSubscriber;
import examp.com.vts.net.NetRepository;
import examp.com.vts.utils.CameraUtils;
import examp.com.vts.view.CameraPreview;

public class TakePhoteActivity extends BaseActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Camera        mCamera;
    private CameraPreview mPreview;
    private FrameLayout   preview;
    private ImageView     captureButton;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_phote);
        mCamera = getCamera();
        mPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        captureButton = findViewById(R.id.button_capture);

        captureButton.setOnClickListener(v -> {
            try {
                mCamera.takePicture(shutter, raw, jpeg);
                Toast.makeText(TakePhoteActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TakePhoteActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 创建相机实例
     *
     * @return
     */
    private Camera getCamera() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


    private static final String                 TAG     = "Save";
    public               Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i(TAG, "shutter");
        }
    };
    // 获得没有压缩过的图片数据
    public               Camera.PictureCallback raw     = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "raw");
        }
    };
    //创建jpeg图片回调数据对象
    public               Camera.PictureCallback jpeg    = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            BufferedOutputStream bos = null;
            Bitmap               bm  = null;
            try {
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                CameraUtils.getInstance().setTakePicktrueOrientation(0, bm);
                imagePath = returnPathName().toString();
                File file = new File(imagePath);
                Log.i(TAG, file.toString());
                if (!file.exists()) {
                    file.createNewFile();
                }

                bos = new BufferedOutputStream(new FileOutputStream(file));
//                                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);


                // yasuotupian
//                int width  = bm.getWidth();
//                int height = bm.getHeight();
//                // 设置想要的大小
//                int newWidth  = 800;
//                int newHeight = 600;
//                // 计算缩放比例
//                float scaleWidth  = ((float) newWidth) / width;
//                float scaleHeight = ((float) newHeight) / height;
//                // 取得想要缩放的matrix参数
//                Matrix matrix = new Matrix();
//                matrix.postScale(scaleWidth, scaleHeight);
//                // 得到新的图片
//                bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
//                        true);
                compressImage(bm, imagePath);

                Log.i(TAG, "jpeg保存成功");
                Log.i(TAG, new String(data));


            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "jpeg保存失败");
            } finally {
                try {
                    bos.flush();
                    bos.close();
                    bm.recycle();
                    Log.i(TAG, "关闭成功 ");

                    File file = new File(imagePath);
                    Log.i(TAG, file.toString());
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    // 上传照片到服务器
                    upPic(file);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "关闭失败");
                }
            }

        }
    };


    //    class ImagePathName {
    private File returnPathName() {
        String           TAG        = "ImagePathName";
        File             filepath   = null;
        String           name       = null;
        int              x          = (int) (Math.random() * 100);
        Date             date       = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        name = dateFormat.format(date) + x;
        // Log.i(TAG, name.toString());
        String stat = Environment.getExternalStorageState();
        try {
            if (!Environment.MEDIA_MOUNTED.equals(stat)) {
                //进入这里说明sd卡没有挂载，则就返回提示信息并且return
                //Toast.makeText(myfinalcamera.caicai.com.myfinalcamera.MyCamera,"请检查sd卡状态",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "请检查sd卡状态");
                return null;
            }
            filepath = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
            // Toast.makeText(this,"成功获取路径信息"+filepath.toString(),Toast.LENGTH_SHORT).show();
            Log.i(TAG, "成功获取路径信息" + filepath.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "异常");
        }
        return filepath;
    }

    private void upPic(File picture) {
        //        showProgressDialog("正在上传图片...");
        NetRepository.getInstance().uploadpic(picture)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .subscribe(new DefaultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        String mp3 = o.toString();
                        //                        dismissProgressDialog();
                        Intent intent = new Intent(TakePhoteActivity.this, MusicActivity.class);
                        intent.putExtra("music", mp3);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        //                        dismissProgressDialog();
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //
                        //                        //todo  删掉
                        //                        Intent intent = new Intent(TakePhoteActivity.this, MusicActivity.class);
                        //                        intent.putExtra("music", "http://39.106.32.139/mp3/1.mp3");
                        //                        startActivity(intent);
                        finish();

                        return super.onFailed(e);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.release();
        }
    }

    //图片按比例大小压缩方法（根据路径获取图片并压缩）：
    //    public Bitmap getSmallBitmap(File file, int reqWidth, int reqHeight) {
    //        try {
    //            String                filePath = file.getAbsolutePath();
    //            BitmapFactory.Options options  = new BitmapFactory.Options();
    //            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
    //            BitmapFactory.decodeFile(filePath, options);//此时返回bm为空
    //            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例
    //            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了
    //            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
    //            //压缩好比例大小后不进行质量压缩
    //            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(currentImageFile));
    //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bos中
    //            //压缩好比例大小后再进行质量压缩
    //            //compressImage(bitmap,filePath);
    //            return bitmap;
    //        } catch (Exception e) {
    //            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
    //                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
    //            return null;
    //        }
    //    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            int height       = options.outHeight;
            int width        = options.outWidth;
            int inSampleSize = 1;  //1表示不缩放
            if (height > reqHeight || width > reqWidth) {
                int heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio  = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        } catch (Exception e) {
            Log.d("wzc", "类:" + this.getClass().getName() + " 方法：" + Thread.currentThread()
                    .getStackTrace()[0].getMethodName() + " 异常 " + e);
            return 1;
        }
    }

    // 质量压缩法：
    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
//            while (baos.toByteArray().length / 1024 > 800) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 1;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

//            }
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
