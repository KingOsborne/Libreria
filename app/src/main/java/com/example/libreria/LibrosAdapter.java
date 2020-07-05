package com.example.libreria;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.LibrosViewHolder> {

    ArrayList<EstanteParaLibros> libros;
    public LibrosAdapter(ArrayList<EstanteParaLibros> libros) {
        this.libros = libros;
    }

    @NonNull
    @Override
    public LibrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_item_view,parent,false);
        return new LibrosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibrosViewHolder holder, int position) {
        EstanteParaLibros unlibro = libros.get(position);
        holder.bind(unlibro);
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public class LibrosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookTitle;
        TextView bookSubtitle;
        TextView bookAuthors;
        TextView bookRating;
        TextView bookPages;
        TextView bookDate;
        TextView bookPublisher;

        public LibrosViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = (TextView) itemView.findViewById(R.id.bookTitle);
            bookSubtitle = (TextView) itemView.findViewById(R.id.bookSubtitle);
            bookAuthors = (TextView) itemView.findViewById(R.id.bookAuthors);
            bookRating = (TextView) itemView.findViewById(R.id.bookRating);
            bookPages = (TextView) itemView.findViewById(R.id.bookPages);
            bookDate = (TextView) itemView.findViewById(R.id.bookPublishedDate);
            bookPublisher = (TextView) itemView.findViewById(R.id.bookPublisher);
            itemView.setOnClickListener(this);
        }

        public void bind(EstanteParaLibros unlibro){
            bookTitle.setText(unlibro.getTitle());
            if (unlibro.getSubtitle().isEmpty()) {
                bookSubtitle.setVisibility(View.GONE);
            } else {
                bookSubtitle.setText(unlibro.getSubtitle());
            }
            if (unlibro.getAuthors().isEmpty()) {
                bookAuthors.setText("N/A");
            } else {
                bookAuthors.setText(unlibro.getAuthors());
            }
            if (unlibro.getAverageRating() == 0) {
                bookRating.setText("N/A");
            } else {
                bookRating.setText(Double.toString(unlibro.getAverageRating()));
            }
            if (unlibro.getPageCount() == 0) {
                bookPages.setText("N/A");
            } else {
                bookPages.setText(Double.toString(unlibro.getPageCount()));
            }

            bookDate.setText(unlibro.getPublishedDate());
            bookPublisher.setText(unlibro.getPublisher());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            EstanteParaLibros unlibro = libros.get(position);
            Intent intent = new Intent(v.getContext(),BookDetail.class);
            intent.putExtra("Book",unlibro);
            v.getContext().startActivity(intent);
        }
    }
}
