package pl.edu.uwr.pum.studentcrimeapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import pl.edu.uwr.pum.studentcrimeapp.models.Crime;
import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragment;

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
