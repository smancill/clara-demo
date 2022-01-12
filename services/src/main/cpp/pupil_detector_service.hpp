/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#ifndef SCIA_RESAMPLING_SERVICE_HPP
#define SCIA_RESAMPLING_SERVICE_HPP

#include <clara/engine.hpp>

#include <atomic>
#include <memory>

namespace clara {
namespace demo {

class PupilDetector;

class PupilDetectorService : public clara::Engine
{
public:
    PupilDetectorService() = default;

    PupilDetectorService(const PupilDetectorService&) = delete;
    PupilDetectorService& operator=(const PupilDetectorService&) = delete;

    PupilDetectorService(PupilDetectorService&&) = default;
    PupilDetectorService& operator=(PupilDetectorService&&) = default;

    ~PupilDetectorService() override = default;

public:
    clara::EngineData configure(clara::EngineData&) override;

    clara::EngineData execute(clara::EngineData&) override;

    clara::EngineData execute_group(const std::vector<clara::EngineData>&) override;

public:
    std::vector<clara::EngineDataType> input_data_types() const override;

    std::vector<clara::EngineDataType> output_data_types() const override;

    std::set<std::string> states() const override;

public:
    std::string name() const override;

    std::string author() const override;

    std::string description() const override;

    std::string version() const override;

private:
    std::shared_ptr<PupilDetector> detector_{};
};

} // end namespace demo
} // end namespace clara

#endif
