package com.zgz.base.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DoubleUtils {

    public static final Double ZERO = 0.0;
    public static final Double ONE = 1.0;

    public static final int DEFAULT_RATIO_SCALE = 4;
    public static final int DEFAULT_VALUE_SCALE = 2;

    /**
     * 根据数值计算该值在数据中所占比例，精度按默认值2位，比例4位
     */
    public static <T> Map<T, Double> calculate(Map<T, Double> map) {

        if (CollectionUtils.mapIsEmpty(map)) {
            return Collections.emptyMap();
        }

        return calculate(map, DEFAULT_VALUE_SCALE, DEFAULT_RATIO_SCALE);
    }

    /**
     * 根据数值计算该值在数据中所占比例
     *
     * @param valueScale 值精度
     * @param ratioScale 比例精度
     */
    public static <T> Map<T, Double> calculate(Map<T, Double> map, int valueScale, int ratioScale) {

        if (CollectionUtils.mapIsEmpty(map)) {
            return Collections.emptyMap();
        }

        final Map<T, Double> resultMap = new HashMap<>();//直接计算比例的结果集

//        map.values().stream().mapToDouble(e -> e).sum();直接使用DoubleStream做加法，精度需确认

//        map.entrySet().forEach(e -> {
//            Optional.ofNullable(e.getValue()).orElseGet(ZERO);
//        });

        Optional<Double> sum = map.values().stream().reduce((e, f) -> BigDecimalUtils.add(e, f, valueScale));

        sum.ifPresent(e -> {
            map.forEach((m, n) -> {
                Double ratio = BigDecimalUtils.divide(n, e, ratioScale);
                resultMap.put(m, ratio);
            });
        });

        return adjustRatio(resultMap, ratioScale);
    }

    /**
     * 调整比例和为1
     *
     * @param resultMap 粗略计算后的比例
     * @param scale     比例精度
     */
    private static <T> Map<T, Double> adjustRatio(Map<T, Double> resultMap, int scale) {
        if (CollectionUtils.mapIsEmpty(resultMap)) {
            return Collections.emptyMap();
        }

        Optional<Double> sumRatio = resultMap.values().stream().reduce((e, f) -> BigDecimalUtils.add(e, f, scale));

        sumRatio.ifPresent(e -> {
            double sub = BigDecimalUtils.subtract(ONE, e, scale);
            double base = Math.pow(10, scale * -1);

            if (sub < 0) {
                base = base * (-1);
            }

            Map<T, Double> sortedByValue = CollectionUtils.mapSortedByValue(resultMap, Boolean.FALSE);//比例降序

            int count = Math.abs((int) (sub / base));
            int i = 0;

//            sortedByValue.entrySet().forEach(k -> {
            for (Map.Entry<T, Double> entry : sortedByValue.entrySet()) {
                if (i < count) {
                    entry.setValue(BigDecimalUtils.add(entry.getValue(), base, scale));
                    i++;
                    continue;
                }
                break;
            }

        });

        return resultMap;
    }

}
