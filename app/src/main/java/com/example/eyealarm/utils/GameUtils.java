package com.example.eyealarm.utils;

import android.os.Build;

import java.util.Random;
import java.util.stream.IntStream;

public class GameUtils {
    // Function to remove the element
    public static int[] removeTheElement(int[] arr, int index)
    {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // return the resultant array
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return IntStream.range(0, arr.length)
                    .filter(i -> i != index)
                    .map(i -> arr[i])
                    .toArray();
        }
        return arr;
    }

    public static int[] swapArray(int[] arr, int i, int j) {
        arr[i] = (arr[i] + arr[j]) - (arr[j] = arr[i]);
        return arr;
    }

    public static int[] shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }

    public static boolean checkArrayEqual(int[] arr1, int[] arr2 ) {
        int n = arr1.length;
        for (int i=0; i<n; ++i){
            if (arr1[i] != arr2[i]){
                return false;
            }
        }
        return true;
    }

    public static int[] copyArray(int[] arr) {
        int n = arr.length;
        int res[]= new int[n];
        for (int i=0; i<n; ++i) res[i] = arr[i];
        return res;
    }
}

