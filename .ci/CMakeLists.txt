# SPDX-FileCopyrightText: © Sebastián Mancilla
#
# SPDX-License-Identifier: Apache-2.0

cmake_minimum_required(VERSION 3.16.3)
project(CLARA_DEMO_DEPS NONE)

if(POLICY CMP0135)
  cmake_policy(SET CMP0135 NEW)
endif()

if(NOT DEFINED ENV{CLARA_HOME})
  message(FATAL_ERROR "CLARA_HOME is not defined")
endif()

option(INSTALL_OPENCV "Build and install OpenCV" OFF)
option(INSTALL_CLARA "Build and install Clara" OFF)

set(OPENCV_INSTALL_DIR $ENV{CLARA_HOME} CACHE STRING "OpenCV install directory")


include(ExternalProject)
set_directory_properties(PROPERTIES EP_PREFIX ${CMAKE_CURRENT_BINARY_DIR})

if(INSTALL_OPENCV)
# Build the minimum set of features that make the services work.
ExternalProject_Add(
  opencv
  URL               https://github.com/opencv/opencv/archive/4.7.0.zip
  URL_HASH          SHA256=db6fb5e7dc76829d738fdbcdba11a810c66ca0a4752e531eaf3d793361e96de8
  DOWNLOAD_NAME     opencv-4.7.0.zip
  CMAKE_ARGS        -DCMAKE_INSTALL_PREFIX:PATH=${OPENCV_INSTALL_DIR}
                    -DCMAKE_INSTALL_LIBDIR=lib
                    -DCMAKE_BUILD_TYPE=Release
                    -DBUILD_DOCS=OFF
                    -DBUILD_EXAMPLES=OFF
                    -DBUILD_PERF_TESTS=OFF
                    -DBUILD_SHARED_LIBS=OFF
                    -DBUILD_TESTS=OFF
                    -DBUILD_opencv_apps=OFF
                    -DBUILD_opencv_dnn=OFF
                    -DBUILD_opencv_js_bindings_generator=OFF
                    -DBUILD_opencv_ml=OFF
                    -DBUILD_opencv_objc_bindings_generator=OFF
                    -DBUILD_opencv_photo=OFF
                    -DBUILD_opencv_python_bindings_generator=OFF
                    -DBUILD_opencv_stitching=OFF
                    -DBUILD_opencv_video=OFF
                    -DBUILD_opencv_videoio=OFF
                    -DENABLE_CXX11=ON
                    -DWITH_1394=OFF
                    -DWITH_ADE=OFF
                    -DWITH_EIGEN=OFF
                    -DWITH_FFMPEG=OFF
                    -DWITH_GSTREAMER=OFF
                    -DWITH_GTK=OFF
                    -DWITH_IPP=OFF
                    -DWITH_ITT=OFF
                    -DWITH_IMGCODEC_HDR=OFF
                    -DWITH_IMGCODEC_PFM=OFF
                    -DWITH_IMGCODEC_PXM=OFF
                    -DWITH_IMGCODEC_SUNRASTER=OFF
                    -DWITH_JASPER=OFF
                    -DWITH_LAPACK=OFF
                    -DWITH_OPENCLAMDBLAS=OFF
                    -DWITH_OPENCLAMDFFT=OFF
                    -DWITH_OPENCL=OFF
                    -DWITH_OPENEXR=OFF
                    -DWITH_QT=OFF
                    -DWITH_PROTOBUF=OFF
                    -DWITH_TIFF=OFF
                    -DWITH_WEBP=OFF
                    -DWITH_QUIRC=OFF
                    -DWITH_V4L=OFF
                    -DWITH_VTK=OFF
)
endif()

if(INSTALL_CLARA)
# This could get a binary distribution, but compiling is not so bad.
Externalproject_Add(
  clara-java
  GIT_REPOSITORY https://github.com/smancill/clara-java
  GIT_TAG master
  CONFIGURE_COMMAND touch <SOURCE_DIR>/settings.gradle
  BUILD_COMMAND <SOURCE_DIR>/gradlew build -x javadoc -x check
  INSTALL_COMMAND <SOURCE_DIR>/gradlew deploy
  BUILD_IN_SOURCE ON
)

ExternalProject_Add(
  clara-cpp
  GIT_REPOSITORY https://github.com/smancill/clara-cpp
  GIT_TAG master
  CMAKE_ARGS  -DCMAKE_INSTALL_PREFIX:PATH=$ENV{CLARA_HOME}
              -DCMAKE_INSTALL_LIBDIR=lib
              -DCMAKE_BUILD_TYPE=Release
              -DCLARA_BUILD_TESTS:BOOL=OFF
  DEPENDS clara-java
)
endif()
