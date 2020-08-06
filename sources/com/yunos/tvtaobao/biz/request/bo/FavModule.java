package com.yunos.tvtaobao.biz.request.bo;

public class FavModule {
    private CardPackageCnt cardPackageCnt;
    private FavoriteCnt favoriteCnt;
    private FollowCnt followCnt;
    private FootprintCnt footprintCnt;

    public void setFavoriteCnt(FavoriteCnt favoriteCnt2) {
        this.favoriteCnt = favoriteCnt2;
    }

    public FavoriteCnt getFavoriteCnt() {
        return this.favoriteCnt;
    }

    public void setFootprintCnt(FootprintCnt footprintCnt2) {
        this.footprintCnt = footprintCnt2;
    }

    public FootprintCnt getFootprintCnt() {
        return this.footprintCnt;
    }

    public void setFollowCnt(FollowCnt followCnt2) {
        this.followCnt = followCnt2;
    }

    public FollowCnt getFollowCnt() {
        return this.followCnt;
    }

    public void setCardPackageCnt(CardPackageCnt cardPackageCnt2) {
        this.cardPackageCnt = cardPackageCnt2;
    }

    public CardPackageCnt getCardPackageCnt() {
        return this.cardPackageCnt;
    }
}
