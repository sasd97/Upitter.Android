package com.github.sasd97.upitter.models.response.fileServer;

import com.github.sasd97.upitter.models.response.BaseResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexadner Dadukin on 22.07.2016.
 */
public class MediaResponseModel extends BaseResponseModel<MediaResponseModel> {

    @SerializedName("url")
    @Expose
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }
}