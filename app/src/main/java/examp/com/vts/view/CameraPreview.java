package examp.com.vts.view;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by a55 on 2017/11/29.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {
    private Camera        mCamera;
    private SurfaceHolder mHolder;
    private static final String TAG     = "";
    private static final String TAGDRAW = "";

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90); /* 设置一下preview的大小 */
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            //            parameters.setPreviewSize(640, 480);
            //            parameters.setPictureSize(800, 600);
            //            parameters.setJpegQuality(100);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            //            Camera.Parameters parameters = mCamera.getParameters();
            //            parameters.setPreviewSize(320, 240);
            //            parameters.setPictureSize(800, 600);
            //            parameters.setJpegQuality(100);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            //            Log.d(TAG, & quot; Errorstarting camera preview: &quot;
            //            +e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();//停止预览 mCamera.release();//释放相机资源 mCamera = null; holder = null; } @Override public void onAutoFocus(boolean success, Camera Camera) { if (success) { Log.i(TAG, &quot;onAutoFocus success=&quot;+success); } } }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
    }
}