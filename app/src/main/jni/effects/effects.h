#ifndef EFFECTS_H
#define EFFECTS_H

#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <android/bitmap.h>

typedef unsigned int Color;

#define SetColor(a, r, g, b) (((a) << 24) | ((b) << 16) | ((g) << 8) | ((r) << 0));
#define GetA(color) (((color) >> 24) & 0xFF)
#define GetB(color) (((color) >> 16) & 0xFF)
#define GetG(color) (((color) >> 8) & 0xFF)
#define GetR(color) (((color) >> 0) & 0xFF)

#define MIN(a, b) ((a) < (b) ? (a) : (b))
#define MAX(a, b) ((a) > (b) ? (a) : (b))

#define LOG(msg...) __android_log_print(ANDROID_LOG_VERBOSE, "NativeFilters", msg)

#define JNIFUNCF(cls, name, vars...) Java_com_overlay0110_simplestuidio_effect_ ## cls ## _ ## name(JNIEnv* env, jobject obj_unused __unused, vars)

#define RED i
#define GREEN (i+1)
#define BLUE (i+2)
#define ALPHA (i+3)
#define CLAMP(c) (MAX(0, MIN(255, c)))

extern unsigned char  clamp(int c);
extern int clampMax(int c,int max);

extern void rgb2hsv( unsigned char *rgb,int rgbOff,unsigned short *hsv,int hsvOff);
extern void hsv2rgb(unsigned short *hsv,int hsvOff,unsigned char  *rgb,int rgbOff);
extern void filterRedEye(unsigned char *src, unsigned char *dest, int iw, int ih, short *rect);
extern double fastevalPoly(double *poly,int n, double x);

#endif //EFFECTS_H
