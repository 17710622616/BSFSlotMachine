package com.bs.john_li.bsfslotmachine.BSSMModel;

/**
 * 評論成功返回的內容
 * Created by John_Li on 24/10/2017.
 */

public class ReturnCommentsOutModel {
    /**
     * code : 200
     * msg :
     * data : {"id":12,"contentid":15,"content":"你这个车位太贵了，我租不起，可以送给我不。","creatorid":15,"creator":"小李飞刀","createtime":1508070880000}
     */

    private int code;
    private String msg;
    private ReturnCommentModel data;

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

    public ReturnCommentModel getData() {
        return data;
    }

    public void setData(ReturnCommentModel data) {
        this.data = data;
    }

    public static class ReturnCommentModel {
        /**
         * id : 12
         * contentid : 15
         * content : 你这个车位太贵了，我租不起，可以送给我不。
         * creatorid : 15
         * creator : 小李飞刀
         * createtime : 1508070880000
         */

        private int id;
        private int contentid;
        private String content;
        private int creatorid;
        private String creator;
        private long createtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getContentid() {
            return contentid;
        }

        public void setContentid(int contentid) {
            this.contentid = contentid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCreatorid() {
            return creatorid;
        }

        public void setCreatorid(int creatorid) {
            this.creatorid = creatorid;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }
    }
}
