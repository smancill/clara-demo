/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include "pupil_detector_service.hpp"

#include <image_data_type.hpp>
#include <pupil_detector.hpp>

#include <clara/stdlib/json_utils.hpp>

#include <cmath>
#include <iostream>

extern "C"
std::unique_ptr<clara::Engine> create_engine()
{
    return std::make_unique<clara::demo::PupilDetectorService>();
}


namespace clara {
namespace demo {

clara::EngineData PupilDetectorService::configure(clara::EngineData& input)
{
    // Clara provides a simple JSON parser to read configuration data
    // and configure the service.
    auto config = clara::stdlib::parse_json(input);

    // Example for when the service has state that is configured by
    // the orchestrator. The "state" object should be a std::shared_ptr
    // accessed atomically.
    //
    // (This service is actually stateless, so detector_ could just simply be
    // initialized in the service constructor).
    std::atomic_store(&detector_, std::make_shared<PupilDetector>());
    return {};
}


clara::EngineData PupilDetectorService::execute(clara::EngineData& input)
{
    auto output = clara::EngineData{};

    // If the mime-type is not supported, return an error.
    if (input.mime_type() != IMAGE_TYPE) {
        output.set_status(clara::EngineStatus::ERROR);
        output.set_description("Wrong input type");
        return output;
    }

    auto& img = clara::data_cast<Image>(input);

    // This always loads the shared_pointer into a new shared_ptr
    std::atomic_load(&detector_)->run(img.mat);

    // Set and return output data
    output.set_data(IMAGE_TYPE, img);
    return output;
}


clara::EngineData PupilDetectorService::execute_group(const std::vector<clara::EngineData>&)
{
    return {};
}


std::vector<clara::EngineDataType> PupilDetectorService::input_data_types() const
{
    return { clara::type::JSON, IMAGE_TYPE };
}


std::vector<clara::EngineDataType> PupilDetectorService::output_data_types() const
{
    return { clara::type::JSON, IMAGE_TYPE };
}


std::set<std::string> PupilDetectorService::states() const
{
    return std::set<std::string>{};
}


std::string PupilDetectorService::name() const
{
    return "PupilDetectorService";
}


std::string PupilDetectorService::author() const
{
    return "Sebastián Mancilla";
}


std::string PupilDetectorService::description() const
{
    return "Writes a circle around detected pupils in a given image";
}


std::string PupilDetectorService::version() const
{
    return "0.5";
}

}
}
