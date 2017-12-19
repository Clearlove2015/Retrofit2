package com.zc.retrofit2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_upload)
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_upload)
    public void onViewClicked() {
        uploadImg();
    }

    public class HeadInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = new Request.Builder()
                    .addHeader("key","value")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 上传图片
     */
    private void uploadImg() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeadInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriMethod.ROOT_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);

//        File file = new File(Environment.getExternalStorageDirectory(), "wangshu.png");
//        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
//        MultipartBody.Part photo = MultipartBody.Part.createFormData("photos", "wangshu.png", photoRequestBody);

        Map<String,RequestBody> imgFils = new HashMap<>();
        ArrayList<String> paths = new ArrayList<>();
        paths.add("1.jpg");
        paths.add("2.jpg");
        for(String str : paths){
            File file = new File(str);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imgFils.put("image",requestFile);
        }

        apiClient.uploadImages(imgFils)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                    }
                });
    }
}
