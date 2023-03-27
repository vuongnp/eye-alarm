//
// Created by AWL VN on 07/02/2023.
//

#ifndef ALARM_PIPELINE_HPP
#define ALARM_PIPELINE_HPP
#include <iostream>
#include <fstream>
#include <string>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "nlohmann/json.hpp"
#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing/render_face_detections.h>
#include <dlib/image_processing.h>
#include <dlib/opencv/cv_image.h>
#include <opencv2/highgui/highgui.hpp>

#include "modules/detection/nanodet.hpp"
#include "modules/landmark/pfldlandmarker.hpp"
#include "modules/utils/objects.hpp"
#include "modules/utils/funcs.hpp"

using json = nlohmann::json;

using namespace std;
using namespace cv;

class Pipeline{
public:
    Pipeline(json pipeline_config);
    ~Pipeline();
    void process_frame(cv::Mat &frame, vector<BoxInfo> &person_results, vector<Eye> &left_eyes, vector<Eye> &right_eyes);

private:
    int frame_id;
    int min_width;
    int min_height;

    // detection
    json detection_config;
    json landmark_config;
    unique_ptr<NanoDet> detector;
    unique_ptr<PFLDLandmarker> landmarker;
//    dlib::shape_predictor landmark_model;

};
#endif //ALARM_PIPELINE_HPP
