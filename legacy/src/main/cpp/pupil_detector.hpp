/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#ifndef CLARA_DEMO_LEGACY_PUPIL_DETECTOR_HPP_
#define CLARA_DEMO_LEGACY_PUPIL_DETECTOR_HPP_

#include <string>

namespace cv {

class Mat;

} // end namespace cv


namespace clara {
namespace demo {

/**
 * Writes a circle around detected pupils in a given image.
 */
class PupilDetector
{
public:
    /**
     * Runs the detector on the given image.
     *
     * @param img the input image
     */
    void run(cv::Mat& img);
};

} // end namespace demo
} // end namespace clara

#endif
