package com.mirea.bykonyaia.mireaproject.labs.lab_8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import com.mirea.bykonyaia.mireaproject.databinding.FragmentLab8WindowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Lab8WindowFragment extends Fragment {
    public Lab8WindowFragment() {}
    public static Lab8WindowFragment newInstance(String param1, String param2) {
        Lab8WindowFragment fragment = new Lab8WindowFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLab8WindowBinding.inflate(inflater, container, false);
        list_view = (PointsOfInterestListFragment)getChildFragmentManager().findFragmentById(R.id.points_of_interest_list);
        map_view = (PointsOfInterestMapFragment)getChildFragmentManager().findFragmentById(R.id.points_of_interest_map);

        binding.updatePointsButton.setOnClickListener(v -> {
            UpdatePointsOfInterest();
        });
        list_view.listener = new OnSelectPointOfInterestListener() {
            @Override
            public void onPointOfInterestSelect(PointsOfInterestDto point) {
                map_view.MoveMapToLocation(point.location);
            }
        };
        map_view.listener = new OnSelectPointOfInterestListener() {
            @Override
            public void onPointOfInterestSelect(PointsOfInterestDto point) {
                Intent showPointInfoIntent = new Intent(getActivity().getApplicationContext(), ShowPointOfInterestInfoActivity.class);
                showPointInfoIntent.putExtra(ShowPointOfInterestInfoActivity.KEY_TITLE, point.title);
                showPointInfoIntent.putExtra(ShowPointOfInterestInfoActivity.KEY_DESCRIPTION, point.description);
                showPointInfoIntent.putExtra(ShowPointOfInterestInfoActivity.KEY_LATITUDE, point.location.getLatitude());
                showPointInfoIntent.putExtra(ShowPointOfInterestInfoActivity.KEY_LONGITUDE, point.location.getLongitude());
                startActivity(showPointInfoIntent);
            }
        };



        MakePermissionsRequest();
        return binding.getRoot();
    }

    // =============================================================

    private PointsOfInterestListFragment list_view = null;
    private PointsOfInterestMapFragment map_view = null;
    private FragmentLab8WindowBinding binding = null;

    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean is_permissions_granted = false;

    @Override
    public void onStart() {
        super.onStart();
        UpdatePointsOfInterest();
    }


    private void MakePermissionsRequest() {
        is_permissions_granted =
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.INTERNET) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_NETWORK_STATE) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) &&
                        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.i("HE_HE", is_permissions_granted ? "YES" : "NO");
        if(!is_permissions_granted) {
            ActivityCompat.requestPermissions(getActivity(),
                    new	String[] { android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
    private void UpdatePointsOfInterest() {
        Log.i("HE_HE", "UpdatePointsOfInterest");
        if(is_permissions_granted)
            new RequestPointsOfInterestList().execute();
        else
            MakePermissionsRequest();
    }

    private class BaseHttpRequestTask extends AsyncTask<Void, Void, String> {
        private final String address;
        private final String method;
        public BaseHttpRequestTask(String address, String method) {
            this.address = address;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return MakeRequest();
            } catch (IOException | RuntimeException e) {
                Log.i("HE_HE", e.toString());
                return null;
            }
        }
        private String MakeRequest() throws IOException, RuntimeException {
            final URL url = new URL(address);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod(method);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Invalid return code");

            Log.i("HE_HE", "MAKE_READ");
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
    private class RequestPointsOfInterestList extends BaseHttpRequestTask {
        public RequestPointsOfInterestList() {
            super("http://178.208.86.244:8000/points.json", "GET");
            Log.i("HE_HE", "RequestPointsOfInterestList");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null)
                return;

            Log.i("HE_HE", result);
            try {
                final List<PointsOfInterestDto> points = new ArrayList<>();
                final JSONArray pointsJson = new JSONArray(result);
                for(int index = 0; index < pointsJson.length(); ++index) {
                    try {
                        final JSONObject pointJson = pointsJson.getJSONObject(index);
                        final JSONObject pointJsonLocation = pointJson.getJSONObject("location");
                        points.add(new PointsOfInterestDto(
                                pointJson.getString("title"),
                                pointJson.getString("description"),
                                new GeoPoint(
                                        pointJsonLocation.getDouble("latitude"),
                                        pointJsonLocation.getDouble("longitude")
                                )
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                list_view.SetPointsOfInterestModel(points);
                map_view.SetPointsOfInterestModel(points);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}