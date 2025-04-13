package cn.coderule.common.convention.container;

import cn.coderule.common.lang.exception.lang.ClassNotFoundException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.collection.MapUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

@Getter
public class ApplicationContext implements Context {
    private final ConcurrentHashMap<String, List<Object>> objectMap = new ConcurrentHashMap<>();

    public void registerContext(ApplicationContext subContext) {
        ConcurrentHashMap<String, List<Object>> subMap = subContext.getObjectMap();
        if (MapUtil.isEmpty(subMap)) {
            return;
        }

        this.objectMap.putAll(subMap);
    }

    public void register(Object o) {
        addBean(o.getClass().getName(), o);
    }

    public void register(Object o, Class<?> clazz) {
        addBean(clazz.getName(), o);
    }

    public <T> T getBean(Class<T> clazz) {
        return getBean(clazz, true);
    }

    public <T> T getBean(Class<T> clazz, boolean throwNotFoundException) {
        List<Object> list = objectMap.get(clazz.getName());

        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        Object bean = list.get(0);
        if (clazz.isInstance(bean)) {
            return clazz.cast(bean);
        }

        if (!throwNotFoundException) {
            return null;
        }

        throw new ClassNotFoundException(clazz.getName());
    }

    private void addBean(String className, Object bean) {
        initBeanList(className);
        List<Object> list = objectMap.get(className);
        list.add(bean);
    }

    private void initBeanList(String className) {
        if (objectMap.containsKey(className)) {
            return;
        }

        objectMap.put(className, new ArrayList<Object>());
    }

//    public static void main(String[] args) {
//        ApplicationContext context = new ApplicationContext();
//        Person p = new Person();
//
//        context.register(p, Person.class);
//
//        ObjectTree objectTree = new ObjectTree();
//        context.register(objectTree);
//
//        context.getBean(ObjectTree.class);
//    }
//
//    static class Person {
//
//    }
}
