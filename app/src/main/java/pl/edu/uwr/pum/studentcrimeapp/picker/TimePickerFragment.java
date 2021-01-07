package pl.edu.uwr.pum.studentcrimeapp.picker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import pl.edu.uwr.pum.studentcrimeapp.R;

public class TimePickerFragment extends DialogFragment
{
    private static final String ARG_TIME = "time";
    public static final String Extra_Time = "Time";
    private TimePicker timePicker;

    public static TimePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        System.out.println(hours + ":" + minutes);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_fragment, null);

        timePicker = v.findViewById(R.id.dialog_time_picker);
        timePicker.setHour(hours);
        timePicker.setMinute(minutes);
        timePicker.setIs24HourView(true);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Time of Crime")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                int hours = timePicker.getHour();
                                int minutes = timePicker.getMinute();
                                sendResult(Activity.RESULT_OK, hours, minutes);
                            }
                        }).create();
    }

    private void sendResult(int resultCode, int hour, int minute){
        if(getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra("hour", hour);
        intent.putExtra("minutes", minute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);


    }
}
