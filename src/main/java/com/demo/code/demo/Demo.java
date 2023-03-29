package com.demo.code.demo;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jason Q
 * @create 2023-03-23 09:49
 * @Description
 **/
public class Demo {


    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


        boolean b = list.stream().noneMatch(e -> e > 5);
        System.out.println(b);

    }

}
