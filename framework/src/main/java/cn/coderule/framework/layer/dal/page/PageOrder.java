package cn.coderule.framework.layer.dal.page;

import java.io.Serializable;
import lombok.Data;

/**
 * com.wolf.framework.rpc.page
 *
 * @author Wingle
 * @since 2020/1/4 5:13 下午
 **/
@Data
public class PageOrder implements Serializable {
    private String orderBy;
    private Long orderValue;
}
