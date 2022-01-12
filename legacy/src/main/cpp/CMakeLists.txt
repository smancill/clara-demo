# SPDX-FileCopyrightText: © Sebastián Mancilla
#
# SPDX-License-Identifier: Apache-2.0

set(LEGACY_SRC
  pupil_detector.cpp
)

set(LEGACY_HEADERS
  pupil_detector.hpp
)

add_library(demo_legacy SHARED ${LEGACY_SRC})
target_link_libraries(demo_legacy PUBLIC ${OpenCV_LIBS})
target_include_directories(demo_legacy PUBLIC
  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}>
)

install(TARGETS demo_legacy DESTINATION ${CMAKE_INSTALL_PLUGINDIR}/demo/lib)
