package cn.coderule.framework.layer.dal.db;

public class ShardingUtil {
    public static int DEFAULT_SHARDING_NUM = 1024;

    public static int calculate(long id) {
        return calculate(id, DEFAULT_SHARDING_NUM);
    }

    public static int calculate(long id, int total) {
        return (int) id % total;
    }
}
