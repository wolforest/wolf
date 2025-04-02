package cn.coderule.framework.layer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * com.wolf.framework.rpc.page
 *
 * @author Wingle
 * @since 2020/1/4 5:09 下午
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Request {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_NUM = 1;

    private Integer pageSize = DEFAULT_PAGE_SIZE;
    private Integer current;

    /**
     * sort order:
     * 0: default value 0 | null
     * 1: asc
     * 2: desc
     */
    private Integer order;

    /**
     * it is not safe to
     */
    private String orderBy;

    public void init() {
        if (current == null) {
            current = DEFAULT_PAGE_NUM;
        }

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
    }

    public Integer getCurrent() {
        if (current != null) {
            return current;
        }

        return DEFAULT_PAGE_NUM;
    }

    public Integer getPageSize() {
        if (pageSize != null) {
            return pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
        }

        return DEFAULT_PAGE_SIZE;
    }

    public Long getOffset() {
        return (getCurrent() - 1) * getPageSize().longValue();
    }
}
