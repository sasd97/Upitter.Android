package com.github.sasd97.upitter.models.response.pointers;

import com.github.sasd97.upitter.models.CoordinatesModel;
import com.github.sasd97.upitter.models.SocialIconModel;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Names;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

/**
 * Created by alexander on 28.06.16.
 */

public class CompanyPointerModel {

    @SerializedName("customId")
    @Expose
    private String mCustomId;

    @SerializedName("isVerify")
    @Expose
    private boolean mIsVerify;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("alias")
    @Expose
    private String mAlias;

    @SerializedName("logoUrl")
    @Expose
    private String mLogoUrl;

    @SerializedName("activity")
    @Expose
    private List<Integer> mActivities;

    @SerializedName("site")
    @Expose
    private String mSite;

    @SerializedName("contactPhones")
    @Expose
    private List<String> mContactPhones;

    @SerializedName("coordinates")
    @Expose
    private List<CoordinatesPointerModel> mCoordinates;

    @SerializedName("socialLinks")
    @Expose
    private List<SocialIconPointerModel> mSocialLinks;

    @SerializedName("phone")
    @Expose
    private PhonePointerModel mPhone;

    @SerializedName("accessToken")
    @Expose
    private String mAccessToken;

    @SerializedName("subscribersAmount")
    @Expose
    private String mSubscribersAmount;

    @SerializedName("isMySubscription")
    @Expose
    private boolean mIsMySubscription;

    public String getId() {
        return mCustomId;
    }

    public boolean isVerify() {
        return mIsVerify;
    }

    public String getName() {
        return mName;
    }

    public String getPreview() {
        return Names.getNamePreview(mName);
    }

    public String getDescription() { return mDescription; }

    public String getAlias() {
        return mAlias;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public List<Integer> getActivity() {
        return mActivities;
    }

    public String getSite() {
        return mSite;
    }

    public List<String> getContactPhones() {
        return mContactPhones;
    }

    public List<SocialIconPointerModel> getSocialLinks() {
        return mSocialLinks;
    }

    public List<SocialIconModel> getSocialLinksBoxed() {
        return ListUtils.mutate(mSocialLinks, new ListUtils.OnListMutateListener<SocialIconPointerModel, SocialIconModel>() {
            @Override
            public SocialIconModel mutate(SocialIconPointerModel object) {
                return new SocialIconModel
                        .Builder()
                        .id(object.getId())
                        .title(object.getTitle())
                        .link(object.getLink())
                        .icon(object.getIcon())
                        .build();
            }
        });
    }

    public List<CoordinatesModel> getCoordinates() {
        return ListUtils.mutate(mCoordinates, new ListUtils.OnListMutateListener<CoordinatesPointerModel, CoordinatesModel>() {
            @Override
            public CoordinatesModel mutate(CoordinatesPointerModel object) {
                return new CoordinatesModel.Builder()
                        .latitude(object.getLatitude())
                        .longitude(object.getLongitude())
                        .addressName(object.getAddress())
                        .build();
            }
        });
    }

    public PhonePointerModel getPhone() {
        return mPhone;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getSubscribersAmount() {
        return mSubscribersAmount;
    }

    public boolean isMySubscription() {
        return mIsMySubscription;
    }

    @Override
    public String toString() {
        StringBuilder activities = new StringBuilder();
        for (Integer activity: mActivities)
            activities.append(activity).append("\n");

        return String.format(Locale.getDefault(), "Company\nCustom id: %1$s\nName: %2$s\nAlias %7$s\nIs verify: %3$b\nActivities: [%4$s]\nSite: %5$s\nAvatar url: %6$s\nSocial icons: %8$s",
                mCustomId,
                mName,
                mIsVerify,
                activities.toString(),
                mSite,
                mLogoUrl,
                mAlias,
                ListUtils.toString(mSocialLinks));
    }
}
