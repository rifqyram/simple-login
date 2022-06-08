package com.example.simplelogin;

import java.util.Arrays;

public class Permutation {

    public static void main(String[] args) {
        try {
            int[] numbers = arrayNumber(4);
            int[] pairNumber = chkPair(numbers);
            permute(pairNumber);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int[] arrayNumber(int number) {
        if (number < 4 || number > 6)
            throw new IllegalArgumentException("Error: Minimum number is 4 and Maximum number is 6");
        int[] array = new int[number];
        for (int i = 0; i < number; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    static int[] chkPair(int[] a) {
        for (int i = 0; i < (a.length - 1); i++) {
            for (int j = (i + 1); j < a.length; j++) {
                if (a[i] + a[j] == a.length) {
                    int[] arr = new int[a.length];
                    arr[i] = 0;
                    arr[i] = a[i];
                    arr[j] = a[j];
                    return arr;
                }
            }
        }
        return a;
    }

    static void permute(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                while (arr[i] < 9) {
                    arr[i]++;
                    System.out.println(Arrays.toString(arr));
                }
            }
        }
    }
}
