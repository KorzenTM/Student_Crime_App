package pl.edu.uwr.pum.studentcrimeapp.activities;

import androidx.fragment.app.Fragment;

import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragmentViewPager;
import pl.edu.uwr.pum.studentcrimeapp.activities.SingleFragmentActivity;

public class CrimeViewPagerActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragmentViewPager();
    }
}
