package com.ang.acb.nasaapp.ui.mars;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.nasaapp.R;
import com.ang.acb.nasaapp.data.local.entity.MarsPhoto;
import com.ang.acb.nasaapp.databinding.MarsPhotoItemBinding;
import com.ang.acb.nasaapp.utils.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import timber.log.Timber;

public class MarsPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MarsPhoto> marsPhotos;
    private MarsPhotoListener photoListener;

    public MarsPhotosAdapter(MarsPhotoListener photoListener) {
        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        MarsPhotoItemBinding itemBinding = MarsPhotoItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MarsPhotoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data.
        MarsPhoto marsPhoto = marsPhotos.get(position);
        ((MarsPhotoViewHolder) holder).bindTo(marsPhoto);

        // Handle item click events.
        holder.itemView.setOnClickListener(view -> {
            if (marsPhoto != null && photoListener != null) {
                photoListener.onPhotoItemClick(marsPhoto,
                        holder.itemView.findViewById(R.id.mars_photo_item_image) );
            }
        });
    }

    @Override
    public int getItemCount() {
        return marsPhotos == null ? 0 :  marsPhotos.size();
    }

    public void updateData(List<MarsPhoto> marsPhotos) {
        this.marsPhotos = marsPhotos;
        // Notify any registered observers that the data set has changed.
        notifyDataSetChanged();
    }

    public interface MarsPhotoListener {
        void onPhotoItemClick(MarsPhoto marsPhoto, ImageView sharedImage);
        void onPhotoLoaded();
    }

    class MarsPhotoViewHolder extends RecyclerView.ViewHolder {

        private MarsPhotoItemBinding binding;

        // Required constructor matching super.
        MarsPhotoViewHolder(@NonNull MarsPhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(MarsPhoto marsPhoto) {
            // Bind data for this item.
            binding.setMarsPhoto(marsPhoto);

            // Set the string value of the pin ID as the unique transition name
            // for the image view that will be used in the shared element transition.
            ViewCompat.setTransitionName(binding.marsPhotoItemImage, String.valueOf(marsPhoto.getId()));

            GlideApp.with(binding.marsPhotoItemImage.getContext())
                    // Calling GlideApp.with() returns a RequestBuilder.
                    // By default you get a Drawable RequestBuilder, but
                    // you can change the requested type using as... methods.
                    // For example, asBitmap() returns a Bitmap RequestBuilder.
                    .asBitmap()
                    .load(marsPhoto.getImgSrc())
                    // Tell Glide not to use its standard crossfade animation.
                    .dontAnimate()
                    // Display a placeholder until the image is loaded and processed.
                    .placeholder(R.color.imagePlaceholder)
                    // Keep track of errors and successful image loading.
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Bitmap> target, boolean isFirstResource) {
                            photoListener.onPhotoLoaded();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                                       com.bumptech.glide.load.DataSource dataSource,
                                                       boolean isFirstResource) {
                            photoListener.onPhotoLoaded();
                            return false;
                        }
                    })
                    .into(binding.marsPhotoItemImage);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
