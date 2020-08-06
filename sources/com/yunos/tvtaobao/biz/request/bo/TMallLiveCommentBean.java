package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class TMallLiveCommentBean {
    private ModelBean model;

    public ModelBean getModel() {
        return this.model;
    }

    public void setModel(ModelBean model2) {
        this.model = model2;
    }

    public static class ModelBean {
        private List<DataBean> data;
        private String endId;
        private String endTime;
        private String startId;
        private String startTime;

        public String getEndId() {
            return this.endId;
        }

        public void setEndId(String endId2) {
            this.endId = endId2;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime2) {
            this.endTime = endTime2;
        }

        public String getStartId() {
            return this.startId;
        }

        public void setStartId(String startId2) {
            this.startId = startId2;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setStartTime(String startTime2) {
            this.startTime = startTime2;
        }

        public List<DataBean> getData() {
            return this.data;
        }

        public void setData(List<DataBean> data2) {
            this.data = data2;
        }

        public static class DataBean {
            private AuthorBean author;
            private String commentId;
            private String dislikeCount;
            private String disliked;
            private String floor;
            private String gmtCreate;
            private List<?> images;
            private String likeCount;
            private String liked;
            private String self;
            private String source;
            private String status;
            private String subjectId;
            private String text;
            private String type;

            public boolean equals(Object o) {
                if (o instanceof DataBean) {
                    return this.commentId.equals(((DataBean) o).getCommentId());
                }
                return super.equals(o);
            }

            public AuthorBean getAuthor() {
                return this.author;
            }

            public void setAuthor(AuthorBean author2) {
                this.author = author2;
            }

            public String getCommentId() {
                return this.commentId;
            }

            public void setCommentId(String commentId2) {
                this.commentId = commentId2;
            }

            public String getDislikeCount() {
                return this.dislikeCount;
            }

            public void setDislikeCount(String dislikeCount2) {
                this.dislikeCount = dislikeCount2;
            }

            public String getDisliked() {
                return this.disliked;
            }

            public void setDisliked(String disliked2) {
                this.disliked = disliked2;
            }

            public String getFloor() {
                return this.floor;
            }

            public void setFloor(String floor2) {
                this.floor = floor2;
            }

            public String getGmtCreate() {
                return this.gmtCreate;
            }

            public void setGmtCreate(String gmtCreate2) {
                this.gmtCreate = gmtCreate2;
            }

            public String getLikeCount() {
                return this.likeCount;
            }

            public void setLikeCount(String likeCount2) {
                this.likeCount = likeCount2;
            }

            public String getLiked() {
                return this.liked;
            }

            public void setLiked(String liked2) {
                this.liked = liked2;
            }

            public String getSelf() {
                return this.self;
            }

            public void setSelf(String self2) {
                this.self = self2;
            }

            public String getSource() {
                return this.source;
            }

            public void setSource(String source2) {
                this.source = source2;
            }

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status2) {
                this.status = status2;
            }

            public String getSubjectId() {
                return this.subjectId;
            }

            public void setSubjectId(String subjectId2) {
                this.subjectId = subjectId2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }

            public List<?> getImages() {
                return this.images;
            }

            public void setImages(List<?> images2) {
                this.images = images2;
            }

            public static class AuthorBean {
                private String avatar;
                private String displayName;
                private String userId;

                public String getAvatar() {
                    return this.avatar;
                }

                public void setAvatar(String avatar2) {
                    this.avatar = avatar2;
                }

                public String getDisplayName() {
                    return this.displayName;
                }

                public void setDisplayName(String displayName2) {
                    this.displayName = displayName2;
                }

                public String getUserId() {
                    return this.userId;
                }

                public void setUserId(String userId2) {
                    this.userId = userId2;
                }
            }
        }
    }
}
