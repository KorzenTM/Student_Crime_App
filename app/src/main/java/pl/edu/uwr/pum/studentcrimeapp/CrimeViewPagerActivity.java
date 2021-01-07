package pl.edu.uwr.pum.studentcrimeapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import pl.edu.uwr.pum.studentcrimeapp.fragments.CrimeFragmentViewPager;
import pl.edu.uwr.pum.studentcrimeapp.fragments.SingleFragmentActivity;

public class CrimeViewPagerActivity extends SingleFragmentActivity
{
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;

    @Override
    protected Fragment createFragment()
    {
        return new CrimeFragmentViewPager();
    }

    public void chooseCamera()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener()
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
        new AlertDialog.Builder(this)
                .setMessage("This feature requires permissions")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
