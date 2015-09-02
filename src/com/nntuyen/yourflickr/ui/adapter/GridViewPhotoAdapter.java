package com.nntuyen.yourflickr.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.util.ImageLoader;
import com.nntuyen.yourflickr.domain.model.Photo;

public class GridViewPhotoAdapter extends BaseAdapter {

	private Context context;
	private List<Photo> photos;
	private ImageLoader imageLoader;
	
	public GridViewPhotoAdapter(Context context, List<Photo> photos) {
		this.context = context;
		this.photos = photos;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(R.layout.grid_item, parent, false);
			holder = new ViewHolder();
			holder.ivPhoto = (ImageView)row.findViewById(R.id.ivPhoto);
			row.setTag(holder);
		} else {
			holder = (ViewHolder)row.getTag();
		}
		
		//holder.ivPhoto.setimagebitmapabc
		Photo photo = photos.get(position);
		imageLoader.DisplayImage(photo.toString(), R.drawable.ic_launcher, holder.ivPhoto);
		
		return row;
	}

	static class ViewHolder {
		ImageView ivPhoto;
	}

}
