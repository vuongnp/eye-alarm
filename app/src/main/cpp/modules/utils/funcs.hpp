//
// Created by AWL VN on 07/02/2023.
//

#ifndef ALARM_FUNCS_HPP
#define ALARM_FUNCS_HPP

#include <string>
#include <vector>
#include <chrono>
#include <math.h>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include "android/bitmap.h"
#include "objects.hpp"

using namespace std;
using namespace cv;

void crop_image(cv::Mat org_img, float x1, float y1, float x2, float y2, cv::Mat &crop_img);

void bitmapToMat(JNIEnv *env, jobject bitmap, Mat& dst, jboolean needUnPremultiplyAlpha);

void matToBitmap(JNIEnv* env, Mat src, jobject bitmap, jboolean needPremultiplyAlpha);

float calculate_euclidean(float x1, float y1, float x2, float y2);

float calculate_EAR(Eye eye);

#endif //ALARM_FUNCS_HPP
