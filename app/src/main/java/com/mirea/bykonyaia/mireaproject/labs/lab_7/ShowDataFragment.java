package com.mirea.bykonyaia.mireaproject.labs.lab_7;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentShowDataBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowDataFragment extends Fragment {
    private static final String TAG = ShowDataFragment.class.getSimpleName();
    private FragmentShowDataBinding binding = null;
    public ShowDataFragment() {}

    public static ShowDataFragment newInstance() {
        ShowDataFragment fragment = new ShowDataFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadTestFileTask().execute();
    }

    private class LoadTestFileTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.statusPanel.setText("Start loading");
            binding.dataPanel.setText("--");
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                return MakeRequest();
            } catch (IOException | RuntimeException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null) {
                binding.statusPanel.setText("Success loading");
                binding.dataPanel.setText(result);
            } else {
                binding.statusPanel.setText("Error loading");
                binding.dataPanel.setText("--");
            }
        }

        private String MakeRequest() throws IOException, RuntimeException {
            final URL url = new URL("http://178.208.86.244:8000/data.txt");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Invalid return code");

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int read = 0; (read = inputStream.read()) != -1;) {
                bos.write(read);
            }
            final String result = bos.toString();
            connection.disconnect();
            bos.close();
            return result;
        }
    }
}