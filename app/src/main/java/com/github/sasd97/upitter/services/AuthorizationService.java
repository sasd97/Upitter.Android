package com.github.sasd97.upitter.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.CompanyPointerModel;
import com.github.sasd97.upitter.services.query.UserAuthorizationQueryService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orhanobut.logger.Logger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

import static com.github.sasd97.upitter.constants.RequestCodesConstants.FACEBOOK_SIGN_IN_REQUEST;

/**
 * Created by Alex on 10.06.2016.
 */

public final class AuthorizationService {

    private static GoogleSignInOptions googleSignInOptions;
    private static TwitterAuthClient twitterClient;
    private static CallbackManager facebookClient;

    private AuthorizationService() {}

    public static void init(Context context) {
        initFacebook(context);
        initTwitter(context);
        initGoogle(context);
        initVK(context);
    }

    private static void initFacebook(Context context) {
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        FacebookSdk.sdkInitialize(context, FACEBOOK_SIGN_IN_REQUEST);
        facebookClient = CallbackManager.Factory.create();
    }

    private static void initTwitter(Context context) {
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(context.getString(R.string.twitter_consumer_key),
                context.getString(R.string.twitter_consumer_secret));

        Fabric.with(context, new TwitterCore(authConfig));
        twitterClient = new TwitterAuthClient();
    }

    private static void initGoogle(Context context) {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_server_client_id))
                .requestEmail()
                .build();
    }

    private static void initVK(Context context) {
        VKSdk.initialize(context);
    }

    public static GoogleApiClient google(@NonNull Context context,
                                         @NonNull FragmentActivity activity,
                                         @NonNull GoogleApiClient.OnConnectionFailedListener listener) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public static TwitterAuthClient twitter() {
        return twitterClient;
    }

    public static CallbackManager facebook() {
        return facebookClient;
    }

    public static void obtainGoogle(UserAuthorizationQueryService service, GoogleSignInResult result) {
        GoogleSignInAccount googleAccount = result.getSignInAccount();
        Logger.e(googleAccount.getIdToken());
        service.notifyByGoogle(googleAccount.getIdToken());
    }
}
