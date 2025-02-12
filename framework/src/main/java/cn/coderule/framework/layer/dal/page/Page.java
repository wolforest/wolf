package cn.coderule.framework.layer.dal.page;

import com.github.pagehelper.PageHelper;
import lombok.NonNull;

/**
 * com.wolf.framework.rpc
 *
 * @author Wingle
 * @since 2020/1/4 4:27 下午
 **/
public class Page {

    public static void startPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
    }

    public static void startPage(int pageNum, int pageSize, boolean count) {
        PageHelper.startPage(pageNum, pageSize, count);
    }

    public static void offsetPage(int offset, int limit, boolean count) {
        PageHelper.offsetPage(offset, limit, count);
    }

    public static void offsetPage(int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
    }


}
