package cn.coderule.common.lang.enums.common;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import cn.coderule.common.util.lang.bean.EnumUtil;
import cn.coderule.common.util.lang.string.StringUtil;

public class CountryUtil {
    public static CountryEnum parse(String name) {
        return parse(name, null);
    }

    public static CountryEnum parse(String name, CountryEnum defaultCountry) {
        if (StringUtil.isBlank(name)) {
            return defaultCountry;
        }

        return EnumUtil.nameOf(name, CountryEnum.class, true);
    }

    public static CodeBasedEnum codeOf(Integer code) {
        if (code == null) {
            return null;
        }

        return EnumUtil.codeOf(code, CountryEnum.class);
    }

    public static String getCurrencyCode(Integer code) {
        if (code == null) {
            return "";
        }

        CodeBasedEnum currency = EnumUtil.codeOf(code, CountryEnum.class);
        return currency.getName();
    }

    public static String getCurrencyCode(CountryEnum currency) {
        if (currency == null) {
            return "";
        }

        return currency.getName();
    }
}
