package app.dongzhao.com.graduationproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import app.dongzhao.com.graduationproject.R;
import app.dongzhao.com.graduationproject.utils.PrefUtil;

/**
 * 新手引导界面
 */
public class GuidActivity extends Activity {

    private ViewPager mViewPager;
    private MyViewPager myViewPager;

    private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();
    private int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private LinearLayout llcontains;
    private ImageView ivRedPoint;
    private int distancePoint;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llcontains = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        mButton = (Button) findViewById(R.id.btn_start);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到主页面
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //引导页走完就不是第一次登录了,设置为false
                PrefUtil.setBoolean(getApplicationContext(), "is_first_enter", false);
            }
        });
        //初始化图片,加载指示器
        initDate();

        myViewPager = new MyViewPager();
        mViewPager.setAdapter(myViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //计算小红点互动过程中的位置
                int marginLeft = (int) (distancePoint * positionOffset + position * distancePoint);
                //动态布局,相对于父窗体位置
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = marginLeft;// 修改左边距

                // 重新设置布局参数
                ivRedPoint.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {
                //选中某一页时回调
                if (position == mImageViewList.size() - 1) {
                    //最后一页显示才按钮
                    mButton.setVisibility(View.VISIBLE);
                } else {
                    mButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //开始翻页时候回调
            }
        });

        //计算两个小灰点之间的距离
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //布局加载完后的回调
            @Override
            public void onGlobalLayout() {

                // 移除监听,避免重复回调
                ivRedPoint.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                distancePoint = llcontains.getChildAt(1).getLeft() - llcontains.getChildAt(0).getLeft();
            }

        });
    }
    /**
     * 初始化数据
     */
    private void initDate() {
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            //把图片添加进集合
            mImageViewList.add(view);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guid_point_gray);
            //找父控件的LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //不是第一个圆点,就添加间距
            if (i > 0) {
                params.leftMargin = 10;
            }
            //给小灰点重新设置布局参数
            point.setLayoutParams(params);
            //把小灰点添加进线性布局
            llcontains.addView(point);
        }
    }


    class MyViewPager extends PagerAdapter {

        /**
         * 返回item数量
         */
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        /**
         * 服用item
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化一个item
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        /**
         * 销毁一个item
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
