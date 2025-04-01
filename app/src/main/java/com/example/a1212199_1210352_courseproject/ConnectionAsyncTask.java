package com.example.a1212199_1210352_courseproject;

import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    // Use a WeakReference to avoid memory leaks
    private final WeakReference<APIFragment> fragmentRef;

    public ConnectionAsyncTask(APIFragment fragment) {
        this.fragmentRef = new WeakReference<>(fragment);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Get the fragment from the WeakReference
        APIFragment fragment = fragmentRef.get();
        if (fragment != null && fragment.isAdded()) {
            fragment.setButtonText("Connecting");
            fragment.setProgress(true);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        // Fetch data from the network
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // Get the fragment from the WeakReference
        APIFragment fragment = fragmentRef.get();
        if (fragment != null && fragment.isAdded()) {
            fragment.setProgress(false);
            fragment.setButtonText("Connected");

            // Parse the JSON data and update the UI
            List<Task> t = TaskJsonParser.getObjectFromJson(s);
            ArrayList<Task> tasks = new ArrayList<>();
            tasks.addAll(t);
            fragment.fillTasks(tasks);
        }
    }
}
