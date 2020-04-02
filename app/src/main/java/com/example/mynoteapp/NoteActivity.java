package com.example.mynoteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.zip.Inflater;

public class NoteActivity extends AppCompatActivity  implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "NoteActivity";
    private FloatingActionButton editButton;
    RecyclerView recyclerView;
    NotesRecyclerAdapter notesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        recyclerView=findViewById(R.id.recyclerViewId);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        editButton=findViewById(R.id.fabId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {

        final EditText noteEditText = new EditText(NoteActivity.this);

        noteEditText.setHeight(150);
        noteEditText.setClickable(true);
        noteEditText.setSingleLine(true);
        noteEditText.setCursorVisible(true);

        noteEditText.setHighlightColor(Color.BLACK);


          new AlertDialog.Builder(this,R.style.MyDialogTheme)

                .setTitle("Add a Note")
                  .setView(noteEditText)
                  .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.dismiss();
                       /* Toast.makeText(NoteActivity.this, "poskik",Toast.LENGTH_SHORT).show();*/
                        Log.d(TAG, "onClick: working on posi " + noteEditText.getText().toString().trim());
                       addNote(noteEditText.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ialog.dismiss();
                        Toast.makeText(NoteActivity.this, "nehgiuhu",Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onClick: working on negi");
                    }
                })

                  .create()
                  .show();
    }

    private void addNote(String text){

        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Notes note =new Notes(text,false,new Timestamp(new Date()),userId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        switch(id){
            case R.id.profilemenu:
                Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.signoutmenu:
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if(notesRecyclerAdapter !=null){
            notesRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser()==null) {
            Log.d(TAG, "onAuthStateChanged: called");
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            return;

        }

        initRecyclerView(firebaseAuth.getCurrentUser());
    }

    private void initRecyclerView(FirebaseUser user){

        Query query= FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId",user.getUid());
        FirestoreRecyclerOptions<Notes> options=new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();
        notesRecyclerAdapter=new NotesRecyclerAdapter(options);
        recyclerView.setAdapter(notesRecyclerAdapter);

        notesRecyclerAdapter.startListening();

    }
}
