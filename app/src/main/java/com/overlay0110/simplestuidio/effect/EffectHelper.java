package com.overlay0110.simplestuidio.effect;

import android.graphics.Bitmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EffectHelper {
    static {
        System.loadLibrary("ss_effects");
    }

    double[] mHighlightCurve = { 0.0, 0.32, 0.418, 0.476, 0.642 };
    SplineMath mSpline = new SplineMath(5);

    public HashMap<Integer,String> list = new LinkedHashMap<Integer,String>(){{
        put(10001, "Brightness");
        put(10002, "Exposure");
        put(10003, "Contrast");
        put(10004, "Saturation");
        put(10005, "Hue");
        put(10006, "White balance");
        put(10007, "Highlight");
        put(10008, "Shadow");
    }};

    native protected void applyExposure(Bitmap bitmap, int w, int h, float value);
    native protected void applyContrast(Bitmap bitmap, int w, int h, float value);
    native protected void applySaturation(Bitmap bitmap, int w, int h, float value);
    native protected void applyHue(Bitmap bitmap, int w, int h, float []matrix);
    native protected void applyWBanlance(Bitmap bitmap, int w, int h, int locX, int locY, float value);
    native protected void applyHighlight(Bitmap bitmap, int w, int h, float[] luminanceMap);
    native protected void applyShadows(Bitmap bitmap, int w, int h, float value);

    /**
     * 프리뷰 이미지 효과 적용
     * @param effectName 효과 이름
     * @param getImage 적용할 이미지
     * @param value 상태값 0~1 사이
     * @return
     */
    public Bitmap setEffect(String effectName, Bitmap getImage, float value){
        if(effectName.equals("Brightness")){
            return effectBrightness(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Exposure")){
            return effectExposure(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Contrast")){
            return effectContrast(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Saturation")){
            return effectSaturation(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Hue")){
            return effectHue(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("White balance")){
            return effectWBanlance(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Highlight")){
            return effectHighlight(getImage, setNormal(-100, 100, value) );
        }

        if(effectName.equals("Shadow")){
            return effectShadows(getImage, setNormal(-100, 100, value) );
        }

        return getImage;
    }

    public float setNormal(float min, float max, float inValue){
        return setNormal(min, max, inValue, 0f, 1f);
    }

    public float setNormal(float min, float max, float inValue, float setMin, float setMax){
        float data = inValue;
        float actual_min = setMin;
        float actual_max = setMax;

        float value = (data - actual_min)*((max - min)/(actual_max - actual_min)) + min;
        float result = (float) (Math.round(value*100)/100.0);

        return result;
    }

    public Bitmap effectBrightness(Bitmap bitmap, float brightness){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        applyExposure(bitmap, w, h, brightness);
        return bitmap;
    }

    public Bitmap effectExposure(Bitmap bitmap, float value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        applyExposure(bitmap, w, h, value*2);
        return bitmap;
    }
    public Bitmap effectContrast(Bitmap bitmap, float value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        applyContrast(bitmap, w, h, value/3);
        return bitmap;
    }
    public Bitmap effectSaturation(Bitmap bitmap, float value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        value = setEffectNormal(0f, 2f, value);

        applySaturation(bitmap, w, h, value);
        return bitmap;
    }
    public Bitmap effectHue(Bitmap bitmap, float value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        ColorSpaceMatrix cmatrix = new ColorSpaceMatrix();
        cmatrix.identity();
        cmatrix.setHue(value);

        applyHue(bitmap, w, h, cmatrix.getMatrix());

        return bitmap;
    }
    final float WB_THRES_HOLD = 400;
    public Bitmap effectWBanlance(Bitmap bitmap, float value){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        applyWBanlance(bitmap, w, h, -1, -1, (value+WB_THRES_HOLD)/WB_THRES_HOLD);
        return bitmap;
    }
    public Bitmap effectHighlight(Bitmap bitmap, float value) {
        double t = value/100.;
        for (int i = 0; i < 5; i++) {
            double x = i / 4.;
            double y = mHighlightCurve[i] *t+x*(1-t);
            mSpline.setPoint(i, x, y);
        }

        float[][] curve = mSpline.calculatetCurve(256);
        float[] luminanceMap = new float[curve.length];
        for (int i = 0; i < luminanceMap.length; i++) {
            luminanceMap[i] = curve[i][1];
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        applyHighlight(bitmap, w, h, luminanceMap);
        return bitmap;
    }
    public Bitmap effectShadows(Bitmap bitmap, float value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        applyShadows(bitmap, w, h, value);
        return bitmap;
    }

    private float setEffectNormal(float min, float max, float inValue){
        float data = inValue;
        float actual_min = -100f;
        float actual_max = 100f;

        float value = (data - actual_min)*((max - min)/(actual_max - actual_min)) + min;
        float temp = (float) (Math.round(value*100)/100.0);

        return temp;
    }

}