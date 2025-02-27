package com.github.sasd97.upitter.ui.adapters.recyclers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.models.response.pointers.CategoryPointerModel;
import com.github.sasd97.upitter.ui.base.BaseViewHolder;
import com.github.sasd97.upitter.utils.ListUtils;
import com.github.sasd97.upitter.utils.Palette;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by alexander on 27.06.16.
 */

public class CategoriesListRecycler extends RecyclerView.Adapter<CategoriesListRecycler.CategoryViewHolder> {

    public interface OnItemClickListener {
        void onClick(CategoryPointerModel category, int position);
    }

    private Context context;
    private OnItemClickListener listener;
    private List<CategoryPointerModel> categories;

    public CategoriesListRecycler(Context context, List<CategoryPointerModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class CategoryViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.category_title_category_single_view) TextView categoryTitleTextView;
        @BindView(R.id.category_preview_category_single_view) ImageView categoryPreviewImageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void setupViews() {
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(categories.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_categories_list, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CategoryPointerModel category = categories.get(position);
        final String preview = getCategoryPreview(category.getTitle());

        TextDrawable circlePreview = TextDrawable
                .builder()
                .beginConfig()
                    .textColor(R.color.colorPrimary)
                .endConfig()
                .buildRound(preview, Palette.obtainColor(R.color.colorLightBabyBlue));

        if (category.getSelectedSubcategories() == null
                || category.getSelectedSubcategories().length == 0
                || category.getSelectedSubcategoriesIds() == null
                || category.getSelectedSubcategoriesIds().length == 0)
            holder.categoryTitleTextView.setTextColor(Palette.obtainColor(R.color.colorBlack));
        else
            holder.categoryTitleTextView.setTextColor(Palette.obtainColor(R.color.colorAccent));

        holder.categoryTitleTextView.setText(category.getTitle());
        holder.categoryPreviewImageView.setImageDrawable(circlePreview);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addAll(List<CategoryPointerModel> list) {
        categories.addAll(list);
        notifyItemInserted(categories.size());
    }

    public ArrayList<Integer> getSelected() {
        ArrayList<Integer> result = new ArrayList<>();

        for (CategoryPointerModel category: categories) {
            if (category.getSelectedSubcategoriesIds() != null)
                for (Integer id: category.getSelectedSubcategoriesIds())
                    result.add(id);
        }

        return result;
    }

    public void each(@NonNull ListUtils.OnIteratorListener<CategoryPointerModel> listener) {
        final List<CategoryPointerModel> parents = ListUtils.filter(categories, new ListUtils.OnListInteractionListener<CategoryPointerModel>() {
            @Override
            public boolean isFit(CategoryPointerModel other) {
                return other.isParent();
            }
        });

        ListUtils.each(parents, listener);
    }

    private String getCategoryPreview(@NonNull String title) {
        return title.substring(0, 1);
    }
}
