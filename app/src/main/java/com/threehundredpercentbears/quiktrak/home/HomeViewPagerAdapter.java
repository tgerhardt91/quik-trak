package com.threehundredpercentbears.quiktrak.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.threehundredpercentbears.quiktrak.categories.CategoriesFragment;
import com.threehundredpercentbears.quiktrak.spending.SpendingFragment;
import com.threehundredpercentbears.quiktrak.transactions.TransactionsFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_LAYOUT_SIZE = 3;

    HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull @Override public Fragment createFragment(int position) {
        if (position == 0) {
            return new SpendingFragment();
        } else if (position == 1) {
            return new TransactionsFragment();
        } else {
            return new CategoriesFragment();
        }
    }

    @Override public int getItemCount() {
        return TAB_LAYOUT_SIZE;
    }
}
