package com.overlay0110.simplestuidio.main;

import static com.overlay0110.simplestuidio.extra.Util.getScale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.overlay0110.simplestuidio.R;
import com.overlay0110.simplestuidio.crop.CropFragment;
import com.overlay0110.simplestuidio.databinding.ActivityMainBinding;
import com.overlay0110.simplestuidio.databinding.EditLayoutBinding;
import com.overlay0110.simplestuidio.effect.EffectFragment;
import com.overlay0110.simplestuidio.extra.Util;
import com.overlay0110.simplestuidio.filter.FilterFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class EditActivity extends FragmentActivity implements NavigationBarView.OnItemSelectedListener {

    Handler handler = new Handler(Looper.getMainLooper());

    private EditLayoutBinding binding;

    HashMap<Integer, Fragment> fragmentInfo = new HashMap<Integer, Fragment>();

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.edit_layout);
        binding.setVm(this);
        init();
    }

    private void init() {
        binding.bnBar.setOnItemSelectedListener(EditActivity.this);

        fragmentManager = getSupportFragmentManager();

        fragmentInfo.put(R.id.effect_item, new EffectFragment());
        fragmentInfo.put(R.id.filter_item, null);
        fragmentInfo.put(R.id.crop_item, null);

        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentInfo.get(R.id.effect_item)).commitAllowingStateLoss();

        handler.post(new Runnable() {
            @Override
            public void run() {
                checkScale();
            }
        });
    }

    public void onBtnClick(int i) {
        Util.debug("Edit onBtnClick "+i);
        if (i == 1) {
            finish();
        }

        if (i == 2) {
            boolean isSave = Util.saveImage(this, Util.getEditImage(false, true, true) );
            if(isSave){
                Toast.makeText(this, "Photo save success", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Photo save failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addFragment(int value, Fragment getFragment){
        if(fragmentInfo.get(value) == null){
            fragmentInfo.put(value, getFragment);
            fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentInfo.get(value)).commitAllowingStateLoss();
        }
    }

    private void showFragments(int value){
        for( Integer key : fragmentInfo.keySet() ){
            if(fragmentInfo.get(key) != null){
                fragmentManager.beginTransaction().hide(fragmentInfo.get(key)).commitAllowingStateLoss();
            }
        }

        if(fragmentInfo.get(value) != null){
            fragmentManager.beginTransaction().show(fragmentInfo.get(value)).commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.effect_item:
                if(fragmentInfo.get(item.getItemId()) == null){
                    addFragment(item.getItemId(), new EffectFragment());
                }
                else{
                    showFragments(item.getItemId());
                }
                return true;
            case R.id.filter_item:
                if(fragmentInfo.get(item.getItemId()) == null){
                    addFragment(item.getItemId(), new FilterFragment());
                }
                else{
                    showFragments(item.getItemId());
                }
                return true;
            case R.id.crop_item:
                if(fragmentInfo.get(item.getItemId()) == null){
                    addFragment(item.getItemId(), new CropFragment());
                }
                else{
                    showFragments(item.getItemId());
                }
                return true;
        }
        return false;
    }

    private void checkScale() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams paramsL;
        RelativeLayout.LayoutParams paramsR;
        GridLayout.LayoutParams paramsG;

        paramsR = (RelativeLayout.LayoutParams) binding.headBar.getLayoutParams();
        paramsR.height = getScale(50, width, 360);

        paramsR = (RelativeLayout.LayoutParams) binding.backBtn.getLayoutParams();
        paramsR.width = getScale(50, width, 360);

        paramsL = (LinearLayout.LayoutParams) binding.backIcon.getLayoutParams();
        paramsL.width = paramsL.height = getScale(24, width, 360);

        paramsR = (RelativeLayout.LayoutParams) binding.saveBtn.getLayoutParams();
        paramsR.width = getScale(50, width, 360);

        paramsL = (LinearLayout.LayoutParams) binding.saveIcon.getLayoutParams();
        paramsL.width = paramsL.height = getScale(24, width, 360);
    }
}
