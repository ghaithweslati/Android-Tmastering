package com.example.hp.tmastering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.tmastering.beans.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 06/02/2018.
 */

public class DocumentAdapter extends ArrayAdapter {
    List list = new ArrayList();


    public DocumentAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final DocumentHolder documentHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.document_row_layout, parent, false);
            documentHolder = new DocumentHolder();
            documentHolder.docImage=(ImageView) row.findViewById(R.id.docimg);
            documentHolder.docname=(TextView) row.findViewById(R.id.doctitre);

            row.setTag(documentHolder);
        } else {
            documentHolder = (DocumentHolder) row.getTag();
        }
        final document document = (document) this.getItem(position);
        documentHolder.docname.setText(document.getNom());

        if(document.getNom().contains(".doc")||document.getNom().contains(".docx"))
              documentHolder.docImage.setBackgroundResource(R.drawable.word);
        else if (document.getNom().contains(".pdf"))
            documentHolder.docImage.setBackgroundResource(R.drawable.pdf);
             return row;
    }



    static class DocumentHolder {
        ImageView docImage;
        TextView docname;
    }
}