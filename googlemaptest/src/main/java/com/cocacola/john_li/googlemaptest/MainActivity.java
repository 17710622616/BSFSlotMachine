package com.cocacola.john_li.googlemaptest;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cocacola.john_li.googlemaptest.BSSMFragment.ChatFragment;
import com.cocacola.john_li.googlemaptest.BSSMFragment.MainFragment;
import com.cocacola.john_li.googlemaptest.BSSMFragment.SearchFragment;
import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RadioGroup mainRg;
    private RadioButton parkingRb,searchRb,chatRb;
    private FragmentManager fm;
    private Fragment cacheFragment;

    private static int[] frameAnimRes = new int[]{
            R.mipmap.compose_anim_1,
            R.mipmap.compose_anim_2,
            R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4,
            R.mipmap.compose_anim_5,
            R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7,
            R.mipmap.compose_anim_8,
            R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10,
            R.mipmap.compose_anim_11,
            R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13,
            R.mipmap.compose_anim_14,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_16,
            R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18,
            R.mipmap.compose_anim_19
    };

    private SpringFloatingActionMenu springFloatingActionMenu;
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        initData();

        // 点击弹窗
        createFabFrameAnim();
        createFabReverseFrameAnim();

        // 初始化浮動菜單
        final FloatingActionButton fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setScaleType(ImageView.ScaleType.CENTER_CROP);
        fab.setImageDrawable(frameAnim);
//        fab.setImageResource(android.R.drawable.ic_dialog_email);
        fab.setColorPressedResId(R.color.colorPrimary);
        fab.setColorNormalResId(R.color.fab);
        fab.setColorRippleResId(R.color.text_color);
        fab.setShadow(true);
        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                .addMenuItem(R.color.bg, R.mipmap.head_little, "頭像", R.color.text_color,this)
                .addMenuItem(R.color.chat, R.mipmap.evaluate, "歡迎評價", R.color.text_color,this)
                .addMenuItem(R.color.quote, R.mipmap.history_order, "歷史訂單", R.color.text_color,this)
                .addMenuItem(R.color.link, R.mipmap.recommend, "推薦有禮", R.color.text_color,this)
                .addMenuItem(R.color.audio, R.mipmap.suggest, "意見反饋", R.color.text_color,this)
                .addMenuItem(R.color.text, R.mipmap.system_server, "客服熱線", R.color.text_color,this)
                .addMenuItem(R.color.video, R.mipmap.car, "我的車輛", R.color.text_color,this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.bg)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                    }
                })
                .build();
        initSpingFloatingMenu();
    }

    private void initView() {
        mainRg = (RadioGroup) this.findViewById(R.id.main_rg);
        parkingRb = (RadioButton) findViewById(R.id.main_parking);
        searchRb = (RadioButton) findViewById(R.id.main_search);
        chatRb = (RadioButton) findViewById(R.id.main_chat);
    }

    private void setListener() {
        mainRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.main_parking:
                        parkingRb.setChecked(true);
                        searchRb.setChecked(false);
                        chatRb.setChecked(false);
                        switchPages(MainFragment.class,MainFragment.TAG);
                        break;
                    case R.id.main_search:
                        parkingRb.setChecked(false);
                        searchRb.setChecked(true);
                        chatRb.setChecked(false);
                        switchPages(SearchFragment.class,SearchFragment.TAG);
                        break;
                    case R.id.main_chat:
                        parkingRb.setChecked(false);
                        searchRb.setChecked(false);
                        chatRb.setChecked(true);
                        switchPages(ChatFragment.class,ChatFragment.TAG);
                        break;
                }
            }
        });
    }

    private void initData() {
        fm = getSupportFragmentManager();
        FragmentTransaction traslation = fm.beginTransaction();
        cacheFragment = new MainFragment();
        traslation.add(R.id.main_containor,cacheFragment,MainFragment.TAG);
        traslation.commit();
    }

    private void initSpingFloatingMenu() {

    }

    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    @Override
    public void onBackPressed() {
        if(springFloatingActionMenu.isMenuOpen()){
            springFloatingActionMenu.hideMenu();
        }else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        Log.d("TAG eg","onclick");
        MenuItemView menuItemView = (MenuItemView) v;
        Toast.makeText(this,menuItemView.getLabelTextView().getText(),Toast.LENGTH_SHORT).show();
    }

    private void switchPages(Class<?> cls, String tag){
        FragmentTransaction transaction = fm.beginTransaction();
        if (cacheFragment != null){
            transaction.hide(cacheFragment);
        }
        cacheFragment = fm.findFragmentByTag(tag);
        if (cacheFragment != null){
            transaction.show(cacheFragment);
        } else {
            try{
                cacheFragment = (Fragment) cls.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            transaction.add(R.id.main_containor, cacheFragment, tag);
        }
        transaction.commit();
    }
}
