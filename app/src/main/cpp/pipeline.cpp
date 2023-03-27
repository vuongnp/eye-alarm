//
// Created by AWL VN on 07/02/2023.
//

#include "pipeline.hpp"
#include <android/log.h>
#define  LOG_TAG    "someTag"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)

//unique_ptr<NanoDet> make_detector(json detection_config)
//{
//    return make_unique<NanoDet>(detection_config);
//}

Pipeline::Pipeline(json pipeline_config){

    frame_id = 0;
    detection_config = pipeline_config["detection"];
    landmark_config = pipeline_config["landmark"];
//    LOGD("detection config ");
    detector = make_unique<NanoDet>(detection_config);
    landmarker = make_unique<PFLDLandmarker>(landmark_config);
//    dlib::deserialize(landmark_config["model_path"]) >> landmark_model;
    min_width = pipeline_config["landmark"]["min_width"];
    min_height = pipeline_config["landmark"]["min_height"];
}

Pipeline::~Pipeline(){
    detector.release();
    landmarker.release();
}

void Pipeline::process_frame(cv::Mat &frame, vector<BoxInfo> &person_results, vector<Eye> &left_eyes, vector<Eye> &right_eyes){
    vector<BoxInfo> results;
    Mat detect_frame;
//    std::vector<dlib::full_object_detection> shapes;


    detect_frame = frame.clone();
    detector->detect(detect_frame, results);
//    LOGD("[Pipeline] Num detected %d", results.size());
//    dlib::array2d<dlib::bgr_pixel> dlibImage;
//    dlib::assign_image(dlibImage, dlib::cv_image<dlib::bgr_pixel>(detect_frame));

    // Add detected bounding box to track
    for (auto box : results)
    {
        if (detector->get_label_str(box.label) == "face"){
//            LOGD("Xmin %d - Ymin %d - Xmax %d - Ymax %d", (int)box.x1, (int)box.y1, (int)box.x2, (int)box.y2);
            if (((box.x2 - box.x1) < min_width) || ((box.y2 - box.y1) < min_height)){
                continue;
            }
            person_results.push_back(box);
//            dlib::rectangle r(
//                    (int)box.x1,
//                    (int)box.y1,
//                    (int)box.x2,
//                    (int)box.y2
//            );
//            dlib::full_object_detection shape = landmark_model(dlibImage, r);

            std::vector<float> points;
            cv::Rect face ((int)box.x1, (int)box.y1, (int)(box.x2-box.x1), (int)(box.y2-box.y1));
            landmarker->ExtractKeypoints(detect_frame, face, &points);

            struct Eye left_eye;
            // PFLD landmark
            left_eye.x1 = points[60*2];
            left_eye.x2 = points[61*2];
            left_eye.x3 = points[62*2];
            left_eye.x4 = points[63*2];
            left_eye.x5 = points[64*2];
            left_eye.x6 = points[65*2];
            left_eye.x7 = points[66*2];
            left_eye.x8 = points[67*2];
            left_eye.x9 = points[96*2];
            left_eye.y1 = points[60*2+1];
            left_eye.y2 = points[61*2+1];
            left_eye.y3 = points[62*2+1];
            left_eye.y4 = points[63*2+1];
            left_eye.y5 = points[64*2+1];
            left_eye.y6 = points[65*2+1];
            left_eye.y7 = points[66*2+1];
            left_eye.y8 = points[67*2+1];
            left_eye.y9 = points[96*2+1];

            // ZQ landmark
//            left_eye.x1 = points[66*2];
//            left_eye.x2 = points[67*2];
//            left_eye.x3 = points[68*2];
//            left_eye.x4 = points[69*2];
//            left_eye.x5 = points[70*2];
//            left_eye.x6 = points[71*2];
//            left_eye.x7 = points[72*2];
//            left_eye.x8 = points[73*2];
//            left_eye.x9 = points[104*2];
//            left_eye.y1 = points[66*2+1];
//            left_eye.y2 = points[67*2+1];
//            left_eye.y3 = points[68*2+1];
//            left_eye.y4 = points[69*2+1];
//            left_eye.y5 = points[70*2+1];
//            left_eye.y6 = points[71*2+1];
//            left_eye.y7 = points[72*2+1];
//            left_eye.y8 = points[73*2+1];
//            left_eye.y9 = points[104*2+1];

//            left_eye.x1 = shape.part(36).x();
//            left_eye.x2 = shape.part(37).x();
            left_eyes.push_back(left_eye);

            struct Eye right_eye;
            // PFLD landmark
            right_eye.x1 = points[68*2];
            right_eye.x2 = points[69*2];
            right_eye.x3 = points[70*2];
            right_eye.x4 = points[71*2];
            right_eye.x5 = points[72*2];
            right_eye.x6 = points[73*2];
            right_eye.x7 = points[74*2];
            right_eye.x8 = points[75*2];
            right_eye.x9 = points[97*2];
            right_eye.y1 = points[68*2+1];
            right_eye.y2 = points[69*2+1];
            right_eye.y3 = points[70*2+1];
            right_eye.y4 = points[71*2+1];
            right_eye.y5 = points[72*2+1];
            right_eye.y6 = points[73*2+1];
            right_eye.y7 = points[74*2+1];
            right_eye.y8 = points[75*2+1];
            right_eye.y9 = points[97*2+1];

            // ZQ landmark
//            right_eye.x1 = points[75*2];
//            right_eye.x2 = points[76*2];
//            right_eye.x3 = points[77*2];
//            right_eye.x4 = points[78*2];
//            right_eye.x5 = points[79*2];
//            right_eye.x6 = points[80*2];
//            right_eye.x7 = points[81*2];
//            right_eye.x8 = points[82*2];
//            right_eye.x9 = points[105*2];
//            right_eye.y1 = points[75*2+1];
//            right_eye.y2 = points[76*2+1];
//            right_eye.y3 = points[77*2+1];
//            right_eye.y4 = points[78*2+1];
//            right_eye.y5 = points[79*2+1];
//            right_eye.y6 = points[80*2+1];
//            right_eye.y7 = points[81*2+1];
//            right_eye.y8 = points[82*2+1];
//            right_eye.y9 = points[105*2+1];

//            right_eye.x1 = shape.part(42).x();
//            right_eye.x2 = shape.part(43).x();
            right_eyes.push_back(right_eye);

        }
    }
}
