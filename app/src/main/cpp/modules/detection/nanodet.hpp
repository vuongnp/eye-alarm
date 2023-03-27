//
// Created by AWL VN on 07/02/2023.
//

#ifndef ALARM_NANODET_HPP
#define ALARM_NANODET_HPP

#include <algorithm>
#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <chrono>
#include "MNN/Interpreter.hpp"
#include "MNN/MNNDefine.h"
#include "MNN/Tensor.hpp"
#include "MNN/ImageProcess.hpp"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "nlohmann/json.hpp"

#include "../utils/objects.hpp"

using json = nlohmann::json;

class NanoDet{
public:
    NanoDet(json detection_cfg);
    ~NanoDet();
    std::string get_label_str(int label);
    void detect(cv::Mat &img, std::vector<BoxInfo> &result_list);
    int reg_max = 7; // `reg_max` set in the training config. Default: 7.
    std::vector<int> strides = { 8, 16, 32, 64 }; // strides of the multi-level feature.
    int input_size; // input size of model
    int num_class; // number of classes. 80 for COCO
    std::string input_name;
    std::string output_name;

protected:
    std::shared_ptr<MNN::Interpreter> interpreter;
    MNN::Session *session = nullptr;
    MNN::Tensor *input_tensor = nullptr;

    std::string mnn_path;
    int num_thread;
    float score_threshold;
    float nms_threshold;
    std::vector<std::string>labels;

private:
    void decode_infer(MNN::Tensor *pred, std::vector<CenterPrior>& center_priors, float threshold, std::vector<std::vector<BoxInfo>> &results);
    BoxInfo disPred2Bbox(const float *&dfl_det, int label, float score, int x, int y, int stride);
    void nms(std::vector<BoxInfo> &input_boxes, float NMS_THRESH);

private:
    int image_w; // width of origin image
    int image_h;  // height of origin image
    const float mean_vals[3] = { 103.53f, 116.28f, 123.675f };
    const float norm_vals[3] = { 0.017429f, 0.017507f, 0.017125f };
};

template <typename _Tp>
int activation_function_softmax(const _Tp *src, _Tp *dst, int length);

inline float fast_exp(float x);
inline float sigmoid(float x);
#endif //ALARM_NANODET_HPP
