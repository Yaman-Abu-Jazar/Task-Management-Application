package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static currentTask currentTaskSearch;
    private Database db;
    private LinearLayout taskContainer;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new Database(getActivity(),"test4",null,1);

        EditText searchField = (EditText) getActivity().findViewById(R.id.searchField);
        EditText date1Field = (EditText) getActivity().findViewById(R.id.startDateField);
        EditText date2Field = (EditText) getActivity().findViewById(R.id.endDateField);
        ConstraintLayout searchBtn = (ConstraintLayout) getActivity().findViewById(R.id.searchBtn);
        ConstraintLayout filterBtn = (ConstraintLayout) getActivity().findViewById(R.id.filterBtn);
        taskContainer = (LinearLayout) getActivity().findViewById(R.id.taskContainer3);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE); // Background color
        border.setCornerRadius(8);    // Corner radius
        border.setStroke(2, Color.RED); // Border width and color

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = searchField.getText().toString().trim();
                if(!text.isEmpty()){
                    List<currentTask> tasksList = db.searchTasks(text, user.getEmail());

                    ArrayList<currentTask> tasks = new ArrayList<>(tasksList);

                    taskContainer.removeAllViews();

                    for(currentTask task : tasks){
                        taskContainer.addView(createTaskLayout(task.getTitle(), task.getDescription()));
                    }
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String date1 = date1Field.getText().toString().trim();
                    String date2 = date2Field.getText().toString().trim();

                    if (!date1.matches("\\d{4}-\\d{2}-\\d{2}") || date1.isEmpty()) {
                        date1Field.setError("Invalid date format! Use YYYY-MM-DD.");
                        date1Field.requestFocus();
                        return;
                    }

                    if (!date2.matches("\\d{4}-\\d{2}-\\d{2}") || date2.isEmpty()) {
                        date2Field.setError("Invalid date format! Use YYYY-MM-DD.");
                        date2Field.requestFocus();
                        return;
                    }
                    List<currentTask> tasksList = db.getTasksBetweenDates(date1, date2, user.getEmail());
                    ArrayList<currentTask> tasks = new ArrayList<>(tasksList);
                    taskContainer.removeAllViews();

                    for(currentTask task : tasks){
                        taskContainer.addView(createTaskLayout(task.getTitle(), task.getDescription()));
                    }
                }
            });
        }
    }

    private ConstraintLayout createTaskLayout(String title, String description) {
        // Create the ConstraintLayout
        ConstraintLayout taskLayout = new ConstraintLayout(getContext());
        taskLayout.setBackgroundResource(R.drawable.lavender_border);

        // Set layout params with margins
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(20, 40, 20, 0);
        taskLayout.setPadding(20, 40, 20, 40);
        taskLayout.setLayoutParams(layoutParams);

        // Create title TextView
        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextSize(20);
        titleView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.lavender));
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setId(View.generateViewId());

        // Create description TextView
        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(description);
        descriptionView.setTextSize(14);
        descriptionView.setTextColor(ContextCompat.getColor(getContext(), R.color.lavender));
        descriptionView.setId(View.generateViewId());

        // Add views to the layout
        taskLayout.addView(titleView);
        taskLayout.addView(descriptionView);

        // Set up constraints for the layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(taskLayout);

        // Constraints for title TextView
        constraintSet.connect(titleView.getId(), ConstraintSet.TOP, taskLayout.getId(), ConstraintSet.TOP, 50);
        constraintSet.connect(titleView.getId(), ConstraintSet.START, taskLayout.getId(), ConstraintSet.START, 10);
        constraintSet.connect(titleView.getId(), ConstraintSet.END, taskLayout.getId(), ConstraintSet.END, 10);

        // Constraints for description TextView
        constraintSet.connect(descriptionView.getId(), ConstraintSet.TOP, titleView.getId(), ConstraintSet.BOTTOM, 10);
        constraintSet.connect(descriptionView.getId(), ConstraintSet.START, taskLayout.getId(), ConstraintSet.START, 10);
        constraintSet.connect(descriptionView.getId(), ConstraintSet.END, taskLayout.getId(), ConstraintSet.END, 10);

        // Apply constraints
        constraintSet.applyTo(taskLayout);

        // Set OnClickListener to navigate to edit task fragment
        taskLayout.setOnClickListener(v -> {
            // Pass title and description to the new fragment to edit the task
            Bundle bundle = new Bundle();

//(int id, String title, String description, String priority, int completionStatus, String dueDate, String dueTime, String reminderDate, String reminderTime, User user)
            // Get the task data based on title and description
            Cursor task = db.getTask(user.getEmail(), title, description);
            if (task != null && task.moveToFirst()) {
                currentTaskSearch = new currentTask(
                        Integer.parseInt(task.getString(0)),
                        task.getString(2),
                        task.getString(3),
                        task.getString(6),
                        Integer.parseInt(task.getString(7)),
                        task.getString(4),
                        task.getString(5),
                        task.getString(8),
                        task.getString(9),
                        user
                );

                // Create and pass data to the EditTaskFragment
                EditTaskFragment editFragment = new EditTaskFragment();
                bundle.putSerializable("currentTask", currentTaskSearch);
                editFragment.setArguments(bundle);

                // Replace the current fragment with the EditTaskFragment
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_in_home, editFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                // Handle case where task is not found in the database
                Log.d("Cursor Error", "Task not found for title: " + title + " and description: " + description);
            }
        });

        return taskLayout;
    }
}