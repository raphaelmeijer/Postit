package rma.postit;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.List;

import rma.postit.adapter.PostAdapter;
import rma.postit.helper.FirebaseConnector;
import rma.postit.helper.Globals;
import rma.postit.helper.ImageLoader;
import rma.postit.model.Category;
import rma.postit.model.Post;

public class test extends AppCompatActivity {
    RecyclerView mRecyclerView;
    PostAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mAdapter = new PostAdapter();
        mRecyclerView = findViewById(R.id.category_holder);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManager);
        FirebaseConnector
            .getInstance()
            .getUserCategory("1235511f-6bcb-443a-8571-3bfa1c110576")
            .addSnapshotListener((document, e) -> {
                Category category = document.toObject(Category.class);
                // set base data :)
                // mCategoryTitle.setText(category.getName());
                // mCategoryDescription.setText(category.getDescription());
            });

        getPostsFromCategory();
    }
    private void getPostsFromCategory() {
        FirebaseConnector
            .getInstance()
            .getCategoryPosts("1235511f-6bcb-443a-8571-3bfa1c110576")
            .addSnapshotListener((collection, e)->{
                mAdapter.addAll(collection.toObjects(Post.class));
                mAdapter.addAll(collection.toObjects(Post.class));
                mAdapter.addAll(collection.toObjects(Post.class));
                mAdapter.setCategoryId("1235511f-6bcb-443a-8571-3bfa1c110576");
            });
    }
}