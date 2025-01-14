package com.github.sasd97.upitter.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sasd97.upitter.models.skeletons.RequestSkeleton;
import com.github.sasd97.upitter.utils.ListUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexadner Dadukin on 30.06.2016.
 */

public class CompanyModel extends UserModel
        implements RequestSkeleton {

    @Unique
    private String mId;

    private PhoneModel mPhone;

    private boolean mIsVerify = false;

    private String mAccessToken;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("alias")
    @Expose
    private String mAlias;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("activity")
    @Expose
    @Ignore
    private List<Integer> mCategories;
    private String mCategoriesRepresentation;

    @SerializedName("contactPhones")
    @Expose
    @Ignore
    private List<String> mContactPhones;
    private String mContactPhonesRepresentation;

    @SerializedName("site")
    @Expose
    private String mSite;

    @SerializedName("coordinates")
    @Expose
    @Ignore
    private List<CoordinatesModel> mCoordinates;
    private String mCoordinatesRepresentation;

    @SerializedName("socialLinks")
    @Expose
    @Ignore
    private List<SocialIconModel> mSocialIcons;
    private String mSocialIconsRepresentation;

    @SerializedName("logoUrl")
    @Expose
    private String mAvatarUrl;

    @SerializedName("temporaryToken")
    @Expose
    private String mTemporaryToken;

    public CompanyModel() {}

    private CompanyModel(Builder builder) {
        mId = builder.id;
        mIsVerify = builder.isVerify;
        mName = builder.name;
        mAlias = builder.alias;
        mDescription = builder.description;
        mCategories = builder.categories;
        mPhone = builder.phone;
        mContactPhones = builder.contactPhones;
        mSite = builder.site;
        mCoordinates = builder.coordinates;
        mAccessToken = builder.accessToken;
        mSocialIcons = builder.socialIcons;
        mTemporaryToken = builder.temporaryToken;
        mAvatarUrl = builder.avatarUrl;

        Gson gson = new Gson();
        mCategoriesRepresentation = gson.toJson(mCategories);
        mContactPhonesRepresentation = gson.toJson(mContactPhones);
        mCoordinatesRepresentation = gson.toJson(mCoordinates);
        mSocialIconsRepresentation = gson.toJson(mSocialIcons);
    }

    @Override
    public String getUId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public String getAlias() {
        return mAlias == null ? mId : mAlias;
    }

    public void setAlias(@NonNull String alias) {
        mAlias = alias;
    }

    @Override
    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(@NonNull String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@NonNull String description) {
        mDescription = description;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(@NonNull String site) {
        mSite = site;
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    public String getTemporaryToken() {
        return mTemporaryToken;
    }

    public PhoneModel getPhone() {
        return mPhone;
    }

    @Override
    public boolean isVerify() {
        return mIsVerify;
    }

    @Override
    public UserType getType() {
        return UserType.Company;
    }

    public List<Integer> getCategories() {
        if (mCategories != null) return mCategories;
        if (mCategoriesRepresentation == null) return new ArrayList<>();
        Type type = new TypeToken<List<Integer>>(){}.getType();
        mCategories = ListUtils.fromJson(type, mCategoriesRepresentation);
        return mCategories;
    }

    public void setCategories(List<Integer> list) {
        mCategories = list;
        Gson gson = new Gson();
        mCategoriesRepresentation = gson.toJson(mCategories);
    }

    public List<String> getContactPhones() {
        if (mContactPhones != null) return mContactPhones;
        if (mContactPhonesRepresentation == null) return new ArrayList<>();
        Type type = new TypeToken<List<String>>(){}.getType();
        mContactPhones = ListUtils.fromJson(type, mContactPhonesRepresentation);
        return mContactPhones;
    }

    public void setContactPhones(@NonNull List<String> contactPhones) {
        mContactPhones = contactPhones;
    }

    public List<CoordinatesModel> getCoordinates() {
        if (mCoordinates != null) return mCoordinates;
        if (mCoordinatesRepresentation == null) return new ArrayList<>();
        Logger.i(mCoordinatesRepresentation);
        Type type = new TypeToken<List<CoordinatesModel>>(){}.getType();
        mCoordinates = ListUtils.fromJson(type, mCoordinatesRepresentation);
        return mCoordinates;
    }

    public void setCoordinates(List<CoordinatesModel> coordinates) {
        mCoordinates = coordinates;
        mCoordinatesRepresentation = new Gson().toJson(mCoordinates);
    }

    public List<SocialIconModel> getSocialIcons() {
        if (mSocialIcons != null) return mSocialIcons;
        if (mSocialIconsRepresentation == null) return new ArrayList<>();
        Logger.i(mSocialIconsRepresentation);
        Type type = new TypeToken<List<SocialIconModel>>(){}.getType();
        mSocialIcons = ListUtils.fromJson(type, mSocialIconsRepresentation);
        return mSocialIcons;
    }

    public void setSocialLinks(List<SocialIconModel> list) {
        mSocialIcons = list;
        Gson gson = new Gson();
        mSocialIconsRepresentation = gson.toJson(mSocialIcons);
    }

    @Override
    public long save() {
        Gson gson = new Gson();
        mCategoriesRepresentation = gson.toJson(mCategories);
        mContactPhonesRepresentation = gson.toJson(mContactPhones);
        mCoordinatesRepresentation = gson.toJson(mCoordinates);
        mSocialIconsRepresentation = gson.toJson(mSocialIcons);
        if (mPhone != null) mPhone.save();
        return super.save();
    }

    @Override
    public String toJson() {
        getContactPhones();
        getCategories();
        getCoordinates();
        Gson builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return builder.toJson(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%15$s #%1$s\nType: Company\nIs verify: %2$b\nDescription: %3$s\nCategories: {%4$s}\n" +
                        "Phone: %5$s\nContact phones: {%6$s}\nSite: %7$s\nCoordinates: {%8$s}\n" +
                        "Access token: %9$s\nTemporary token: %10$s\nAvatar url: %11$s\n" +
                        "---Representations of base dataTypes---\n" +
                        "Categories: [%12$s]\nContact Phones: [%13$s]\nCoordinates: [%14$s]",
                mId == null ? "Null" : mId,
                mIsVerify,
                mDescription == null ? "Null" : mDescription,
                mCategories == null ? "Null" : ListUtils.toString(mCategories),
                mPhone == null ? "Null" : mPhone,
                mContactPhones == null ? "Null" : ListUtils.toString(mContactPhones),
                mSite == null ? "Null" : mSite,
                mCoordinates == null ? "Null" : ListUtils.toString(mCoordinates),
                mAccessToken == null ? "Null" : mAccessToken,
                mTemporaryToken == null ? "Null" : mTemporaryToken,
                mAvatarUrl == null ? "Null" : mAvatarUrl,
                mCategoriesRepresentation == null ? "Null" : mCategoriesRepresentation,
                mContactPhonesRepresentation == null ? "Null" : mContactPhonesRepresentation,
                mCoordinatesRepresentation == null ? "Null" : mCoordinatesRepresentation,
                mName == null ? "Null" : mName);
    }

    public static class Builder {

        private String id;
        private String alias;
        private boolean isVerify;
        private String name;
        private String description;
        private String avatarUrl;
        private List<Integer> categories;
        private PhoneModel phone;
        private List<String> contactPhones;
        private String site;
        private List<CoordinatesModel> coordinates;
        private List<SocialIconModel> socialIcons;
        private String accessToken;
        private String temporaryToken;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder isVerify(boolean isVerify) {
            this.isVerify = isVerify;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder categories(List<Integer> categories) {
            this.categories = categories;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder phone(PhoneModel phone) {
            this.phone = phone;
            return this;
        }

        public Builder contactPhones(List<String> contactPhones) {
            this.contactPhones = contactPhones;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder coordinates(List<CoordinatesModel> coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder socialIcons(List<SocialIconModel> socialIcons) {
            this.socialIcons = socialIcons;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder temporaryToken(String temporaryToken) {
            this.temporaryToken = temporaryToken;
            return this;
        }

        public CompanyModel build() {
            return new CompanyModel(this);
        }
    }
}
