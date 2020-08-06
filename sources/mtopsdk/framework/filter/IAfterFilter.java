package mtopsdk.framework.filter;

import mtopsdk.framework.domain.MtopContext;

public interface IAfterFilter extends IMtopFilter {
    String doAfter(MtopContext mtopContext);
}
