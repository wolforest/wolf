package cn.coderule.common.util.encrypt;

import cn.coderule.common.util.lang.string.StringUtil;

public class PasswordUtil {
    public static boolean validWeekPassword(String password) {
        return StringUtil.isNoneBlank(password)
            && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&-_])[A-Za-z\\d$@$!%*?&-_]{8,20}");
    }





}
