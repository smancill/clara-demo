cmake_minimum_required(VERSION 3.5.2...3.12)
project(CLARA_DEMO C CXX)

if(NOT CMAKE_BUILD_TYPE AND NOT CMAKE_CONFIGURATION_TYPES)
  message(STATUS "No build type selected, default to Release")
  set(CMAKE_BUILD_TYPE RelWithDebInfo CACHE STRING "Choose the type of build." FORCE)
  set_property(CACHE CMAKE_BUILD_TYPE PROPERTY STRINGS
    Debug Release MinSizeRel RelWithDebInfo)
endif()

if(NOT DEFINED ENV{CLARA_HOME})
  message(FATAL_ERROR "$CLARA_HOME is not defined.")
endif()

message(STATUS "CLARA_HOME = $ENV{CLARA_HOME}")

if(NOT EXISTS "$ENV{CLARA_HOME}")
  message(FATAL_ERROR "$CLARA_HOME directory does not exist.")
endif()

if(NOT OpenCV_ROOT)
  set(OpenCV_ROOT /usr/local/opt/opencv3 /opt/opencv3 /usr/local /usr/
      CACHE PATH "OpenCV installation prefix")
endif()
mark_as_advanced(OpenCV_ROOT)


set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_compile_options(-Wall -Wextra)

if(APPLE)
  set(CMAKE_MACOSX_RPATH ON)
  set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
  set(CMAKE_INSTALL_RPATH @loader_path/../lib)
endif()


list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake/Modules)

find_package(Clara REQUIRED)
find_package(OpenCV 3 REQUIRED HINTS ${OpenCV_ROOT})


add_subdirectory(legacy/src/main/cpp)
add_subdirectory(data/src/main/cpp)
add_subdirectory(services/src/main/cpp)
