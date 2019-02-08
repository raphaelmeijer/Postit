package rma.postit.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import rma.postit.AddPostActivity;
import rma.postit.MainActivity;
import rma.postit.R;
import rma.postit.helper.Globals;
import rma.postit.helper.ImageLoader;
import rma.postit.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    List<Post> mPosts;
    MainActivity main;
    String mCategoryId;
    public void setCategoryId(String id) {
        mCategoryId = id;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView mDescription;
        TextView mTitle;
        Button mView;
        Button mEdit;
        ImageView mImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            getComponentsFromView(itemView);
            setOnEditListener(itemView);
        }

        private void setOnEditListener(View itemView) {
            mEdit.setOnClickListener((event) -> {
                // load next activity
                Bundle bundle = new Bundle();
                Intent intent = new Intent(main, AddPostActivity.class);
                bundle.putString("id", mCategoryId);
                bundle.putString("pid", id);
                intent.putExtras(bundle);
                main.startActivity(intent);
            });
        }

        private void setDataToView(Post post) throws IOException {
            id = post.getId();
            mTitle.setText(post.getTitle());
            mDescription.setText(post.getDescription());
            mView.setOnClickListener((e) -> {

                if( post.getUri().isEmpty() ){
                    Toast.makeText(main, "No url found", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(Globals.parseUriString(post.getUri())));
                    main.startActivity(i);
                }
            });

            if( !post.getImageUrl().isEmpty() ){
                new ImageLoader(mImage, post.getImageUrl()).execute();
            }
        }

        private void getComponentsFromView(View itemView) {
            mTitle = itemView.findViewById(R.id.title);
            mDescription = itemView.findViewById(R.id.description);
            mView = itemView.findViewById(R.id.view_button);
            mEdit = itemView.findViewById(R.id.edit);
            mImage = itemView.findViewById(R.id.featured_image);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_post, viewGroup, false);
        PostViewHolder holder = new PostViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        try {
            postViewHolder.setDataToView(mPosts.get(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size();
    }

    public void clear() {
        mPosts = null;
        notifyDataSetChanged();
    }
    public void addAll(List<Post> list) {
        // mPosts = list;
        if( mPosts != null ){
            mPosts.addAll(list);
        } else {
            mPosts = list;
        }
        notifyDataSetChanged();
    }

    public void setContext(MainActivity main){
        this.main = main;
    }
}
