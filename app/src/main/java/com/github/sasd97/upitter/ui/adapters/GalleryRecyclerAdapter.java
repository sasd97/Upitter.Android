package com.github.sasd97.upitter.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sasd97.upitter.R;
import com.github.sasd97.upitter.components.NumerableCheckView;
import com.github.sasd97.upitter.events.OnGalleryInteractionListener;
import com.github.sasd97.upitter.models.skeletons.ImageSkeleton;
import com.github.sasd97.upitter.ui.adapters.filters.GalleryImageFilter;
import com.github.sasd97.upitter.utils.Names;
import com.github.sasd97.upitter.utils.mutators.PhotoMutator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 31.01.2016.
 */

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ImageChooserViewHolder>
                                        implements Filterable {

    private Context context;
    private boolean isMultiSelectionMode;

    private int selectItemCounter = 0;
    private int maxSelectItemsAmount = 3;

    private ArrayList<ImageSkeleton> multiSelectedList;
    private ArrayList<ImageSkeleton> imagesPathsOriginals;
    private ArrayList<ImageSkeleton> imagesPathsFiltered;

    private OnGalleryInteractionListener galleryInteractionListener;

    public GalleryRecyclerAdapter(boolean isMultiSelectionMode,
                                  int maxSelectItemsAmount,
                                  @NonNull Context context,
                                  @NonNull ArrayList<ImageSkeleton> imagesPaths) {
        this.context = context;

        this.isMultiSelectionMode = isMultiSelectionMode;
        this.maxSelectItemsAmount = maxSelectItemsAmount;

        this.imagesPathsOriginals = imagesPaths;
        imagesPathsFiltered = (ArrayList<ImageSkeleton>) imagesPaths.clone();

        if (isMultiSelectionMode) multiSelectedList = new ArrayList<>(maxSelectItemsAmount);
    }

    public void setOnImageChooserListener(OnGalleryInteractionListener listener) {
        galleryInteractionListener = listener;
    }

    public class ImageChooserViewHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener {

        private ImageView imagePreview;
        private NumerableCheckView multiSelectCounterCheckView;

        public ImageChooserViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imagePreview = (ImageView) itemView.findViewById(R.id.image_preview_gallery_thumbnail_single_view);
            multiSelectCounterCheckView = (NumerableCheckView) itemView.findViewById(R.id.multi_select_checkbox_gallery_thumbnail_single_view);

            if (!isMultiSelectionMode) {
                multiSelectCounterCheckView.setVisibility(View.GONE);
                return;
            }

            multiSelectCounterCheckView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!(v instanceof NumerableCheckView)) {
                if (galleryInteractionListener != null)
                    galleryInteractionListener.onThumbnailClick(getAdapterPosition(), imagesPathsFiltered.get(getAdapterPosition()));
                return;
            }

            if (!isMultiSelectionMode) return;

            final int currentPosition = getAdapterPosition();
            final ImageSkeleton currentItem = imagesPathsFiltered.get(currentPosition);
            final boolean checkState = !currentItem.isChecked();

            if (checkState && (selectItemCounter >= maxSelectItemsAmount || selectItemCounter < 0)) {
                galleryInteractionListener.onMultiSelectionLimitExceeded();
                return;
            }

            if (checkState) {
                changeSelectionState(currentItem, true, selectItemCounter + 1);
                currentItem.setListPosition(currentPosition);
                multiSelectedList.add(selectItemCounter, currentItem);
                selectItemCounter++;
            } else {
                selectItemCounter--;
                multiSelectedList.remove(currentItem.getMultiSelectCounter() - 1);
                changeSelectionState(currentItem, false, selectItemCounter);

                int counter = 1;
                for (ImageSkeleton item: multiSelectedList) {
                    item.setMultiSelectCounter(counter);
                    notifyItemChanged(item.getListPosition());
                    counter++;
                }
            }

            if (galleryInteractionListener != null)
            galleryInteractionListener.onMultiSelectionCounterClick(getAdapterPosition(), multiSelectedList);
        }

        private void changeSelectionState(ImageSkeleton item, boolean state, int position) {
            item.setMultiSelectCounter(position);
            item.setCheck(state);
            multiSelectCounterCheckView.setPosition(position);
            multiSelectCounterCheckView.setChecked(state);
        }
    }

    @Override
    public ImageChooserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail_single_view, parent, false);
        return new ImageChooserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageChooserViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(Names
                        .getInstance()
                        .getFilePath(imagesPathsFiltered.get(position).getPath())
                        .toString())
                .centerCrop()
                .into(holder.imagePreview);

        holder.multiSelectCounterCheckView.setPosition(imagesPathsFiltered.get(position).getMultiSelectCounter());
        holder.multiSelectCounterCheckView.setChecked(imagesPathsFiltered.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return imagesPathsFiltered.size();
    }

    public void addAll(List<ImageSkeleton> list) {
        imagesPathsOriginals.addAll(list);
        notifyItemInserted(imagesPathsOriginals.size());
    }

    @Override
    public Filter getFilter() {
        return new GalleryImageFilter(this, imagesPathsOriginals);
    }

    public void filter(ArrayList<ImageSkeleton> filteredData) {
        imagesPathsFiltered.clear();
        imagesPathsFiltered = filteredData;
        notifyDataSetChanged();
    }

    public ArrayList<String> getFilterPathList() {
        return PhotoMutator.backMutate(imagesPathsFiltered);
    }

    public ArrayList<ImageSkeleton> getFilterImageList() {
        return imagesPathsFiltered;
    }
}
