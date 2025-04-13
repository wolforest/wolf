package cn.coderule.common.util.lang.collection.lb;

import cn.coderule.common.util.lang.StringUtil;
import lombok.NonNull;
import cn.coderule.common.lang.exception.BusinessException;
import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.common.util.lang.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * com.wolf.common.util.lb
 *
 * @author Wingle
 * @since 2020/12/10 7:51 下午
 **/
public class WeightBalance {
    private boolean hasInit;
    private final List<Node> nodeList;

    private int totalWeight;
    private int averageWeight;

    public WeightBalance() {
        nodeList = new ArrayList<>(8);
        totalWeight = 0;
        averageWeight = 0;
    }

    public WeightBalance add(@NonNull Object key, Integer weight) {
        Node node = Node.create(key, weight);
        nodeList.add(node);

        return this;
    }

    public Object get() {
        init();

        if (1 == nodeList.size()) {
            return nodeList.get(0).getKey();
        }

        int weight = ThreadLocalRandom.current().nextInt(0, totalWeight);
        return locate(weight);
    }

    public void showWeightList() {
        if (CollectionUtil.isEmpty(nodeList)) {
            System.out.println("empty weight list");
            return;
        }

        for (Node node : nodeList) {
            System.out.println(
                    StringUtil.joinWith(":",
                            node.getKey(),
                            node.getWeight(),
                            node.getStart(),
                            node.getEnd()
                            )
            );
        }
    }

    public void init() {
        if (hasInit) {
            return;
        }
        hasInit = true;

        if (CollectionUtil.isEmpty(nodeList)) {
            throw new BusinessException("channel is not configured");
        }

        calculate();
    }

    private void calculate() {
        averageWeight = calculateAverageWeight();
        initDefaultWeight(averageWeight);
        calculateOffset();
    }

    private int calculateAverageWeight() {
        int weightSize = 0;
        int nodeWeight;

        for (Node node : nodeList) {
            nodeWeight = node.getWeight();
            if (nodeWeight <= 0) {
                continue;
            }

            totalWeight += nodeWeight;
            weightSize++;
        }

        if (0 == totalWeight || weightSize == 0) {
            totalWeight = weightSize;
            return 1;
        }

        return  (int)Math.round((double) totalWeight/weightSize);
    }

    private void initDefaultWeight(int averageWeight) {
        int nodeWeight;
        for (Node node : nodeList) {
            nodeWeight = node.getWeight();
            if (nodeWeight > 0) {
                continue;
            }
            node.setWeight(averageWeight);
            totalWeight += averageWeight;
        }
    }

    private void calculateOffset() {
        int current = 0;
        int nodeWeight;
        int end;
        for (Node node : nodeList) {
            nodeWeight = node.getWeight();

            node.setStart(current);
            end = current + nodeWeight - 1;
            node.setEnd(end);

            current = end + 1;
        }
    }

    private Object locate(int weight) {
        for (Node node : nodeList) {
            if (weight >= node.getStart() && weight <= node.getEnd()) {
                return node.getKey();
            }
        }

        throw new SystemException("fail to locate in the WeightBalance");
    }
}
