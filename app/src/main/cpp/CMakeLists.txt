# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("eyealarm")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

include_directories(include)
# OpenCV
add_library( OpenCV SHARED IMPORTED)
set_target_properties(
        OpenCV
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libopencv_java4.so
)
# MNN
add_library( MNN SHARED IMPORTED)
set_target_properties(
        MNN
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libMNN.so
)

# MNN_Express
add_library( MNN_Express SHARED IMPORTED)
set_target_properties(
        MNN_Express
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libMNN_Express.so
)

# Dlib
add_library( Dlib SHARED IMPORTED)
set_target_properties(
        Dlib
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libdlib.so
)

add_library( # Sets the name of the library.
        eyealarm

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        pipeline.cpp
        modules/detection/nanodet.cpp
        modules/landmark/pfldlandmarker.cpp
        modules/utils/funcs.cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
find_library(jnigraphics-lib jnigraphics)

target_link_libraries( # Specifies the target library.
        eyealarm

        # Links the target library to the log library
        # included in the NDK.
        OpenCV
        MNN
        MNN_Express
        Dlib
        ${jnigraphics-lib}
        ${log-lib})