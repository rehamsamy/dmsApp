package com.dmsegypt.dms.rest.model;

import com.dmsegypt.dms.BuildConfig;
import com.dmsegypt.dms.rest.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amr on 04/10/2017.
 */


/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public class UploadClient {

    // dms
    // private static final String BASE_URL = "http://41.33.128.141/dms/Allista.svc/";
    //private static final String BASE_URL = "http://10.0.2.2/Allista.svc/";
    // private static final String BASE_URL_IMAGE = "http://41.33.128.139:8080/";
     //private static final String BASE_URL_IMAGE = "http://217.139.89.21:8080/";
    private static final String BASE_URL_IMAGE = "http://dmsapi-001-site1.atempurl.com/uploader/";


    //public static final String BASE_URL_IMAGE="http://10.0.2.2:8080/";

    // sedhom
//    private static final String BASE_URL = "http://41.38.76.240/dms/Allista.svc/";

    private ApiService apiService;

    //     System.IO.Directory.Move(@"D:\\BackEnd\\imageUploader\\images\\defult", @"D:\\BackEnd\\imageUploader\\images\\"+image_name);

    public UploadClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_IMAGE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(ApiService.class);
    }



    public ApiService getApiService() {
        return apiService;
    }
}



