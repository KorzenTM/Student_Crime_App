package pl.edu.uwr.pum.studentcrimeapp;

import androidx.fragment.app.Fragment;

public class CrimeViewPagerActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragmentViewPager();
    }
}
