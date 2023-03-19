#include "effects.h"

void JNIFUNCF(EffectHelper, applySaturation, jobject bitmap, jint width, jint height, jfloat saturation)
{
    uint8_t* destination = 0;
    AndroidBitmap_lockPixels(env, bitmap, (void**) &destination);
    int i;
    int len = width * height * 4;
    float Rf = 0.2999f;
    float Gf = 0.587f;
    float Bf = 0.114f;
    float S = saturation;;
    float MS = 1.0f - S;
    float Rt = Rf * MS;
    float Gt = Gf * MS;
    float Bt = Bf * MS;
    float R, G, B;
    for (i = 0; i < len; i+=4)
    {
        int r = destination[RED];
        int g = destination[GREEN];
        int b = destination[BLUE];
        R = r;
        G = g;
        B = b;

        float Rc = R * (Rt + S) + G * Gt + B * Bt;
        float Gc = R * Rt + G * (Gt + S) + B * Bt;
        float Bc = R * Rt + G * Gt + B * (Bt + S);

        destination[RED] = CLAMP(Rc);
        destination[GREEN] = CLAMP(Gc);
        destination[BLUE] = CLAMP(Bc);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}
