package com.coding.doublea.carezoneshoppinglist.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 4/4/2015.
 */
public class ShoppingItem implements Parcelable {

    @Expose
    private String category;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private Integer id;
    @Expose
    private String name;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.createdAt);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.updatedAt);
        dest.writeValue(this.userId);
    }

    public ShoppingItem() {
    }

    private ShoppingItem(Parcel in) {
        this.category = in.readString();
        this.createdAt = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.updatedAt = in.readString();
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShoppingItem> CREATOR = new Parcelable.Creator<ShoppingItem>() {
        public ShoppingItem createFromParcel(Parcel source) {
            return new ShoppingItem(source);
        }

        public ShoppingItem[] newArray(int size) {
            return new ShoppingItem[size];
        }
    };
}