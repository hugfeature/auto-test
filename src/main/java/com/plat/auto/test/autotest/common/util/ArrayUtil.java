/*
 * @Author: 丑牛
 * @Date: 2021-10-26 15:00:52
 * @LastEditors: 丑牛
 * @LastEditTime: 2021-10-26 15:27:08
 * @Description: 数组工具类
 */
package com.plat.auto.test.autotest.common.util;

import java.lang.reflect.Array;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ArrayUtil {
    public static final int INDEX_NOT_FOUND = -1;

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    public static boolean isNotEmpt (final Object[] array){
        return !isEmpty(array);
    }
    public static int indexOf(final Object[] array, final Object objectWant){
        if (isEmpty(array)) {
            log.info(array + "数组为空");
            return INDEX_NOT_FOUND;
        }
        for (int i = 0; i < array.length; i++) {
            if ((objectWant == null && array[i] == null) || (objectWant != null && objectWant.equals(array[i]))) {
                return i;
            }
        }
        log.info("数组中未查询到：", objectWant);
        return INDEX_NOT_FOUND;
    }

    public static boolean contains(final Object[] array, final Object objectWant){
        return indexOf(array, objectWant) != INDEX_NOT_FOUND;
    }

    public static void main(String[] args) {
        System.out.println(isEmpty(new String[]{"a", "b", "c"}));
    }
}
