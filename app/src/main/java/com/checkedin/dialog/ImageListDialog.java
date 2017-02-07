package com.checkedin.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.checkedin.R;
import com.checkedin.adapter.ViewImageAdapter;
import com.checkedin.model.Photos;
import com.checkedin.views.SwipeBackLayout;

import java.util.ArrayList;

public class ImageListDialog extends SwipeBackDialog implements OnItemClickListener {

	private ListView lvPhotos;
	private ViewImageAdapter adptPhotoActivity;
	private ArrayList<Photos> alPhoto;
	private Context context;

	public ImageListDialog(Context context, ArrayList<Photos> alPhoto) {
		super(context, R.style.Theme_Swipe_Dialog);
		this.context = context;
		this.alPhoto = alPhoto;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_photos);

		setDragEdge(SwipeBackLayout.DragEdge.LEFT);
		initViews();

		lvPhotos.setAdapter(adptPhotoActivity);

		lvPhotos.setOnItemClickListener(this);
	}

	private void initViews() {
		lvPhotos = (ListView) findViewById(R.id.lv_photos);
		adptPhotoActivity = new ViewImageAdapter(context, R.layout.adapter_image_list, alPhoto);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
		ViewPhotoPagerDialog dialog = new ViewPhotoPagerDialog(context, postion, alPhoto);
		dialog.show();

	}

}
