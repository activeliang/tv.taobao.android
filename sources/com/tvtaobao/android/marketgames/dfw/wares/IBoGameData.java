package com.tvtaobao.android.marketgames.dfw.wares;

import android.graphics.PointF;
import java.util.List;

public interface IBoGameData {

    public interface IBoGuideData {
        String getBgColor();

        String getBgImgUrl();
    }

    public interface IBoLoadData {
        String getBgColor();

        String getBgImgUrl();

        List<String> getLoadProgressBarColor();
    }

    String getBgColor();

    String getBtnAwardListImgUrl_focus();

    String getBtnAwardListImgUrl_unfocus();

    String getBtnGameRuleImgUrl_focus();

    String getBtnGameRuleImgUrl_unfocus();

    String getBtnRollDiceEffectImgUrl();

    String getBtnRollDiceImgUrl_normal_focus();

    String getBtnRollDiceImgUrl_normal_press();

    String getBtnRollDiceImgUrl_normal_unfocus();

    String getBtnTaskListImgUrl_focus();

    String getBtnTaskListImgUrl_unfocus();

    String getDiceAnim1ImgUrl();

    String getDiceAnim2ImgUrl();

    String getDiceAnim3ImgUrl();

    String getDiceAnim4ImgUrl();

    String getDiceAnim5ImgUrl();

    String getDiceAnim6ImgUrl();

    List<PointF> getDiceBgnAndEnd();

    String getDiceRlt1ImgUrl();

    String getDiceRlt2ImgUrl();

    String getDiceRlt3ImgUrl();

    String getDiceRlt4ImgUrl();

    String getDiceRlt5ImgUrl();

    String getDiceRlt6ImgUrl();

    String getDiceStaticImgUrl();

    String getGamePlayerImgUrl();

    String getGameSceneImgUrl();

    String getGameUltimatePrizeImgUrl();

    List<PointF> getGameUltimatePrizePos();

    IBoGuideData getGuideData();

    IBoLoadData getLoadData();

    int getPlayerPos();

    int getPlayerPosBeforeRoll();

    List<PointF> getPositions();

    int getRemainTime();

    String getRemainTimeTipBubbleImgUrl();

    boolean isNewGamer();
}
