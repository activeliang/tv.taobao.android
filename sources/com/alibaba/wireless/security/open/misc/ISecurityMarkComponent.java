package com.alibaba.wireless.security.open.misc;

import com.alibaba.wireless.security.framework.InterfacePluginInfo;
import com.alibaba.wireless.security.open.IComponent;
import com.alibaba.wireless.security.open.SecException;
import java.util.HashMap;

@InterfacePluginInfo(pluginName = "misc")
public interface ISecurityMarkComponent extends IComponent {
    HashMap<String, Object> decrypt(HashMap<String, Object> hashMap) throws SecException;

    HashMap<String, Object> encrypt(HashMap<String, Object> hashMap) throws SecException;
}
