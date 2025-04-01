package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTaskFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditTaskFragment newInstance(currentTask task) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        if (task != null) {
            args.putSerializable("currentTask", task);
        } else {
            Log.e("EditTaskFragment", "Task is null. Cannot create fragment.");
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();

        Database db = new Database(getActivity(),"test4",null,1);

        ImageView delete = (ImageView) getActivity().findViewById(R.id.deleteBtn);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.doneBtn1);
        ImageView cancel = (ImageView) getActivity().findViewById(R.id.backBtn1);
        ImageView cancelBtn = (ImageView) getActivity().findViewById(R.id.cancelBtn);
        ImageView confirmBtn = (ImageView) getActivity().findViewById(R.id.confirmBtn);
        EditText email = (EditText) getActivity().findViewById(R.id.distEmailField);
        ImageView share = (ImageView) getActivity().findViewById(R.id.shareBtn);
        ImageView notification = (ImageView) getActivity().findViewById(R.id.notificationBtn);
        EditText title = (EditText) getActivity().findViewById(R.id.titleField1);
        EditText description = (EditText) getActivity().findViewById(R.id.descriptionField1);
        EditText date = (EditText) getActivity().findViewById(R.id.dateField1);
        EditText reminderDate = (EditText) getActivity().findViewById(R.id.reminderDateField1);
        EditText time = (EditText) getActivity().findViewById(R.id.timeField1);
        EditText reminderTime = (EditText) getActivity().findViewById(R.id.reminderTimeField1);
        ConstraintLayout emailDist = (ConstraintLayout) getActivity().findViewById(R.id.distEmailInterface);
        ConstraintLayout basicInterface = (ConstraintLayout) getActivity().findViewById(R.id.basicInterface);

        emailDist.setEnabled(false);
        emailDist.setVisibility(View.GONE);


        RadioGroup priorityGroup = (RadioGroup) getActivity().findViewById(R.id.priorityGroup1);  // Use the correct ID of your RadioGroup

        RadioButton highPriority = getActivity().findViewById(R.id.highBtn1);
        RadioButton mediumPriority = getActivity().findViewById(R.id.mediumBtn1);
        RadioButton lowPriority = getActivity().findViewById(R.id.lowBtn1);

        CheckBox done = (CheckBox) getActivity().findViewById(R.id.doneCheck);

        final String[] selectedPriority = new String[1];

        final int[] task_idWrapper = new int[1];

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

        if (arguments != null) {
            currentTask task = (currentTask) arguments.getSerializable("currentTask");

            if (task != null) {
                // Retrieve each property from the Task object
                task_idWrapper[0] = task.getId();
                String taskTitle = task.getTitle();
                String taskDesc = task.getDescription();
                String taskDate = task.getDueDate();
                String reminderDates = task.getReminderDate();
                String taskTime = task.getDueTime();
                String reminderTimes = task.getReminderTime();
                String priority = task.getPriority();
                int status = task.getCompletionStatus();

                title.setText(taskTitle);
                description.setText(taskDesc);
                date.setText(taskDate);
                reminderDate.setText(reminderDates);
                time.setText(taskTime);
                reminderTime.setText(reminderTimes);

                if(priority.equals("High") || priority.equals("high")) {
                    priorityGroup.check(highPriority.getId());
                }
                else if(priority.equals("Medium") || priority.equals("medium")) {
                    priorityGroup.check(mediumPriority.getId());
                }
                else if(priority.equals("Low") || priority.equals("low")) {
                    priorityGroup.check(lowPriority.getId());
                }

                if(status == 1){
                    done.setChecked(true);
                }
                else{
                    done.setChecked(false);
                }
            }

        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTask task = (currentTask) arguments.getSerializable("currentTask");
                if (task != null) {
                    db.deleteTask(task.getId());
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new fragment_home());
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Task is null. Cannot delete.", Toast.LENGTH_SHORT).show();
                    Log.e("EditTaskFragment", "Task is null. Cannot delete.");
                }
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInterface.setEnabled(false);
                basicInterface.setVisibility(View.INVISIBLE);
                emailDist.setEnabled(true);
                emailDist.setVisibility(View.VISIBLE);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInterface.setEnabled(true);
                basicInterface.setVisibility(View.VISIBLE);
                emailDist.setEnabled(false);
                emailDist.setVisibility(View.GONE);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString().trim();
                GradientDrawable border = new GradientDrawable();
                border.setColor(Color.WHITE); // Background color
                border.setCornerRadius(8);    // Corner radius
                border.setStroke(2, Color.RED); // Border width and color
                if (TextUtils.isEmpty(emailAddress) || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    email.setError("Invalid email address");
                    email.setBackground(border);
                    return;
                } else {
                    email.setBackgroundColor(Color.WHITE); // Reset to default
                    email.setError(null);
                    currentTask task = (currentTask) arguments.getSerializable("currentTask");
                    sendTaskEmail(task, emailAddress);
                    basicInterface.setEnabled(true);
                    basicInterface.setVisibility(View.VISIBLE);
                    emailDist.setEnabled(false);
                    emailDist.setVisibility(View.GONE);
                }

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                currentTask task = (currentTask) arguments.getSerializable("currentTask");
//                AlarmUtils.setNotificationAlarm(getActivity(), task.get, 0, task.getTitle(), task.getDescription());
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskTitle1 = title.getText().toString();
                    String taskDesc1 = description.getText().toString();
                    String taskDate1 = date.getText().toString();
                    String dateReminder1 = reminderDate.getText().toString();
                    String taskTime1 = time.getText().toString();
                    String timeReminder1 = reminderTime.getText().toString();
                    int status = 0;
                    if(done.isChecked()){
                        status = 1;
                    }

                    // Validate Task Title
                    if (taskTitle1.isEmpty()) {
                        title.setError("Title cannot be empty!");
                        title.requestFocus();
                        return;
                    }

                    // Validate Task Date
                    if (!taskDate1.matches("\\d{4}-\\d{2}-\\d{2}") || taskDate1.isEmpty()) {
                        date.setError("Invalid date format! Use YYYY-MM-DD.");
                        date.requestFocus();
                        return;
                    }

                    try {
                        // Parse Task Date
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate parsedTaskDate = LocalDate.parse(taskDate1, dateFormatter);

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
                    if (!dateReminder1.isEmpty()) {
                        if (!dateReminder1.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            reminderDate.setError("Invalid date format! Use YYYY-MM-DD.");
                            reminderDate.requestFocus();
                            return;
                        }

                        try {
                            LocalDate parsedReminderDate = LocalDate.parse(dateReminder1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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
                    if (!taskTime1.isEmpty()) {
                        if (!taskTime1.matches("\\d{2}:\\d{2}")) { // HH:mm format
                            time.setError("Invalid time format! Use HH:mm.");
                            time.requestFocus();
                            return;
                        }
                    }

                    // Validate Reminder Time (if not empty)
                    if (!timeReminder1.isEmpty()) {
                        if (!timeReminder1.matches("\\d{2}:\\d{2}")) { // HH:mm format
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
                    currentTask task = new currentTask(task_idWrapper[0], taskTitle1, taskDesc1, selectedPriority[0], status, taskDate1, taskTime1, dateReminder1, timeReminder1, user);
                    db.deleteTask(task_idWrapper[0]);
                    db.insertTask1(task);
                    // Navigate to Home Fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new fragment_home());
                    fragmentTransaction.commit();
                }
            });
        }
    }

    // Function to send the task via email
    public void sendTaskEmail(currentTask task, String recipientEmail) {
        // Create an implicit intent to send email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // Set the type of data to send as email
        emailIntent.setType("message/rfc822"); // Standard MIME type for email

        // Put the subject, recipient, and body
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail}); // optional, recipient email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Task: " + task.getTitle()); // Set subject as task title
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Task Title: " + task.getTitle() + "\n\n" + "Description: " + task.getDescription()); // Set body with task description

        // Start the email intent
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
