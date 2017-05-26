LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := C:\Android\NDKTest\app\src\main\jni\com_example_anannyauberoi_ndktest_NativeClass.cpp
LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyLibs

include $(BUILD_SHARED_LIBRARY)
