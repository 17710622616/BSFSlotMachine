package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 用戶信息
 * Created by John_Li on 29/9/2017.
 */

public class UserInfoOutsideModel {

    /**
     * code : 200
     * msg :
     * data : {"mobile":"13557163151","nickname":"小李飞刀","headimg":"asdfasf.jpg","birthday":null,"realname":"李寻欢","descx":"耍飞刀","sex":1,"idcardno":null,"address":null}
     */

    private int code;
    private String msg;
    private UserInfoModel data;

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

    public UserInfoModel getData() {
        return data;
    }

    public void setData(UserInfoModel data) {
        this.data = data;
    }

    public static class UserInfoModel {
        /**
         * mobile : 13557163151
         * nickname : 小李飞刀
         * headimg : asdfasf.jpg
         * birthday : null
         * realname : 李寻欢
         * descx : 耍飞刀
         * sex : 1
         * idcardno : null
         * address : null
         */

        private String mobile;
        private String nickname;
        private String headimg;
        private String birthday;
        private String realname;
        private String descx;
        private int sex;
        private String idcardno;
        private String address;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getDescx() {
            return descx;
        }

        public void setDescx(String descx) {
            this.descx = descx;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getIdcardno() {
            return idcardno;
        }

        public void setIdcardno(String idcardno) {
            this.idcardno = idcardno;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
