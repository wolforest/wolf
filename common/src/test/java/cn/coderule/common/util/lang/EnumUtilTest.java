package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.bean.EnumUtil;
import org.junit.jupiter.api.Test;
import cn.coderule.common.lang.enums.common.GenderEnum;
import cn.coderule.common.lang.exception.lang.UnsupportedEnumCodeException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertTrue(male.equals(unknown), "EnumUtil.codeOf() get code fail");
    }

    @Test
    public void test_get_name_list_work() {
        List<String> nameList = EnumUtil.getNameList(Color.class);

        assertEquals(2, nameList.size(), "EnumUtil.getNameList failed");
        assertTrue(nameList.contains("RED"), "EnumUtil.getNameList failed");
        assertTrue(nameList.contains("BLUE"), "EnumUtil.getNameList failed");
        assertFalse(nameList.contains("WHITE"), "EnumUtil.getNameList failed");
    }

    enum Color {
        RED, BLUE
    }

    @Test
    public void test_code_of_throws_exception_work() {
        assertThrows(UnsupportedEnumCodeException.class, () -> EnumUtil.codeOf(11, GenderEnum.class));
    }
}
