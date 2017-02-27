/* -*- Mode: C++; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*- */
/* vim: set ts=8 sts=4 et sw=4 tw=80: */
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
