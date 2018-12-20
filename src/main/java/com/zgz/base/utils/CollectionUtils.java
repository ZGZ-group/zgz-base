package com.zgz.base.utils;

import java.util.*;

public class CollectionUtils {

    /**
     * 判断集合是否为空
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }

    public static <T, R> boolean mapIsEmpty(Map<T, R> map) {
        return null == map || map.size() == 0;
    }

    /**
     * HashMap按照值来排序,升序
     *
     * @param map map
     */
    public static <T, R extends Comparable<? super R>> Map<T, R> mapSortedByValue(Map<T, R> map) {

        if (mapIsEmpty(map)) {
            return Collections.emptyMap();
        }

        return mapSortedByValue(map, Boolean.TRUE);

    }

    /**
     * HashMap按照值来排序
     *
     * @param map  map
     * @param flag true-升序,false-降序
     */
    public static <T, R extends Comparable<? super R>> Map<T, R> mapSortedByValue(Map<T, R> map, boolean flag) {

        if (mapIsEmpty(map)) {
            return Collections.emptyMap();
        }

        List<Map.Entry<T, R>> entryList = new ArrayList<>(map.entrySet());

        entryList.sort(Comparator.comparing(Map.Entry::getValue));//(e, f) -> e.getValue().compareTo(f.getValue())

        Map<T, R> result = new LinkedHashMap<>();
        entryList.forEach(e -> result.put(e.getKey(), e.getValue()));

        if (!flag) {

            result.clear();
            //逆序遍历
            ListIterator<Map.Entry<T, R>> entryListIterator = entryList.listIterator(entryList.size());
            while (entryListIterator.hasPrevious()) {
                Map.Entry<T, R> previous = entryListIterator.previous();

                result.put(previous.getKey(), previous.getValue());
            }

        }

        return result;
    }
}
