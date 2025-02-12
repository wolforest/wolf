package cn.coderule.common.util.lang.collection.lb;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * com.wolf.common.util.lb.weight
 *
 * @author Wingle
 * @since 2020/12/10 8:00 下午
 **/
@Data
public class Node implements Serializable {
    private Object key;
    private int weight;

    private int start;
    private int end;

    public static Node create(@NonNull Object key, Integer weight) {
        if (null != weight && weight < 0){
            throw new IllegalArgumentException("weight can't less than 0");
        }

        Node node = new Node();
        node.setKey(key);
        if (null == weight || 0 == weight) {
            node.setWeight(-1);
        } else {
            node.setWeight(weight);
        }

        return node;
    }
}
