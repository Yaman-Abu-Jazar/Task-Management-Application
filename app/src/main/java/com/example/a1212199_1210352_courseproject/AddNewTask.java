package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewTask extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewTask.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewTask newInstance(String param1, String param2) {
        AddNewTask fragment = new AddNewTask();
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
        return inflater.inflate(R.layout.fragment_add_new_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database db = new Database(getActivity(),"test4",null,1);

        ImageView imageView = (ImageView) getActivity().findViewById(R.id.doneBtn);
        ImageView cancel = (ImageView) getActivity().findViewById(R.id.backBtn);
        EditText title = (EditText) getActivity().findViewById(R.id.titleField);
        EditText description = (EditText) getActivity().findViewById(R.id.descriptionField);
        EditText date = (EditText) getActivity().findViewById(R.id.dateField);
        EditText reminderDate = (EditText) getActivity().findViewById(R.id.reminderDateField);
        EditText time = (EditText) getActivity().findViewById(R.id.timeField);
        EditText reminderTime = (EditText) getActivity().findViewById(R.id.reminderTimeField);

        RadioGroup priorityGroup = (RadioGroup) getActivity().findViewById(R.id.priorityGroup);  // Use the correct ID of your RadioGroup
        final String[] selectedPriority = new String[1];
        // Set a listener to detect the selected RadioButton
        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the RadioButton ID
                RadioButton selectedRadioButton = (RadioButton) getActivity().findViewById(checkedId);
                // Get the text of the selected RadioButton
                selectedPriority[0] = selectedRadioButton.getText().toString();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskTitle = title.getText().toString();
                    String taskDesc = description.getText().toString();
                    String taskDate = date.getText().toString();
                    String dateReminder = reminderDate.getText().toString();
                    String taskTime = time.getText().toString();
                    String timeReminder = reminderTime.getText().toString();
                    int status = 0;

                    // Validate Task Title
                    if (taskTitle.isEmpty()) {
                        title.setError("Title cannot be empty!");
                        title.requestFocus();
                        return;
                    }

                    // Validate Task Date
                    if (!taskDate.matches("\\d{4}-\\d{2}-\\d{2}") || taskDate.isEmpty()) {
                        date.setError("Invalid date format! Use YYYY-MM-DD.");
                        date.requestFocus();
                        return;
                    }

                    try {
                        // Parse Task Date
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate parsedTaskDate = LocalDate.parse(taskDate, dateFormatter);

                        // Ensure Task Date is not in the past
                        if (parsedTaskDate.isBefore(LocalDate.now())) {
                            date.setError("Task date cannot be in the past!");
                            date.requestFocus();
                            return;
                        }
                    } catch (DateTimeParseException e) {
                        date.setError("Invalid date format! Use YYYY-MM-DD.");
                        date.requestFocus();
                        return;
                    }

                    // Validate Reminder Date (if not empty)
                    if (!dateReminder.isEmpty()) {
                        if (!dateReminder.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            reminderDate.setError("Invalid date format! Use YYYY-MM-DD.");
                            reminderDate.requestFocus();
                            return;
                        }

                        try {
                            LocalDate parsedReminderDate = LocalDate.parse(dateReminder, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                            // Ensure Reminder Date is not in the past
                            if (parsedReminderDate.isBefore(LocalDate.now())) {
                                reminderDate.setError("Reminder date cannot be in the past!");
                                reminderDate.requestFocus();
                                return;
                            }
                        } catch (DateTimeParseException e) {
                            reminderDate.setError("Invalid date format! Use YYYY-MM-DD.");
                            reminderDate.requestFocus();
                            return;
                        }
                    }

                    // Validate Task Time (if not empty)
                    if (!taskTime.isEmpty()) {
                        if (!taskTime.matches("\\d{2}:\\d{2}")) { // HH:mm format
                            time.setError("Invalid time format! Use HH:mm.");
                            time.requestFocus();
                            return;
                        }
                    }

                    // Validate Reminder Time (if not empty)
                    if (!timeReminder.isEmpty()) {
                        if (!timeReminder.matches("\\d{2}:\\d{2}")) { // HH:mm format
                            reminderTime.setError("Invalid time format! Use HH:mm.");
                            reminderTime.requestFocus();
                            return;
                        }
                    }

                    // Set default priority if none is selected
                    if (priorityGroup.getCheckedRadioButtonId() == -1) {
                        selectedPriority[0] = "Medium";
                    }

                    // Create Task object and insert into database
                    Task task = new Task(taskTitle, taskDesc, selectedPriority[0], status, taskDate, taskTime, dateReminder, timeReminder, user);
                    db.insertTask(task);

                    // Navigate to Home Fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new fragment_home());
                    fragmentTransaction.commit();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new fragment_home());
                    fragmentTransaction.commit();
                }
            });
        }
    }
}