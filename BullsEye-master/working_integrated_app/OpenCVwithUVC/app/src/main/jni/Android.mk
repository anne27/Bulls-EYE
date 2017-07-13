LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

#opencv
OPENCVROOT := C:\OpenCV-android-sdk
OPENCV_CAMERA_MODULES := on
OPENCV_INSTALL_MODULES := on
OPENCV_LIB_TYPE := SHARED
include $(OPENCVROOT)/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := \
                    C:\Android\OpenCVwithUVC\app\src\main\jni\com_serenegiant_opencvwithuvc_cvClass.cpp \
                    C:\Android\OpenCVwithUVC\app\src\main\jni\com_serenegiant_opencvwithuvc_DetectIris.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := LibsCV

include $(BUILD_SHARED_LIBRARY)