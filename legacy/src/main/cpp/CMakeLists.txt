include_directories(${OpenCV_INCLUDE_DIRS})

set(LEGACY_SRC
  pupil_detector.cpp
)

set(LEGACY_HEADERS
  pupil_detector.hpp
)

add_library(demo_legacy SHARED ${LEGACY_SRC})
target_link_libraries(demo_legacy ${OpenCV_LIBS})
target_include_directories(demo_legacy PUBLIC
  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}>
  $<BUILD_INTERFACE:${OpenCV_INCLUDE_DIRS}>
)

# Install the common library
install(TARGETS demo_legacy DESTINATION $ENV{CLARA_HOME}/lib)
