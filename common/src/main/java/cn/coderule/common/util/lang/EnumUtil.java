package cn.coderule.common.util.lang;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import cn.coderule.common.lang.exception.lang.UnsupportedEnumCodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/9/29 10:22 PM
 **/
public class EnumUtil {
    public static <T extends CodeBasedEnum> T codeOf(int code, Class<T> enumType) {
        return codeOf(code, enumType, null);
    }

    public static <T extends CodeBasedEnum> T codeOf(int code, Class<T> enumType, T defaultValue) {
        for (T e : enumType.getEnumConstants()) {
            if (code == e.getCode()) {
                return e;
            }
        }

        if (null != defaultValue) {
            return defaultValue;
        }

        throw new UnsupportedEnumCodeException("code: " + code + " is not supported");
    }

    public static <T extends CodeBasedEnum> T codeOfAllowReturnNull(Integer code, Class<T> enumType) {
        if (code == null) {
            return null;
        }
        for (T e : enumType.getEnumConstants()) {
            if (code == e.getCode()) {
                return e;
            }
        }

        return null;
    }

    public static <T extends CodeBasedEnum> T nameOfAllowReturnNull(String name, Class<T> enumType) {
        if(StringUtil.isBlank(name)){
            return null;
        }
        for (T e : enumType.getEnumConstants()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }

        return null;
    }

    public static <T extends CodeBasedEnum> T nameOf(String name, Class<T> enumType) {
        return nameOf(name, enumType, false);
    }

    public static <T extends CodeBasedEnum> T nameOf(String name, Class<T> enumType, boolean ignoreCase) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("name of enum can't be blank");
        }

        name = name.trim();
        for (T e : enumType.getEnumConstants()) {
            if (ignoreCase && e.getName().equalsIgnoreCase(name)) {
                return e;
            }

            if (!ignoreCase && e.getName().equals(name)) {
                return e;
            }
        }

        throw new UnsupportedEnumCodeException("name: " + name + " is not supported");
    }

    public static <T extends CodeBasedEnum> List<String> toNameList(Class<T> enumType) {
        List<String> nameList = new ArrayList<>();

        for (T e : enumType.getEnumConstants()) {
            nameList.add(e.getName());
        }

        return nameList;
    }

    @SuppressWarnings("all")
    public static <T extends Enum> List<String> getNameList(Class<T> enumType) {
        List<String> nameList = new ArrayList<>();

        for (T e : enumType.getEnumConstants()) {
            nameList.add(e.name());
        }

        return nameList;
    }

    public static <T extends CodeBasedEnum> Map<String, Integer> toNameCodeMap(Class<T> enumType) {
        Map<String, Integer> nameMap = new HashMap<>();

        for (T e : enumType.getEnumConstants()) {
            nameMap.put(e.getName(), e.getCode());
        }

        return nameMap;
    }

    public static <T extends CodeBasedEnum> Map<String, T> toNameMap(Class<T> enumType) {
        Map<String, T> nameMap = new HashMap<>();

        for (T e : enumType.getEnumConstants()) {
            nameMap.put(e.getName(), e);
        }

        return nameMap;
    }

    public static Integer getCode(CodeBasedEnum enumELe) {
        if (enumELe == null) {
            return null;
        }
        return enumELe.getCode();
    }

    public static String getName(CodeBasedEnum enumELe) {
        if (enumELe == null) {
            return null;
        }
        return enumELe.getName();
    }
}
