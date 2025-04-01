package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainActivityCallback callback;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database db = new Database(getActivity(),"test4",null,1);

        ImageView done = (ImageView) getActivity().findViewById(R.id.doneBtn8);
        ImageView cancel = (ImageView) getActivity().findViewById(R.id.backBtn8);

        EditText emailField = (EditText) getActivity().findViewById(R.id.newEmailField);
        EditText pass1Field = (EditText) getActivity().findViewById(R.id.newPasswordField);
        EditText pass2Field = (EditText) getActivity().findViewById(R.id.newRePasswordField);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE); // Background color
        border.setCornerRadius(8);    // Corner radius
        border.setStroke(2, Color.RED); // Border width and color

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = pass1Field.getText().toString().trim();
                String password2 = pass2Field.getText().toString().trim();

                boolean isValid = true;

                // Validate Email
                if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Invalid email address");
                    emailField.setBackground(border);
                    isValid = false;
                } else {
                    emailField.setBackgroundColor(Color.WHITE); // Reset to default
                    emailField.setError(null);
                }

                if (TextUtils.isEmpty(password) ||
                        password.length() < 6 || password.length() > 12 ||
                        !password.matches(".*[0-9].*") ||
                        !password.matches(".*[a-z].*") ||
                        !password.matches(".*[A-Z].*")) {
                    pass1Field.setError("Password must be 6-12 characters with at least 1 number, 1 uppercase, and 1 lowercase letter");
                    pass1Field.setBackground(border);
                    isValid = false;
                } else {
                    pass1Field.setBackgroundColor(Color.WHITE);
                    pass1Field.setError(null);
                }

                // Validate Confirm Password
                if (TextUtils.isEmpty(password2) || !password.equals(password2)) {
                    pass2Field.setError("Passwords do not match");
                    pass2Field.setBackground(border);
                    isValid = false;
                } else {
                    pass2Field.setBackgroundColor(Color.WHITE);
                    pass2Field.setError(null);
                }

                if (isValid) {
                    db.updateUserEmailAndPassword(user.getEmail(), email, password);
                    user.setEmail(email);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.myFunction();
                    Toast.makeText(getContext(), "Updating Successful!", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getContext(), "Please fix the errors highlighted in red", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface MainActivityCallback {
        void myFunction(); // The function to be implemented in MainActivity
    }
}