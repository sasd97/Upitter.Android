package com.github.sasd97.upitter.services;

import com.github.sasd97.upitter.services.query.factory.BaseFactory;
import com.github.sasd97.upitter.services.query.factory.FileServerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.sasd97.upitter.constants.MethodConstants.BASE_SUB_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.BASE_SUB_ASTRAL_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.FILE_SERVER_API_URL;
import static com.github.sasd97.upitter.constants.MethodConstants.FILE_SUB_ASTRAL_SERVER_API_URL;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */

public final class RestService {

    private static final String IMAGE_MEDIATYPE = "image/*";
    private static final String FILE_MULTIPART_SCHEMA = "files\"; filename=\"%1$s";

    private static BaseFactory baseFactory;
    private static FileServerFactory fileServerFactory;

    private RestService() {}

    public static void init() {
        Retrofit baseAPI = new Retrofit.Builder()
                .baseUrl(BASE_SUB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit fileServerAPI = new Retrofit.Builder()
                .baseUrl(FILE_SERVER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        baseFactory = baseAPI.create(BaseFactory.class);
        fileServerFactory = fileServerAPI.create(FileServerFactory.class);
    }

    public static BaseFactory baseFactory() {
        return baseFactory;
    }

    public static FileServerFactory fileServerFactory() {
        return fileServerFactory;
    }

    public static HashMap<String, RequestBody> obtainImageMultipart(File image) {
        HashMap<String, RequestBody> map = new HashMap<>();
        RequestBody photoBody = RequestBody.create(MediaType.parse(IMAGE_MEDIATYPE), image);
        map.put(String.format(Locale.ENGLISH, FILE_MULTIPART_SCHEMA, image.getName()), photoBody);
        return map;
    }
}
