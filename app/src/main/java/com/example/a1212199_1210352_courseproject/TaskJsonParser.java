package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class TaskJsonParser {
    public static List<Task> getObjectFromJson(String json) {
        List<Task> tasks;
        try {
            JSONArray jsonArray = new JSONArray(json);
            tasks = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                int status = jsonObject.getInt("status");
                String priority = jsonObject.getString("priority");
                String date = jsonObject.getString("deadline");
                Task task = new Task(title, description, priority, status, date, "", "", "", user);
                tasks.add(task);
            }
        } catch (JSONException e) {
            Log.e("TaskJsonParser", "Error parsing JSON", e);
            // Return an empty list instead of null
            return new ArrayList<>();
        }
        return tasks;
    }
}
