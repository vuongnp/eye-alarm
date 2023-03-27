package com.example.eyealarm.utils;

public class EyeUtils {
    public static float calculateEuclidean(float x1, float y1, float x2, float y2){
        float euc = (float) Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1)));
        return euc;
    }

    public static float calculateEAR(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float x5, float y5, float x6, float y6){
        //calculate vertical distance
        float v1 = calculateEuclidean(x2, y2, x6, y6);
        float v2 = calculateEuclidean(x3, y3, x5, y5);
        //calculate horizontal distance
        float h1 = calculateEuclidean(x1, y1, x4, y4);

        float EAR = (v1+v2)/h1;

        return EAR;
    }

    public static float []twoEyesEAR(float []all_results){
        float x1_l, y1_l, x2_l, y2_l, x3_l, y3_l, x4_l, y4_l, x5_l, y5_l, x6_l, y6_l, x7_l, y7_l, x8_l, y8_l;
        float x1_r, y1_r, x2_r, y2_r, x3_r, y3_r, x4_r, y4_r, x5_r, y5_r, x6_r, y6_r, x7_r, y7_r, x8_r, y8_r;
        x1_l = all_results[7];
        y1_l = all_results[8];
        x2_l = all_results[9];
        y2_l = all_results[10];
        x3_l = all_results[11];
        y3_l = all_results[12];
        x4_l = all_results[13];
        y4_l = all_results[14];
        x5_l = all_results[15];
        y5_l = all_results[16];
        x6_l = all_results[17];
        y6_l = all_results[18];
        x7_l = all_results[19];
        y7_l = all_results[20];
        x8_l = all_results[21];
        y8_l = all_results[22];


        x1_r = all_results[25];
        y1_r = all_results[26];
        x2_r = all_results[27];
        y2_r = all_results[28];
        x3_r = all_results[29];
        y3_r = all_results[30];
        x4_r = all_results[31];
        y4_r = all_results[32];
        x5_r = all_results[33];
        y5_r = all_results[34];
        x6_r = all_results[35];
        y6_r = all_results[36];
        x7_r = all_results[37];
        y7_r = all_results[38];
        x8_r = all_results[39];
        y8_r = all_results[40];

        float left_EAR = calculateEAR(x1_l, y1_l, x2_l, y2_l, x4_l, y4_l, x5_l, y5_l, x6_l, y6_l, x8_l, y8_l);
        float right_EAR = calculateEAR(x1_r, y1_r, x2_r, y2_r, x4_r, y4_r, x5_r, y5_r, x6_r, y6_r, x8_r, y8_r);
        float[] EARs = {left_EAR, right_EAR};

        return EARs;
    }

    public static int widenTwoEyes(float []EARs, float blink_thres){
        if (EARs[0] >= blink_thres && EARs[1] >= blink_thres){
            return 1;
        }else{
            return 0;
        }
    }

    public static int widenLeftEye(float []EARs, float blink_thres){
        if (EARs[0] >= blink_thres && EARs[1] < blink_thres){
            return 1;
        }else{
            return 0;
        }
    }

    public static int widenRightEye(float []EARs, float blink_thres){
        if (EARs[0] < blink_thres && EARs[1] >= blink_thres){
            return 1;
        }else{
            return 0;
        }
    }
    public static int closeTwoEyes(float []EARs, float blink_thres){
        if (EARs[0] < blink_thres && EARs[1] < blink_thres){
            return 1;
        }else{
            return 0;
        }
    }
}

