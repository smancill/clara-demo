# CLARA multi-language application demo

Example of a CLARA application wrapping a multi-language legacy library into services.

OpenCV is used to simulate a legacy application that detects faces and pupils
on a given set of images. For simplicity the images are stored in a ZIP file.
In a real-world scenario the dataset would be saved into a more
complex file format, like HDF5.


## Application architecture

The application is presented as a multi-project.

### `legacy` subproject

The `legacy` subproject simulates the legacy application.

The Java implementation presents I/O classes and a simple image detection
program:

  * The `ImageReader` class opens a ZIP file and read images from it.
  * The `ImageWriter` class opens a ZIP file and writes images to it.
  * The `Image` class stores the content and name of a file image.
  * The `FaceDetector` class finds faces in a image.

The C++ implementation adds a new detection algorithm:

  * The `Image` class stores the content and name of a file image.
  * The `PupilDetector` class finds pupils in a image.

The `legacy` subproject creates the `demo-legacy-0.1.jar` and
`libdemo_legacy.so` artifacts.

### `data` subproject

When the services are developed to be shared with other developers and
investigation groups, it is a good idea to present the data types used by the
services into its own package.

Orchestrators can then add a dependency on the data type package to send
requests to the services, without downloading all the services implementation
dependencies.

The `data` subproject creates the `demo-data-0.1.jar` and `libdemo_data.so`
artifacts.

### `services` subproject

The final CLARA services are just a tiny layer over the `legacy` code.
The most important thing to consider is thread-safety of the legacy classes.
CLARA services will run in a multi-thread environment and developers must
ensure thread-safety.

The Java implementation provides the `ImageReaderService` and the
`ImageWriterService` to open the ZIP dataset files and read or save the
images. The services extend the standard CLARA I/O services
`AbstractEventReaderService` and `AbstractEventWriterService`.

The Java `FaceDetectorService` and the C++ `PupilDetectorService` pass any
received image to its wrapped detection class.

The `services` subproject creates the `demo-services-0.1.jar` and
`libpupils_detector.so,` artifacts.


## Dependencies

Install [CLARA Java](https://github.com/JeffersonLab/clara-java#build-notes)
and [CLARA C++](https://github.com/JeffersonLab/clara-cpp#build-notes)
into `$CLARA_HOME`.

Install OpenCV following [these instructions][install-opencv-java].

[install-opencv-java]: http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html


## Build and deploy

Build and deploy the Java services:

    $ ./gradlew
    $ ./gradlew deploy

Build and deploy the C++ services:

    $ mkdir build
    $ cd build
    $ cmake -DCMAKE_INSTALL_PREFIX=$CLARA_HOME ..
    $ make
    $ make install


## Running the application

### Using the CLARA Shell

Use the `clara-shell` to run the services locally with a set of ZIP files
(each containing many images).
The shell provides a high-level interface to configure and start
the different CLARA components required to run an application.

 1. Start the CLARA shell:

        $ $CLARA_HOME/bin/clara-shell

 2. Define the application within a `services.yaml` file.
    There is one file ready in the root of the repository.

        clara> set servicesFile services.yaml

 3. Define the data set with the input files to be processed.
    Put a list with their names into a text file.
    There is a `files.list` file ready in the root of the repository.

        clara> set fileList files.list

 4. Define the locations for the input data set and the output result
    (relative to where the shell was started):

        clara> set inputDir resources
        clara> set outputDir /tmp/out

    Optionally you can change the number of parallel threads
    used by the services to process requests

        clara> set threads <NUM_THREADS>

 5. Start the data processing.
    This will start the main Java DPE,
    a C++ DPE if the C++ service is listed in `services.yaml`,
    and it will run the standard orchestrator to process the dataset.

        clara> run local

 6. Inspect the output files in the output directory.

You can put the above shell commands into a script and run the script instead.
There is a `demo.clara` script ready in the root of the repository:

    $ $CLARA_HOME/bin/clara-shell demo.clara

### Starting the CLARA DPEs and orchestrator manually

All CLARA components started by the CLARA shell can be run manually.

Use the standard `clara-orchestrator` to run the services with a set of ZIP
files (each containing many images):

 1. Start the main Java DPE:

        $ $CLARA_HOME/bin/j_dpe

 2. Start the C++ DPE (if the application uses C++ services):

        $ $CLARA_HOME/bin/c_dpe --fe-host localhost

 3. Define the application with a `services.yaml` file.
    There is one file ready in the root of the repository.

 4. Define the data set with the input files to be processed.
    Put a list with the names into text file.
    There is a `files.list` file ready in the root of the repository.

 5. Run the orchestrator with the application configuration file,
    the input and output directories, and the list of input files:

        $ $CLARA_HOME/bin/clara-orchestrator -F -i ./resources -o /tmp/out services.yaml files.list

    To change the number of parallel threads used by the services to process
    requests, pass the `-t <NUM_THREADS>` option.

 6. Inspect the output files in the output directory.


## Example

<a href="https://asciinema.org/a/VkSg1qWC7nAFpa8Wii3LeJ21r" target="_blank"><img src="https://asciinema.org/a/VkSg1qWC7nAFpa8Wii3LeJ21r.svg" width="600"/></a>
