package com.wolf.common.lang.enums.country;

import com.wolf.common.lang.enums.CodeBasedEnum;
import com.wolf.common.util.lang.EnumUtil;
import com.wolf.common.util.lang.StringUtil;

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
