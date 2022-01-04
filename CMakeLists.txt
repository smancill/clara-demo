cmake_minimum_required(VERSION 3.16.3)
project(CLARA_DEMO C CXX)

if(NOT CMAKE_BUILD_TYPE AND NOT CMAKE_CONFIGURATION_TYPES)
  message(STATUS "No build type selected, default to RelWithDebInfo")
  set(CMAKE_BUILD_TYPE RelWithDebInfo CACHE STRING "Choose the type of build." FORCE)
  set_property(CACHE CMAKE_BUILD_TYPE PROPERTY STRINGS
    Debug Release MinSizeRel RelWithDebInfo)
endif()

if(DEFINED ENV{CLARA_HOME} AND CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
  # Provide useful default paths when $CLARA_HOME is defined
  set(CMAKE_INSTALL_PREFIX "$ENV{CLARA_HOME}" CACHE PATH "" FORCE)
  list(APPEND CMAKE_PREFIX_PATH "$ENV{CLARA_HOME}")
endif()

if(NOT DEFINED CMAKE_INSTALL_PLUGINDIR)
  set(CMAKE_INSTALL_PLUGINDIR plugins CACHE PATH "Clara plugins directory (plugins)")
endif()
mark_as_advanced(CMAKE_INSTALL_PLUGINDIR)

if(POLICY CMP0074)
  cmake_policy(SET CMP0074 NEW)
endif()
if(NOT OpenCV_ROOT)
  set(OpenCV_ROOT $ENV{CLARA_HOME} /usr/local/opt/opencv4 /opt/opencv4 /usr/local /usr/
      CACHE PATH "OpenCV installation prefix")
endif()
mark_as_advanced(OpenCV_ROOT)

option(DEMO_BUILD_TESTS "Build tests" ON)


set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_compile_options(-Wall -Wextra)

if(APPLE)
  set(CMAKE_MACOSX_RPATH ON)
  set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
  set(CMAKE_INSTALL_RPATH @loader_path/../lib)
endif()

include(FetchContent)

list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake/Modules)

find_package(Clara REQUIRED)
find_package(OpenCV 4 REQUIRED HINTS ${OpenCV_ROOT})


add_subdirectory(legacy/src/main/cpp)
add_subdirectory(data/src/main/cpp)
add_subdirectory(services/src/main/cpp)

if(DEMO_BUILD_TESTS)
  enable_testing()
  include(cmake/External/GoogleTest.cmake)
  add_subdirectory(data/src/test/cpp)
endif(DEMO_BUILD_TESTS)
