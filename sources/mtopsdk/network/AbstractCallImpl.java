package mtopsdk.network;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MockResponse;
import mtopsdk.network.domain.NetworkStats;
import mtopsdk.network.domain.Request;
import mtopsdk.network.domain.Response;
import mtopsdk.network.domain.ResponseBody;
import org.json.JSONObject;

public abstract class AbstractCallImpl implements Call {
    private static final String TAG = "mtopsdk.AbstractCallImpl";
    protected static AtomicBoolean flag = new AtomicBoolean(false);
    public static volatile boolean isDebugApk;
    public static volatile boolean isOpenMock;
    protected volatile boolean canceled;
    protected Future future;
    protected Context mContext;
    protected Request mRequest;
    protected String seqNo;

    protected AbstractCallImpl(Request mRequest2, Context context) {
        this.mRequest = mRequest2;
        if (this.mRequest != null) {
            this.seqNo = this.mRequest.seqNo;
        }
        this.mContext = context;
        if (this.mContext != null && flag.compareAndSet(false, true)) {
            isDebugApk = MtopUtils.isApkDebug(this.mContext);
            isOpenMock = MtopUtils.isAppOpenMock(this.mContext);
            TBSdkLog.i(TAG, this.seqNo, "isDebugApk=" + isDebugApk + ",isOpenMock=" + isOpenMock);
        }
    }

    public Request request() {
        return this.mRequest;
    }

    public void cancel() {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "try to cancel call.");
        }
        this.canceled = true;
        if (this.future != null) {
            this.future.cancel(true);
        }
    }

    /* access modifiers changed from: protected */
    public MockResponse getMockResponse(String api) {
        MockResponse mockResponse = null;
        if (api == null) {
            TBSdkLog.e(TAG, this.seqNo, "[getMockResponse] apiName is null!");
            return null;
        } else if (this.mContext == null) {
            TBSdkLog.e(TAG, this.seqNo, "[getMockResponse] mContext is null!");
            return null;
        } else {
            try {
                byte[] byteData = MtopUtils.readFile(this.mContext.getFilesDir().getCanonicalPath() + "/mock/deMock/" + api + ".json");
                if (byteData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(byteData));
                        if (jsonObject != null) {
                            MockResponse mockResponse2 = new MockResponse();
                            try {
                                mockResponse2.api = api;
                                String byteBody = jsonObject.optString("mock_body");
                                if (byteBody != null) {
                                    mockResponse2.byteData = byteBody.getBytes("utf-8");
                                }
                                JSONObject headers = jsonObject.optJSONObject("response_header");
                                if (headers != null) {
                                    mockResponse2.headers = new HashMap();
                                    Iterator<String> iterator = headers.keys();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next().toString();
                                        String value = headers.getString(key);
                                        ArrayList<String> list = new ArrayList<>();
                                        list.add(value);
                                        mockResponse2.headers.put(key, list);
                                    }
                                }
                                String statusCode = jsonObject.optString("response_status");
                                if (statusCode != null) {
                                    mockResponse2.statusCode = Integer.parseInt(statusCode);
                                }
                                mockResponse = mockResponse2;
                            } catch (Exception e) {
                                e = e;
                                mockResponse = mockResponse2;
                                TBSdkLog.e(TAG, this.seqNo, "[getMockData] get MockData error.api=" + api, e);
                                return mockResponse;
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        TBSdkLog.e(TAG, this.seqNo, "[getMockData] get MockData error.api=" + api, e);
                        return mockResponse;
                    }
                }
                return mockResponse;
            } catch (IOException e3) {
                TBSdkLog.e(TAG, this.seqNo, "[getMockResponse] parse ExternalFilesDir/mock/deMock/" + api + ".json filePath error.", e3);
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Response buildResponse(Request request, int responseCode, String responseMessage, final Map<String, List<String>> responseHeaders, final byte[] responseByteData, NetworkStats networkStats) {
        return new Response.Builder().request(request).code(responseCode).message(responseMessage).headers(responseHeaders).body(new ResponseBody() {
            public String contentType() {
                return HeaderHandlerUtil.getSingleHeaderFieldByKey(responseHeaders, "Content-Type");
            }

            public long contentLength() throws IOException {
                if (responseByteData != null) {
                    return (long) responseByteData.length;
                }
                return 0;
            }

            public InputStream byteStream() {
                return null;
            }

            public byte[] getBytes() throws IOException {
                return responseByteData;
            }
        }).stat(networkStats).build();
    }
}
