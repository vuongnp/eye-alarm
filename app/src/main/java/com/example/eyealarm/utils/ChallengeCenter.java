package com.example.eyealarm.utils;

import android.util.Log;

public class      ChallengeCenter {
    public static int challengeManager(float []all_results, int challenge_id){
        float[] EARs = EyeUtils.twoEyesEAR(all_results);
        String ears = "Left  " + String.valueOf(EARs[0]) + "- Right  "+ String.valueOf(EARs[1]);
        Log.d("EARs", ears);
        switch (challenge_id){
            case 1:
                return EyeUtils.widenTwoEyes(EARs, 0.8f);
            case 2:
                return EyeUtils.closeTwoEyes(EARs, 0.4f);
            case 3:
                return EyeUtils.widenRightEye(EARs, 0.4f);
            case 4:
                return EyeUtils.widenLeftEye(EARs, 0.4f);
            default:
                return 0;
        }
//        return 0;
    }
}
