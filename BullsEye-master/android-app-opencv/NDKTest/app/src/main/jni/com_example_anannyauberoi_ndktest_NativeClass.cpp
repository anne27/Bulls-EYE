#include <com_example_anannyauberoi_ndktest_NativeClass.h>

JNIEXPORT jstring JNICALL Java_com_example_anannyauberoi_ndktest_NativeClass_getMessageFromJNI
  (JNIEnv *env , jclass obj){
  return env->NewStringUTF("This is a message from JNI");
  }

