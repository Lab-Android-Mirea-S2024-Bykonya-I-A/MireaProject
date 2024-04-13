package com.mirea.bykonyaia.mireaproject.lab_4;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentBackgroundWorkerBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackgroundWorkerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundWorkerFragment extends Fragment {
    private FragmentBackgroundWorkerBinding binding = null;
    public BackgroundWorkerFragment() { }

    public static BackgroundWorkerFragment newInstance(String param1, String param2) {
        BackgroundWorkerFragment fragment = new BackgroundWorkerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBackgroundWorkerBinding.inflate(inflater, container, false);
        binding.startTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OnStartTaskButtonClicked(view); }
        });
        return binding.getRoot();

    }
    private void OnStartTaskButtonClicked(View v) {
        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class).build();
        WorkManager.getInstance(requireContext()).enqueue(workRequest);
    }
}