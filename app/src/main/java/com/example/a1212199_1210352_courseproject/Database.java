package com.example.a1212199_1210352_courseproject;

import static com.example.a1212199_1210352_courseproject.SignInActivity.user;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    // Users Table
    public static final String CREATE_USERS_TABLE = "CREATE TABLE Users (" +
            "email TEXT PRIMARY KEY, " +
            "first_name TEXT NOT NULL CHECK(LENGTH(first_name) >= 5), " +
            "last_name TEXT NOT NULL CHECK(LENGTH(last_name) >= 5), " +
            "password_hash TEXT NOT NULL, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    // Tasks Table
    public static final String CREATE_TASKS_TABLE = "CREATE TABLE Tasks (" +
            "task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_email TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "description TEXT, " +
            "due_date TEXT NOT NULL, " +
            "due_time TEXT , " +
            "priority TEXT DEFAULT 'Medium', " +
            "completion_status INTEGER DEFAULT 0, " +
            "reminder_date TEXT, " +
            "reminder_time TEXT , " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY(user_email) REFERENCES Users(email) ON UPDATE CASCADE ON DELETE CASCADE );";

    // Task Actions Table
    public static final String CREATE_TASK_ACTIONS_TABLE = "CREATE TABLE TaskActions (" +
            "action_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "task_id INTEGER NOT NULL, " +
            "action_type TEXT, " +
            "action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY(task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE);";

    // Notifications Table
    public static final String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE Notifications (" +
            "notification_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "task_id INTEGER NOT NULL, " +
            "notification_time DATETIME NOT NULL, " +
            "snoozed_until DATETIME, " +
            "sent INTEGER DEFAULT 0, " +
            "FOREIGN KEY(task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE);";

    // Imported Tasks Table
    public static final String CREATE_IMPORTED_TASKS_TABLE = "CREATE TABLE ImportedTasks (" +
            "imported_task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "task_id INTEGER NOT NULL, " +
            "api_source TEXT NOT NULL, " +
            "import_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY(task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE);";


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TASKS_TABLE);
        sqLiteDatabase.execSQL(CREATE_IMPORTED_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops the existing "tasks" table if it exists
        db.execSQL("DROP TABLE IF EXISTS tasks");

        // Calls the onCreate method to recreate the tables (based on the new schema)
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Turn on foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("password_hash", user.getPassword());

        long result = db.insert("Users", null, values);
        return result != -1; // Return true if insertion is successful
    }


    public boolean insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_email", task.getUser().getEmail());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("due_date", task.getDueDate());
        values.put("due_time", task.getDueTime());
        values.put("priority", task.getPriority());
        values.put("completion_status", task.getCompletionStatus());
        values.put("reminder_date", task.getReminderDate());
        values.put("reminder_time", task.getReminderTime());

        long result = db.insert("Tasks", null, values);
        return result != -1; // Return true if insertion is successful
    }

    public boolean insertTask1(currentTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_email", task.getUser().getEmail());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("due_date", task.getDueDate());
        values.put("due_time", task.getDueTime());
        values.put("priority", task.getPriority());
        values.put("completion_status", task.getCompletionStatus());
        values.put("reminder_date", task.getReminderDate());
        values.put("reminder_time", task.getReminderTime());

        long result = db.insert("Tasks", null, values);
        return result != -1; // Return true if insertion is successful
    }

    public Cursor getAllUsers(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Users", null);
    }

    public Cursor getAllTasks(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        // Define the query to select tasks for the provided email, sorted by due_date and priority
        String query = "SELECT * FROM Tasks WHERE user_email = ? " +
                "ORDER BY due_date ASC, " +
                "CASE UPPER(priority) " +
                "WHEN 'HIGH' THEN 1 " +
                "WHEN 'MEDIUM' THEN 2 " +
                "WHEN 'LOW' THEN 3 " +
                "ELSE 4 END";

        // Execute the query and return the Cursor
        return sqLiteDatabase.rawQuery(query, new String[]{email});
    }



    public Cursor getTask(String email, String title, String description) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Query to select tasks where user_email, title, and description match the provided values
        return sqLiteDatabase.rawQuery("SELECT * FROM Tasks WHERE user_email = ? AND title = ? AND description = ?", new String[]{email, title, description});
    }





    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM tasks WHERE task_id = ?";
        SQLiteStatement statement = db.compileStatement(deleteQuery);
        statement.bindLong(1, taskId);  // Bind the task ID to the query
        statement.executeUpdateDelete();
        db.close();
    }

    // Method to update the completion state of a task
    public void updateCompletionState(int taskId, int newCompletionState) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the ContentValues for the update query
        ContentValues contentValues = new ContentValues();
        contentValues.put("completion_status", newCompletionState);  // Assuming completion_status is the column name

        // Execute the update query
        String whereClause = "task_id = ?";
        String[] whereArgs = new String[]{String.valueOf(taskId)};

        int rowsAffected = db.update("Tasks", contentValues, whereClause, whereArgs);

        // Check if the update was successful
        if (rowsAffected > 0) {
            Log.d("Database", "Task completion state updated successfully.");
        } else {
            Log.d("Database", "No task found with the given ID.");
        }

        db.close();
    }

    public List<currentTask> getCompletedTasks(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<currentTask> completedTasks = new ArrayList<>();

        // Define the query to retrieve completed tasks sorted by due date and priority
        String query = "SELECT * FROM Tasks WHERE completion_status = 1 AND user_email = ? " +
                "ORDER BY due_date ASC, " +
                "CASE UPPER(priority) " +
                "WHEN 'HIGH' THEN 1 " +
                "WHEN 'MEDIUM' THEN 2 " +
                "WHEN 'LOW' THEN 3 " +
                "ELSE 4 END";

        // Execute the query with the provided email
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Check if there are results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve task properties from the cursor
                @SuppressLint("Range") int taskId = cursor.getInt(cursor.getColumnIndex("task_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex("due_date"));
                @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex("due_time"));
                @SuppressLint("Range") String reminderDate = cursor.getString(cursor.getColumnIndex("reminder_date"));
                @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex("priority"));
                @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("user_email"));
                @SuppressLint("Range") int completionStatus = cursor.getInt(cursor.getColumnIndex("completion_status"));

                // Create a currentTask object and add it to the list
                currentTask task = new currentTask(taskId, title, description, priority, completionStatus, dueDate, dueTime, reminderDate, reminderTime, user);
                completedTasks.add(task);
            } while (cursor.moveToNext()); // Move to the next result in the Cursor
        }

        // Close the cursor and the database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return completedTasks; // Return the list of completed tasks
    }


    public List<currentTask> getTodayTasks(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<currentTask> todayTasks = new ArrayList<>();

        // Get the current date in the format used by the database
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        String todayDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // Define the query with an ORDER BY clause and case normalization for priority
        String query = "SELECT * FROM Tasks WHERE due_date = ? AND user_email = ? " +
                "ORDER BY CASE UPPER(priority) " +
                "WHEN 'HIGH' THEN 1 " +
                "WHEN 'MEDIUM' THEN 2 " +
                "WHEN 'LOW' THEN 3 " +
                "ELSE 4 END";

        // Execute the query with the current date and email
        Cursor cursor = db.rawQuery(query, new String[]{todayDate, email});

        // Check if there are results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve task properties from the cursor
                @SuppressLint("Range") int taskId = cursor.getInt(cursor.getColumnIndex("task_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex("due_date"));
                @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex("due_time"));
                @SuppressLint("Range") String reminderDate = cursor.getString(cursor.getColumnIndex("reminder_date"));
                @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex("priority"));
                @SuppressLint("Range") int completionStatus = cursor.getInt(cursor.getColumnIndex("completion_status"));

                // Create a currentTask object and add it to the list
                currentTask task = new currentTask(taskId, title, description, priority, completionStatus, dueDate, dueTime, reminderDate, reminderTime, user);
                todayTasks.add(task);
            } while (cursor.moveToNext()); // Move to the next result in the Cursor
        }

        // Close the cursor and the database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return todayTasks; // Return the list of today's tasks
    }

    public boolean updateUserEmailAndPassword(String oldEmail, String newEmail, String newPasswordHash) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Start a transaction to ensure both updates happen together
        db.beginTransaction();
        try {
            // Step 1: Update the email and password in the Users table first
            ContentValues userValues = new ContentValues();
            userValues.put("email", newEmail);  // Update new email
            userValues.put("password_hash", newPasswordHash);  // Update password
            userValues.put("updated_at", System.currentTimeMillis());  // Use a valid timestamp
            int rowsAffectedUsers = db.update("Users", userValues, "email = ?", new String[]{oldEmail});

            // If no rows were updated in the Users table, rollback the transaction
            if (rowsAffectedUsers == 0) {
                throw new Exception("Failed to update Users table. No matching email found.");
            }

            // Step 2: Update related foreign key references in Tasks table
            ContentValues taskValues = new ContentValues();
            taskValues.put("user_email", newEmail); // Update the user_email in related table
            int rowsAffectedTasks = db.update("Tasks", taskValues, "user_email = ?", new String[]{oldEmail});

            // Set the transaction as successful
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // End the transaction
            db.endTransaction();
        }
    }


    public List<currentTask> searchTasks(String searchQuery, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<currentTask> filteredTasks = new ArrayList<>();

        // Use the LIKE operator to search for tasks where the title or description contains the search query
        // We use "%"+searchQuery+"%" to match any substring that contains the search query
        String query = "SELECT * FROM Tasks WHERE (title LIKE ? OR description LIKE ?) AND user_email = ?";

        // Prepare the arguments for the query
        String[] args = new String[] {
                "%" + searchQuery + "%",
                "%" + searchQuery + "%",
                email
        };

        // Execute the query
        Cursor cursor = db.rawQuery(query, args);

        // Check if there are results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve task properties from the cursor
                @SuppressLint("Range") int taskId = cursor.getInt(cursor.getColumnIndex("task_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex("due_date"));
                @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex("due_time"));
                @SuppressLint("Range") String reminderDate = cursor.getString(cursor.getColumnIndex("reminder_date"));
                @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex("priority"));
                @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("user_email"));
                @SuppressLint("Range") int completionStatus = cursor.getInt(cursor.getColumnIndex("completion_status"));

                // Create a currentTask object and add it to the list
                currentTask task = new currentTask(taskId, title, description, priority, completionStatus, dueDate, dueTime, reminderDate, reminderTime, user);
                filteredTasks.add(task);
            } while (cursor.moveToNext()); // Move to the next result in the Cursor
        }

        // Close the cursor and return the result
        if (cursor != null) {
            cursor.close();
        }
        return filteredTasks;
    }

    public List<currentTask> getTasksBetweenDates(String startDate, String endDate, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<currentTask> tasks = new ArrayList<>();

        // SQL query to filter tasks between two dates and by email
        String query = "SELECT * FROM Tasks WHERE due_date BETWEEN ? AND ? AND user_email = ?";

        // Arguments for the query
        String[] args = new String[]{
                startDate, // Start date
                endDate,   // End date
                email      // User email
        };

        // Execute the query
        Cursor cursor = db.rawQuery(query, args);

        // Check if there are results and iterate through them
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int taskId = cursor.getInt(cursor.getColumnIndex("task_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex("due_date"));
                @SuppressLint("Range") String dueTime = cursor.getString(cursor.getColumnIndex("due_time"));
                @SuppressLint("Range") String reminderDate = cursor.getString(cursor.getColumnIndex("reminder_date"));
                @SuppressLint("Range") String reminderTime = cursor.getString(cursor.getColumnIndex("reminder_time"));
                @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex("priority"));
                @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("user_email"));
                @SuppressLint("Range") int completionStatus = cursor.getInt(cursor.getColumnIndex("completion_status"));

                // Create a currentTask object
                currentTask task = new currentTask(
                        taskId,
                        title,
                        description,
                        priority,
                        completionStatus,
                        dueDate,
                        dueTime,
                        reminderDate,
                        reminderTime,
                        user
                );

                // Add the task to the list
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // Close the cursor to release resources
        if (cursor != null) {
            cursor.close();
        }

        // Return the list of tasks
        return tasks;
    }


}
