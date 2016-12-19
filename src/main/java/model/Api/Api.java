package model.Api;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by peiyan on 2016/1/28.
 */
public class Api {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://203:8081/keyword/services/userinfoService/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    Log.i("zzz", "request====111111111111111111111111111111");
//                    Log.i("zzz", "request====" + request.headers().toString());
//                    okhttp3.Response proceed = chain.proceed(request);
//                    Log.i("zzz", "proceed====" + new String(proceed.body().bytes()));
//                    return proceed;
//                }
//            } ).build())
            .build();
    public static APIService apiService = retrofit.create(APIService.class);


}
