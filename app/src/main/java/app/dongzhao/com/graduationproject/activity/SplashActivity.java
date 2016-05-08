package app.dongzhao.com.graduationproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import app.dongzhao.com.graduationproject.R;
import app.dongzhao.com.graduationproject.utils.PrefUtil;

/**
 * 闪屏界面
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);

        // 缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);// 保持动画结束状态

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);// 动画时间
        animAlpha.setFillAfter(true);// 保持动画结束状态

        // 动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);
        //开始动画
        rl_splash.startAnimation(set);
        //设置动画监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirstEnter = PrefUtil.getBoolean(getApplicationContext(), "is_first_enter", true);
                Intent intent;
                //判断是否是第一次登录app
                if (isFirstEnter) {
                    //进入新手引导
                    intent = new Intent(getApplicationContext(), GuidActivity.class);
                } else {
                    //进入主界面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                //开启界面
                startActivity(intent);
                //关闭自己界面
                finish();
            }
        });
    }
}

