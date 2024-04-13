package com.mirea.bykonyaia.mireaproject.labs.lab_5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.ActivityMainBinding;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentLab5WindowBinding;


public class Lab5WindowFragment extends Fragment implements DistanceSensorFragment.IDistanceSensorValueChangedListener {
    static final private int REQUEST_CODE_PERMISSION = 200;
    private DistanceSensorFragment distance_sensor = null;
    private ProfilePhotoFragment profile_photo = null;
    private VoiceRecordFragment profile_voice = null;
    private boolean is_permissions_granted = false;
    private ActivityMainBinding binding = null;


    private void MakePermissionsRequest() {
        final boolean storage_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final boolean camera_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);

        if(storage_enabled && camera_enabled) {
            is_permissions_granted = true;
        } else {
            is_permissions_granted = false;
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] { Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	REQUEST_CODE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("", "onRequestPermissionsResult: " + String.valueOf(requestCode));
        if(requestCode == REQUEST_CODE_PERMISSION) {
            is_permissions_granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void ValueChanged(float value) {
        Log.i("he-he", String.format("Distance changed: %.2f", value));
        if(value < 4) {
            profile_voice.StartRecord();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lab5_window, container, false);
        distance_sensor = (DistanceSensorFragment) getChildFragmentManager().findFragmentById(R.id.distance_sensor);
        profile_photo = (ProfilePhotoFragment) getChildFragmentManager().findFragmentById(R.id.profile_photo);
        profile_voice = (VoiceRecordFragment) getChildFragmentManager().findFragmentById(R.id.profile_voice);
        distance_sensor.onValueChanged = this;
        MakePermissionsRequest();
        return view;
    }
}
