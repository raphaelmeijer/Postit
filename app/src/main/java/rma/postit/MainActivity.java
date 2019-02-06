package rma.postit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import rma.postit.adapter.CategoryAdapter;
import rma.postit.helper.FirebaseConnector;
import rma.postit.model.Category;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponentsFromView();
        setUpRecyclerView();
        setCategoriesToView();
    }

    private void setUpRecyclerView() {
        // - content doesn't change so use this for permormacne
        mRecyclerView.setHasFixedSize(true);
        // - set layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setCategoriesToView() {
        ProgressDialog pd = new ProgressDialog(this );
        pd.show();
        FirebaseConnector
            .getInstance()
            .getUserCategories()
            .get()
            .addOnSuccessListener(categoryCollection -> {
                CategoryAdapter categoryAdapter = new CategoryAdapter(categoryCollection.toObjects(Category.class), this);

                mRecyclerView.setAdapter(categoryAdapter);
                pd.dismiss();
            });
    }

    private void getComponentsFromView() {
        mRecyclerView = findViewById(R.id.category_listing);
    }
}
