package com.overlay0110.simplestuidio.crop;

import static com.overlay0110.simplestuidio.extra.Util.getScale;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.overlay0110.simplestuidio.R;
import com.overlay0110.simplestuidio.databinding.FragmentCropBinding;
import com.overlay0110.simplestuidio.databinding.FragmentFilterBinding;
import com.overlay0110.simplestuidio.extra.Util;
import com.overlay0110.simplestuidio.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class CropFragment extends Fragment {
    private FragmentCropBinding binding;

    Handler handler = new Handler(Looper.getMainLooper());

    HashMap<Integer, View> ivMap;
    HashMap<Integer, TextView> txMap;

    HashMap<Integer, LinearLayout> llMap;

    HashMap<Integer, TextView> searchMap;

    Bitmap resizeBitmap;
    Bitmap resizeBitmapSub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crop, container, false);
        View view = binding.getRoot();
        binding.setVm(this);

        binding.mainImg.setOnSetCropOverlayReleasedListener(new CropImageView.OnSetCropOverlayReleasedListener() {
            @Override
            public void onCropOverlayReleased(@Nullable Rect rect) {
                Util.debug("onCropOverlayReleased");
                Util.originalCropBitmap = binding.mainImg.getCroppedImage();
            }
        });

        init();
        return view;
    }

    private void init() {
        LinearLayout optionList = binding.selScroll;
        optionList.removeAllViews();

        ivMap = new HashMap<Integer, View>();
        txMap = new HashMap<Integer, TextView>();
        llMap = new HashMap<Integer, LinearLayout>();
        searchMap = new HashMap<Integer, TextView>();

        ArrayList<Integer> icons = new ArrayList<Integer>();
        icons.add(R.drawable.baseline_crop_rotate_24);
        icons.add(R.drawable.baseline_panorama_vertical_24);
        icons.add(R.drawable.baseline_panorama_horizontal_24);

        ArrayList<String> iconTexts = new ArrayList<String>();
        iconTexts.add("Rotation");

        resizeBitmap = Util.getEditImage(true);
        resizeBitmapSub = Util.resizeBitmapImage(resizeBitmap, 1000);
        binding.mainImg.setImageBitmap(resizeBitmap);

        for(int i=0;i<iconTexts.size();i++){
            int num = i+1;

            View vi = new View(getContext());
            vi.setId(num);
            vi.setBackgroundResource(icons.get(i));

            TextView text = new TextView(getContext());
            text.setText(iconTexts.get(i));
            text.setTextColor(Color.WHITE);
            text.setId(num+1000);

            LinearLayout rl = new LinearLayout(getContext());
            rl.setId(num+10000);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnClick(view.getId());
                }
            });

            ivMap.put(num+10000, vi);
            txMap.put(num+1000, text);
            llMap.put(num+10000, rl);
            searchMap.put(num+10000, text);

            rl.addView(vi);
            rl.addView(text);

            rl.setGravity(Gravity.CENTER);
            rl.setOrientation(LinearLayout.VERTICAL);

            optionList.addView(rl);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                checkScale();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void onBtnClick(int id){
        Util.debug("Crop onBtnClick "+id);

        if(id == 10001){
            binding.mainImg.rotateImage(90);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Util.originalCropBitmap = binding.mainImg.getCroppedImage();
                }
            });
        }

    }

    private void checkScale() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.getMain().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams paramsL;
        RelativeLayout.LayoutParams paramsR;
        GridLayout.LayoutParams paramsG;
        FrameLayout.LayoutParams paramsF;

        paramsF = (FrameLayout.LayoutParams) binding.selScroll.getLayoutParams();
        paramsF.height = getScale(110, width, 360);
        paramsF.topMargin = paramsF.rightMargin = getScale(10, width, 360);

        int len = binding.selScroll.getChildCount();
        View childView;
        for (int i = 0; i < len; i++) {
            childView = binding.selScroll.getChildAt(i);
            paramsL = (LinearLayout.LayoutParams) childView.getLayoutParams();

            paramsL.leftMargin = getScale(10, width, 360);

            childView.setLayoutParams(paramsL);
        }

        for( Integer key : ivMap.keySet() ){
            paramsL = (LinearLayout.LayoutParams) ivMap.get(key).getLayoutParams();
            paramsL.width = paramsL.height = getScale(50, width, 360);
            paramsL.bottomMargin = getScale(10, width, 360);
        }

    }
}
