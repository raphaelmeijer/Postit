package rma.postit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rma.postit.AddPostActivity;
import rma.postit.MainActivity;
import rma.postit.R;
import rma.postit.adapter.PostAdapter;
import rma.postit.helper.FirebaseConnector;
import rma.postit.model.Category;
import rma.postit.model.Post;

public class PostsFragment extends Fragment {
    RecyclerView mRecyclerView;
    String mCategoryName;
    PostAdapter mAdapter;
    TextView mCategoryTitle;
    TextView mCategoryDescription;
    TextView mPostPlaceholder;
    Button mAddButton;

    public PostsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onViewStateRestored(savedInstanceState);
        }

        // save it in a variable so we can create variables and stuff
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        getDataFromArguments();
        getViewComponents(view);
        setUpRecyclerView();
        setOnClickListener();
        return view;
    }

    private void setOnClickListener() {
        mAddButton.setOnClickListener((event) -> {
            Intent intent = new Intent(getActivity(), AddPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", mCategoryName);

            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void getDataFromArguments() {
        Bundle bundle = getArguments();

        if( bundle != null ){
            mCategoryName = bundle.getString("id");
        }
    }

    private void getViewComponents(View view) {
        mRecyclerView = view.findViewById(R.id.posts_listing);
        mCategoryTitle = view.findViewById(R.id.category_title);
        mCategoryDescription = view.findViewById(R.id.category_description);
        mPostPlaceholder = view.findViewById(R.id.placeholder_text);
        mAddButton = view.findViewById(R.id.add_button);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void clearAdapter() {
        if( mAdapter != null )
            mAdapter.clear();
    }

    public void addCategory(String tag) {
        mCategoryName = tag;

        if( mAdapter == null ){
            mAdapter = new PostAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setContext((MainActivity) getActivity());
        }

        FirebaseConnector
            .getInstance()
            .getUserCategory(mCategoryName)
            .addSnapshotListener((document, e) -> {
                Category category = document.toObject(Category.class);
                // set base data :)
                mCategoryTitle.setText(category.getName());
                mCategoryDescription.setText(category.getDescription());
            });

        getPostsFromCategory();
    }

    private void getPostsFromCategory() {
        FirebaseConnector
            .getInstance()
            .getCategoryPosts(mCategoryName)
            .addSnapshotListener((collection, e)->{
                mAdapter.addAll(collection.toObjects(Post.class));
                mAdapter.setCategoryId(mCategoryName);
                if( mAdapter.getItemCount() == 0 ){
                    mPostPlaceholder.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mPostPlaceholder.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
    }
}
