package com.cocacola.john_li.bsfmerchantsversionapp.Model;

/**
 * Created by John on 18/11/2018.
 */

public class UserLoginOutModel {
    /**
     * code : 200
     * msg : success
     * data : {"username":"admin","sellertoken":"8FFB68ACE23FFD788CE84179D433C9CF9DC75ED4C7FD9354048505878B08EC32EE443CFECFBBF082A788CBE6323AE2D4282C84A1219F6738F0B41DF48F4E075863778269A9AA34AE495855FDE760E62D"}
     */

    private int code;
    private String msg;
    private UserLoginModel data;

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

    public UserLoginModel getData() {
        return data;
    }

    public void setData(UserLoginModel data) {
        this.data = data;
    }

    public static class UserLoginModel {
        /**
         * username : admin
         * sellertoken : 8FFB68ACE23FFD788CE84179D433C9CF9DC75ED4C7FD9354048505878B08EC32EE443CFECFBBF082A788CBE6323AE2D4282C84A1219F6738F0B41DF48F4E075863778269A9AA34AE495855FDE760E62D
         */

        private String username;
        private String sellertoken;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSellertoken() {
            return sellertoken;
        }

        public void setSellertoken(String sellertoken) {
            this.sellertoken = sellertoken;
        }
    }
}
