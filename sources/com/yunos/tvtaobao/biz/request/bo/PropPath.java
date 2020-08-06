package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class PropPath implements Serializable {
    private static final long serialVersionUID = -1252568582085185382L;
    private Set<Pvid> items = new HashSet();

    public PropPath(String path) {
        for (String str : path.split(SymbolExpUtil.SYMBOL_SEMICOLON)) {
            String[] temp = str.split(SymbolExpUtil.SYMBOL_COLON);
            this.items.add(new Pvid(Long.valueOf(temp[0]), Long.valueOf(temp[1])));
        }
    }

    public void addItem(Long pid, Long vid) {
        if (this.items == null) {
            this.items = new HashSet();
        }
        this.items.add(new Pvid(pid, vid));
    }

    public boolean contains(Pvid pvid) {
        if (pvid == null) {
            return true;
        }
        for (Pvid item : this.items) {
            if (item.isMatch(pvid)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(List<Pvid> idList) {
        if (idList == null) {
            return true;
        }
        boolean match = true;
        for (Pvid id : idList) {
            if (!match || !contains(id)) {
                match = false;
                continue;
            } else {
                match = true;
                continue;
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    public static class Pvid implements Serializable {
        private static final long serialVersionUID = 1435715796820368855L;
        private Long pid;
        private Long vid;

        public Pvid(Long pid2, Long vid2) {
            this.pid = pid2;
            this.vid = vid2;
        }

        public boolean isMatch(Long pid2, Long vid2) {
            return this.pid.equals(pid2) && this.vid.equals(vid2);
        }

        public boolean isMatch(Pvid pvid) {
            return this.pid.equals(pvid.getPid()) && this.vid.equals(pvid.getVid());
        }

        public Long getPid() {
            return this.pid;
        }

        public Long getVid() {
            return this.vid;
        }

        public String toString() {
            return this.pid + SymbolExpUtil.SYMBOL_COLON + this.vid;
        }

        public int hashCode() {
            return this.pid.hashCode() + this.vid.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Pvid)) {
                return false;
            }
            return isMatch((Pvid) obj);
        }
    }

    public Set<Pvid> getItems() {
        return this.items;
    }
}
