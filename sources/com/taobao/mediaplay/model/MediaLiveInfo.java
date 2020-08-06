package com.taobao.mediaplay.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaLiveInfo implements Serializable {
    public String anchorId;
    public boolean edgePcdn;
    public boolean h265;
    public String liveId;
    public ArrayList<QualityLiveItem> liveUrlList;
    public String mediaSourceType;
    public boolean rateAdapte;
}
