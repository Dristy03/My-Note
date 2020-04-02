package com.example.mynoteapp;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class NotesRecyclerAdapter extends FirestoreRecyclerAdapter<Notes,NotesRecyclerAdapter.NotesViewHolder> {



    public NotesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Notes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Notes note) {

        holder.noteTextView.setText(note.getText());
        holder.checkBox.setChecked(note.getisCompleted());
        CharSequence dataCharSeq= DateFormat.format("EEEE,MMM d,yyyy h:mm:ss a",note.getCreated().toDate());
        holder.dateTextView.setText(dataCharSeq);

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.note_row,parent,false);

        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView noteTextView,dateTextView;
        CheckBox checkBox;

        public NotesViewHolder(@NonNull View itemView){
            super(itemView);
            noteTextView=itemView.findViewById(R.id.noteTextView);
            dateTextView=itemView.findViewById(R.id.dateTextView);
            checkBox=itemView.findViewById(R.id.checkBox);

        }
    }
}
