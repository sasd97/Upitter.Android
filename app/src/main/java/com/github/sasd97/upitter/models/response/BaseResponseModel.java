package com.github.sasd97.upitter.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Alex on 10.06.2016.
 */
public abstract class BaseResponseModel<T> {

    @SerializedName("success")
    @Expose
    private boolean mIsSuccess = false;

    @SerializedName("error")
    @Expose
    private ErrorResponseModel mError;

    @SerializedName("response")
    @Expose
    protected T mResponse;

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public boolean isError() {
        return mError != null;
    }

    public ErrorResponseModel getError() {
        return mError;
    }

    public T getResponseModel() {
        return mResponse;
    }

    public boolean isField(Object object) {
        return object != null;
    }

    public String getResponseInfo() {
        return String.format(Locale.getDefault(), "Response status: %1$b\nwith error: %2$s\nwith response %3$s",
                mIsSuccess,
                mError == null ? "null" : mError.toString(),
                mResponse == null ? "null" : mResponse.toString());
    }
}
