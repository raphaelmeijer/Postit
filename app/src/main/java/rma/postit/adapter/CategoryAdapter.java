package rma.postit.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import rma.postit.MainActivity;
import rma.postit.R;
import rma.postit.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    MainActivity mMainActivity;
    List<Category> mCategories;

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;
        TextView description;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            getViewFromComponents(itemView);
        }

        private void getViewFromComponents(View itemView) {
            view = itemView;
            title = itemView.findViewById(R.id.category_name);
            description = itemView.findViewById(R.id.category_description);
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
        Log.d("POST-IT", i+"");
        Log.d("POST-IT", category.getName());

        CategoryViewHolder holder = new CategoryViewHolder(v);
        fillViewWithCategory(holder, category);
        setOnClickListener(holder);
        return holder;
    }

    private void fillViewWithCategory(CategoryViewHolder holder, Category category) {
        holder.title.setText(category.getName());
        holder.description.setText(category.getDescription());
    }

    private void setOnClickListener(CategoryViewHolder holder) {
        holder.view.setOnClickListener((e) -> {

        });
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
