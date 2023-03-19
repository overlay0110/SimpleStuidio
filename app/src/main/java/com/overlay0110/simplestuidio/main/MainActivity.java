package com.overlay0110.simplestuidio.main;

import static com.overlay0110.simplestuidio.extra.Util.debug;
import static com.overlay0110.simplestuidio.extra.Util.getScale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.overlay0110.simplestuidio.R;
import com.overlay0110.simplestuidio.databinding.ActivityMainBinding;
import com.overlay0110.simplestuidio.effect.EffectFragment;
import com.overlay0110.simplestuidio.extra.Util;
import com.overlay0110.simplestuidio.filter.FilterFragment;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    private ActivityMainBinding binding;

    private HashMap<Integer, RoundedImageView> ivMap;

    private static MainActivity mainActivity;

    private ArrayList<String> img_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_layout);
        SplashScreen.installSplashScreen(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(this);

        mainActivity = this;

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
                        init();
                    }
                },500);

            }
        });

        init();
    }

    private void init() {
        Util.debug("start init");
        if(!Util.hasPermissions(this, Util.storagePermissions())){
            Util.debug("start storagePermissions if");
            binding.gridLayout.setVisibility(View.GONE);

            binding.permissionText.setVisibility(View.VISIBLE);
            binding.button.setVisibility(View.VISIBLE);
            return;
        }
        else{
            Util.debug("start storagePermissions false");
            binding.gridLayout.setVisibility(View.VISIBLE);

            binding.permissionText.setVisibility(View.GONE);
            binding.button.setVisibility(View.GONE);
        }

        GridLayout imageList = binding.gridLayout;
        imageList.removeAllViews();

        ivMap = new HashMap<Integer, RoundedImageView>();

        img_list = getAllImgList();

        int img_len = img_list.size();

        Util.debug("img list size "+img_len);
        if(img_len == 0){
            binding.noPhotoAlert.setVisibility(View.VISIBLE);
        }
        else{
            binding.noPhotoAlert.setVisibility(View.GONE);
        }

        for(int i=0;i<img_len;i++){
            RoundedImageView riv = new RoundedImageView(this);
            riv.setCornerRadius((float) 80);
            riv.setVisibility(View.GONE);
            try {
                Bitmap originalBitmap = Util.getBitmapFromUri( this, Uri.parse( img_list.get(i) ) );
                Bitmap resizeBitmap = Util.resizeBitmapImage(originalBitmap, 1000);
                riv.setImageBitmap( resizeBitmap );
            }
            catch (Exception e){
                e.printStackTrace();
                riv.setBackgroundColor(Color.BLACK);
            }

//            riv.setImageResource(R.drawable.a_icon2);
            riv.setId(i);
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnClick(view.getId());
                }
            });

            ivMap.put(i, riv);

            imageList.addView(riv);
        }



        handler.post(new Runnable() {
            @Override
            public void run() {
                checkScale();
            }
        });

    }

    private ArrayList<String> getAllImgList(){
        Util.debug("getAllImgList");
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null);

        ArrayList<String> pathArr = new ArrayList<>();

        if (cursor != null) {
            Util.debug("cursor not null");
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                Util.debug("check path "+path);
                pathArr.add(path);
            }
            cursor.close();
        }
        else{
            Util.debug("cursor null");
        }

        return pathArr;
    }

    public void onBtnClick(int value){
        Util.debug("onBtnClick "+value);

        if(value == 999999999){
            if(!Util.hasPermissions(this, Util.storagePermissions())){
                ActivityCompat.requestPermissions(MainActivity.this, Util.storagePermissions(), 1);
            }
        }
        else{
            try {
                Util.selImage = img_list.get(value);
                Util.originalBitmap = Util.getBitmapFromUri( this, Uri.parse( Util.selImage ) );
                Util.resetImage();
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static MainActivity getMain(){
        return mainActivity;
    }

    private void checkScale() {
        int width = Util.getScreenWidth(this);

        LinearLayout.LayoutParams paramsL;
        RelativeLayout.LayoutParams paramsR;
        GridLayout.LayoutParams paramsG;
        FrameLayout.LayoutParams paramsF;

//        paramsR = (RelativeLayout.LayoutParams) binding.button.getLayoutParams();
//        paramsR.width = getScale(350, width, 360);

        binding.scrollView.setPadding(0,0,0,getScale(20f, width, 360));

        int len = binding.gridLayout.getChildCount();
        View childView;
        for (int i = 0; i < len; i++) {
            childView = binding.gridLayout.getChildAt(i);
            paramsG = (GridLayout.LayoutParams) childView.getLayoutParams();

            paramsG.width = paramsG.height = getScale(100f, width, 360);
            paramsG.topMargin = getScale(20f, width, 360);
            paramsG.leftMargin = getScale(15f, width, 360);

//            childView.setLayoutParams(paramsG);
            childView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            Util.debug("requestCode "+requestCode);
            if(requestCode == 1){
                init();
            }

        }
    }
}
