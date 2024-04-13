package com.mirea.bykonyaia.mireaproject.labs.lab_6;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentUserSettingsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSettingsFragment extends Fragment {
    private static final String SETTINGS_STORAGE_FILE_NAME = "mirea.project.settings";
    private static final String KEY_EMAIL = "user.email";
    private static final String KEY_PHONE = "user.phone";
    private static final String KEY_NAME = "user.name";

    private FragmentUserSettingsBinding binding = null;
    public UserSettingsFragment() { }
    public static UserSettingsFragment newInstance() {
        UserSettingsFragment fragment = new UserSettingsFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserSettingsBinding.inflate(inflater, container, false);
        binding.saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OnSaveInfoButtonPressed(view); }
        });
        binding.loadProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OnLoadInfoButtonPressed(view); }
        });
        return binding.getRoot();
    }


    public void OnSaveInfoButtonPressed(View v) {
        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	200);
            return;
        }

        try {
            final SharedPreferences settings = getActivity().getSharedPreferences(SETTINGS_STORAGE_FILE_NAME, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString(KEY_EMAIL, binding.emailInput.getText().toString());
            editor.putString(KEY_PHONE, binding.phoneInput.getText().toString());
            editor.putString(KEY_NAME, binding.nameInput.getText().toString());
            editor.apply();
            Toast.makeText(getActivity(), "Success saving", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), String.format("Error: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
    public void OnLoadInfoButtonPressed(View v) {
        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },	200);
            return;
        }

        final SharedPreferences settings = getActivity().getSharedPreferences(SETTINGS_STORAGE_FILE_NAME, Context.MODE_PRIVATE);
        binding.emailInput.setText(settings.getString(KEY_EMAIL, ""));
        binding.phoneInput.setText(settings.getString(KEY_PHONE, ""));
        binding.nameInput.setText(settings.getString(KEY_NAME, ""));
    }
}