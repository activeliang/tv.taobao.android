package com.taobao.detail.domain.biz;

import com.taobao.detail.domain.base.ServiceUnit;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ServiceInfo implements Serializable {
    public Boolean groupMulti;
    public Boolean groupMustSelect;
    public Map<Long, String> serIdMap;
    public List<List<ServiceUnit>> serviceUnit;
    public Map<String, List<List<ServiceUnit>>> skuServiceUnit;
    public Map<Long, String> unqIdMap;
}
