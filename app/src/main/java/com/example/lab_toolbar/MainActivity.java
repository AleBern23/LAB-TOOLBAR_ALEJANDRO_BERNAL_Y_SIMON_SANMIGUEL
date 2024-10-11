package com.example.lab_toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteLongClickListener {

    private static final String TAG = "MainActivity";
    private List<String> notes;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupRecyclerView();

        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public void onNoteLongClick(int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNoteAt(position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            addNote();
            return true;
        } else if (id == R.id.action_share) {
            shareNotes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        EditText editText = new EditText(this);
        editText.setHint("Enter note");
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("New Note")
                .setView(editText)
                .setPositiveButton("Add", (dialog, which) -> {
                    String note = editText.getText().toString();
                    if (!note.isEmpty()) {
                        notes.add(note);
                        noteAdapter.notifyItemInserted(notes.size() - 1);
                    } else {
                        Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNoteAt(int position) {
        notes.remove(position);
        noteAdapter.notifyItemRemoved(position);
    }

    private void shareNotes() {
        StringBuilder notesText = new StringBuilder();
        for (String note : notes) {
            notesText.append(note).append("\n");
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, notesText.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share notes via"));
    }
}