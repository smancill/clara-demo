cmake_minimum_required(VERSION 3.1.3)
project(CLARA_DEMO C CXX)


if(NOT DEFINED ENV{CLARA_HOME})
  message(FATAL_ERROR "$CLARA_HOME is not defined.")
endif()

message(STATUS "CLARA_HOME = $ENV{CLARA_HOME}")

if(NOT EXISTS "$ENV{CLARA_HOME}")
  message(FATAL_ERROR "$CLARA_HOME directory does not exist.")
endif()


set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/cmake/Modules/")

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra")
if (CMAKE_CXX_COMPILER_ID MATCHES "Clang")
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-return-type-c-linkage")
endif()

if(APPLE)
  set(CMAKE_MACOSX_RPATH ON)
  set(CMAKE_SKIP_BUILD_RPATH FALSE)
  set(CMAKE_BUILD_WITH_INSTALL_RPATH FALSE)
  set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
  list(FIND CMAKE_PLATFORM_IMPLICIT_LINK_DIRECTORIES "${CMAKE_INSTALL_PREFIX}/${CMAKE_INSTALL_LIBDIR}" isSystemDir)
  if("${isSystemDir}" STREQUAL "-1")
    set(CMAKE_INSTALL_RPATH "${CMAKE_INSTALL_PREFIX}/${CMAKE_INSTALL_LIBDIR}")
  endif()
endif()


if(NOT OPENCV_ROOT)
  set(OPENCV_ROOT "/usr/local/opt/opencv3" "/opt/opencv3" "/usr/local" "/usr"
      CACHE PATH "OpenCV installation prefix")
endif()

find_package(clara CONFIG REQUIRED)
find_package(OpenCV 3 REQUIRED HINTS ${OPENCV_ROOT})

add_subdirectory("legacy/src/main/cpp")
add_subdirectory("data/src/main/cpp")
add_subdirectory("services/src/main/cpp")
