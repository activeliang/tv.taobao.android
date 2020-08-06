package com.tvtaobao.voicesdk.bean;

import com.tvtaobao.voicesdk.utils.JSONUtil;
import java.io.Serializable;
import org.json.JSONObject;

public class JinnangDo implements Serializable {
    private String content;
    private String name;
    private int type;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public static JinnangDo resolverData(JSONObject object) {
        JinnangDo mDo = new JinnangDo();
        mDo.setName(JSONUtil.getString(object, "name"));
        mDo.setContent(JSONUtil.getString(object, "content"));
        mDo.setType(Integer.parseInt(JSONUtil.getString(object, "type")));
        return mDo;
    }
}
