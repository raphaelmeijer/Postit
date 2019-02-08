package rma.postit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import rma.postit.adapter.CategoryAdapter;
import rma.postit.adapter.PostAdapter;
import rma.postit.fragment.PostsFragment;
import rma.postit.helper.FirebaseConnector;
import rma.postit.model.Category;
import rma.postit.model.Post;

public class MainActivity extends AppCompatActivity {
    CategoryAdapter mCategoryAdapter;
    RecyclerView mCategoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponentsFromView();
        getCategories();
    }

    private void getCategories() {
        FirebaseConnector.getInstance().getUserCategories().addSnapshotListener((categories, e) -> {
            if( categories != null )
                setAdapter(categories.toObjects(Category.class));
        });
    }

    private void setAdapter(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(categories, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        if( mCategoryView.getAdapter() == null ) {
            mCategoryView.setAdapter(mCategoryAdapter);
            mCategoryView.setLayoutManager(llm);
        }

    }

    private void getComponentsFromView() {
        mCategoryView = findViewById(R.id.category_listing);
    }
}
