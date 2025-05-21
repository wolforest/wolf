package cn.coderule.framework.layer.api.result;

import com.github.pagehelper.Page;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.bean.BeanUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class PageResult<T> extends Result<T> implements Serializable {
    private List<T> list;

    private long total;
    private int pageSize;
    private int current;

    private int pages;
    private boolean hasNextPage;
    private boolean hasPrePage;

    private long minId;
    private long maxId;

    public PageResult() {}

    public boolean isEmpty() {
        if (!this.success) {
            return true;
        }

        return CollectionUtil.isEmpty(this.list);
    }

    public void initMinAndMaxId() {

    }

    public void list(List<T> list) {
        this.list = list;
    }

    public static <T> PageResult<T> empty() {
        List<T> list = new ArrayList<>();
        return of(list);
    }

    public static <T> PageResult<T> of(T data) {
        List<T> list = new ArrayList<>();
        list.add(data);

        return of(list);
    }

    public static <T> PageResult<T> of(List<T> list) {
        if (list instanceof Page) {
            return of((Page<T>) list);
        }

        int total = list.size();
        return PageResult.<T>builder()
            .success(true)
            .code(DEFAULT_SUCCESS_CODE)
            .codeType(SUCCESS_CODE_TYPE)
            .message(DEFAULT_SUCCESS_MESSAGE)

            .list(list)
            .total(total)
            .pageSize(total)
            .pages(1)
            .current(1)
            .hasNextPage(false)
            .hasPrePage(false)
            .build();
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return PageResult.<T>builder()
            .success(true)
            .code(DEFAULT_SUCCESS_CODE)
            .codeType(SUCCESS_CODE_TYPE)
            .message(DEFAULT_SUCCESS_MESSAGE)

            .total(page.getTotal())
            .pageSize(page.getPageSize())
            .current(page.getPageNum())
            .pages(page.getPages())
            .hasNextPage(page.getPages() > page.getPageNum())
            .hasPrePage(page.getPages() > 0 && page.getPageNum() > 1)
            .list(page)
            .build() ;
    }

    public <M> PageResult<M> to(List<M> list) {
        PageResult<M> newPage = new PageResult<>();
        BeanUtil.copyProperties(this, newPage);

        newPage.setList(list);
        return newPage;
    }
}
