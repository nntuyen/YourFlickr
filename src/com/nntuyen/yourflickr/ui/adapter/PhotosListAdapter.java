package com.nntuyen.yourflickr.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.app.util.ImageLoader;
import com.nntuyen.yourflickr.domain.broadcast.ObservableObject;
import com.nntuyen.yourflickr.domain.model.Photo;
import com.nntuyen.yourflickr.ui.activity.PhotoViewerActivity;

public class PhotosListAdapter extends Adapter<PhotosListAdapter.PhotoViewHolder> {

	List<Photo> photos;
	private ImageLoader imageLoader;
	
	public PhotosListAdapter(Context context, List<Photo> photos) {
		this.photos = photos;
		this.imageLoader = new ImageLoader(context);
	}
	
	public static class PhotoViewHolder extends RecyclerView.ViewHolder implements OnClickListener {      
        ImageView ivPhoto;
 
        PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView)itemView.findViewById(R.id.ivPhoto);
            ivPhoto.setOnClickListener(this);
        }

		@Override
		public void onClick(View v) {
			Photo p = ObservableObject.getInstance().getPhotos().get(getPosition());
			
			Intent intent = new Intent(v.getContext(), PhotoViewerActivity.class);
			intent.putExtra(KeyValueConst.PHOTO_URL, p.toString());
			v.getContext().startActivity(intent);
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
