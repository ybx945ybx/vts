package examp.com.vts.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import examp.com.vts.data.ImgBean;
import examp.com.vts.data.UserBean;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by a55 on 2017/11/28.
 */

public class NetRepository extends BaseRepository {

    private static volatile NetRepository instance;
    private final           NetService    mHomeService;

    private NetRepository() {
        mHomeService = RetrofitFactory.createService(NetService.class);
    }

    public static NetRepository getInstance() {
        if (instance == null) {
            synchronized (NetRepository.class) {
                if (instance == null) {
                    instance = new NetRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 注册
     */
    public Observable<UserBean> register(String username, String password, String nickname) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("nickname", nickname);
        return transform(mHomeService.register(map));
    }

    /**
     * 登录
     */
    public Observable<UserBean> login(String username, String password) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("username", username);
        map.put("password", password);
        return transform(mHomeService.login(map));
    }

    /**
     * 修改头像以及昵称接口
     * <p>
     * 1.picture 头像图片 file类型
     * 2.nickname 昵称
     * 3.userId 用户的主键id int 类型
     */
    public Observable<UserBean> uploadHeadPic(File file, String nickname, int userId) {
        //        TreeMap<String, Object> map = new TreeMap<>();
        //        map.put("picture", picture);
        //        map.put("nickname", nickname);
        //        map.put("userId", 1);
        //构建body
        //        RequestBody requestBody;
        if (file != null) {
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("nickname", nickname)
                    .addFormDataPart("userId", String.valueOf(userId))
                    .addFormDataPart("picture", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                    .build();
            return transform(mHomeService.uploadHeadPic(requestBody));
        } else {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("nickname", nickname);
            map.put("userId", 1);
            return transform(mHomeService.uploadHeadPicNullHead(map));
        }

        //        return transform(mHomeService.uploadHeadPic(requestBody));
    }

    /**
     * 上传照片
     */
    public Observable<Object> uploadpic(File file) {
        //构建body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("picture", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        return transform(mHomeService.uploadpic(requestBody));
    }

    /**
     * 5.关于我们打开的图片列表 接口
     */
    public Observable<ArrayList<ImgBean>> picList() {
        TreeMap<String, Object> map = new TreeMap<>();
        return transform(mHomeService.picList(map));
    }

    /**
     * 预约
     * 参数为
     * 1.name 名字
     * 2.phone 手机号
     */
    public Observable<Object> saveOrder(String name, String phone) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("name", name);
        map.put("phone", phone);
        return transform(mHomeService.saveOrder(map));
    }

    /**
     * 9.建议
     * http://39.106.32.139:8080/saveOpinion
     */
    public Observable<Object> saveOpinion(String opinion) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("opinion", opinion);
        return transform(mHomeService.saveOpinion(map));
    }
}
