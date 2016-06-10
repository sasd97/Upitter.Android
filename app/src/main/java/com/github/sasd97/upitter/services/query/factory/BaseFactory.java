package com.github.sasd97.upitter.services.query.factory;

import com.github.sasd97.upitter.models.response.authorization.AuthorizationResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Alexander Dadukin on 06.06.2016.
 */
public interface BaseFactory {

    @FormUrlEncoded
    @POST("authorization/google/verify")
    Call<AuthorizationResponseModel> authorizeWithGooglePlus(@Field("tokenId") String googleTokenId);
}
