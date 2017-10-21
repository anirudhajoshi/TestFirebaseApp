package com.example.aniru.testfirebaseapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aniru on 10/13/2017.
 */

public class BooksAdapter extends ArrayAdapter<BookDetails_FB> {


    public BooksAdapter(Context context, int resource, List<BookDetails_FB> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_bookdetails, parent, false);
        }

        BookDetails_FB bookDetails = getItem(position);

        TextView tvBookDetails = convertView.findViewById(R.id.tvBookDetails);
        tvBookDetails.setText(bookDetails.getTitle());
        return convertView;
    }
}
