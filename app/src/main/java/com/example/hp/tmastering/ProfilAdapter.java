package com.example.hp.tmastering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.tmastering.beans.user;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 06/02/2018.
 */

public class ProfilAdapter extends ArrayAdapter {
    List list = new ArrayList();
    List<String> lstId= new ArrayList<String>();
    boolean checked;

    public String getChecked()
    {
        return  lstId.toString().substring(1,lstId.toString().length()-1)+",";
    }

    public ProfilAdapter(Context context, int resource) {
        super(context, resource);
        this.checked=true;
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
        final UserHolder userHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.profil_row_layout, parent, false);
            userHolder = new UserHolder();
            userHolder.userImage=(ImageView) row.findViewById(R.id.profilImage);
            userHolder.username=(TextView) row.findViewById(R.id.profilNom);

            row.setTag(userHolder);
        } else {
            userHolder = (UserHolder) row.getTag();
        }
        final user user = (user) this.getItem(position);
        userHolder.username.setText(user.getNom()+" "+user.getPrenom());

        Picasso.with(getContext()).load("https://tmastering.000webhostapp.com/images/"+user.getId()+".jpg").into(userHolder.userImage);



        return row;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    static class UserHolder {
        ImageView userImage;
        TextView username;
        CheckBox profilBox;
    }
}