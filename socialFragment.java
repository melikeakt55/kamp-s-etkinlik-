package com.example.kampsuyg;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class SocialFragment extends Fragment {
    private ListView listView;
    private DBHelper db;
    private PostAdapter adapter;
    private ArrayList<PostModel> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(android.R.layout.list_content, container, false);
        listView = view.findViewById(android.R.id.list);
        db = new DBHelper(getContext());
        
        loadPosts();
        return view;
    }

    private void loadPosts() {
        postList = new ArrayList<>();
        Cursor cursor = db.getAllPosts();
        
        if (cursor.moveToFirst()) {
            do {
                PostModel model = new PostModel(
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("content")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_uri")),
                    cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
                );
                postList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (getContext() != null) {
            adapter = new PostAdapter();
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    static class PostModel {
        String username, content, imageUri, timestamp;
        PostModel(String u, String c, String i, String t) {
            this.username = u; this.content = c; this.imageUri = i; this.timestamp = t;
        }
    }

    class PostAdapter extends BaseAdapter {
        @Override
        public int getCount() { return postList.size(); }
        @Override
        public Object getItem(int i) { return postList.get(i); }
        @Override
        public long getItemId(int i) { return i; }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_post, viewGroup, false);
            }
            
            PostModel model = postList.get(i);
            TextView username = view.findViewById(R.id.postUsername);
            TextView content = view.findViewById(R.id.postContentText);
            TextView time = view.findViewById(R.id.postTime);
            ImageView img = view.findViewById(R.id.postImageDisplay);
            
            username.setText("@" + model.username);
            content.setText(model.content);
            time.setText(model.timestamp);
            
            if (model.imageUri != null && !model.imageUri.isEmpty()) {
                img.setImageURI(Uri.parse(model.imageUri));
                img.setVisibility(View.VISIBLE);
            } else {
                img.setVisibility(View.GONE);
            }
            
            return view;
        }
    }
}
