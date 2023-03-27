//
// Created by AWL VN on 07/02/2023.
//

#ifndef ALARM_OBJECTS_HPP
#define ALARM_OBJECTS_HPP

#include <iostream>
#include <string>
#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

struct PointInfo
{
    float x;
    float y;
};

struct BoxInfo
{
    float x1;
    float y1;
    float x2;
    float y2;
    float score = -1;
    int label = -1;
};

struct Eye
{
    float x1;
    float x2;
    float x3;
    float x4;
    float x5;
    float x6;
    float y1;
    float y2;
    float y3;
    float y4;
    float y5;
    float y6;
    float x7;
    float y7;
    float x8;
    float y8;
    float x9;
    float y9;
};
/*struct DetResult
{
    cv::Rect_<float> cvfRect;
    int iLabel;
    float fConf;

    bool operator>(const DetResult &strDetRet) const
    {
        return (fConf > strDetRet.fConf);
    }
};*/

typedef struct CenterPrior_
{
    int x;
    int y;
    int stride;
} CenterPrior;
#endif //ALARM_OBJECTS_HPP
