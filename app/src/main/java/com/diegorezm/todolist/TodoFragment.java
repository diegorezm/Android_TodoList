package com.diegorezm.todolist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TodoFragment<T extends Todo> extends Fragment {

    public static final String ARG_TODO = "todo";
    private T todo;
    private TextView titleTextView;
    private TextView dateTextView;
    private Button doneButton;

    public static TodoFragment newInstance(Todo todo) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO, todo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todo = (T) getArguments().getSerializable(ARG_TODO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        titleTextView = view.findViewById(R.id.titleTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        doneButton = view.findViewById(R.id.doneButton);

        titleTextView.setText(todo.getTitle());
        dateTextView.setText(todo.getCreatedAt());

        updateTodoView();

        doneButton.setOnClickListener(v -> {
            todo.setCompleted(true);
            updateTodoView();
        });

        return view;
    }

    private void updateTodoView() {
        if (todo.isCompleted()) {
            titleTextView.setTextColor(Color.GRAY);
            dateTextView.setTextColor(Color.GRAY);
            doneButton.setVisibility(View.GONE);
        } else {
            boolean isExpired = todo.isExpired();
            if (isExpired) {
                titleTextView.setTextColor(Color.RED);
                dateTextView.setTextColor(Color.RED);
                var date = todo.getCreatedAt();
                dateTextView.setText(date + " (EXPIRED)");
                doneButton.setEnabled(false);
            } else {
                titleTextView.setTextColor(Color.WHITE);
                dateTextView.setTextColor(Color.GRAY);
            }
        }
    }
}

