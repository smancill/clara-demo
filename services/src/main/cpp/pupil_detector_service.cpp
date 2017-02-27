#include "pupil_detector_service.hpp"

#include "image_data_type.hpp"

#include <cmath>
#include <iostream>

extern "C"
std::unique_ptr<clara::Engine> create_engine()
{
    return std::make_unique<clara::demo::PupilDetectorService>();
}


namespace clara {
namespace demo {

clara::EngineData PupilDetectorService::configure(clara::EngineData&)
{
    return {};
}


clara::EngineData PupilDetectorService::execute(clara::EngineData& input)
{
    auto output = clara::EngineData{};

    if (input.mime_type() != IMAGE_TYPE.mime_type()) {
        output.set_status(clara::EngineStatus::ERROR);
        output.set_description("Wrong input type");
        return output;
    }

    auto& img = input.data<Image>();
    detector_.load()->run(img.mat);
    output.set_data(IMAGE_TYPE.mime_type(), img);

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

}
}
