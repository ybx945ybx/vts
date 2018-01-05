package examp.com.vts.net;

import java.util.ArrayList;
import java.util.Map;

import examp.com.vts.data.ImgBean;
import examp.com.vts.data.UserBean;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by a55 on 2017/11/28.
 */

public interface NetService {

    /**
     * 注册
     */
    @GET("register")
    Observable<ApiModel<UserBean>> register(@QueryMap Map<String, Object> body);

    /**
     * 登录
     */
    @GET("login")
    Observable<ApiModel<UserBean>> login(@QueryMap Map<String, Object> body);

    /**
     * 修改头像以及昵称接口
     */
    @POST("uploadHeadPic")
    Observable<ApiModel<UserBean>> uploadHeadPic(@Body RequestBody Body);

    /**
     * 修改头像以及昵称接口
     */
    @POST("uploadHeadPic")
    Observable<ApiModel<UserBean>> uploadHeadPicNullHead(@Body Map<String, Object> body);

    /**
     * 拍照上传
     */
    @POST("uploadpic")
    Observable<ApiModel<Object>> uploadpic(@Body RequestBody Body);

    /**
     * 关于我们图片列表
     */
    @POST("picList")
    Observable<ApiModel<ArrayList<ImgBean>>> picList(@Body Map<String, Object> body);

    /**
     * 预约
     */
    @GET("saveOrder")
    Observable<ApiModel<Object>> saveOrder(@QueryMap Map<String, Object> body);

    /**
     * 建议
     */
    @GET("saveOpinion")
    Observable<ApiModel<Object>> saveOpinion(@QueryMap Map<String, Object> body);


}
