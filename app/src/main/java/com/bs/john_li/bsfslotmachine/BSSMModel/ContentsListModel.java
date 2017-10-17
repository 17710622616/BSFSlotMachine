package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 17/10/2017.
 */

public class ContentsListModel {

    /**
     * code : 200
     * msg :
     * data : {"contents":[{"id":9,"title":"海生chaomian","cover":"beautifulgirl.jpg","type":1,"contents":"撒；垃圾发电；爱玩儿呢 的减肥了；as撒；垃圾发电；爱的减肥了；按时；afjdls","creator":"小李飞刀","creatorid":15,"valid":1,"likecount":0,"readcount":0,"commentcount":0,"createtime":1507046888000,"updatetime":""},{"id":3,"title":"海生炒面","cover":"beautifulgirl.jpg","type":1,"contents":"海生炒面，海生呢，玩儿呢啊，被你造啦，请给我一个完美的解释。艹，八格牙路。","creator":"小李飞刀","creatorid":15,"valid":1,"likecount":0,"readcount":0,"commentcount":0,"createtime":1507044115000,"updatetime":""}]}
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
        private List<ContentsModel> contents;

        public List<ContentsModel> getContents() {
            return contents;
        }

        public void setContents(List<ContentsModel> contents) {
            this.contents = contents;
        }

        public static class ContentsModel {
            /**
             * id : 9
             * title : 海生chaomian
             * cover : beautifulgirl.jpg
             * type : 1
             * contents : 撒；垃圾发电；爱玩儿呢 的减肥了；as撒；垃圾发电；爱的减肥了；按时；afjdls
             * creator : 小李飞刀
             * creatorid : 15
             * valid : 1
             * likecount : 0
             * readcount : 0
             * commentcount : 0
             * createtime : 1507046888000
             * updatetime :
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
            private String updatetime;

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

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }
}
