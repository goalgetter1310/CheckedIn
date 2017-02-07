package com.checkedin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.databinding.AdapterGalleryBinding;
import com.checkedin.utility.Utility;
import com.material.widget.Button;

import java.io.File;
import java.util.ArrayList;


public class GalleryActivity extends Activity implements View.OnClickListener {

    private ArrayList<Gallery> alGallery;
    public static int MAX_SELECTION;
    private int selectedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galley);

        RecyclerView rvGallery = (RecyclerView) findViewById(R.id.rv_gallery);
        Button btnDone = (Button) findViewById(R.id.btn_done);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        LinearLayout llActionBtn = (LinearLayout) findViewById(R.id.ll_action_btn);

        boolean isMultiSelect = getIntent().getExtras().getBoolean("multi_selectable", false);

        llActionBtn.setVisibility(isMultiSelect ? View.VISIBLE : View.GONE);

        alGallery = new ArrayList<>();
        getAllImagePath();
        GalleryAdapter adptGallery = new GalleryAdapter(this, alGallery, isMultiSelect);

        rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        rvGallery.setAdapter(adptGallery);

        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void getAllImagePath() {
        String[] projection = new String[]{MediaStore.Images.Media.DATA};

        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getContentResolver().query(imagesUri, projection, null, null,  MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cur != null) {
            if (cur.moveToFirst()) {

                while (!cur.isAfterLast()) {
                    Gallery gallery = new Gallery();
                    gallery.setPath(cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA)));

                    alGallery.add(gallery);
                    cur.moveToNext();
                }
            }
            cur.close();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                if (selectedCount <= MAX_SELECTION) {
                    ArrayList<String> alImagePath = new ArrayList<>();
                    for (Gallery gallery : alGallery) {
                        if (gallery.isSelected())
                            alImagePath.add(gallery.getPath());
                    }
                    setResult(RESULT_OK, new Intent().putStringArrayListExtra("images_path", alImagePath));
                    finish();
                } else {
                    Toast.makeText(this, "You can select upto 10 image in a post.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {


        private final Context context;
        private final ArrayList<Gallery> items;
        private final boolean isMultiSelect;
//        private int selectedCount;

        GalleryAdapter(Context context, ArrayList<Gallery> items, boolean isMultiSelect) {
            this.context = context;
            this.items = items;
            this.isMultiSelect = isMultiSelect;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AdapterGalleryBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_gallery, parent, false);
            return new ViewHolder(mBinding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mBinding.setHolder(holder);
            holder.mBinding.setGallery(items.get(position));
            holder.mBinding.cbSelect.setChecked(items.get(position).isSelected());
            holder.mBinding.cbSelect.setTag(position);
            holder.mBinding.getRoot().setTag(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final AdapterGalleryBinding mBinding;

            public ViewHolder(AdapterGalleryBinding mBinding) {
                super(mBinding.getRoot());
                this.mBinding = mBinding;

            }

            public boolean isMultiSelectable() {
                return isMultiSelect;
            }

            public void onItemClick(View v) {
                if (!isMultiSelect) {
                    int position = (Integer) v.getTag();
                    items.get(position).setSelected(!items.get(position).isSelected());
                    ArrayList<String> alImagePath = new ArrayList<>();
                    for (Gallery gallery : alGallery) {
                        if (gallery.isSelected())
                            alImagePath.add(gallery.getPath());
                    }
                    setResult(RESULT_OK, new Intent().putStringArrayListExtra("images_path", alImagePath));
                    finish();
                }
            }

            public void onItemSelectedClick(View v) {
                if (isMultiSelect) {
                    int position = (Integer) v.getTag();
                    if (items.get(position).isSelected()) {
                        selectedCount--;
                        items.get(position).setSelected(false);
                    } else {
                        selectedCount++;
                        items.get(position).setSelected(true);
                    }
                }


            }
        }

    }


    public static class Gallery {

        private String path;
        private boolean isSelected;


        @BindingAdapter({"bind:galleryImg"})
        public static void galleryImage(ImageView imageView, String imgUrl) {
            Utility.loadImageGlide(imageView, new File(imgUrl));
//                    Glide.with(imageView.getContext()).load(new File(imgUrl)).error(R.drawable.ic_placeholder).into(imageView);
        }

        public String getPath() {
            return path;
        }

        void setPath(String path) {
            this.path = path;
        }

        public boolean isSelected() {
            return isSelected;
        }

        void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
