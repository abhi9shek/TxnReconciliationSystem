package com.example.TxnReconciliation.utils;

public class StringMatching {

    public StringMatching(){
    }

    public int getDistanceBetweenStrings( String str1, String str2){
        int rows = str1.length() + 1;
        int cols = str2.length() + 1;
        int[][] distance = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j < cols; j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    distance[i][j] = distance[i - 1][j - 1];
                } else {
                    distance[i][j] = Math.min(distance[i - 1][j] + 1,
                            Math.min(distance[i][j - 1] + 1, distance[i - 1][j - 1] + 1));
                }
            }
        }

        return distance[rows - 1][cols - 1];
    }
}
