include_directories(${OpenCV_INCLUDE_DIRS})

# Create one shared library per service
set(DEMO_SERVICES
  pupil_detector_service
)

foreach(service ${DEMO_SERVICES})
  add_library(${service} SHARED ${service}.cpp)
  target_link_libraries(${service} demo_data demo_legacy Clara::clara)
endforeach()

# Install the services
install(TARGETS ${DEMO_SERVICES} DESTINATION $ENV{CLARA_HOME}/lib)
