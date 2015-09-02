package com.nntuyen.yourflickr.ui.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.util.ImageLoader;
import com.nntuyen.yourflickr.domain.model.Photo;

public class PhotosListAdapter extends Adapter<PhotosListAdapter.PhotoViewHolder> {

	List<Photo> photos;
	private ImageLoader imageLoader;
	
	public PhotosListAdapter(Context context, List<Photo> photos) {
		this.photos = photos;
		this.imageLoader = new ImageLoader(context);
	}
	
	public static class PhotoViewHolder extends RecyclerView.ViewHolder {      
        ImageView ivPhoto;
 
        PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView)itemView.findViewById(R.id.ivPhoto);
        }
    }

	@Override
	public int getItemCount() {
		return photos.size();
	}

	@Override
	public void onBindViewHolder(PhotoViewHolder photoViewHolder, int i) {
		imageLoader.DisplayImage(photos.get(i).toString(), R.drawable.ic_launcher, photoViewHolder.ivPhoto);
	}

	@Override
	public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item, viewGroup, false);
		PhotoViewHolder pvh = new PhotoViewHolder(v);
		
	    return pvh;
	}
	
	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}
}
