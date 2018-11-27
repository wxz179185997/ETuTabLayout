package tablayout.etu.com.etutablayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.etu.tablayout.ETuTabLayout;
import com.etu.tablayout.listener.CommonTabEnity;
import com.etu.tablayout.listener.TabSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<CommonTabEnity> mListData = new ArrayList<>();

    private List<String> mListTitle = new ArrayList<>();
    private List<Integer> mListSelectedIcon = new ArrayList<>();
    private List<Integer> mListUnSelectedIcon = new ArrayList<>();
    private List<Fragment> mListFragment = new ArrayList<>();
    private ETuTabLayout mTabLayout;
    private ViewPager mViewPager;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRandom = new Random();
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mListTitle.add("标签一");
        mListTitle.add("标签二");
        mListTitle.add("标签三");
        mListTitle.add("标签四");
        mListTitle.add("标签五");
        mListTitle.add("标签六");

        mListFragment.add(new Fragment1());
        mListFragment.add(new Fragment2());
        mListFragment.add(new Fragment3());
        mListFragment.add(new Fragment4());
//        mListFragment.add(new Fragment3());
//        mListFragment.add(new Fragment2());

        mListSelectedIcon.add(R.mipmap.ic_launcher);
        mListSelectedIcon.add(R.mipmap.ic_launcher);
        mListSelectedIcon.add(R.mipmap.ic_launcher);
        mListSelectedIcon.add(R.mipmap.ic_launcher);
        mListSelectedIcon.add(R.mipmap.ic_launcher);
        mListSelectedIcon.add(R.mipmap.ic_launcher);

        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);
        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);
        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);
        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);
        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);
        mListUnSelectedIcon.add(R.mipmap.ic_add_attach);

        mTabLayout.setIndicatorWidth(DeviceUtils.SCREEN_WIDTH_PIXELS / mListFragment.size());

        for (int i = 0; i < mListFragment.size(); i++) {
            mListData.add(new TabInfo(mListTitle.get(i), mListSelectedIcon.get(i), mListUnSelectedIcon.get(i)));
        }
        mViewPager.setAdapter(new TestPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setTabData(mListData);
        mTabLayout.setOnTabSelectedListener(new TabSelectedListener() {

            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position);
//                if (position == 2) {
//                    mTabLayout.getMsgView(position).setText("88");
////                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
//                }

            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    mTabLayout.showMsgView(position, mRandom.nextInt(200) + 1);
                }else if(position==3){
                    mTabLayout.showRedDot(position);

                }

                mTabLayout.setCurrenTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);

    }


    private class TestPagerAdapter extends FragmentPagerAdapter {
        public TestPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }
    }
}
