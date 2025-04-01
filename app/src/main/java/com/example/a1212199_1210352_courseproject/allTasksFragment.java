package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class allTasksFragment extends Fragment {

    public static currentTask currentTaskAll;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout taskContainer;
    private Database db;

    public allTasksFragment() {
        // Required empty public constructor
    }

    public static allTasksFragment newInstance(String param1, String param2) {
        allTasksFragment fragment = new allTasksFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        taskContainer = rootView.findViewById(R.id.taskContainer);
        db = new Database(getActivity(), "test4", null, 1);

        // Sample tasks
        Cursor tasks = db.getAllTasks(user.getEmail());

        // Dynamically create the task layouts
        if (tasks != null && tasks.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = tasks.getString(tasks.getColumnIndex("title"));
                @SuppressLint("Range") String description = tasks.getString(tasks.getColumnIndex("description"));
                taskContainer.addView(createTaskLayout(title, description));
            } while (tasks.moveToNext());
        } else {
            Log.d("Database Error", "No tasks found in database.");
        }

        return rootView;
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
        titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.lavender));
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
                currentTaskAll = new currentTask(
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
                bundle.putSerializable("currentTask", currentTaskAll);
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
