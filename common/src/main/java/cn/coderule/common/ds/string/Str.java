package cn.coderule.common.ds.string;

import cn.coderule.common.util.lang.StringUtil;

import javax.annotation.Nullable;

/**
 * com.wolf.common.lang.string
 *
 * @author Wingle
 * @since 2019/12/12 3:34 下午
 **/
@Deprecated
public class Str {
    public static String join(String f, @Nullable Object s, Object... r) {
        return StringUtil.joinWith(StringUtil.BLANK, f, s, r);
    }

}
