package cn.coderule.common.util.lang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import cn.coderule.common.lang.exception.lang.ClassNotFoundException;
import cn.coderule.common.lang.exception.lang.NullPointerException;
import cn.coderule.common.util.lang.collection.CollectionUtil;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Stream;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/2/14 5:44 下午
 **/
@Slf4j
public class BeanUtil {
    public static void checkNotNull(Object a) {
        if (a != null) {
            return;
        }

        throw new NullPointerException();
    }

    public static boolean isTrue(Boolean data) {
        return data != null && data;
    }

    public static boolean notTrue(Boolean data) {
        return data == null || !data;
    }

    public static boolean equals(Object a, Object b) {
        if (a == null) {
            return null == b;
        }

        if (a == b) {
            return true;
        }

        return a.equals(b);
    }

    public static int diff(Integer left, Integer right) {
        if (left == null && null == right) {
            return -1;
        }

        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        int diff = left - right;
        if (0 == diff) {
            return diff;
        }

        return diff > 0 ? diff : -1 * diff;

    }

    public static boolean hasZero(Integer... arr) {
        for (Integer i : arr) {
            if (0 == i) {
                return true;
            }
        }

        return false;
    }

    public static boolean inArray(Object target, Object... arr) {
        for (Object o : arr) {
            if (equals(target, o)) {
                return true;
            }
        }

        return false;
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        }

        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (null != field.get(object)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            return true;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            Class<?> c = Class.forName(className);
            return (T) c;
        } catch (java.lang.ClassNotFoundException e) {
            throw new ClassNotFoundException(className);
        }
    }

    public static boolean isSimpleObject(Object o) {
        if (o instanceof Number) {
            return true;
        }

        if (o instanceof CharSequence) {
            return true;
        }

        if (o instanceof TemporalAccessor) {
            return true;
        }

        if (o instanceof Date) {
            return true;
        }

        return o instanceof Boolean;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> {
                    try {
                        return wrappedSource.getPropertyValue(propertyName) == null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toArray(String[]::new);
    }

    public static void copyProperties(@NonNull Object source, Object target) {
        copyProperties(source, target, true);
    }

    public static void copyProperties(@NonNull Object source, Object target, Collection<String> ignoreKeys) {
        if (CollectionUtil.isEmpty(ignoreKeys)) {
            copyProperties(source, target);
            return;
        }

        String[] keys = new String[ignoreKeys.size()];
        ignoreKeys.toArray(keys);
        BeanUtils.copyProperties(source, target, keys);
    }

    public static void copyProperties(@NonNull Object source, Object target, boolean filterNulls, Collection<String> ignoreKeys) {
        if (!filterNulls) {
            copyProperties(source, target, ignoreKeys);
            return;
        }

        if (CollectionUtil.isEmpty(ignoreKeys)) {
            copyProperties(source, target, true);
            return;
        }

        String[] nullKeys = getNullPropertyNames(source);
        String[] iKeys = new String[ignoreKeys.size()];
        ignoreKeys.toArray(iKeys);

        String[] keys = new String[ignoreKeys.size() + ignoreKeys.size()];
        System.arraycopy(nullKeys, 0, keys, 0, nullKeys.length);
        System.arraycopy(iKeys, 0, keys, 0, iKeys.length);

        BeanUtils.copyProperties(source, target, keys);
    }

    public static void copyProperties(@NonNull Object source, Object target, boolean filterNulls) {
        if (filterNulls) {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } else {
            BeanUtils.copyProperties(source, target);
        }
    }

    public static void nullToBlank(@NonNull Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Object value;
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);

            try {
                value = field.get(target);
                if (null != value) {
                    continue;
                }

                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    field.set(target, "");
                }

            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void trim(@NonNull Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        Object value;
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);

            try {
                value = field.get(target);
                if (null == value) {
                    continue;
                }

                if (value instanceof String) {
                    field.set(target, ((String) value).trim());
                }

            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static <T> Map<String, Object> toMap(@NonNull Class<T> clazz) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(clazz));
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            return map;
        }

        return map;
    }

    public static <T> ArrayList<String> getPropertyNames(@NonNull Class<T> clazz) {
        ArrayList<String> properties = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                properties.add(field.getName());
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            return properties;
        }

        return properties;
    }

    public static Map<String, Object> toMap(@NonNull Object object) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            log.error("BeanUtil toMap Error:", e);
            return map;
        }

        return map;
    }

    public static Map<String, Object> toNestedMap(@NonNull Object object) {
        Map<String, Object> map = new HashMap<>();

        List<Field> fieldList = new ArrayList<>();
        Class tempClass = object.getClass();
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        try {
            for (Field field : fieldList) {
                String fieldName = field.getName();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
                Method readMethod = propertyDescriptor.getReadMethod();
                Object value = readMethod.invoke(object);
                if (value == null) {
                    continue;
                }

                if (isCustomClass(value)) {
                    map.put(fieldName, toNestedMap(value));
                    continue;
                }

                map.put(fieldName, value);
            }
        } catch (Throwable throwable) {
            log.warn("BeanUtil toNestedMap error; ", throwable);
        }

        return map;
    }

    public static boolean isCustomClass(Object obj) {
        Class<?> clazz = obj.getClass();

        // 排除基础类型和标准类
        return !clazz.isPrimitive() && !clazz.getName().startsWith("java.");
    }

    @SneakyThrows
    public static <T> T toBean(@NonNull Map<String, Object> map, Class<T> beanClass) {
        T object;
        try {
            object = beanClass.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    field.set(object, map.get(field.getName()));
                }
            }
        } catch (IllegalAccessException e) {
            return null;
        }

        return object;
    }

    public static String toString(Object o) {
        if (o == null) {
            return "";
        }

        return o.toString();
    }

    public static boolean onlyNonNullFields(Object object, String... targetFields) {
        List<String> targetFieldList = targetFields == null ? new ArrayList<>() : Arrays.asList(targetFields);
        if (object == null) {
            return false;
        }
        if (targetFieldList == null || targetFieldList.isEmpty()) {
            return false;
        }

        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (targetFieldList.contains(field.getName())) {
                if (isNullOrBlank(field, object)) {
                    return false;
                }
            } else {
                if (!isNullOrBlank(field, object)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isNullOrBlank(Field field, Object object) {
        Class type = field.getType();
        try {
            field.setAccessible(true);
            Object value = field.get(object);

            if (type == String.class) {
                return StringUtil.isBlank(value);
            }
            return value == null;
        } catch (IllegalAccessException e) {
            return true;
        }
    }


    public static String toString(final Properties properties) {
        return toString(properties, false);
    }

    public static String toString(final Properties properties, final boolean isSort) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Object, Object>> entrySet = isSort ? new TreeMap<>(properties).entrySet() : properties.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            if (entry.getValue() == null) {
                continue;
            }

            sb.append(entry.getKey().toString())
                .append("=")
                .append(entry.getValue().toString())
                .append("\n");
        }
        return sb.toString();
    }

    public static Properties toProperties(final String str) {
        Properties properties = new Properties();
        try {
            InputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            properties.load(in);
        } catch (Exception e) {
            log.error("Failed to handle properties", e);
            return null;
        }

        return properties;
    }

    public static Properties toProperties(final Object object) {
        Properties properties = new Properties();

        Class<?> objectClass = object.getClass();
        while (true) {
            toProperties(object, objectClass, properties);

            if (objectClass == Object.class || objectClass.getSuperclass() == Object.class) {
                break;
            }
            objectClass = objectClass.getSuperclass();
        }

        return properties;
    }

    private static void toProperties(final Object object, final Class<?> objectClass, Properties properties) {
        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String name = field.getName();
            if (name.startsWith("this")) {
                continue;
            }

            Object value = null;
            try {
                field.setAccessible(true);
                value = field.get(object);
            } catch (IllegalAccessException e) {
                log.error("Failed to handle properties", e);
            }

            if (value != null) {
                properties.setProperty(name, value.toString());
            }
        }
    }

    public static void toObject(final Properties p, final Object object) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String mn = method.getName();
            if (!mn.startsWith("set")) {
                continue;
            }

            try {
                String tmp = mn.substring(4);
                String first = mn.substring(3, 4);

                String key = first.toLowerCase() + tmp;
                String property = p.getProperty(key);
                if (property == null) {
                    continue;
                }

                Class<?>[] pt = method.getParameterTypes();
                if (pt.length <= 0) {
                    continue;
                }

                String cn = pt[0].getSimpleName();
                Object arg = getArg(cn, property);
                if (arg == null) {
                    continue;
                }

                method.invoke(object, arg);
            } catch (Throwable ignored) {
            }
        }
    }

    private static Object getArg(String cn, String property) {
        Object arg;
        switch (cn) {
            case "int":
            case "Integer":
                arg = Integer.parseInt(property);
                break;
            case "long":
            case "Long":
                arg = Long.parseLong(property);
                break;
            case "double":
            case "Double":
                arg = Double.parseDouble(property);
                break;
            case "boolean":
            case "Boolean":
                arg = Boolean.parseBoolean(property);
                break;
            case "float":
            case "Float":
                arg = Float.parseFloat(property);
                break;
            case "String":
                property = property.trim();
                arg = property;
                break;
            default:
                return null;
        }

        return arg;
    }

    public static boolean isPropertiesEqual(final Properties p1, final Properties p2) {
        return p1.equals(p2);
    }

    public static boolean isPropertyValid(Properties props, String key, Predicate<String> validator) {
        return validator.test(props.getProperty(key));
    }

    public static CompareToBuilder compareToBuilder() {
        return new CompareToBuilder();
    }

    public static EqualsBuilder equalsBuilder() {
        return new EqualsBuilder();
    }

    public static HashCodeBuilder hashCodeBuilder() {
        return new HashCodeBuilder();
    }

    public static HashCodeBuilder hashCodeBuilder( int initialOddNumber, int multiplierOddNumber) {
        return new HashCodeBuilder(initialOddNumber, multiplierOddNumber);
    }
}
