package com.ali.user.open.core.registry;

import java.util.Map;

public interface ServiceRegistration {
    void setProperties(Map<String, String> map);

    void unregister();
}
