package com.bs.john_li.bsfslotmachine.BSSMModel;

import java.util.List;

/**
 * Created by John_Li on 18/10/2017.
 */

public class CommentListModel {

    /**
     * code : 200
     * msg :
     * data : {"comments":[{"id":12,"contentid":15,"content":"你这个车位太贵了，我租不起，可以送给我不。","creatorid":15,"creator":"小李飞刀","createtime":1508070880000,"replies":[{"id":9,"commentid":12,"content":"哎呀，本山大叔，你的买不起就不买呗，不差钱儿，疼，不疼能这么抽抽吗。","creatorid":15,"creator":"小李飞刀","createtime":1508071251000}]}]}
     */

    private int code;
    private String msg;
    private CommentsArrayModel data;

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

    public CommentsArrayModel getData() {
        return data;
    }

    public void setData(CommentsArrayModel data) {
        this.data = data;
    }

    public static class CommentsArrayModel {
        private List<CommentsModel> comments;

        public List<CommentsModel> getComments() {
            return comments;
        }

        public void setComments(List<CommentsModel> comments) {
            this.comments = comments;
        }

        public static class CommentsModel {
            /**
             * id : 12
             * contentid : 15
             * content : 你这个车位太贵了，我租不起，可以送给我不。
             * creatorid : 15
             * creator : 小李飞刀
             * createtime : 1508070880000
             * replies : [{"id":9,"commentid":12,"content":"哎呀，本山大叔，你的买不起就不买呗，不差钱儿，疼，不疼能这么抽抽吗。","creatorid":15,"creator":"小李飞刀","createtime":1508071251000}]
             */

            private int id;
            private int contentid;
            private String content;
            private int creatorid;
            private String creator;
            private long createtime;
            private List<RepliesBean> replies;

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

            public List<RepliesBean> getReplies() {
                return replies;
            }

            public void setReplies(List<RepliesBean> replies) {
                this.replies = replies;
            }

            public static class RepliesBean {
                /**
                 * id : 9
                 * commentid : 12
                 * content : 哎呀，本山大叔，你的买不起就不买呗，不差钱儿，疼，不疼能这么抽抽吗。
                 * creatorid : 15
                 * creator : 小李飞刀
                 * createtime : 1508071251000
                 */

                private int id;
                private int commentid;
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

                public int getCommentid() {
                    return commentid;
                }

                public void setCommentid(int commentid) {
                    this.commentid = commentid;
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
    }
}
