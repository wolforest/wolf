package cn.coderule.common.lang.type;

import lombok.Data;

/**
 * com.daydayup.learn.hello.common.lang.type
 *
 * @author Wingle
 * @since 2019/7/12 10:30 AM
 **/
@Data
public class Tuple<L, M, R> {
    private final L left;
    private final M middle;
    private final R right;

    public static <L, M, R> Tuple<L, M, R> create(L left, M middle, R right) {
        return new Tuple<>(left, middle, right);
    }

    public Tuple(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }
}
