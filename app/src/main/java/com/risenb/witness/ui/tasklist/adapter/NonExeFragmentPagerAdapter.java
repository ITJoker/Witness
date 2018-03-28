package com.risenb.witness.ui.tasklist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class NonExeFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private FragmentManager fm;

    public NonExeFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
    //@Override
    // public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = list.get(position);
//        //判断当前的fragment是否已经被添加进入Fragmentanager管理器中
//        if (!fragment.isAdded()) {
//            FragmentTransaction transaction = fm.beginTransaction();
//            transaction.add(fragment, fragment.getClass().getSimpleName());
//            //不保存系统参数，自己控制加载的参数
//            transaction.commitAllowingStateLoss();
//            //手动调用,立刻加载Fragment片段
//            fm.executePendingTransactions();
//        }
//        if (fragment.getView().getParent() == null) {
//            //添加布局
//            container.addView(fragment.getView());
//        }
//        Fragment fragment = list.get(position);
//                if(!fragment.isAdded()){ // 如果fragment还没有added
//                         FragmentTransaction ft = fm.beginTransaction();
//                         ft.add(fragment, fragment.getClass().getSimpleName());
//                         ft.commit();
//                         /**
//                           * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
//                           * 会在进程的主线程中，用异步的方式来执行。
//                           * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
//                           * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
//                           */
//                         fm.executePendingTransactions();
//                     }
//
//                 if(fragment.getView().getParent() == null){
//                         container.addView(fragment.getView()); // 为viewpager增加布局
//                    }
    //  return fragment.getView();
    // }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //这句千万不要写
        //container.removeView(list.get(position).getView());
    }
}
