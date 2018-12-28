package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 28/12/2018.
 */

public class ShareModel {
    /**
     * content : 您下单，我们帮你泊车，再不需要为停车而烦恼！
     * pictureLinking : https://parkingman-common.oss-cn-shenzhen.aliyuncs.com/share/logo.png
     * title : 轻松为您代入咪表
     * pictureUrl : https://www.bosoftmacao.cn/parkingman-web/share/invite?inviteCode=yoan4l
     * platform : ["2","3"]
     */

    private String content;
    private String pictureLinking;
    private String title;
    private String pictureUrl;
    private List<String> platform;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureLinking() {
        return pictureLinking;
    }

    public void setPictureLinking(String pictureLinking) {
        this.pictureLinking = pictureLinking;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<String> getPlatform() {
        return platform;
    }

    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }
}
