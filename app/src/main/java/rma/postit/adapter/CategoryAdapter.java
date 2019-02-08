package rma.postit.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import rma.postit.AddPostActivity;
import rma.postit.MainActivity;
import rma.postit.R;
import rma.postit.helper.FirebaseConnector;
import rma.postit.model.Category;
import rma.postit.model.Post;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    MainActivity mMainActivity;
    List<Category> mCategories;
    CategoryViewHolder mHolder;

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        RecyclerView mRecyclerView;
        Category mCategory;
        PostAdapter mAdapter;
        MainActivity mMain;
        Button mButton;

        public CategoryViewHolder(@NonNull View itemView, @NonNull Category category, @NonNull MainActivity mainActivity) {
            super(itemView);
            mCategory = category;
            mMain = mainActivity;

            getViewFromComponents(itemView);
            setOnClickListener();
            getPosts();
        }

        private void setOnClickListener() {
            mButton.setOnClickListener((event) -> {
                // start intent
                Intent intent = new Intent(mMain, AddPostActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("id", mCategory.getId());
                intent.putExtras(bundle);

                mMain.startActivity(intent);
            });
        }

        private void getPosts() {
            FirebaseConnector.getInstance().getCategoryPosts(mCategory.getId()).addSnapshotListener((posts, e) -> {
                if( mAdapter == null ) {
                    mAdapter = new PostAdapter();
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mMain, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView.setLayoutManager(horizontalLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setContext(mMain);
                    mAdapter.setCategoryId(mCategory.getId());

                }

                if( posts != null ) {
                    mAdapter.clear();
                    mAdapter.addAll(posts.toObjects(Post.class));
                }
            });
        }

        private void getViewFromComponents(View itemView) {
            mTitle = itemView.findViewById(R.id.category_title);
            mRecyclerView = itemView.findViewById(R.id.post_holder);
            mButton = itemView.findViewById(R.id.add_button);
        }

    }

    public CategoryAdapter(List<Category> categoryCollection, MainActivity main) {
        super();
        mCategories = categoryCollection;
        mMainActivity = main;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category, parent, false);
        Category category = mCategories.get(i);
        mHolder = new CategoryViewHolder(v, category, mMainActivity);
        fillViewWithCategory(mHolder, category);
        return mHolder;
    }

    private void fillViewWithCategory(CategoryViewHolder holder, Category category) {
        holder.mTitle.setText(category.getName());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, int i) {}

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public int getItemViewType(int i){
        return i;
    }

    public void clear() {
        mCategories = null;
        notifyDataSetChanged();
    }
    public void addAll(List<Category> list) {
        mCategories = list;
        notifyDataSetChanged();
    }
}
