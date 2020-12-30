package pl.edu.uwr.pum.studentcrimeapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class CrimeViewPagerAdapter extends FragmentStateAdapter
{
    private final List<Crime> mCrimeData;

    public CrimeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Crime> crimeData)
    {
        super(fragmentActivity);
        mCrimeData = crimeData;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return CrimeFragment.newInstance(position);
    }

    @Override
    public int getItemCount()
    {
        return mCrimeData.size();
    }
}
