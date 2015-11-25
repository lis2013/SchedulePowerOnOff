LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES := bouncycastle
LOCAL_STATIC_JAVA_LIBRARIES := gson

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SchedulePowerOnOff
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := gson:libs/gson-2.3.1.jar
include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
