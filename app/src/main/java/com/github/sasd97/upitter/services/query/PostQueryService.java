package com.github.sasd97.upitter.services.query;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.events.OnErrorQueryListener;
import com.github.sasd97.upitter.models.response.posts.PostsResponseModel;
import com.github.sasd97.upitter.services.RestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexander on 08.07.16.
 */
public class PostQueryService {

    public interface OnPostListener extends OnErrorQueryListener {
        void onPostObtained(List<PostsResponseModel> posts);
    }

    private OnPostListener listener;

    private PostQueryService(OnPostListener listener) {
        this.listener = listener;
    }

    public static PostQueryService getService(OnPostListener listener) {
        return new PostQueryService(listener);
    }

    public void obtainPosts(@NonNull String accessToken,
                            @NonNull String language,
                            @NonNull Integer limit,
                            @NonNull Integer offset) {
        Call<PostsResponseModel> obtainPosts =
                RestService
                .baseFactory()
                .obtainPosts(accessToken, language, limit, offset);

        obtainPosts.enqueue(new Callback<PostsResponseModel>() {
            @Override
            public void onResponse(Call<PostsResponseModel> call, Response<PostsResponseModel> response) {
                if (!RestService.handleError(call, response, listener)) return;
                listener.onPostObtained(response.body().getResponseModel());
            }

            @Override
            public void onFailure(Call<PostsResponseModel> call, Throwable t) {
                t.printStackTrace();
                listener.onError(RestService.getEmptyError());
            }
        });
    }
}
