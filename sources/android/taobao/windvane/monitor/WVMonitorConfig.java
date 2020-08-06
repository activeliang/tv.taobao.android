package android.taobao.windvane.monitor;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WVMonitorConfig {
    public List<ErrorRule> errorRule = new ArrayList();
    public boolean isErrorBlacklist = true;
    public double perfCheckSampleRate = ClientTraceData.b.f47a;
    public String perfCheckURL = "";
    public StatRule stat = new StatRule();
    public String v = "0";

    public ErrorRule newErrorRuleInstance(String url, String msg, String code) {
        ErrorRule res = new ErrorRule();
        res.url = url;
        res.msg = msg;
        res.code = code;
        return res;
    }

    public class StatRule {
        public boolean netstat = false;
        public long onDomLoad = 0;
        public long onLoad = 0;
        public int resSample;
        public long resTime = 0;

        public StatRule() {
        }
    }

    public class ErrorRule {
        public String code = "";
        public String msg = "";
        public Pattern msgPattern = null;
        public String url = "";
        public Pattern urlPattern = null;

        public ErrorRule() {
        }
    }
}
