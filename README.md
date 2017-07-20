# Bulls-EYE

BullsEYE is a portable corneal topography generation device based on Android which can be used to detect keratoconus and other corneal deformities by a simple clipper module attached to the smartphone.

## Software and Platforms used:

* Android Studio
* OpenCV with C++
* MATLAB (for prototyping)

## MATLAB Prototyping

This is done for still images shot by the smartphone. There are two basic image analysis codes involved:

* Sharpness detection using focus measure algorithms
* Iris center detection

These are re-written into the dummy Android app in native C++ language and interfaced with the ```MainActivity``` using JNI.

## Android App

The Android app is complete with SQL statements, image storage, image processing and server activity. The launcher activity is ```FirstActivity```. The external camera used for the second prototype can be interfaced with the phone using an OTG cable and the code written into ```MainActivity```.

*Changes*
* Added OpenCV code.
* Added permission prompt.
* Perfected SQL checks.
* Pictures directly save to gallery.
* Added support for external camera.
* Removed crashes.
* Modified layouts and design.

*Dependencies*

* usbCameraCommon
* libuvccamera
* opencv

*Screenshots*
