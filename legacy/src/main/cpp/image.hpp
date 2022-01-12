/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#ifndef CLARA_DEMO_LEGACY_IMAGE_HPP_
#define CLARA_DEMO_LEGACY_IMAGE_HPP_

#include <opencv2/core/mat.hpp>

#include <string>

namespace clara {
namespace demo {

struct Image
{
    cv::Mat mat;
    std::string name;
};

} // end namespace demo
} // end namespace clara

#endif
