# SPDX-FileCopyrightText: © Sebastián Mancilla
#
# SPDX-License-Identifier: Apache-2.0

FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/release-1.12.1.tar.gz
  URL_HASH SHA256=81964fe578e9bd7c94dfdb09c8e4d6e6759e19967e397dbea48d1c10e45d0df2
)

FetchContent_GetProperties(googletest)

if(NOT googletest_POPULATED)
  FetchContent_Populate(googletest)

  set(CMAKE_CXX_CLANG_TIDY "")
  set_directory_properties(PROPERTIES EXCLUDE_FROM_ALL YES)

  option(INSTALL_GTEST "Install googletest." OFF)
  set(BUILD_SHARED_LIBS OFF)

  add_subdirectory(${googletest_SOURCE_DIR} ${googletest_BINARY_DIR})

  mark_as_advanced(INSTALL_GTEST BUILD_GMOCK)
endif()

if(NOT TARGET GTest::GTest)
  add_library(GTest::GTest ALIAS gtest)
endif()
if(NOT TARGET GTest::GMock)
  add_library(GTest::GMock ALIAS gmock)
endif()
