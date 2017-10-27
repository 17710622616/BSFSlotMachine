package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 發佈帖子返回的帖子類
 * Created by John_Li on 28/10/2017.
 */

public class ReturnContentsOutModel {

    /**
     * code : 200
     * msg :
     * data : {"id":15,"title":"高端小区车位出租","cover":"testabc.jpg","type":1,"contents":"澳门特别行政区最繁华的大都市，有一个很不错的车位需要出租，你想要吗？拿起你手中的电话，拨打13300000000,10万一个月。","creator":"小李飞刀","creatorid":15,"valid":1,"likecount":0,"readcount":0,"commentcount":0,"createtime":1508069709000,"updatetime":null}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 15
         * title : 高端小区车位出租
         * cover : testabc.jpg
         * type : 1
         * contents : 澳门特别行政区最繁华的大都市，有一个很不错的车位需要出租，你想要吗？拿起你手中的电话，拨打13300000000,10万一个月。
         * creator : 小李飞刀
         * creatorid : 15
         * valid : 1
         * likecount : 0
         * readcount : 0
         * commentcount : 0
         * createtime : 1508069709000
         * updatetime : null
         */

        private int id;
        private String title;
        private String cover;
        private int type;
        private String contents;
        private String creator;
        private int creatorid;
        private int valid;
        private int likecount;
        private int readcount;
        private int commentcount;
        private long createtime;
        private Object updatetime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public int getCreatorid() {
            return creatorid;
        }

        public void setCreatorid(int creatorid) {
            this.creatorid = creatorid;
        }

        public int getValid() {
            return valid;
        }

        public void setValid(int valid) {
            this.valid = valid;
        }

        public int getLikecount() {
            return likecount;
        }

        public void setLikecount(int likecount) {
            this.likecount = likecount;
        }

        public int getReadcount() {
            return readcount;
        }

        public void setReadcount(int readcount) {
            this.readcount = readcount;
        }

        public int getCommentcount() {
            return commentcount;
        }

        public void setCommentcount(int commentcount) {
            this.commentcount = commentcount;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public Object getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Object updatetime) {
            this.updatetime = updatetime;
        }
    }
}
