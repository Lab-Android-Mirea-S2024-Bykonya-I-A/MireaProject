package com.mirea.bykonyaia.mireaproject.labs.lab_8;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.mirea.bykonyaia.mireaproject.R;
import com.mirea.bykonyaia.mireaproject.databinding.FragmentPointsOfInterestListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PointsOfInterestListFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private FragmentPointsOfInterestListBinding binding = null;
    public OnSelectPointOfInterestListener listener = null;
    public PointsOfInterestListFragment() {}

    public static PointsOfInterestListFragment newInstance(String param1, String param2) {
        PointsOfInterestListFragment fragment = new PointsOfInterestListFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPointsOfInterestListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void SetPointsOfInterestModel(List<PointsOfInterestDto> points) {
        ArrayList<HashMap<String, Object>> points_view_model = new ArrayList<>();
        for(final PointsOfInterestDto point: points) {
            HashMap<String, Object> point_fields = new HashMap<>();
            point_fields.put("Title", point.title);
            point_fields.put("Point", point);
            points_view_model.add(point_fields);
        }

        final SimpleAdapter sensors_history = new SimpleAdapter(
                getActivity(), points_view_model, android.R.layout.simple_list_item_1,
                new String[]{ "Title" },
                new int[]{ android.R.id.text1 }
        );
        binding.pointsOfInterestList.setOnItemClickListener(this);
        binding.pointsOfInterestList.setAdapter(sensors_history);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(listener == null)
            return;

        final Object selected_item = parent.getAdapter().getItem(position);
        final HashMap<String, Object> selected_item_data = (HashMap<String, Object>)selected_item;
        final PointsOfInterestDto selected_point = (PointsOfInterestDto)selected_item_data.get("Point");
        listener.onPointOfInterestSelect(selected_point);
    }
}
