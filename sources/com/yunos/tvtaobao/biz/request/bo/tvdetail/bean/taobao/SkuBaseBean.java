package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.io.Serializable;
import java.util.List;

public class SkuBaseBean implements Serializable {
    private List<PropsBean> props;
    private List<SkusBean> skus;

    public List<SkusBean> getSkus() {
        return this.skus;
    }

    public void setSkus(List<SkusBean> skus2) {
        this.skus = skus2;
    }

    public List<PropsBean> getProps() {
        return this.props;
    }

    public void setProps(List<PropsBean> props2) {
        this.props = props2;
    }

    public static class SkusBean implements Serializable {
        private String propPath;
        private String skuId;

        public String getSkuId() {
            return this.skuId;
        }

        public void setSkuId(String skuId2) {
            this.skuId = skuId2;
        }

        public String getPropPath() {
            return this.propPath;
        }

        public void setPropPath(String propPath2) {
            this.propPath = propPath2;
        }
    }

    public static class PropsBean implements Serializable {
        private String name;
        private String pid;
        private List<ValuesBean> values;

        public String getPid() {
            return this.pid;
        }

        public void setPid(String pid2) {
            this.pid = pid2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public List<ValuesBean> getValues() {
            return this.values;
        }

        public void setValues(List<ValuesBean> values2) {
            this.values = values2;
        }

        public static class ValuesBean implements Serializable {
            private String image;
            private String name;
            private String vid;

            public String getVid() {
                return this.vid;
            }

            public void setVid(String vid2) {
                this.vid = vid2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public String getImage() {
                return this.image;
            }

            public void setImage(String image2) {
                this.image = image2;
            }
        }
    }
}
