/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#ifndef CLARA_DEMO_DATA_IMAGE_HPP_
#define CLARA_DEMO_DATA_IMAGE_HPP_

#include <image.hpp>

#include <clara/engine_data_type.hpp>

namespace clara {
namespace demo {

using byte_t = std::uint8_t;
using bytes_t = std::vector<byte_t>;

class ByteBuffer
{

public:
    ByteBuffer(size_t size)
    {
        data_.reserve(size);
    }

public:
    void put(std::int32_t v)
    {
        data_.insert(data_.end(), (byte_t*) &v, (byte_t*) &v + sizeof(int));
    }

    template<typename T>
    void putRange(const T& v)
    {
        data_.insert(data_.end(), v.begin(), v.end());
    }

public:
    bytes_t data_;
};


const extern clara::EngineDataType IMAGE_TYPE;

} // end namespace type

} // end namespace clara

#endif
