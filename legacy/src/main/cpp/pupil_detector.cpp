// Program to detect pupil, based on
// https://github.com/bsdnoobz/opencv-code/blob/master/pupil-detect.cpp

#include "pupil_detector.hpp"

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <cmath>
#include <iostream>

namespace clara {
namespace demo {

void PupilDetector::run(cv::Mat& img)
{
    if (img.empty()) {
        return;
    }

    std::cout << "\nRunning PupilDetector" << std::endl;

    // Invert the source image and convert to grayscale
    cv::Mat gray;
    cv::cvtColor(~img, gray, cv::COLOR_BGR2GRAY);

    // Convert to binary image by thresholding it
    cv::threshold(gray, gray, 220, 255, cv::THRESH_BINARY);

    // Find all contours
    std::vector<std::vector<cv::Point>> contours;
    cv::findContours(gray.clone(), contours, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_NONE);

    // Fill holes in each contour
    cv::drawContours(gray, contours, -1, CV_RGB(255,255,255), -1);

    int counter = 0;
    for (size_t i = 0; i < contours.size(); i++) {
        double area = cv::contourArea(contours[i]);
        cv::Rect rect = cv::boundingRect(contours[i]);
        int radius = rect.width/2;

        // If contour is big enough and has round shape
        // Then it is the pupil
        if (area >= 30
                && std::abs(1 - ((double) rect.width / (double) rect.height)) <= 0.2
                && std::abs(1 - (area / (CV_PI * std::pow(radius, 2)))) <= 0.2) {
            cv::circle(img, cv::Point(rect.x + radius, rect.y + radius), radius, CV_RGB(255,0,0), 2);
            counter++;
        }
    }

    std::cout << "Detected " << counter << " pupils" << std::endl;
}

} // end namespace demo
} // end namespace clara
