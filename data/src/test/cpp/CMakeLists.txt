set(DEMO_DATA_TESTS
  image_data_type
)

foreach(name IN LISTS DEMO_DATA_TESTS)
  add_executable(test_${name} test_${name}.cpp)
  add_test(NAME test_${name} COMMAND test_${name})
  target_link_libraries(test_${name} PRIVATE demo_data GTest::GMock)
endforeach()

configure_file(
  ${CMAKE_CURRENT_SOURCE_DIR}/../resources/lena.png
  ${CMAKE_CURRENT_BINARY_DIR}
  COPYONLY)
