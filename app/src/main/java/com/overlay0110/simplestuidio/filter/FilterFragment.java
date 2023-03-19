package com.overlay0110.simplestuidio.filter;

import static com.overlay0110.simplestuidio.extra.Util.getScale;

import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.jabistudio.androidjhlabs.filter.GrayscaleFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.overlay0110.simplestuidio.R;
import com.overlay0110.simplestuidio.databinding.FragmentEffectBinding;
import com.overlay0110.simplestuidio.databinding.FragmentFilterBinding;
import com.overlay0110.simplestuidio.extra.Util;
import com.overlay0110.simplestuidio.main.MainActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

public class FilterFragment extends Fragment {
    private FragmentFilterBinding binding;

    Handler handler = new Handler(Looper.getMainLooper());

    HashMap<Integer, RoundedImageView> ivMap;
    HashMap<Integer, TextView> txMap;

    HashMap<Integer, LinearLayout> llMap;

    Bitmap resizeBitmap;
    Bitmap resizeBitmapSub;

    boolean firstRun = false;

    private int sel_id = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        View view = binding.getRoot();
        binding.setVm(this);

        init();
        return view;
    }

    private void init() {
        LinearLayout optionList = binding.selScroll;
        optionList.removeAllViews();

        ivMap = new LinkedHashMap<Integer, RoundedImageView>();
        txMap = new LinkedHashMap<Integer, TextView>();
        llMap = new LinkedHashMap<Integer, LinearLayout>();

        resizeBitmap = Util.getEditImage(false);
        resizeBitmapSub = Util.resizeBitmapImage( Util.getEditImage(false, false) , 100);
        binding.mainImg.setImageBitmap(resizeBitmap);

        for( Integer key : FilterHelper.filters.keySet() ){
            Util.debug(FilterHelper.filters.get(key));
            RoundedImageView riv = new RoundedImageView(getContext());
            riv.setCornerRadius((float) 10);
            riv.setVisibility(View.GONE);
            riv.setImageBitmap( FilterHelper.setFilter(FilterHelper.filters.get(key), resizeBitmapSub) );

            TextView text = new TextView(getContext());
            text.setText(FilterHelper.filters.get(key));
            text.setTextColor(Color.WHITE);

            LinearLayout rl = new LinearLayout(getContext());
            rl.setId(key);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnClick(view.getId());
                }
            });

            ivMap.put(key, riv);
            txMap.put(key, text);
            llMap.put(key, rl);

            rl.addView(riv);
            rl.addView(text);

            rl.setGravity(Gravity.CENTER);
            rl.setOrientation(LinearLayout.VERTICAL);

            optionList.addView(rl);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                checkScale();

                if(!firstRun){
                    firstRun = true;

                    Optional<Integer> firstKey = llMap.keySet().stream().findFirst();
                    if (firstKey.isPresent()) {
                        int key = firstKey.get();
                        onBtnClick(key);
                    }
                }

                if(sel_id != 0){
                    onBtnClick(sel_id);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            init();
        }
    }

    private void resetUi(){
        for( Integer key : txMap.keySet() ){
            txMap.get(key).setTextColor(Color.WHITE);
        }
    }

    public void onBtnClick(int id){
        Util.debug("Filter onBtnClick "+id);
        resetUi();
        txMap.get(id).setTextColor(ContextCompat.getColor(getContext(), R.color.custom_pink));
        sel_id = id;

        Util.setFilterName = FilterHelper.filters.get(id);

        resizeBitmap = Util.getEditImage(false);
        binding.mainImg.setImageBitmap( resizeBitmap );

        moveScroll(id%10000);
    }

    private void moveScroll(int index){
        handler.post(new Runnable() {
            @Override
            public void run() {
                int value = 0;
                value = binding.selScroll.getChildAt( index-1 ).getLeft() - (binding.selOptions.getWidth() - binding.selScroll.getChildAt( index-1 ).getWidth()) / 2;
                binding.selOptions.smoothScrollTo(value, 0);
            }
        });
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
            paramsL.width = paramsL.height = getScale(70, width, 360);

            ivMap.get(key).setVisibility(View.VISIBLE);
        }

    }
}
