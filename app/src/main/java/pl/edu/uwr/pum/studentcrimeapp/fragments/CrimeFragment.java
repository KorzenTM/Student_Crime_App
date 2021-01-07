package pl.edu.uwr.pum.studentcrimeapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import pl.edu.uwr.pum.studentcrimeapp.R;
import pl.edu.uwr.pum.studentcrimeapp.models.Crime;
import pl.edu.uwr.pum.studentcrimeapp.picker.DatePickerFragment;
import pl.edu.uwr.pum.studentcrimeapp.picker.TimePickerFragment;

public class CrimeFragment extends Fragment {
    private EditText titleField;
    private Button dateButton;
    private Button updateButton;
    private Button mTakePictureButton;
    private ImageView mPictureImageView;
    private CheckBox solvedCheckBox;

    private Crime crime;
    private List<Crime> mCrimeData;
    private Uri savePicturePath = null;
    private Date mNewDate;
    private int mIndex;

    public static int CurrentPosition = 0;
    private static final String DIALOG_DATE = "DATE";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "TIME";
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;

    public CrimeFragment()
    {
        //require empty public constructor
    }

    public static CrimeFragment newInstance(int counter) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putInt("KEY", counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            mIndex = getArguments().getInt("KEY");
        crime = new Crime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mCrimeData = CrimeListFragment.mCrimes;
        crime = mCrimeData.get(mIndex);

        titleField = v.findViewById(R.id.crime_title);
        dateButton = v.findViewById(R.id.crime_date);
        updateButton = v.findViewById(R.id.update_button);
        mTakePictureButton = v.findViewById(R.id.button_add_picture);
        mPictureImageView = v.findViewById(R.id.image_view_picture);
        solvedCheckBox = v.findViewById(R.id.crime_solved);

        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (crime.getPicture() != null)
            mPictureImageView.setImageURI(Uri.parse(crime.getPicture()));

        dateButton.setText(crime.getDate().toString());

        dateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
                TimePickerFragment dialog2 = TimePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog2.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog2.show(manager, DIALOG_TIME);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = titleField.getText().toString();
                String date = dateButton.getText().toString();
                System.out.println(date);
                Boolean isSolved = solvedCheckBox.isChecked();
                CrimeListFragment.dbHandler.updateCrime(crime.getId(), title, date, isSolved, savePicturePath.toString());
                CrimeListFragment.getStudentsCrimes();
                Toast.makeText(getContext(), "Case has been updated", Toast.LENGTH_LONG).show();
            }
        });

        mTakePictureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseCamera();
            }
        });
        return v;
    }

    public void chooseCamera()
    {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_INTENT);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
            {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken)
            {
                showRationaleDialog();
            }
        }).onSameThread().check();
    }

    private void showRationaleDialog()
    {
        new AlertDialog.Builder(getActivity())
                .setMessage("This feature requires permissions")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .show();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        titleField.setText(crime.getTitle());
        dateButton.setText(crime.getDate().toString());
        solvedCheckBox.setChecked(crime.isSolved());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                switch (requestCode)
                {
                    case REQUEST_DATE:
                        mNewDate = (Date) data.getSerializableExtra(DatePickerFragment.Extra_Date);
                        break;
                    case REQUEST_TIME:
                        int hour = data.getIntExtra("hour", 0);
                        int minutes = data.getIntExtra("minutes", 0);
                        mNewDate.setHours(hour);
                        mNewDate.setMinutes(minutes);
                        dateButton.setText(mNewDate.toString());
                        break;
                    case CAMERA_INTENT:
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        mPictureImageView.setImageBitmap(thumbnail);
                        savePicturePath = savePicture(thumbnail);
                        Log.d("PICTURE", "Path" + savePicturePath);
                        break;
                }
            }
        }
        else return;
    }

    private Uri savePicture(Bitmap bitmap)
    {
        ContextWrapper wrapper = new ContextWrapper(getActivity().getApplicationContext());
        File file = wrapper.getDir("myGallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");

        try
        {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return Uri.parse(file.getAbsolutePath());
    }
}

