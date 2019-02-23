cmake_minimum_required(VERSION 3.5.1)

include(ExternalProject)
set_directory_properties(PROPERTIES EP_PREFIX ${CMAKE_CURRENT_BINARY_DIR})

# Build the minimum set of features that make the services work.
ExternalProject_Add(
  opencv
  URL               https://github.com/opencv/opencv/archive/3.4.5.zip
  URL_HASH          SHA256=0cc341e3b77cf96ae431ee55b7ba53d41d184cc11506d100711022abfcfe6bb3
  DOWNLOAD_NAME     opencv-3.4.5.zip
  CMAKE_ARGS        -DCMAKE_INSTALL_PREFIX:PATH=$ENV{CLARA_HOME}
                    -DCMAKE_BUILD_TYPE=Release
                    -DBUILD_DOCS=OFF
                    -DBUILD_EXAMPLES=OFF
                    -DBUILD_PERF_TESTS=OFF
                    -DBUILD_SHARED_LIBS=OFF
                    -DBUILD_TESTS=OFF
                    -DBUILD_opencv_apps=OFF
                    -DBUILD_opencv_dnn=OFF
                    -DBUILD_opencv_ml=OFF
                    -DBUILD_opencv_photo=OFF
                    -DBUILD_opencv_python_bindings_generator=OFF
                    -DBUILD_opencv_stitching=OFF
                    -DBUILD_opencv_video=OFF
                    -DBUILD_opencv_videoio=OFF
                    -DENABLE_CXX11=ON
                    -DWITH_1394=OFF
                    -DWITH_EIGEN=OFF
                    -DWITH_FFMPEG=OFF
                    -DWITH_GSTREAMER=OFF
                    -DWITH_GTK=OFF
                    -DWITH_IPP=OFF
                    -DWITH_ITT=OFF
                    -DWITH_IMGCODEC_HDR=OFF
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

# This could get a binary distribution, but compiling is not so bad.
Externalproject_Add(
  clara-java
  GIT_REPOSITORY https://github.com/JeffersonLab/clara-java
  GIT_TAG master
  CONFIGURE_COMMAND ""
  BUILD_COMMAND <SOURCE_DIR>/gradlew build -x javadoc -x check
  INSTALL_COMMAND <SOURCE_DIR>/gradlew deploy
  BUILD_IN_SOURCE ON
  DEPENDS opencv
)

ExternalProject_Add(
  clara-cpp
  GIT_REPOSITORY https://github.com/JeffersonLab/clara-cpp
  GIT_TAG master
  CMAKE_ARGS  -DCMAKE_INSTALL_PREFIX:PATH=$ENV{CLARA_HOME}
              -DCMAKE_BUILD_TYPE=Release
              -DCLARA_BUILD_TESTS:BOOL=OFF
  DEPENDS clara-java
)
