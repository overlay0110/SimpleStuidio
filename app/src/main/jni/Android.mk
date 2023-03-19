LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -DANDROID -D__ARM_NEON__ -O2 $(OPTS) -DPROFILE_ON=1 -DHAVE_NEON=1

LOCAL_MODULE := ss_effects

LOCAL_C_INCLUDES += \
	effects/effects.h \

LOCAL_SRC_FILES := \
    effects/saturated.c \
    effects/exposure.c \
    effects/contrast.c \
    effects/hue.c \
    effects/shadows.c \
    effects/highlight.c \
    effects/hsv.c \
    effects/wbalance.c

LOCAL_LDLIBS += -llog -ldl -ljnigraphics -lz -lm -lEGL

include $(BUILD_SHARED_LIBRARY)