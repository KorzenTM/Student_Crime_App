package pl.edu.uwr.pum.studentcrimeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.ViewHolder>
{
    private final FragmentActivity mContext;
    private final List<Crime> mCrimeData;


    public CrimeListAdapter(FragmentActivity context, List<Crime> CrimeData)
    {
        this.mCrimeData = CrimeData;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final ImageView mIsSolvedImageView;
        private final Button mDeleteButton;
        private Button mAddCrimeFAB;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mDateTextView = itemView.findViewById(R.id.date_text_view);
            mIsSolvedImageView = itemView.findViewById(R.id.is_solved_image_view);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mAddCrimeFAB = itemView.findViewById(R.id.fab);

            itemView.setOnClickListener(this);
        }

        public void bind(Crime currentCrime)
        {
            mTitleTextView.setText(currentCrime.getTitle());
            mDateTextView.setText(currentCrime.getDate().toString());
            if (currentCrime.isSolved())
            {
                Glide.with(mContext).load(R.mipmap.true_icon)
                        .into(mIsSolvedImageView);
            }
            else
            {
                Glide.with(mContext).load(R.mipmap.false_icon).into(mIsSolvedImageView);
            }
        }

        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(mContext, CrimeViewPagerActivity.class);
            CrimeFragment.CurrentPosition = getAbsoluteAdapterPosition();
            mContext.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public CrimeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.crime_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        final Crime currentCrime = mCrimeData.get(position);
        holder.bind(currentCrime);

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CrimeListFragment.dbHandler.deleteCrime(currentCrime.getTitle());
                CrimeListFragment.getStudentsCrimes();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mCrimeData.size();
    }
}
