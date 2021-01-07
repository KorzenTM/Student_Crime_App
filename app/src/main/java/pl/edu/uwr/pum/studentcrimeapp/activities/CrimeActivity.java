package pl.edu.uwr.pum.studentcrimeapp.activities;

import androidx.fragment.app.Fragment;

import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragment;
import pl.edu.uwr.pum.studentcrimeapp.activities.SingleFragmentActivity;


public class CrimeActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragment();
    }
}