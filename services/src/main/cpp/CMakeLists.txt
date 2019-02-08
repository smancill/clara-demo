# Create one shared library per service
set(DEMO_SERVICES
  pupil_detector_service
)

foreach(service ${DEMO_SERVICES})
  add_library(${service} SHARED ${service}.cpp)
  target_link_libraries(${service} demo_data demo_legacy Clara::clara)
  if (CMAKE_CXX_COMPILER_ID MATCHES Clang)
    target_compile_options(${service} PRIVATE -Wno-return-type-c-linkage)
  endif()
endforeach()

# Install the services
install(TARGETS ${DEMO_SERVICES} DESTINATION $ENV{CLARA_HOME}/plugins/demo/services)
