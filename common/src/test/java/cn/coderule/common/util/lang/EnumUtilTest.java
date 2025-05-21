package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.bean.EnumUtil;
import org.junit.Test;
import cn.coderule.common.lang.enums.common.GenderEnum;
import cn.coderule.common.lang.exception.lang.UnsupportedEnumCodeException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/9/30 11:18 AM
 **/
public class EnumUtilTest {

    @Test
    public void test_code_of_work_fine() {
        GenderEnum male = GenderEnum.MALE;
        GenderEnum unknown = EnumUtil.codeOf(1, GenderEnum.class);

        assertTrue("EnumUtil.codeOf() get code fail", male.equals(unknown));
    }

    @Test
    public void test_get_name_list_work() {
        List<String> nameList = EnumUtil.getNameList(Color.class);

        assertEquals("EnumUtil.getNameList failed", 2, nameList.size());
        assertTrue("EnumUtil.getNameList failed", nameList.contains("RED"));
        assertTrue("EnumUtil.getNameList failed", nameList.contains("BLUE"));
        assertFalse("EnumUtil.getNameList failed", nameList.contains("WHITE"));
    }

    enum Color {
        RED, BLUE
    }

    @Test(expected = UnsupportedEnumCodeException.class)
    public void test_code_of_throws_exception_work() {
        GenderEnum unknow = EnumUtil.codeOf(11, GenderEnum.class);
    }
}
