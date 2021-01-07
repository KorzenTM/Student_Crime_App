package pl.edu.uwr.pum.studentcrimeapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.edu.uwr.pum.studentcrimeapp.CrimeViewPagerActivity;
import pl.edu.uwr.pum.studentcrimeapp.R;
import pl.edu.uwr.pum.studentcrimeapp.adapter.CrimeListAdapter;
import pl.edu.uwr.pum.studentcrimeapp.database.DBHandler;
import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragment;
import pl.edu.uwr.pum.studentcrimeapp.models.Crime;

public class CrimeListFragment extends Fragment {
    private RecyclerView crimeRecyclerView;
    private CrimeListAdapter crimeListAdapter;
    private Button mSearchButton;
    private EditText mSearchEditText;
    public static DBHandler dbHandler;
    private static Cursor mCursor;
    public static List<Crime> mCrimes = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =
                inflater.inflate(R.layout.fragment_crime_list, container, false);

        FloatingActionButton mAddCrimeFAB = v.findViewById(R.id.fab);
        crimeRecyclerView = v.findViewById(R.id.recycler_view);
        mSearchEditText = v.findViewById(R.id.search_edit_text);

        dbHandler = new DBHandler(getContext());
        mCursor = dbHandler.getStudentCrimes();

        if (mCursor.getCount() == 0)
        {
            setDefaultCrimes(100);
        }

        getStudentsCrimes();
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        crimeListAdapter = new CrimeListAdapter(getActivity(), mCrimes);
        crimeRecyclerView.setAdapter(crimeListAdapter);

        mAddCrimeFAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Crime crime = new Crime();
                crime.setId(mCrimes.size());
                dbHandler.addCrime(crime);
                getStudentsCrimes();
                crimeListAdapter.notifyItemInserted(mCrimes.size() - 1);
                Intent intent = new Intent(getActivity(), CrimeViewPagerActivity.class);
                CrimeFragment.CurrentPosition = mCrimes.size() - 1;
                startActivity(intent);
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                Cursor cursor = dbHandler.searchCrime(mSearchEditText.getText().toString());
                if (cursor.moveToFirst())
                {
                    Toast.makeText(getContext(), cursor.getCount() +
                                    " matching item has been found",
                            Toast.LENGTH_SHORT).show();
                    crimeRecyclerView.scrollToPosition(cursor.getInt(0) - 1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        return v;
    }

    public static void getStudentsCrimes()
    {
        mCrimes.clear();

        mCursor = dbHandler.getStudentCrimes();

        if (mCursor.getCount() == 0)
            System.out.println("EMPTY");
        else
        {
            while(mCursor.moveToNext())
            {
                Crime crime = new Crime();
                int id = mCursor.getInt(0);
                String title = mCursor.getString(1);
                String date = mCursor.getString(2);
                boolean is_solved = mCursor.getInt(3) > 0;
                crime.setId(id);
                crime.setTitle(title);
                crime.setDate(new Date(date));
                crime.setSolved(is_solved);
                mCrimes.add(crime);
            }
        }
    }

    void setDefaultCrimes(int how_many)
    {
        for(int i = 0; i < how_many; i++)
        {
            Crime crime = new Crime();
            crime.setId(i);
            crime.setTitle("Crime: " + i);
            crime.setDate(new Date());
            crime.setSolved(i % 2 == 0);
            dbHandler.addCrime(crime);
        }
    }

    @Override
    public void onResume()
    {
        crimeListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        dbHandler.close();
        super.onDestroy();
    }

}
