package com.cocacola.john_li.bsfmerchantsversionapp.Model;

import java.util.Objects;

/**
 * Created by John on 18/11/2018.
 */

public class CommonModel {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Objects getData() {
        return data;
    }

    public void setData(Objects data) {
        this.data = data;
    }

    private Objects data;
}
