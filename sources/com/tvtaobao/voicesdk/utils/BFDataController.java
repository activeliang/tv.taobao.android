package com.tvtaobao.voicesdk.utils;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import com.bftv.fui.thirdparty.VoiceFeedback;
import com.bftv.fui.thirdparty.bean.AllIntent;
import com.bftv.fui.thirdparty.bean.MiddleData;
import com.bftv.fui.thirdparty.bean.Tips;
import com.tvtaobao.voicesdk.bean.JinnangDo;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.ProductDo;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import java.util.ArrayList;
import java.util.List;

public class BFDataController {
    public static VoiceFeedback onSearchSuccess(String words, List<ProductDo> productDos, List<JinnangDo> jinnangDos, String message, List<String> tips) {
        VoiceFeedback voiceFb = new VoiceFeedback();
        voiceFb.isHasResult = true;
        voiceFb.listMiddleData = new ArrayList();
        voiceFb.listTips = new ArrayList();
        if (TextUtils.isEmpty(message)) {
            if (productDos.size() == 0) {
                message = "很抱歉，没有找到与\"" + words + "\"相关的商品";
            } else {
                message = "为您推荐如下几款 " + words + "，您想买哪一款？";
            }
        }
        voiceFb.feedback = message;
        voiceFb.listPrompts = new ArrayList();
        if (tips == null || tips.size() == 0) {
            voiceFb.listPrompts.add("想看第三个商品的详情，可以对我说“看看第三个”");
            voiceFb.listPrompts.add("没有喜欢的？可以对我说“换一批”");
        }
        voiceFb.listPrompts.addAll(tips);
        if (jinnangDos.size() > 0) {
            String jinnang = jinnangDos.get(5).getName();
            voiceFb.listPrompts.add(0, "想要" + jinnang + "的？可以对我说“" + jinnang + "”");
        }
        voiceFb.type = 2;
        for (int i = 0; i < productDos.size(); i++) {
            voiceFb.listMiddleData.add(makeProduct(productDos.get(i)));
        }
        for (int j = 0; j < jinnangDos.size(); j++) {
            voiceFb.listTips.add(makeJinNang(jinnangDos.get(j)));
        }
        return voiceFb;
    }

    public static MiddleData makeProduct(ProductDo items) {
        MiddleData middleData = new MiddleData();
        middleData.middlePic = items.getPicUrl();
        middleData.title = items.getTitle();
        middleData.sales = items.getBiz30daySold();
        middleData.price = items.getDiscntPrice();
        middleData.label = items.getPostageTextInfo();
        middleData.isCommodity = true;
        List<AllIntent> list = new ArrayList<>();
        if (Integer.parseInt(items.getStdSkuSize()) > 1 || items.getPresale().equals("true")) {
            middleData.isFastTips = false;
        } else {
            middleData.isFastTips = true;
        }
        if (items.getTopTag() == null || !items.getTopTag().equals("1000")) {
            middleData.isBuyTips = false;
        } else {
            middleData.isBuyTips = true;
        }
        AllIntent allIntent = new AllIntent();
        allIntent.type = HuasuVideo.TAG_URI;
        allIntent.uri = "tvtaobao://home?module=detail&itemId=" + items.getItemId() + "&from=voice_system&from_app=voice_system&notshowloading=true";
        allIntent.entranceWords = ConnType.PK_OPEN;
        list.add(allIntent);
        AllIntent tobuy = new AllIntent();
        tobuy.type = HuasuVideo.TAG_URI;
        tobuy.entranceWords = "tobuy";
        tobuy.uri = "tvtaobao://voice?module=createorder&itemId=" + items.getItemId() + "&skuSize=" + items.getStdSkuSize() + "&from=voice_system&from_app=voice_system&notshowloading=true&presale=" + items.getPresale();
        list.add(tobuy);
        AllIntent toAddCart = new AllIntent();
        toAddCart.type = HuasuVideo.TAG_URI;
        toAddCart.uri = "tvtaobao://addcart?itemId=" + items.getItemId();
        toAddCart.entranceWords = "addcart";
        list.add(toAddCart);
        AllIntent toCollection = new AllIntent();
        toCollection.type = HuasuVideo.TAG_URI;
        toCollection.uri = "tvtaobao://collection?itemId=" + items.getItemId();
        toCollection.entranceWords = "collection";
        list.add(toCollection);
        middleData.listIntent = list;
        return middleData;
    }

    public static Tips makeJinNang(JinnangDo jinNangDO) {
        Tips tips = new Tips();
        tips.name = jinNangDO.getName();
        tips.content = jinNangDO.getContent();
        tips.type = jinNangDO.getType();
        return tips;
    }
}
