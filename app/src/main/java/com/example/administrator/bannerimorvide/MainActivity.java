package com.example.administrator.bannerimorvide;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dawn.qsvideoplayer.DemoQSVideoView;
import com.dawn.qsvideoplayer.IVideoPlayer;
import com.dawn.qsvideoplayer.QSVideoView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, QSVideoView.OnclickBa {

    private List<String> bannerList = new ArrayList<>();
    private ViewPager banner;
    private LinearLayout group;

    private int index;//获取视频的下标
    private String state;//视频播放状态

    private int mIndicatorSelectedResId = R.drawable.radiu_blue;//蓝色导航点
    private int mIndicatorUnselectedResId = R.drawable.radiu_gray;//灰色导航点
    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    DemoQSVideoView demoVideoView;
    String[] arr = {"适应", "填充", "原尺寸", "拉伸", "16:9", "4:3"};
    int mode;//缩放比例
    boolean mute = true;//是否静音
    private boolean isLoad;//视频是否加载
    boolean flag;//记录退出时播放状态 回来的时候继续播放
    boolean start = false;
    int position;//记录销毁时的进度 回来继续盖进度播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        banner = findViewById(R.id.vp_main);
        group = findViewById(R.id.ll_main_dot);
        QSVideoView.setOnclickBa(this);
        ;
        //  设置LinearLayout的子控件的宽高，这里单位是像素。
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
        params.rightMargin = 20;
        // 将点点加入到ViewGroup中
        tips = new ImageView[bannerList.size()];
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            tips[i] = imageView;
            group.addView(imageView);
        }

//        bannerAdapter = new BannerAdapter(this);
//        bannerAdapter.update(bannerList);
        banner.setAdapter(new loadVideoAndImageAdapter());
        // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        // viewPager.setCurrentItem((mImageViews.length) * 100);
        // 一开始不能往滑，但没有图片是默认图片一开始就显示
        banner.setCurrentItem(0);
        // 设置监听，主要是设置点点的背景
        banner.setOnPageChangeListener(this);


    }

    private void initData() {

        bannerList.add("https://erp.vipstation.com.hk/images/itemimg/Q6053406.jpg");
        bannerList.add("https://erp.vipstation.com.hk/upload/image/20160527/20160527150822_4183.jpg");
        bannerList.add("https://erp.vipstation.com.hk/upload/image/20160527/20160527150824_3404.jpg");
        bannerList.add("https://erp.vipstation.com.hk/upload/image/20160527/20160527150825_1275.jpg");
        bannerList.add("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
//        bannerList.add("http://videos.kpie.com.cn/videos/20170526/037DCE54-EECE-4520-AA92-E4002B1F29B0.mp4");

        for (int i = 0; i < bannerList.size(); i++) {
            if (bannerList.get(i).contains(".mp4"))
                index = i;
        }
        bannerList.add(0, bannerList.remove(index));//把视频放在第一个位置

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("instantiateItem", position + "");
        Log.e("instantiateItem", position + "");
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("instantiateItem", position + "");
        setImageBackground(position % bannerList.size());
        int i = position % bannerList.size();
        if (state.equals("4")) {//暂停
            if (i == 0) {
                demoVideoView.play();
            }
        } else if (state.equals("2")) {//播放
            if (i == 0) {
                demoVideoView.play();
            } else {
                demoVideoView.pause();
            }
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e("instantiateItem", state + "");
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setImageResource(mIndicatorSelectedResId);
            } else {
                tips[i].setImageResource(mIndicatorUnselectedResId);
            }
        }
    }


    private class loadVideoAndImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return bannerList == null ? 0 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.e("instantiateItem", position + "");
            final String url = bannerList.get(position % bannerList.size());
            if (!url.contains(".mp4")) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(getApplicationContext()).load(url).into(imageView);
//                ImageLoader.getInstance().displayImage(url, imageView);
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        // 点击图片进入展示（双击放大，单击退出展示）
//                        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
//                        intent.putStringArrayListExtra("image_list", (ArrayList<String>) listImage);
//                        intent.putExtra("image_index", listImage.indexOf(url));
//                        startActivity(intent);
                    }
                });
                return imageView;
            } else {


                if (isLoad == false) {
                    demoVideoView = new DemoQSVideoView(MainActivity.this);
                    Glide.with(getApplicationContext()).load(bannerList.get(1)).into(demoVideoView.getCoverImageView());
                    //    demoVideoView.getCoverImageView().setImageResource(R.mipmap.ic_launcher);//展示视频没有播放时的图片
                    demoVideoView.release();
                    demoVideoView.setUp(url, null);
                    demoVideoView.setMute(mute);
                    //       demoVideoView.play();
                    isLoad = true;
                }
                container.addView(demoVideoView);
                return demoVideoView;
            }
        }
    }

    public void 缩放模式(View v) {
        demoVideoView.setAspectRatio(++mode > 5 ? mode = 0 : mode);
        ((Button) v).setText(arr[mode]);
    }

    public void 静音(View v) {
        demoVideoView.setMute(mute = !mute);
        ((Button) v).setText(mute ? "静音 ON" : "静音 OFF");
    }


    //=======================以下生命周期控制=======================

    @Override
    public void onResume() {
        super.onResume();
        if (flag)
            demoVideoView.play();
        handler.removeCallbacks(runnable);
        if (position > 0) {
            demoVideoView.seekTo(position);
            position = 0;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (demoVideoView.isSystemFloatMode())
            return;
        //暂停
        flag = demoVideoView.isPlaying();
        demoVideoView.pause();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (demoVideoView.isSystemFloatMode())
            return;
        //不马上销毁 延时10秒
        handler.postDelayed(runnable, 1000 * 10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();//销毁
        if (demoVideoView.isSystemFloatMode())
            demoVideoView.quitWindowFloat();
        demoVideoView.release();
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (demoVideoView.getCurrentState() != IVideoPlayer.STATE_AUTO_COMPLETE)
                position = demoVideoView.getPosition();
            demoVideoView.release();
        }
    };

    //具体的实现方法
    @Override
    public void OnclickBa(String state) {
        this.state = state;
    }
}