package rma.postit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import rma.postit.fragment.PostsFragment;
import rma.postit.helper.FirebaseConnector;
import rma.postit.model.Category;

public class MainActivity extends AppCompatActivity {
    TabLayout mCategoryTabs;
    PostsFragment mPostsFragment;
    RelativeLayout mHolder;
    List<Category> mCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponentsFromView();
        setUpFragment();
        setCategoriesToView();
        setOnClickListener();
    }

    private void setUpFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(mPostsFragment.getTag());
        fragmentTransaction.replace(R.id.main_holder, mPostsFragment);
        fragmentTransaction.commit();
    }

    private void setOnClickListener() {
        mCategoryTabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadFragment(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadFragment(TabLayout.Tab tab) {
        mPostsFragment.clearAdapter();
        mPostsFragment.addCategory(mCategories.get(tab.getPosition()).getId());
    }


    private void setCategoriesToView() {
        ProgressDialog pd = new ProgressDialog(this );
        pd.show();
        FirebaseConnector
            .getInstance()
            .getUserCategories()
            .addSnapshotListener((categoryCollection,e) -> {
                int i = 0;

                mCategories = categoryCollection.toObjects(Category.class);

                for( Category category :  mCategories ){
                    TabLayout.Tab tab = mCategoryTabs.newTab();

                    // save the id as tag
                    mCategoryTabs.addTab(tab.setText(category.getName()));

                    if( i == 0 ){
                        // set current tab selected
                        tab.select();
                    }

                    i++;
                }

                pd.dismiss();
            });
    }

    private void getComponentsFromView() {
        mCategoryTabs = findViewById(R.id.category_tabs);
        mPostsFragment = new PostsFragment();
        mHolder = findViewById(R.id.main_holder);
    }
}
