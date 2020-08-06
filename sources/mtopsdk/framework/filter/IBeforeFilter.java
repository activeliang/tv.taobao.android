package mtopsdk.framework.filter;

import mtopsdk.framework.domain.MtopContext;

public interface IBeforeFilter extends IMtopFilter {
    String doBefore(MtopContext mtopContext);
}
