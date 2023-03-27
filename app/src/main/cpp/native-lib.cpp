//
// Created by AWL VN on 07/02/2023.
//

#include <jni.h>
#include <string>
#include <iostream>
#include <fstream>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include "android/bitmap.h"
#include <chrono>
#include <ctime>
#include "pipeline.hpp"
#include "modules/utils/funcs.hpp"
#include <android/log.h>
#define  LOG_TAG    "someTag"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)
static Pipeline *pipeline;
static json pipeline_config;
static String OUTPUT_FOLDER;

using namespace std;
using namespace cv;

int img_process_count = 0;
float total_process_time =0;

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_example_eyealarm_activities_CameraActivity_initPipeline(
        JNIEnv *env,
        jobject /* this */,
        jstring asset_path) {
    // loader config file
    const char *asset_dir = env->GetStringUTFChars(asset_path, nullptr);
    LOGD("[Pipeline] Assert path: %s",asset_dir);
    std::string pipeline_json = std::string(asset_dir) + "pipeline.json";
    const char* str = pipeline_json.c_str();
    LOGD("[Pipeline] json path: %s",str);

    try {
        std::ifstream ifs(pipeline_json);
        if (!ifs.is_open()) throw LOGD("Could not open pipeline.json");
    } catch (const std::invalid_argument &e) {
        LOGD("%s", e.what());
        return false;
    }
    std::ifstream ifs(pipeline_json);
    ifs >> pipeline_config;

    //update path
    pipeline_config["detection"]["mnn_path"] = std::string(asset_dir) +
                                               pipeline_config["detection"]["mnn_path"].get<string>();
//    pipeline_config["landmark"]["model_path"] = std::string(asset_dir) +
//                                                pipeline_config["landmark"]["model_path"].get<string>();
    pipeline_config["landmark"]["mnn_path"] = std::string(asset_dir) +
                                                pipeline_config["landmark"]["mnn_path"].get<string>();

    // init pipeline
    try {
        pipeline = new Pipeline(pipeline_config);
        LOGD("[Pipeline] Init pipeline successfully");
        env->ReleaseStringUTFChars(asset_path, asset_dir);
        return true;
    } catch (const std::exception &e) {
        LOGD(" Error %s", e.what());
        return false;
    }
}

JNIEXPORT jfloatArray JNICALL
Java_com_example_eyealarm_activities_CameraActivity_processImg(JNIEnv *env, jobject p_this, jobject bitmapIn) {
    Mat img;
    bitmapToMat(env, bitmapIn, img, false);
    cv::cvtColor(img, img, COLOR_RGBA2BGR);
    if (img.empty())
        LOGD("IMG NULL");
    auto start = chrono::steady_clock::now();
    vector<BoxInfo> person_results;
    vector<Eye> left_eyes;
    vector<Eye> right_eyes;
    // for front camera
    cv::flip(img, img, 1);
    pipeline->process_frame(img, person_results, left_eyes, right_eyes);
    img_process_count++;

    auto end = chrono::steady_clock::now();
    chrono::duration<double> elapsed = end - start;
    total_process_time += elapsed.count();
//    LOGD("[Pipeline] ************** running *******************");
//    LOGD("[Pipeline] Process FPS: %f ", img_process_count/total_process_time);

    int32_t num_persons = static_cast<int32_t>(person_results.size());
//    LOGD("[Pipeline] Num persons %d", num_persons);
    int out_size = 1 + num_persons * 42;
    float *all_results = new float[out_size];
    all_results[0] = num_persons;
    for (int i = 0; i < num_persons; i++) {
//        LOGD("x1 %d - y1 %d - x2 %d - y2 %d", (int)person_results[i].x1, (int)person_results[i].y1, (int)person_results[i].x2, (int)person_results[i].y2);
        all_results[42 * i + 1] = person_results[i].x1;//left
        all_results[42 * i + 2] = person_results[i].y1;//top
        all_results[42 * i + 3] = person_results[i].x2;//right
        all_results[42 * i + 4] = person_results[i].y2;//bottom
        all_results[42 * i + 5] = person_results[i].score;//scored
        all_results[42 * i + 6] = person_results[i].label;//label

        // left eye
        all_results[42 * i + 7] = left_eyes[i].x1;
        all_results[42 * i + 8] = left_eyes[i].y1;
        all_results[42 * i + 9] = left_eyes[i].x2;
        all_results[42 * i + 10] = left_eyes[i].y2;
        all_results[42 * i + 11] = left_eyes[i].x3;
        all_results[42 * i + 12] = left_eyes[i].y3;
        all_results[42 * i + 13] = left_eyes[i].x4;
        all_results[42 * i + 14] = left_eyes[i].y4;
        all_results[42 * i + 15] = left_eyes[i].x5;
        all_results[42 * i + 16] = left_eyes[i].y5;
        all_results[42 * i + 17] = left_eyes[i].x6;
        all_results[42 * i + 18] = left_eyes[i].y6;
        all_results[42 * i + 19] = left_eyes[i].x7;
        all_results[42 * i + 20] = left_eyes[i].y7;
        all_results[42 * i + 21] = left_eyes[i].x8;
        all_results[42 * i + 22] = left_eyes[i].y8;
        all_results[42 * i + 23] = left_eyes[i].x9;
        all_results[42 * i + 24] = left_eyes[i].y9;

        // right eye
        all_results[42 * i + 25] = right_eyes[i].x1;
        all_results[42 * i + 26] = right_eyes[i].y1;
        all_results[42 * i + 27] = right_eyes[i].x2;
        all_results[42 * i + 28] = right_eyes[i].y2;
        all_results[42 * i + 29] = right_eyes[i].x3;
        all_results[42 * i + 30] = right_eyes[i].y3;
        all_results[42 * i + 31] = right_eyes[i].x4;
        all_results[42 * i + 32] = right_eyes[i].y4;
        all_results[42 * i + 33] = right_eyes[i].x5;
        all_results[42 * i + 34] = right_eyes[i].y5;
        all_results[42 * i + 35] = right_eyes[i].x6;
        all_results[42 * i + 36] = right_eyes[i].y6;
        all_results[42 * i + 37] = right_eyes[i].x7;
        all_results[42 * i + 38] = right_eyes[i].y7;
        all_results[42 * i + 39] = right_eyes[i].x8;
        all_results[42 * i + 40] = right_eyes[i].y8;
        all_results[42 * i + 41] = right_eyes[i].x9;
        all_results[42 * i + 42] = right_eyes[i].y9;
    }
    jfloatArray jall_results = env->NewFloatArray(out_size);
    env->SetFloatArrayRegion(jall_results, 0, out_size, all_results);

    delete[] all_results;
    vector<BoxInfo>().swap(person_results);
    vector<Eye>().swap(left_eyes);
    vector<Eye>().swap(right_eyes);
    return jall_results;
}

JNIEXPORT void JNICALL
Java_com_example_eyealarm_activities_CameraActivity_uninitPipeline(
        JNIEnv *env,
        jobject /* this */){
    delete pipeline;
}
}