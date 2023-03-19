package com.overlay0110.simplestuidio.extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.makeramen.roundedimageview.RoundedImageView;
import com.overlay0110.simplestuidio.effect.EffectHelper;
import com.overlay0110.simplestuidio.filter.FilterHelper;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Util {
    private static boolean isDebug = false;
    private static String tagName = "APP_CHECK";

    public static String selImage = "";

    public static Bitmap originalBitmap;

    public static Bitmap originalCropBitmap = null;

    public static String setFilterName = "Normal";

    private static HashMap<String, Float> effectValues = null;

    public static void debug(String str){
        if(!isDebug){
            return;
        }
        Log.e(tagName, str);
    }

    public static void debug(int value){
        if(!isDebug){
            return;
        }
        Log.e(tagName, String.valueOf(value));
    }

    public static void debug(float value){
        if(!isDebug){
            return;
        }
        Log.e(tagName, String.valueOf(value));
    }

    public static int getScale(int size, int currentWidth, int baseWidth){
        return size * currentWidth / baseWidth;
    }

    public static int getScale(float size, int currentWidth, int baseWidth){
        return (int)(size * currentWidth / baseWidth);
    }

    private static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_AUDIO,
//            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] storagePermissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    debug("catch "+permission);
                    return false;
                }
            }
        }
        return true;
    }

    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri originalUri) throws IOException {
        Uri returnedUri = null;
        if (originalUri.getScheme() == null){
            returnedUri = Uri.fromFile(new File(originalUri.getPath()));
        }else{
            returnedUri = originalUri;
        }

        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(returnedUri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Bitmap이미지의 가로, 세로 사이즈를 리사이징 한다.
     *
     * @param source 원본 Bitmap 객체
     * @param maxResolution 제한 해상도
     * @return 리사이즈된 이미지 Bitmap 객체
     */
    public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
    {

        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        debug("resizeBitmapImage w "+width+" h "+height);

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        debug("resizeBitmapImage New w "+newWidth+" h "+newHeight);

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    private static HashMap<String, Float> resetEffectValues(){
        EffectHelper nativeEffects = new EffectHelper();
        effectValues = new HashMap<String, Float>();
        for( Integer key : nativeEffects.list.keySet() ) {
            effectValues.put(nativeEffects.list.get(key), 0.5f);
        }
        return effectValues;
    }

    public static float getEffectValues(String effectName){
        if(effectValues == null){
            resetEffectValues();
        }
        return effectValues.get(effectName);
    }

    public static void setEffectValues(String effectName, float value){
        if(effectValues == null){
            resetEffectValues();
        }
        effectValues.put(effectName, value);
    }

    public static Bitmap getEditImage(boolean isCrop){
        return getEditImage(isCrop, true, false);
    }

    public static Bitmap getEditImage(boolean isCrop, boolean isFilter){
        return getEditImage(isCrop, isFilter, false);
    }

    public static Bitmap getEditImage(boolean isCrop, boolean isFilter, boolean isSave){
        Bitmap result = originalBitmap;

        if(!isCrop){
            if(originalCropBitmap != null){
                result = originalCropBitmap;
            }

            if(!isSave){
                result = resizeBitmapImage(result, 700);
            }

            if(isFilter){
                Bitmap copy = result;

                if(effectValues != null){
                    EffectHelper nativeEffects = new EffectHelper();

                    for( String key : effectValues.keySet() ) {
                        if(effectValues.get(key) != 0.5f){
                            copy = nativeEffects.setEffect(key, copy, effectValues.get(key));
                        }
                    }
                }

                if(setFilterName != "Normal"){
                    copy = FilterHelper.setFilter(setFilterName, copy);
                }

                result = copy;
            }
        }

        return result;
    }

    public static void resetImage(){
        originalCropBitmap = null;
        setFilterName = "Normal";
        resetEffectValues();
    }

    public static boolean saveImage(Context context, Bitmap bitmap){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String dirName = "/";
            String fileName = System.currentTimeMillis() + ".png";
            File savePath = new File(rootPath + dirName);
            savePath.mkdirs();

            File file = new File(savePath, fileName);
            if (file.exists()) file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

                MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }



}
