package com.taobao.wireless.detail.api;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.base.Unit;
import java.util.Iterator;

public class ApiStackParser {
    private Iterator<Unit> apiStack;

    public ApiStackParser(TBDetailResultVO tbDetailResultVO) {
        if (tbDetailResultVO.apiStack != null) {
            this.apiStack = tbDetailResultVO.apiStack.iterator();
        }
    }

    public Unit nextApi() {
        if (this.apiStack != null && this.apiStack.hasNext()) {
            return this.apiStack.next();
        }
        return null;
    }
}
