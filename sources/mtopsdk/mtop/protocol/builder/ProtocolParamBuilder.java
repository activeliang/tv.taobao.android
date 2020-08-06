package mtopsdk.mtop.protocol.builder;

import java.util.Map;
import mtopsdk.framework.domain.MtopContext;

public interface ProtocolParamBuilder {
    Map<String, String> buildParams(MtopContext mtopContext);
}
