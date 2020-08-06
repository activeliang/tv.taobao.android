package com.ali.user.open.core.registry.impl;

import com.ali.user.open.core.registry.ServiceRegistration;
import com.ali.user.open.core.registry.ServiceRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultServiceRegistry implements ServiceRegistry {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<ServiceRegistration, ServiceEntry> registrationServiceEntries = new HashMap();
    private Map<Class<?>, List<ServiceEntry>> typeServiceEntries = new HashMap();

    public ServiceRegistration registerService(Class<?>[] types, Object instance, Map<String, String> properties) {
        if (types == null || types.length == 0 || instance == null) {
            throw new IllegalArgumentException("service types and instance must not be null");
        }
        ServiceEntry serviceEntry = new ServiceEntry();
        serviceEntry.instance = instance;
        serviceEntry.types = types;
        serviceEntry.properties = Collections.synchronizedMap(new HashMap());
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (!(entry.getKey() == null || entry.getValue() == null)) {
                    serviceEntry.properties.put(entry.getKey(), entry.getValue());
                }
            }
        }
        this.lock.writeLock().lock();
        try {
            for (Class<?> type : types) {
                List<ServiceEntry> instances = this.typeServiceEntries.get(type);
                if (instances == null) {
                    instances = new ArrayList<>(2);
                    this.typeServiceEntries.put(type, instances);
                }
                instances.add(serviceEntry);
            }
            ServiceRegistration serviceRegistration = new InternalServiceRegistration(this, serviceEntry.properties);
            this.registrationServiceEntries.put(serviceRegistration, serviceEntry);
            return serviceRegistration;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0062 A[Catch:{ all -> 0x009d }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0081 A[EDGE_INSN: B:36:0x0081->B:26:0x0081 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T getService(java.lang.Class<T> r11, java.util.Map<java.lang.String, java.lang.String> r12) {
        /*
            r10 = this;
            r5 = 0
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.lock()
            java.util.Map<java.lang.Class<?>, java.util.List<com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry>> r6 = r10.typeServiceEntries     // Catch:{ all -> 0x009d }
            java.lang.Object r2 = r6.get(r11)     // Catch:{ all -> 0x009d }
            java.util.List r2 = (java.util.List) r2     // Catch:{ all -> 0x009d }
            if (r2 == 0) goto L_0x001a
            int r6 = r2.size()     // Catch:{ all -> 0x009d }
            if (r6 != 0) goto L_0x0024
        L_0x001a:
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.unlock()
        L_0x0023:
            return r5
        L_0x0024:
            if (r12 == 0) goto L_0x002c
            int r6 = r12.size()     // Catch:{ all -> 0x009d }
            if (r6 != 0) goto L_0x0043
        L_0x002c:
            r5 = 0
            java.lang.Object r5 = r2.get(r5)     // Catch:{ all -> 0x009d }
            com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry r5 = (com.ali.user.open.core.registry.impl.DefaultServiceRegistry.ServiceEntry) r5     // Catch:{ all -> 0x009d }
            java.lang.Object r5 = r5.instance     // Catch:{ all -> 0x009d }
            java.lang.Object r5 = r11.cast(r5)     // Catch:{ all -> 0x009d }
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.unlock()
            goto L_0x0023
        L_0x0043:
            java.util.Iterator r6 = r2.iterator()     // Catch:{ all -> 0x009d }
        L_0x0047:
            boolean r7 = r6.hasNext()     // Catch:{ all -> 0x009d }
            if (r7 == 0) goto L_0x0093
            java.lang.Object r3 = r6.next()     // Catch:{ all -> 0x009d }
            com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry r3 = (com.ali.user.open.core.registry.impl.DefaultServiceRegistry.ServiceEntry) r3     // Catch:{ all -> 0x009d }
            r1 = 1
            java.util.Set r7 = r12.entrySet()     // Catch:{ all -> 0x009d }
            java.util.Iterator r7 = r7.iterator()     // Catch:{ all -> 0x009d }
        L_0x005c:
            boolean r8 = r7.hasNext()     // Catch:{ all -> 0x009d }
            if (r8 == 0) goto L_0x0081
            java.lang.Object r0 = r7.next()     // Catch:{ all -> 0x009d }
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ all -> 0x009d }
            java.util.Map<java.lang.String, java.lang.String> r8 = r3.properties     // Catch:{ all -> 0x009d }
            java.lang.Object r9 = r0.getKey()     // Catch:{ all -> 0x009d }
            java.lang.Object r4 = r8.get(r9)     // Catch:{ all -> 0x009d }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x009d }
            if (r4 == 0) goto L_0x0080
            java.lang.Object r8 = r0.getValue()     // Catch:{ all -> 0x009d }
            boolean r8 = r4.equals(r8)     // Catch:{ all -> 0x009d }
            if (r8 != 0) goto L_0x005c
        L_0x0080:
            r1 = 0
        L_0x0081:
            if (r1 == 0) goto L_0x0047
            java.lang.Object r5 = r3.instance     // Catch:{ all -> 0x009d }
            java.lang.Object r5 = r11.cast(r5)     // Catch:{ all -> 0x009d }
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.unlock()
            goto L_0x0023
        L_0x0093:
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.unlock()
            goto L_0x0023
        L_0x009d:
            r5 = move-exception
            java.util.concurrent.locks.ReadWriteLock r6 = r10.lock
            java.util.concurrent.locks.Lock r6 = r6.readLock()
            r6.unlock()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.registry.impl.DefaultServiceRegistry.getService(java.lang.Class, java.util.Map):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008d A[Catch:{ all -> 0x00b8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ac A[EDGE_INSN: B:42:0x00ac->B:29:0x00ac ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T[] getServices(java.lang.Class<T> r15, java.util.Map<java.lang.String, java.lang.String> r16) {
        /*
            r14 = this;
            java.util.concurrent.locks.ReadWriteLock r10 = r14.lock
            java.util.concurrent.locks.Lock r10 = r10.readLock()
            r10.lock()
            java.util.Map<java.lang.Class<?>, java.util.List<com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry>> r10 = r14.typeServiceEntries     // Catch:{ all -> 0x00b8 }
            java.lang.Object r5 = r10.get(r15)     // Catch:{ all -> 0x00b8 }
            java.util.List r5 = (java.util.List) r5     // Catch:{ all -> 0x00b8 }
            if (r5 == 0) goto L_0x0019
            int r10 = r5.size()     // Catch:{ all -> 0x00b8 }
            if (r10 != 0) goto L_0x002d
        L_0x0019:
            r10 = 0
            java.lang.Object r10 = java.lang.reflect.Array.newInstance(r15, r10)     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00b8 }
            java.util.concurrent.locks.ReadWriteLock r11 = r14.lock
            java.util.concurrent.locks.Lock r11 = r11.readLock()
            r11.unlock()
            r8 = r10
        L_0x002c:
            return r8
        L_0x002d:
            if (r16 == 0) goto L_0x0035
            int r10 = r16.size()     // Catch:{ all -> 0x00b8 }
            if (r10 != 0) goto L_0x0065
        L_0x0035:
            int r10 = r5.size()     // Catch:{ all -> 0x00b8 }
            java.lang.Object r10 = java.lang.reflect.Array.newInstance(r15, r10)     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00b8 }
            r0 = r10
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x00b8 }
            r8 = r0
            r2 = 0
            int r3 = r5.size()     // Catch:{ all -> 0x00b8 }
        L_0x0048:
            if (r2 >= r3) goto L_0x005b
            java.lang.Object r10 = r5.get(r2)     // Catch:{ all -> 0x00b8 }
            com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry r10 = (com.ali.user.open.core.registry.impl.DefaultServiceRegistry.ServiceEntry) r10     // Catch:{ all -> 0x00b8 }
            java.lang.Object r10 = r10.instance     // Catch:{ all -> 0x00b8 }
            java.lang.Object r10 = r15.cast(r10)     // Catch:{ all -> 0x00b8 }
            r8[r2] = r10     // Catch:{ all -> 0x00b8 }
            int r2 = r2 + 1
            goto L_0x0048
        L_0x005b:
            java.util.concurrent.locks.ReadWriteLock r10 = r14.lock
            java.util.concurrent.locks.Lock r10 = r10.readLock()
            r10.unlock()
            goto L_0x002c
        L_0x0065:
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ all -> 0x00b8 }
            int r10 = r5.size()     // Catch:{ all -> 0x00b8 }
            r7.<init>(r10)     // Catch:{ all -> 0x00b8 }
            java.util.Iterator r10 = r5.iterator()     // Catch:{ all -> 0x00b8 }
        L_0x0072:
            boolean r11 = r10.hasNext()     // Catch:{ all -> 0x00b8 }
            if (r11 == 0) goto L_0x00c3
            java.lang.Object r6 = r10.next()     // Catch:{ all -> 0x00b8 }
            com.ali.user.open.core.registry.impl.DefaultServiceRegistry$ServiceEntry r6 = (com.ali.user.open.core.registry.impl.DefaultServiceRegistry.ServiceEntry) r6     // Catch:{ all -> 0x00b8 }
            r4 = 1
            java.util.Set r11 = r16.entrySet()     // Catch:{ all -> 0x00b8 }
            java.util.Iterator r11 = r11.iterator()     // Catch:{ all -> 0x00b8 }
        L_0x0087:
            boolean r12 = r11.hasNext()     // Catch:{ all -> 0x00b8 }
            if (r12 == 0) goto L_0x00ac
            java.lang.Object r1 = r11.next()     // Catch:{ all -> 0x00b8 }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ all -> 0x00b8 }
            java.util.Map<java.lang.String, java.lang.String> r12 = r6.properties     // Catch:{ all -> 0x00b8 }
            java.lang.Object r13 = r1.getKey()     // Catch:{ all -> 0x00b8 }
            java.lang.Object r9 = r12.get(r13)     // Catch:{ all -> 0x00b8 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ all -> 0x00b8 }
            if (r9 == 0) goto L_0x00ab
            java.lang.Object r12 = r1.getValue()     // Catch:{ all -> 0x00b8 }
            boolean r12 = r9.equals(r12)     // Catch:{ all -> 0x00b8 }
            if (r12 != 0) goto L_0x0087
        L_0x00ab:
            r4 = 0
        L_0x00ac:
            if (r4 == 0) goto L_0x0072
            java.lang.Object r11 = r6.instance     // Catch:{ all -> 0x00b8 }
            java.lang.Object r11 = r15.cast(r11)     // Catch:{ all -> 0x00b8 }
            r7.add(r11)     // Catch:{ all -> 0x00b8 }
            goto L_0x0072
        L_0x00b8:
            r10 = move-exception
            java.util.concurrent.locks.ReadWriteLock r11 = r14.lock
            java.util.concurrent.locks.Lock r11 = r11.readLock()
            r11.unlock()
            throw r10
        L_0x00c3:
            int r10 = r7.size()     // Catch:{ all -> 0x00b8 }
            java.lang.Object r10 = java.lang.reflect.Array.newInstance(r15, r10)     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r10 = (java.lang.Object[]) r10     // Catch:{ all -> 0x00b8 }
            java.lang.Object[] r8 = r7.toArray(r10)     // Catch:{ all -> 0x00b8 }
            java.util.concurrent.locks.ReadWriteLock r10 = r14.lock
            java.util.concurrent.locks.Lock r10 = r10.readLock()
            r10.unlock()
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.registry.impl.DefaultServiceRegistry.getServices(java.lang.Class, java.util.Map):java.lang.Object[]");
    }

    public Object unregisterService(ServiceRegistration registration) {
        Object obj = null;
        if (registration != null) {
            this.lock.writeLock().lock();
            try {
                ServiceEntry serviceEntry = this.registrationServiceEntries.remove(registration);
                if (serviceEntry != null) {
                    if (serviceEntry.types != null) {
                        for (Class<?> type : serviceEntry.types) {
                            List<ServiceEntry> serviceEntries = this.typeServiceEntries.get(type);
                            Iterator<ServiceEntry> it = serviceEntries.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    if (it.next() == serviceEntry) {
                                        it.remove();
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                            if (serviceEntries.size() == 0) {
                                this.typeServiceEntries.remove(type);
                            }
                        }
                    }
                    obj = serviceEntry.instance;
                    this.lock.writeLock().unlock();
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        }
        return obj;
    }

    static class ServiceEntry {
        public Object instance;
        public Map<String, String> properties;
        public Class<?>[] types;

        ServiceEntry() {
        }
    }

    static class InternalServiceRegistration implements ServiceRegistration {
        private Map<String, String> properties;
        private ServiceRegistry serviceRegistry;
        private final String uuid = UUID.randomUUID().toString();

        public InternalServiceRegistration(ServiceRegistry serviceRegistry2, Map<String, String> properties2) {
            this.serviceRegistry = serviceRegistry2;
            this.properties = properties2;
        }

        public void setProperties(Map<String, String> properties2) {
            if (properties2 != null) {
                for (Map.Entry<String, String> entry : properties2.entrySet()) {
                    if (!(entry.getKey() == null || entry.getValue() == null)) {
                        this.properties.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        public void unregister() {
            this.serviceRegistry.unregisterService(this);
        }

        public int hashCode() {
            return (this.uuid == null ? 0 : this.uuid.hashCode()) + 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.uuid.equals(((InternalServiceRegistration) obj).uuid);
        }
    }
}
