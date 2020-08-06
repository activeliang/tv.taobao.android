package anet.channel;

import anet.channel.entity.ConnType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SessionPool {
    private final Map<SessionRequest, List<Session>> connPool = new HashMap();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = this.lock.writeLock();

    SessionPool() {
    }

    public void add(SessionRequest info, Session conn) {
        if (info != null && info.getHost() != null && conn != null) {
            this.writeLock.lock();
            try {
                List<Session> entity = this.connPool.get(info);
                if (entity == null) {
                    entity = new ArrayList<>();
                    this.connPool.put(info, entity);
                }
                if (entity.indexOf(conn) == -1) {
                    entity.add(conn);
                    Collections.sort(entity);
                    this.writeLock.unlock();
                }
            } finally {
                this.writeLock.unlock();
            }
        }
    }

    public void remove(SessionRequest info, Session conn) {
        this.writeLock.lock();
        try {
            List<Session> entity = this.connPool.get(info);
            if (entity != null) {
                entity.remove(conn);
                if (entity.size() == 0) {
                    this.connPool.remove(info);
                }
                this.writeLock.unlock();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public List<Session> getSessions(SessionRequest info) {
        List<Session> list;
        this.readLock.lock();
        try {
            List<Session> entity = this.connPool.get(info);
            if (entity != null) {
                list = new ArrayList<>(entity);
            } else {
                list = Collections.EMPTY_LIST;
                this.readLock.unlock();
            }
            return list;
        } finally {
            this.readLock.unlock();
        }
    }

    public Session getSession(SessionRequest key) {
        Session select = null;
        this.readLock.lock();
        try {
            List<Session> entity = this.connPool.get(key);
            if (entity == null || entity.isEmpty()) {
                return null;
            }
            Iterator i$ = entity.iterator();
            while (true) {
                if (i$.hasNext()) {
                    Session session = i$.next();
                    if (session != null && session.isAvailable()) {
                        select = session;
                        break;
                    }
                } else {
                    break;
                }
            }
            this.readLock.unlock();
            return select;
        } finally {
            this.readLock.unlock();
        }
    }

    public Session getSession(SessionRequest key, ConnType.TypeLevel typeClass) {
        Session session;
        Session select = null;
        this.readLock.lock();
        try {
            List<Session> entity = this.connPool.get(key);
            if (entity == null || entity.isEmpty()) {
                return null;
            }
            Iterator i$ = entity.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                session = i$.next();
                if (session != null && session.isAvailable()) {
                    if (typeClass == null || session.mConnType.getTypeLevel() == typeClass) {
                        select = session;
                    }
                }
            }
            select = session;
            this.readLock.unlock();
            return select;
        } finally {
            this.readLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public List<SessionRequest> getInfos() {
        List<SessionRequest> infos = Collections.EMPTY_LIST;
        this.readLock.lock();
        try {
            if (this.connPool.isEmpty()) {
                this.readLock.unlock();
                return infos;
            }
            List<SessionRequest> infos2 = new ArrayList<>(this.connPool.keySet());
            this.readLock.unlock();
            return infos2;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    public boolean containsValue(SessionRequest request, Session s) {
        boolean z = false;
        this.readLock.lock();
        try {
            List<Session> entity = this.connPool.get(request);
            if (entity != null) {
                if (entity.indexOf(s) != -1) {
                    z = true;
                }
                this.readLock.unlock();
            }
            return z;
        } finally {
            this.readLock.unlock();
        }
    }
}
