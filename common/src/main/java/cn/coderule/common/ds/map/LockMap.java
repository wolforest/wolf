package cn.coderule.common.ds.map;

import cn.coderule.common.lang.exception.SystemException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockMap {
    private final ConcurrentHashMap<String, ReentrantLock> map = new ConcurrentHashMap<>();

    public void addLock(Collection<String> keys) {
        for (String key : keys) {
            boolean result = addLock(key);
            if (!result) {
                throw new SystemException("Add lock failed, key:" + key);
            }
        }
    }

    public void addLock(String ...keys) {
        for (String key : keys) {
            boolean result = addLock(key);
            if (!result) {
                throw new SystemException("Add lock failed, key:" + key);
            }
        }
    }

    public boolean addLock(String key) {
        ReentrantLock result = map.putIfAbsent(key, new ReentrantLock());

        return result == null;
    }

    public boolean addLock(String key, ReentrantLock lock) {
        ReentrantLock result = map.putIfAbsent(key, lock);

        return result == null;
    }

    public boolean tryLock(String key) {
        ReentrantLock lock = map.get(key);
        if (lock == null) {
            lock = new ReentrantLock();
            if (!addLock(key, lock)) {
                return false;
            }
        }

        return lock.tryLock();
    }

    public void unlock(String key) {
        ReentrantLock lock = map.get(key);
        if (lock == null) {
            throw new SystemException("Can't find lock for key:" + key);
        }

        if (!lock.isLocked()) {
            throw new SystemException("Can't unlock lock for key:" + key);
        }

        lock.unlock();
    }

}
