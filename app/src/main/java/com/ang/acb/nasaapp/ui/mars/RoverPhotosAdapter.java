package com.ang.acb.nasaapp.ui.mars;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.data.local.entity.RoverPhoto;
import com.ang.acb.nasaapp.databinding.RoverPhotoItemBinding;
import com.ang.acb.nasaapp.utils.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import timber.log.Timber;

public class RoverPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RoverPhoto> roverPhotos;
    private PhotoClickCallback clickCallback;

    public RoverPhotosAdapter(PhotoClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        RoverPhotoItemBinding itemBinding = RoverPhotoItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RoverPhotoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data.
        RoverPhoto roverPhoto = roverPhotos.get(position);
        ((RoverPhotoViewHolder) holder).bindTo(roverPhoto);

        // Handle item click events.
        holder.itemView.setOnClickListener(view -> {
            if (roverPhoto != null && clickCallback != null) {
                clickCallback.onClick(roverPhoto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roverPhotos == null ? 0 :  roverPhotos.size();
    }

    public void updateData(List<RoverPhoto> roverPhotos) {
        this.roverPhotos = roverPhotos;
        // Notify any registered observers that the data set has changed.
        notifyDataSetChanged();
    }

    public interface PhotoClickCallback {
        void onClick(RoverPhoto roverPhoto);
    }

    class RoverPhotoViewHolder extends RecyclerView.ViewHolder {

        private RoverPhotoItemBinding binding;

        // Required constructor matching super.
        RoverPhotoViewHolder(@NonNull RoverPhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(RoverPhoto roverPhoto) {
            // Bind data for this item.
            binding.setRoverPhoto(roverPhoto);

            GlideApp.with(binding.roverItemImage.getContext())
                    .load(roverPhoto.getImgSrc())
                    .placeholder(R.color.imagePlaceholder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Timber.e("Exception: %s ", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Timber.e("Image not loaded: %s", roverPhoto.getImgSrc());
                            return false;
                        }
                    })
                    .into(binding.roverItemImage);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
