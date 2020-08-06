package com.yunos.tvtaobao.uuid.client;

import com.yunos.tvtaobao.uuid.utils.Logger;
import com.yunos.tvtaobao.uuid.utils.StringUtil;
import java.io.StringWriter;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public abstract class XmlWrite {
    /* access modifiers changed from: protected */
    public abstract void writeContent(XmlSerializer xmlSerializer);

    public String write() {
        StringWriter xmlWriter = new StringWriter();
        try {
            XmlSerializer xmlSerial = XmlPullParserFactory.newInstance().newSerializer();
            xmlSerial.setOutput(xmlWriter);
            xmlSerial.startDocument("UTF-8", true);
            xmlSerial.startTag("", "UUID_Info");
            xmlSerial.startTag("", "UUID_Message");
            writeContent(xmlSerial);
            xmlSerial.endTag("", "UUID_Message");
            xmlSerial.endTag("", "UUID_Info");
            xmlSerial.endDocument();
            return xmlWriter.toString();
        } catch (Exception e) {
            Logger.loge("writexml fail! ", e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void tagBuilder(XmlSerializer xmlSerializer, String tagName, String text) {
        if (StringUtil.isAvailableString(text)) {
            try {
                xmlSerializer.startTag("", tagName);
                xmlSerializer.text(text);
                xmlSerializer.endTag("", tagName);
            } catch (Exception e) {
                Logger.loge("write tag fail!", e);
            }
        }
    }
}
