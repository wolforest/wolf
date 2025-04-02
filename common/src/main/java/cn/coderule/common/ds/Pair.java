package cn.coderule.common.ds;

import java.io.Serializable;
import lombok.Data;

/**
 * com.wolf.common.lang.ds
 *
 * @author Wingle
 * @since 2020/12/2 11:58 上午
 **/
@Data
public class Pair<L, R> implements Serializable {
    private final L left;
    private final R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }


}
