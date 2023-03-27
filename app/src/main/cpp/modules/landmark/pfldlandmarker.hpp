//
// Created by AWL VN on 27/02/2023.
//

#ifndef EYE_ALARM_PFLDLANDMARKER_HPP
#define EYE_ALARM_PFLDLANDMARKER_HPP

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

using json = nlohmann::json;

class PFLDLandmarker{
public:
    PFLDLandmarker(json landmark_cfg);
    ~PFLDLandmarker();
//    int ExtractKeypoints(const cv::Mat& img_src, const cv::Rect& face, std::vector<cv::Point2f>* keypoints);
    int ExtractKeypoints(const cv::Mat& img_src, const cv::Rect& face, std::vector<float>* points);

private:
//    bool initialized_;
    std::string mnn_path;
    std::string output_name;
    int num_thread;

    std::shared_ptr<MNN::CV::ImageProcess> pretreat_ = nullptr;
    std::shared_ptr<MNN::Interpreter> pfld_interpreter_ = nullptr;
    MNN::Session* pfld_sess_ = nullptr;
    MNN::Tensor* input_tensor_ = nullptr;

    const cv::Size inputSize_ = cv::Size(96, 96);
    const float meanVals_[3] = {   123.0f,   123.0f,   123.0f };
    const float normVals_[3] = { 0.01724f, 0.01724f, 0.01724f };

};





#endif //EYE_ALARM_PFLDLANDMARKER_HPP
