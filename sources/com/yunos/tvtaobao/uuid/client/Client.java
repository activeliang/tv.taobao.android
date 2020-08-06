package com.yunos.tvtaobao.uuid.client;

import android.content.Context;
import android.util.Xml;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.uuid.client.exception.CommunicateWithServerException;
import com.yunos.tvtaobao.uuid.client.exception.GenerateUUIDException;
import com.yunos.tvtaobao.uuid.infos.InfosManager;
import com.yunos.tvtaobao.uuid.security.SecurityInfosManager;
import com.yunos.tvtaobao.uuid.utils.Logger;
import com.yunos.tvtaobao.uuid.utils.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.CharEncoding;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Client {
    private static final int SEND_XML_RETRY_TIMES = 300;
    private static final String UUID_ACTIVATE_URL_CIBN = "https://uuidapi.cp12.ott.cibntv.net/actUuid";
    private static final String UUID_ACTIVATE_URL_DEF = "https://uuidapi.yunos.com/actUuid";
    private static final String UUID_ACTIVATE_URL_WASU = "https://uuidapi.cp12.wasu.tv/actUuid";
    private static final String UUID_REGIST_URL_CIBN = "https://uuidapi.cp12.ott.cibntv.net/addUuid";
    private static final String UUID_REGIST_URL_DEF = "https://uuidapi.yunos.com/addUuid";
    private static final String UUID_REGIST_URL_WASU = "https://uuidapi.cp12.wasu.tv/addUuid";
    private String mCode;
    private Context mContext;
    private InfosManager mInfos;
    private SecurityInfosManager mSInfos;
    private StatusListener mStatusListener;
    private String mUUID;
    private String mUuidActivateURL;
    private String mUuidRegistURL;

    public interface StatusListener {
        void onActivate();

        void onRegister(String str);
    }

    public Client(Context context, InfosManager infos, SecurityInfosManager sInfos, StatusListener listener, int licenseType) {
        this.mContext = context;
        this.mInfos = infos;
        this.mSInfos = sInfos;
        this.mStatusListener = listener;
        Logger.log_d("---license type is : " + licenseType);
        switch (licenseType) {
            case 1:
                this.mUuidRegistURL = UUID_REGIST_URL_WASU;
                this.mUuidActivateURL = UUID_ACTIVATE_URL_WASU;
                return;
            case 7:
                this.mUuidRegistURL = UUID_REGIST_URL_CIBN;
                this.mUuidActivateURL = UUID_ACTIVATE_URL_CIBN;
                return;
            default:
                this.mUuidRegistURL = UUID_REGIST_URL_DEF;
                this.mUuidActivateURL = UUID_ACTIVATE_URL_DEF;
                return;
        }
    }

    public void generate() throws GenerateUUIDException {
        try {
            register();
            try {
                activate();
            } catch (GenerateUUIDException e) {
                if (e.getErrorCode() == 17) {
                    this.mStatusListener.onActivate();
                    return;
                }
                throw e;
            }
        } catch (GenerateUUIDException e2) {
            if (e2.getErrorCode() == 14) {
                this.mStatusListener.onActivate();
                return;
            }
            throw e2;
        }
    }

    private void register() throws GenerateUUIDException {
        String xml = createRegistXml(this.mInfos, this.mSInfos);
        Logger.log_d("register xml = " + this.mUuidRegistURL + " " + xml);
        sendXmlAndGetRespond(xml, this.mUuidRegistURL, true);
        if (this.mUUID != null) {
            this.mStatusListener.onRegister(this.mUUID);
        }
    }

    private void activate() throws GenerateUUIDException {
        String xml = createActivateXml();
        Logger.log_d("active xml = " + this.mUuidActivateURL + " " + xml);
        sendXmlAndGetRespond(xml, this.mUuidActivateURL, false);
        this.mStatusListener.onActivate();
    }

    private String createRegistXml(final InfosManager infos, final SecurityInfosManager sInfos) {
        return new XmlWrite() {
            /* access modifiers changed from: protected */
            public void writeContent(XmlSerializer xmlSerial) {
                tagBuilder(xmlSerial, "uuid", infos.getLocalUUID());
                tagBuilder(xmlSerial, "hardware_id", infos.getHardwareID());
                tagBuilder(xmlSerial, "device_id", Constant.NULL);
                tagBuilder(xmlSerial, "mac", infos.getWifiMac());
                tagBuilder(xmlSerial, "product_id", infos.getProductModel());
                tagBuilder(xmlSerial, "build_time", infos.getBuildTime());
                tagBuilder(xmlSerial, "sign", sInfos.getSignedData());
                tagBuilder(xmlSerial, "bt_mac", Constant.NULL);
                tagBuilder(xmlSerial, "wired_mac", infos.getEth0Mac());
                tagBuilder(xmlSerial, "pkmd5", sInfos.getPKMd5());
                tagBuilder(xmlSerial, "profile", infos.getProfile());
            }
        }.write();
    }

    private String createActivateXml() {
        final List<String> list = new ArrayList<>();
        list.add(this.mUUID);
        list.add(this.mCode);
        return new XmlWrite() {
            /* access modifiers changed from: protected */
            public void writeContent(XmlSerializer xmlSerial) {
                String uuid = (String) list.get(0);
                String code = (String) list.get(1);
                if (StringUtil.isAvailableString(uuid)) {
                    tagBuilder(xmlSerial, "uuid", uuid);
                    if (StringUtil.isAvailableString(code)) {
                        byte[] checkSumByte = null;
                        try {
                            checkSumByte = EncryptionUtil.encodeHmacSha1(uuid, code, "UTF-8");
                        } catch (Exception e) {
                            Logger.loge("encode hmac failed!", e);
                        }
                        tagBuilder(xmlSerial, "hash", ByteArrayUtil.toHexString(checkSumByte));
                    }
                }
                tagBuilder(xmlSerial, "place", "EMMC");
                tagBuilder(xmlSerial, "is_otp", "yes");
            }
        }.write();
    }

    private void sendXmlAndGetRespond(String xml, String url, boolean needZip) throws GenerateUUIDException {
        UUIDHttps https = new UUIDHttps(this.mContext);
        InputStream input = null;
        int i = 0;
        while (i < 300) {
            try {
                input = https.httpXmlCommunication(url, xml, needZip);
                break;
            } catch (CommunicateWithServerException e1) {
                e1.printStackTrace();
                e1.print();
                Logger.loge("can not connect to server, sleep 1000ms");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        if (input == null) {
            Logger.loge("can not communicate with server after several attempts");
            throw new GenerateUUIDException(2, "network error");
        }
        try {
            String in = IOUtil.inputStreamToString(input, CharEncoding.ISO_8859_1);
            Logger.log_d("read from server: " + in);
            readServerResult(IOUtil.stringToInputStream(in, CharEncoding.ISO_8859_1));
        } catch (Exception e2) {
            Logger.loge("read from server error!", e2);
            e2.printStackTrace();
            throw new GenerateUUIDException(1024, "network error");
        }
    }

    private void readServerResult(InputStream in) throws GenerateUUIDException {
        int ret;
        int errorCode = 0;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 2:
                        if (!parser.getName().equals("UUID_Info")) {
                            if (!parser.getName().equals("UUID_Message")) {
                                if (!parser.getName().equals("error")) {
                                    if (!parser.getName().equals("msg")) {
                                        if (!parser.getName().equals("uuid")) {
                                            if (!parser.getName().equals("code")) {
                                                break;
                                            } else {
                                                int eventType2 = parser.next();
                                                this.mCode = parser.getText();
                                                break;
                                            }
                                        } else {
                                            int eventType3 = parser.next();
                                            this.mUUID = parser.getText();
                                            break;
                                        }
                                    } else {
                                        int eventType4 = parser.next();
                                        Logger.loge("error message is: " + parser.getText());
                                        throw new GenerateUUIDException(errorCode, parser.getText());
                                    }
                                } else {
                                    int eventType5 = parser.next();
                                    try {
                                        ret = Integer.parseInt(parser.getText());
                                    } catch (NumberFormatException e) {
                                        ret = -1;
                                    }
                                    Logger.loge("error = " + ret);
                                    errorCode = ret;
                                    break;
                                }
                            } else {
                                Logger.log_d("UUID_Message");
                                break;
                            }
                        } else {
                            Logger.log_d("UUID_Info");
                            break;
                        }
                }
            }
        } catch (XmlPullParserException e2) {
            Logger.loge("parser xml error!", e2);
            throw new GenerateUUIDException(1025, "network error");
        } catch (IOException e3) {
            Logger.loge("parser xml error!", e3);
            throw new GenerateUUIDException(1025, "network error");
        }
    }
}
