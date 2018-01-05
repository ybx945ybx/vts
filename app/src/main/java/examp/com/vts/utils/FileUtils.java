package examp.com.vts.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import examp.com.vts.data.Constant;

/**
 * Created by a55 on 2017/12/2.
 */

public class FileUtils {

    /**
     * 只删除相关的文件，不做其它操作
     *
     * @param path
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath()); // 递规的方式删除文件夹
        }
    }

    public static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }

    public static        String CATCH_PATH = Environment.getExternalStorageDirectory().getPath() + Constant.PIC_PATH;
    // 图片路径
    public static        String PIC_PATH   = "";
    private static final String TAG        = "FileUtil";

    public static String initPath() {
        File f = new File(CATCH_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        return CATCH_PATH;
    }

    public static void initPicPath(Context context) {
        File f = new File(context.getExternalCacheDir() + Constant.PIC_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        PIC_PATH = f.getAbsolutePath();
    }

    public static String getPicPath(Context context) {
        if (TextUtils.isEmpty(PIC_PATH)) {
            initPicPath(context);
        }
        return PIC_PATH;
    }

    public static File initDir() {
        File f = new File(CATCH_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    public static String saveBitmap(Bitmap b) {

        String path     = initPath();
        long   dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
//        FLog.e(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream     fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            //            FLog.d(TAG, "saveBitmapSuccess");
            return jpegName;
        } catch (IOException e) {
            //            FLog.e(TAG, "saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将bitmap保存到相册里
     *
     * @param bitmap
     */
    public static void saveBitmapToPicture(Context mContext, Bitmap bitmap) {
        File             parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Date             date       = new Date();
        SimpleDateFormat format     = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String           fileName   = format.format(date) + ".jpg";

        File file = new File(parentpath, fileName);
        file.getParentFile().mkdirs();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
    }


    public static void downloadPicAndSaveToDCIM(Context mContext, String path, String picName) {
        String parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/55海淘/";

        File pic = new File(parentpath, picName);
        if (!pic.getParentFile().exists())
            pic.getParentFile().mkdirs();

        if (pic.exists())
            pic.delete();
        // 从网络上获取图片
        URL url;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream      is     = conn.getInputStream();
                FileOutputStream fos    = new FileOutputStream(pic);
                byte[]           buffer = new byte[1024];
                int              len    = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
            }
            MediaScannerConnection.scanFile(mContext, new String[]{pic.toString()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (!"".equals(path)) {
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
    }

    public static String getFileName(String imageUrl) {
        return String.valueOf(imageUrl.hashCode()) + ".png";
    }

    public static String saveBitmap(Bitmap b, String fileName) {
        String path     = initPath();
        String filePath = path + fileName;
        Logger.d("saveBitmap:jpegName = " + filePath);
        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    public static String saveBitmap(Context context, Bitmap b, String fileName) {
        String path     = getPicPath(context);
        String filePath = path + fileName;
        Logger.d("saveBitmap:jpegName = " + filePath);

        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }
}
