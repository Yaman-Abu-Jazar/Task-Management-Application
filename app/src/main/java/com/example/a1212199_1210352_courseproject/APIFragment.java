package com.example.a1212199_1210352_courseproject;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link APIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class APIFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button button;
    private Button insertBtn;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private ArrayList<Task> newTasks = new ArrayList<>();

    public APIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment APIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static APIFragment newInstance(String param1, String param2) {
        APIFragment fragment = new APIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_p_i_fragement, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database db = new Database(getActivity(),"test4",null,1);

        button = (Button) getActivity().findViewById(R.id.getDataBtn);
        insertBtn = (Button) getActivity().findViewById(R.id.insertBtn);
        setProgress(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(APIFragment.this);
                connectionAsyncTask.execute("https://mocki.io/v1/ecff484f-a8f8-4f81-b4e1-954fd8652691");
            }
        });

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTasks.isEmpty()){
                    for(Task task : newTasks){
                        try{
                            db.insertTask(task);
                        } catch (SQLiteException e) {
                            // Handle the SQLite exception
                            Log.e("DatabaseError", "Error inserting task: " + e.getMessage());
                            // You can also show a Toast or UI message to the user
                            Toast.makeText(getContext(), "Failed to insert task. Please try again.", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) {
                            // Catch any other exceptions
                            Log.e("GeneralError", "Error inserting task: " + e.getMessage());
                            Toast.makeText(getContext(), "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(), "There are No Imported Tasks", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout);
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void fillTasks(ArrayList<Task> tasks) {
        newTasks = tasks;
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout);
        linearLayout.removeAllViews();
        for(Task task : tasks) {
            TextView textView = new TextView(requireContext());
            textView.setText(task.toString());
            linearLayout.addView(textView);
        }
    }

    public void setProgress(boolean progress) {
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}