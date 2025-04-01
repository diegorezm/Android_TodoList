package com.diegorezm.todolist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final TodoManager<Todo> todoManager = new TodoManager<>();

    private TextInputEditText todoEditText;
    private Button dateEditText;
    private Button addButton;
    private Calendar selectedDate;
    private Chip allFilterButton;
    private Chip undoneFilterButton;
    private Chip doneFilterButton;
    private Chip expiredFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        todoEditText = findViewById(R.id.todoEditText);
        dateEditText = findViewById(R.id.dateEditText);
        addButton = findViewById(R.id.addButton);
        allFilterButton = findViewById(R.id.allFilterButton);
        undoneFilterButton = findViewById(R.id.undoneFilterButton);
        doneFilterButton = findViewById(R.id.doneFilterButton);
        expiredFilterButton = findViewById(R.id.expiredFilterButton);

        addButton.setEnabled(false);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        addButton.setOnClickListener(v -> {
            var todo = new Todo(Objects.requireNonNull(todoEditText.getText()).toString(), dateEditText.getText().toString(), false);
            if (todo.isExpired()) {
                showToast("You are trying to add a todo in the past!");
                return;
            }
            todoManager.addTodo(todo);
            todoEditText.setText("");
            dateEditText.setText(R.string.select_date);
            updateAddButtonState();
            addTodosFragments();
        });
        setFilter(todoManager.getFilter());
        undoneFilterButton.setChecked(true);
        allFilterButton.setOnClickListener(v -> setFilter(Filter.ALL));
        undoneFilterButton.setOnClickListener(v -> setFilter(Filter.UNDONE));
        doneFilterButton.setOnClickListener(v -> setFilter(Filter.DONE));
        expiredFilterButton.setOnClickListener(v -> setFilter(Filter.EXPIRED));
    }


    public void showDatePickerDialog() {
        var calendar = Calendar.getInstance();
        var year = calendar.get(Calendar.YEAR);
        var month = calendar.get(Calendar.MONTH);
        var day = calendar.get(Calendar.DAY_OF_MONTH);
        var datePickerDialog = new DatePickerDialog(
                this, (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);
            updateDateButtonText();
            updateAddButtonState();
        }, year, month, day);
        datePickerDialog.show();
    }

    private void clearTodos() {
        var fragmentManager = getSupportFragmentManager();
        var transaction = fragmentManager.beginTransaction();
        for (var fragment : fragmentManager.getFragments()
        ) {
            transaction.remove(fragment);
        }
        transaction.commit();
    }

    private void addTodoFragment(Todo todo) {
        var fragment = TodoFragment.newInstance(todo);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    private void addTodosFragments() {
        clearTodos();
        for (var todo : todoManager.getTodos()) {
            addTodoFragment(todo);
        }
    }

    private void setFilter(Filter filter) {
        resetFiltersChecked();
        todoManager.setFilter(filter);
        switch (filter) {
            case ALL:
                allFilterButton.setChecked(true);
                break;
            case UNDONE:
                undoneFilterButton.setChecked(true);
                break;
            case DONE:
                doneFilterButton.setChecked(true);
                break;
            case EXPIRED:
                expiredFilterButton.setChecked(true);
                break;
        }
        addTodosFragments();
    }

    private void updateAddButtonState() {
        addButton.setEnabled(!Objects.requireNonNull(todoEditText.getText()).toString().trim().isEmpty() && selectedDate != null);
    }

    private void updateDateButtonText() {
        if (selectedDate != null) {
            var dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            var dateString = dateFormat.format(selectedDate.getTime());
            dateEditText.setText(dateString);
        }
    }

    public void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    private void resetFiltersChecked() {
        allFilterButton.setChecked(false);
        undoneFilterButton.setChecked(false);
        doneFilterButton.setChecked(false);
        expiredFilterButton.setChecked(false);
    }
}