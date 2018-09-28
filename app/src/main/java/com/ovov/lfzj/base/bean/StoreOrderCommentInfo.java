package com.ovov.lfzj.base.bean;

/**
 * 我的订单用户对商家评论实体类
 * Created by Administrator on 2017/10/13.
 */

public class StoreOrderCommentInfo {

    /**
     * data : {"id":"30","seller_id":"2","content":"社区智能家居评论","rating":"5","created_time":"2017-10-13 14:37:35","author":{"uid":"33","name":"18735106410","head_img":"59df399f2dba8.jpg"}}
     * status : 0
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * id : 30
         * seller_id : 2
         * content : 社区智能家居评论
         * rating : 5
         * created_time : 2017-10-13 14:37:35
         * author : {"uid":"33","name":"18735106410","head_img":"59df399f2dba8.jpg"}
         */

        private String id;
        private String seller_id;
        private String content;
        private String rating;
        private String created_time;
        private AuthorBean author;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public static class AuthorBean {
            /**
             * uid : 33
             * name : 18735106410
             * head_img : 59df399f2dba8.jpg
             */

            private String uid;
            private String name;
            private String head_img;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead_img() {
                return head_img;
            }

            public void setHead_img(String head_img) {
                this.head_img = head_img;
            }
        }
    }
}
