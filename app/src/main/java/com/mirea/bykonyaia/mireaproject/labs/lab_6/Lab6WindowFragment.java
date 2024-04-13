package com.mirea.bykonyaia.mireaproject.labs.lab_6;

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
import android.widget.TabHost;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentLab6WindowBinding;

public class Lab6WindowFragment extends Fragment {
    private boolean is_permissions_granted = false;
    FragmentLab6WindowBinding binding = null;
    public Lab6WindowFragment() { }
    public static Lab6WindowFragment newInstance(String param1, String param2) {
        Lab6WindowFragment fragment = new Lab6WindowFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLab6WindowBinding.inflate(inflater, container, false);

        binding.fragmentsTab.setup();
        final TabHost.TabSpec profile_spec = binding.fragmentsTab.newTabSpec("Profile");
        profile_spec.setContent(R.id.user_settings_fragment);
        profile_spec.setIndicator("Profile");
        binding.fragmentsTab.addTab(profile_spec);

        final TabHost.TabSpec files_spec = binding.fragmentsTab.newTabSpec("Files");
        files_spec.setContent(R.id.user_file_fragment);
        files_spec.setIndicator("Files");
        binding.fragmentsTab.addTab(files_spec);
        return binding.getRoot();
    }



    private void MakePermissionsRequest() {
        final boolean storage_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final boolean camera_enabled = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);

        if(storage_enabled && camera_enabled) {
            is_permissions_granted = true;
        } else {
            is_permissions_granted = false;
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] { android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	200);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("", "onRequestPermissionsResult: " + String.valueOf(requestCode));
        if(requestCode != 200) {
            Log.i("he-he", String.format("Code no 200: %d", requestCode));
            getActivity().finish();
        }
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.i("he-he", String.format("No granted: %d", grantResults[0]));
            getActivity().finish();
        }
    }
}