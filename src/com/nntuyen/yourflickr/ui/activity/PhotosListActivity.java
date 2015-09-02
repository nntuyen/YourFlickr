package com.nntuyen.yourflickr.ui.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.domain.model.Photo;
import com.nntuyen.yourflickr.ui.adapter.GridViewPhotoAdapter;
import com.nntuyen.yourflickr.ui.adapter.PhotosListAdapter;
import com.nntuyen.yourflickr.ui.presenter.PhotosListPresenter;
import com.nntuyen.yourflickr.ui.presenter.impl.PhotosListPresenterImpl;
import com.nntuyen.yourflickr.ui.view.PhotosListView;

public class PhotosListActivity extends Activity implements OnItemClickListener, PhotosListView {
	
	private static final String TAG = "PhotosListActivity";
	private PhotosListPresenter presenter;
	//private GridView gvPhotos;
	private GridViewPhotoAdapter gvPhotoAdapter;
	private RecyclerView recyclerView;
	LinearLayoutManager llm;
	PhotosListAdapter plAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos_list);
		
		//gvPhotos = (GridView)findViewById(R.id.gvPhotos);
		recyclerView = (RecyclerView)findViewById(R.id.rv);
		llm = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(llm);
		
		presenter = new PhotosListPresenterImpl(this, this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		presenter.registerReceiver();
		presenter.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		presenter.onItemClick(position);
	}

	@Override
	public void setPhotos(List<Photo> photos) {
		Log.d(TAG, "Show gallery");
		/*gvPhotoAdapter = new GridViewPhotoAdapter(this, photos);
		gvPhotos.setAdapter(gvPhotoAdapter);
		gvPhotos.setOnItemClickListener(this);*/
		plAdapter = new PhotosListAdapter(this, photos);
		recyclerView.setAdapter(plAdapter);
	}

	@Override
	public void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
