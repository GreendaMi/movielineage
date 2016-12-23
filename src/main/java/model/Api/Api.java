package model.Api;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by peiyan on 2016/1/28.
 */
public class Api {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.bmob.cn/1/classes/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    Log.i("zzz", "request====111111111111111111111111111111");
//                    Log.d("Api", request.url().encodedQuery());
//                    Log.i("zzz", "request====" + request.headers().toString());
//                    Response proceed = chain.proceed(request);
//                    Log.i("zzz", "proceed====" + new String(proceed.body().bytes()));
//                    return proceed;
//                }
//            } ).build())
            .build();
    public static APIService apiService = retrofit.create(APIService.class);


}
