package com.overlay0110.simplestuidio.filter;

import android.graphics.Bitmap;

import com.jabistudio.androidjhlabs.filter.BlockFilter;
import com.jabistudio.androidjhlabs.filter.ColorHalftoneFilter;
import com.jabistudio.androidjhlabs.filter.CrystallizeFilter;
import com.jabistudio.androidjhlabs.filter.EmbossFilter;
import com.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.jabistudio.androidjhlabs.filter.GlowFilter;
import com.jabistudio.androidjhlabs.filter.GrayscaleFilter;
import com.jabistudio.androidjhlabs.filter.InvertFilter;
import com.jabistudio.androidjhlabs.filter.MaskFilter;
import com.jabistudio.androidjhlabs.filter.NoiseFilter;
import com.jabistudio.androidjhlabs.filter.PointillizeFilter;
import com.jabistudio.androidjhlabs.filter.SmearFilter;
import com.jabistudio.androidjhlabs.filter.SolarizeFilter;
import com.jabistudio.androidjhlabs.filter.StampFilter;
import com.jabistudio.androidjhlabs.filter.ThresholdFilter;
import com.jabistudio.androidjhlabs.filter.WeaveFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/*
* http://www.jhlabs.com/ip/filters/
* https://github.com/min2bro/Android-Image-Processing-example-using-JH-Labs-API-s
*/

public class FilterHelper {

    public static HashMap<Integer,String> filters = new LinkedHashMap<Integer,String>(){{
        put(10001, "Normal");
        put(10002, "Exposure");
        put(10003, "Gray");
        put(10004, "Invert");
        put(10005, "Mask");
        put(10006, "Solarize");
        put(10007, "Threshold");
        put(10008, "Block");
        put(10009, "Halftone");
        put(10010, "Crystallize");
        put(10011, "Emboss");
        put(10012, "Noise");
        put(10013, "Pointillize");
        put(10014, "Weave");
        put(10015, "Smear");
        put(10016, "Glow");
        put(10017, "Stamp");
    }};

    public static Bitmap setFilter(String filterName, Bitmap getImage){

        if(filterName.equals("Normal")){
            return getImage;
        }

        if(filterName.equals("Exposure")){
            return applyExposureFilter(getImage);
        }

        if(filterName.equals("Gray")){
            return applyGrayscaleFilter(getImage);
        }

        if(filterName.equals("Invert")){
            return applyInvertFilter(getImage);
        }

        if(filterName.equals("Mask")){
            return applyMaskFilter(getImage);
        }

        if(filterName.equals("Solarize")){
            return applySolarizeFilter(getImage);
        }

        if(filterName.equals("Threshold")){
            return applyThresholdFilter(getImage);
        }

        if(filterName.equals("Block")){
            return applyBlockFilter(getImage);
        }

        if(filterName.equals("Halftone")){
            return applyColorHalftoneFilter(getImage);
        }

        if(filterName.equals("Crystallize")){
            return applyCrystallizeFilter(getImage);
        }

        if(filterName.equals("Emboss")){
            return applyEmbossFilter(getImage);
        }

        if(filterName.equals("Noise")){
            return applyNoiseFilter(getImage);
        }

        if(filterName.equals("Pointillize")){
            return applyPointillizeFilter(getImage);
        }

        if(filterName.equals("Weave")){
            return applyWeaveFilter(getImage);
        }

        if(filterName.equals("Smear")){
            return applySmearFilter(getImage);
        }

        if(filterName.equals("Glow")){
            return applyGlowFilter(getImage);
        }

        if(filterName.equals("Stamp")){
            return applyStampFilter(getImage);
        }

        return getImage;
    }

    private static Bitmap applyExposureFilter(Bitmap getImage){
        try {
            ExposureFilter solarFilter = new ExposureFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyGrayscaleFilter(Bitmap getImage){
        try {
            GrayscaleFilter solarFilter = new GrayscaleFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyInvertFilter(Bitmap getImage){
        try {
            InvertFilter solarFilter = new InvertFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyMaskFilter(Bitmap getImage){
        try {
            MaskFilter solarFilter = new MaskFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applySolarizeFilter(Bitmap getImage){
        try {
            SolarizeFilter solarFilter = new SolarizeFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyThresholdFilter(Bitmap getImage){
        try {
            ThresholdFilter solarFilter = new ThresholdFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyBlockFilter(Bitmap getImage){
        try {
            BlockFilter solarFilter = new BlockFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyColorHalftoneFilter(Bitmap getImage){
        try {
            ColorHalftoneFilter solarFilter = new ColorHalftoneFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyCrystallizeFilter(Bitmap getImage){
        try {
            CrystallizeFilter solarFilter = new CrystallizeFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyEmbossFilter(Bitmap getImage){
        try {
            EmbossFilter solarFilter = new EmbossFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyNoiseFilter(Bitmap getImage){
        try {
            NoiseFilter solarFilter = new NoiseFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyPointillizeFilter(Bitmap getImage){
        try {
            PointillizeFilter solarFilter = new PointillizeFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyWeaveFilter(Bitmap getImage){
        try {
            WeaveFilter solarFilter = new WeaveFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applySmearFilter(Bitmap getImage){
        try {
            SmearFilter solarFilter = new SmearFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyGlowFilter(Bitmap getImage){
        try {
            GlowFilter solarFilter = new GlowFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

    private static Bitmap applyStampFilter(Bitmap getImage){
        try {
            StampFilter solarFilter = new StampFilter();
            int[] src = AndroidUtils.bitmapToIntArray(getImage);
            int width = getImage.getWidth();
            int height = getImage.getHeight();
            int[] dest = solarFilter.filter(src, width, height);
            Bitmap destBitmap = Bitmap.createBitmap(dest, width, height, Bitmap.Config.ARGB_8888);
            return destBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return getImage;
        }
    }

}
