package model.Api;

import bean.apiBean.base_E;
import bean.apiBean.likebean_E;
import bean.apiBean.login_E;
import bean.apiBean.postPinglun_E;
import bean.apiBean.whetherRejist_E;
import bean.daoBean.pinglunBean;
import bean.likebean;
import bean.userbean;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by peiyan on 2016/1/28.
 */
public interface APIService {
    //判断用户手机是否已注册
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @GET("userbean")
    Observable<userbean> whetherRejist(@Query("where") String sort);

    //注册
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @POST("userbean")
    Observable<base_E> Rejist(@Body whetherRejist_E whetherRejist_e);

    //登录
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @PUT("userbean/{id}")
    Observable<base_E> Login(@Path("id") String id, @Body login_E login_E);

    //获取收藏列表
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @GET("filmlike")
    Observable<likebean> getAllLike(@Query("where") String sort);

    //删除一条收藏
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @DELETE("filmlike/{id}")
    Observable<base_E> deleteLike(@Path("id") String id);

    //插入一条收藏
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @POST("filmlike")
    Observable<base_E> insertLike(@Body likebean_E likebean_e);

    //获取评论列表
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @GET("Pinglun")
    Observable<pinglunBean> getPinglun(@Query("where") String sort);

    //发表评论
    @Headers({"X-Bmob-Application-Id:e4352240f63c88f8a3a2e552a6ecfb73","X-Bmob-REST-API-Key:5239a5c0f814f3954bb5d43f9bb3a57e","Content-Type:application/json"})
    @POST("Pinglun")
    Observable<base_E> postPinglun(@Body postPinglun_E postPinglun_e);
}
