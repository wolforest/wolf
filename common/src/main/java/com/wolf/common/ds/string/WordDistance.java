package com.wolf.common.ds.string;

/**
 * com.wolf.common.lang.string
 *
 * @author Wingle
 * @since 2021/9/16 下午4:49
 **/
public class WordDistance {
    private static final WordDistance INSTANCE = new WordDistance();

    public static int get(String first, String second) {
        return INSTANCE.calculate(first, second);
    }

    public int calculate(String first, String second) {
        if (null == first || null == second) {
            return -1;
        }

        int[][] distance = createDistanceArray(first, second);
        for (int i = 0; i < first.length(); i++) {
            for (int j=0; j<second.length(); j++) {
                int r = first.charAt(i) == second.charAt(j) ? 0 : 1;

                int firstAppend = distance[i][j+1] +1;
                int secondAppend = distance[i+1][j] + 1;
                int replace = distance[i][j] + r;

                int min = Math.min(firstAppend, secondAppend);
                min = Math.min(min, replace);
                distance[i+1][j+1] = min;
            }
        }

        return distance[first.length()][second.length()];
    }

    private int[][] createDistanceArray(String first, String second) {
        int[][] distance = new int[first.length() + 1][second.length() + 1];
        int cursor;

        for (cursor = 0; cursor <= second.length(); cursor++) {
            distance[0][cursor] = cursor;
        }

        for (cursor = 0; cursor <= first.length(); cursor++) {
            distance[cursor][0] = cursor;
        }

        return distance;
    }
}
