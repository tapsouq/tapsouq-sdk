package com.tapsouq.sdk.ads;

/**
 * Created by dell on 8/6/2016.
 */
public class AdCreative {

    public static final int AD_TYPE_TEXT = 1;
    public static final int AD_TYPE_IMAGE = 2;

    public static final int AD_FORMAT_BANNER = 2;
    public static final int AD_FORMAT_INTERSTITIAL = 1;


    private String id; //primary key
    private String clickUrl;
    private String imageFile;
    private String title;
    private String description;
    private int type;
    private int appId;
    private int appUserId;
    private int campUserId;
    private int campId;
    private String imagesPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setCampUserId(int campUserId) {
        this.campUserId = campUserId;
    }

    public int getCampUserId() {
        return campUserId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    public int getCampId() {
        return campId;
    }

    public String getImageFileUrl(){
        return imagesPath + imageFile;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }
}
