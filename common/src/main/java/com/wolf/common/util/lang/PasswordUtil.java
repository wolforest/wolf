package com.wolf.common.util.lang;

public class PasswordUtil {

    public static boolean validWeekPassword(String password) {
        return StringUtil.isNoneBlank(password) && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&-_])[A-Za-z\\d$@$!%*?&-_]{8,20}");
    }





}
