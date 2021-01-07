package pl.edu.uwr.pum.studentcrimeapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.edu.uwr.pum.studentcrimeapp.R;
import pl.edu.uwr.pum.studentcrimeapp.adapter.CrimeViewPagerAdapter;
import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragment;
import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeListFragment;

public class CrimeFragmentViewPager extends Fragment
{
    private Button mMoveEndButton;
    private Button mMoveFirstButton;

    public CrimeFragmentViewPager()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_crime_view_pager, container, false);
        final ViewPager2 viewPager2 = v.findViewById(R.id.view_pager);

        CrimeViewPagerAdapter adapter = new CrimeViewPagerAdapter(requireActivity(), CrimeListFragment.mCrimes);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(CrimeFragment.CurrentPosition, true);

        mMoveEndButton = v.findViewById(R.id.move_to_end_button);
        mMoveFirstButton = v.findViewById(R.id.move_to_top_button);

        mMoveFirstButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewPager2.setCurrentItem(0, true);
            }
        });

        mMoveEndButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewPager2.setCurrentItem(CrimeListFragment.mCrimes.size(), true);
            }
        });

        return v;
    }
}