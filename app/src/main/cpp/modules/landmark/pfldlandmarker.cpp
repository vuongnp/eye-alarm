//
// Created by AWL VN on 27/02/2023.
//

#include "pfldlandmarker.hpp"
#include <android/log.h>
#define  LOG_TAG    "someTag"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,__VA_ARGS__)

PFLDLandmarker::PFLDLandmarker(json landmark_cfg) {
    mnn_path = landmark_cfg["mnn_path"].get<std::string>();
    output_name = landmark_cfg["output_name"].get<std::string>();
    num_thread = landmark_cfg["num_thread"].get<int>();

    pfld_interpreter_ = std::unique_ptr<MNN::Interpreter>(MNN::Interpreter::createFromFile(mnn_path.c_str()));
    MNN::ScheduleConfig config;
    config.type = MNN_FORWARD_CPU;
    config.numThread = num_thread;
    MNN::BackendConfig backend_config;
    backend_config.memory    = MNN::BackendConfig::Memory_Normal;
    backend_config.power     = MNN::BackendConfig::Power_Normal;
    backend_config.precision = MNN::BackendConfig::Precision_Normal;
    config.backendConfig = &backend_config;
    pfld_sess_ = pfld_interpreter_->createSession(config);
    input_tensor_ = pfld_interpreter_->getSessionInput(pfld_sess_, nullptr);
    pfld_interpreter_->resizeTensor(input_tensor_, {1, 3, inputSize_.height, inputSize_.width});
    pfld_interpreter_->resizeSession(pfld_sess_);

    MNN::CV::Matrix trans;
    trans.setScale(1.0f, 1.0f);
    MNN::CV::ImageProcess::Config img_config;
    img_config.filterType = MNN::CV::BICUBIC;
    ::memcpy(img_config.mean, meanVals_, sizeof(meanVals_));
    ::memcpy(img_config.normal, normVals_, sizeof(normVals_));
    img_config.sourceFormat = MNN::CV::BGR;
    img_config.destFormat = MNN::CV::RGB;
    pretreat_ = std::shared_ptr<MNN::CV::ImageProcess>(MNN::CV::ImageProcess::create(img_config));
    pretreat_->setMatrix(trans);
}

PFLDLandmarker::~PFLDLandmarker() {
    pfld_interpreter_->releaseModel();
    pfld_interpreter_->releaseSession(pfld_sess_);
}

int PFLDLandmarker::ExtractKeypoints(const cv::Mat& img_src, const cv::Rect& face, std::vector<float>* points){
//    keypoints->clear();
    points->clear();

    if (img_src.empty()) {
        LOGD("input empty.");
        return 10001;
    }
    cv::Mat img_face = img_src(face).clone();
    int width = img_face.cols;
    int height = img_face.rows;
    cv::Mat img_resized;
    cv::resize(img_face, img_resized, inputSize_);
    float scale_x = static_cast<float>(width) / inputSize_.width;
    float scale_y = static_cast<float>(height) / inputSize_.height;
    pretreat_->convert(img_resized.data, inputSize_.width, inputSize_.height, 0, input_tensor_);

    // run session
    pfld_interpreter_->runSession(pfld_sess_);

    // get output
    auto output_landmark = pfld_interpreter_->getSessionOutput(pfld_sess_, output_name.c_str());
    MNN::Tensor landmark_tensor(output_landmark, output_landmark->getDimensionType());
    output_landmark->copyToHostTensor(&landmark_tensor);

//    for (int i = 0; i < 98; ++i) {
//        cv::Point2f curr_pt(landmark_tensor.host<float>()[2 * i + 0] * scale_x + face.x,
//                            landmark_tensor.host<float>()[2 * i + 1] * scale_y + face.y);
//        keypoints->push_back(curr_pt);
//    }
    for (int i=0; i<98;++i){
        float x = landmark_tensor.host<float>()[2 * i + 0] * scale_x + face.x;
        float y = landmark_tensor.host<float>()[2 * i + 1] * scale_y + face.y;
        points->push_back(x);
        points->push_back(y);
    }

//        std::cout << "end extract keypoints." << std::endl;

    return 0;
}

