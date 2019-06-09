package com.qutiptask.notesapp.adapters;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qutiptask.notesapp.Notes;
import com.qutiptask.notesapp.R;
import com.qutiptask.notesapp.view.AddNewNoteActivity;
import com.qutiptask.notesapp.view_model.NotesViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private Activity mActivity;
    private List<Notes> notesList = new ArrayList<>();
    private Notes mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private NotesViewModel notesViewModel;

    public NotesAdapter(Activity activity){
        this.mActivity = activity;
    }

    public void setNotesList(List<Notes> notesList){
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = notesList.get(position);
        mRecentlyDeletedItemPosition = position;
        notesList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

        notesViewModel = ViewModelProviders.of((FragmentActivity) mActivity).get(NotesViewModel.class);
        notesViewModel.delete(mRecentlyDeletedItem);
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesAdapter.this.undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        notesList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        notesViewModel.insert(mRecentlyDeletedItem);
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        NotesHolder notesHolder;

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);

        notesHolder = new NotesHolder(itemView);

        return notesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder notesHolder, final int i) {

        notesHolder.title_txt.setText(notesList.get(i).getTitle());
        notesHolder.body_txt.setText(notesList.get(i).getBody());
        notesHolder.priority_txt.setText(String.valueOf(notesList.get(i).getPriority()));
        notesHolder.noteContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddNewNoteActivity.class);
                intent.putExtra("TITLE", notesList.get(i).getTitle());
                intent.putExtra("BODY", notesList.get(i).getBody());
                intent.putExtra("PRIORITY", notesList.get(i).getPriority());
                intent.putExtra("ID", notesList.get(i).getId());
                intent.putExtra("EXTRA_TYPE", 1);
                mActivity.startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class NotesHolder extends RecyclerView.ViewHolder{

        TextView title_txt, body_txt, priority_txt;
        RelativeLayout noteContainerLayout;

        NotesHolder(@NonNull final View itemView) {
            super(itemView);
            title_txt = itemView.findViewById(R.id.title_txt);
            body_txt = itemView.findViewById(R.id.body_txt);
            priority_txt = itemView.findViewById(R.id.priority_txt);
            noteContainerLayout = itemView.findViewById(R.id.noteContainerLayout);
        }
    }
}