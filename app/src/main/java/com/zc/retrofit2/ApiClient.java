package com.zc.retrofit2;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by zc on 2017/12/19.
 */

public interface ApiClient {

    @Multipart
    @POST(UriMethod.UPLOAD_IMAGE)
    Observable<ResponseBody> uploadImage(@Part MultipartBody.Part photo, @Part("description") RequestBody description);

}
