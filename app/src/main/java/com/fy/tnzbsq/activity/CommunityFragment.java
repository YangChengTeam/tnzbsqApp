package com.fy.tnzbsq.activity;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fy.tnzbsq.App;
import com.fy.tnzbsq.R;
import com.fy.tnzbsq.common.Contants;
import com.fy.tnzbsq.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by admin on 2017/9/7.
 */

public class CommunityFragment extends CustomBaseFragment {

    @BindView(R.id.iv_add_note)
    ImageView addNoteImageView;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.note_indicator)
    FixedIndicatorView mFixedIndicatorView;

    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;

    @BindView(R.id.ground_view)
    View groundView;

    @BindView(R.id.tv_friends_circle)
    TextView mFriendCircleTextView;

    @BindView(R.id.tv_game)
    TextView mGameTextView;

    private FragmentAdapter mFragmentAdapter;

    private int[] location;

    private float maxSize = 0;

    //子菜单打开，关闭时的动画
    private Animation qq_friend_in, game_in, qq_friend_out, game_out;

    float lastSize = 0;

    public boolean isShow = false;

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.community_fragment, null);
            RxBus.get().register(this);
            ButterKnife.bind(this, mRootView);
            init();
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void init() {
        //topLayout.setVisibility(View.GONE);

        location = new int[]{1000, 0};
        maxSize = 2.5f * 15;

        qq_friend_in = AnimationUtils.loadAnimation(getActivity(), R.anim.friends_in);
        game_in = AnimationUtils.loadAnimation(getActivity(), R.anim.game_in);
        qq_friend_out = AnimationUtils.loadAnimation(getActivity(), R.anim.friends_out);
        game_out = AnimationUtils.loadAnimation(getActivity(), R.anim.game_out);

        mFixedIndicatorView.setAdapter(new MyAdapter(3));

        mFixedIndicatorView.setScrollBar(new ColorBar(getActivity(), ContextCompat.getColor(getActivity(), R.color.colorAccent), 6));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        int unSelectColor = ContextCompat.getColor(getActivity(), R.color.black1);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        mFixedIndicatorView.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
        mFixedIndicatorView.setCurrentItem(0, true);

        mFragmentAdapter = new FragmentAdapter(getFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFixedIndicatorView.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        RxView.clicks(addNoteImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (menuLayout.getVisibility() == View.INVISIBLE) {
                    menuLayout.setVisibility(View.VISIBLE);
                    menuLayout.setFocusable(true);
                    menuLayout.setClickable(true);

                    animationOpen.setDuration(600);
                    animationOpen.setInterpolator(new DecelerateInterpolator());
                    animationOpen.setFillAfter(true);
                    groundView.startAnimation(animationOpen);
                    mFriendCircleTextView.startAnimation(qq_friend_in);
                    mGameTextView.startAnimation(game_in);

                    isShow = true;
                    //showAddIv.setClickable(false);
                } else {
                    closeMenu();
                }
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });

        mFriendCircleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddNoteActivity("1");
            }
        });

        mGameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddNoteActivity("2");
            }
        });
    }

    public void toAddNoteActivity(String type) {
        if (App.loginUser != null) {
            Intent intent = new Intent(getActivity(), CommunityAddActivity.class);
            intent.putExtra("note_type", type);
            startActivity(intent);
            closeMenu();
        } else {
            closeMenu();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Contants.COMMUNITY_ADD_REFRESH)
            }
    )
    public void rxSetCurrentFragment(String tag) {
        if (!StringUtils.isEmpty(tag)) {
            int addItem = Integer.parseInt(tag);
            mFixedIndicatorView.setCurrentItem(addItem, true);
            mViewPager.setCurrentItem(addItem);
        }
    }

    private CommunitySubFragment courseFragment1;
    private CommunitySubFragment courseFragment2;
    private CommunitySubFragment courseFragment3;

    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            if (position == 0) {
                if (courseFragment1 == null) {
                    bundle.putInt("type", 3);
                    courseFragment1 = new CommunitySubFragment();
                    courseFragment1.setArguments(bundle);
                }
                return courseFragment1;
            } else if (position == 1) {
                if (courseFragment2 == null) {
                    bundle.putInt("type", 1);
                    courseFragment2 = new CommunitySubFragment();
                    courseFragment2.setArguments(bundle);
                }
                return courseFragment2;
            } else if (position == 2) {
                if (courseFragment3 == null) {
                    bundle.putInt("type", 2);
                    courseFragment3 = new CommunitySubFragment();
                    courseFragment3.setArguments(bundle);
                }
                return courseFragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {
        private final String[] titles = new String[]{"热门", "自拍交友", "游戏开黑"};

        private final int count;

        public MyAdapter(int count) {
            super();
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.community_tab, parent, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(titles[position]);
            return convertView;
        }
    }

    private Animation animationOpen = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float size = (maxSize - 1) * interpolatedTime + 1;
            lastSize = size;
            Matrix matrix = t.getMatrix();

            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {

            }
        }
    };

    private Animation animationClose = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            float size = lastSize - (maxSize - 1) * interpolatedTime + 1;
            Matrix matrix = t.getMatrix();
            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {
                menuLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mFriendCircleTextView.startAnimation(qq_friend_out);
        mGameTextView.startAnimation(game_out);

        animationClose.setDuration(500);
        animationClose.setInterpolator(new DecelerateInterpolator());
        animationClose.setFillAfter(true);
        groundView.startAnimation(animationClose);
        isShow = false;
    }

}
