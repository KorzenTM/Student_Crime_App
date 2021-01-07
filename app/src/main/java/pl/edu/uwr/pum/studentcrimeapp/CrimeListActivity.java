package pl.edu.uwr.pum.studentcrimeapp;

import androidx.fragment.app.Fragment;

import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeListFragment;
import pl.edu.uwr.pum.studentcrimeapp.fragments.SingleFragmentActivity;

public class CrimeListActivity extends SingleFragmentActivity
{
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;
    private Fragment fragment;

    @Override
    protected Fragment createFragment() {
        fragment = new CrimeListFragment();
        return fragment;
    }
}
