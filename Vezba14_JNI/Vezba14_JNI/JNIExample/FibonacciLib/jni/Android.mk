LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libfibonacci
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := rtrk_pnrs_jniexample_FibonacciNative.c

include $(BUILD_SHARED_LIBRARY)
