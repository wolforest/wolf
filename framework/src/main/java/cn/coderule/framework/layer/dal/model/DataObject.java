package cn.coderule.framework.layer.dal.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * com.wolf.framework.layer.dal
 *
 * @author Wingle
 * @since 2019/10/29 12:22 上午
 **/
@Data
public class DataObject {
    private long id;
    private boolean deleteFlag;
    private int version;

    private long creator;
    private long lastEditor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
