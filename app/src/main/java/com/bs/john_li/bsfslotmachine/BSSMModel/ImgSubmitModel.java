package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * Created by John_Li on 15/3/2018.
 */

public class ImgSubmitModel {
    // oss名称
    private String objectName;
    // 提交状态
    private boolean status;
    // 照片类型
    private String type;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
