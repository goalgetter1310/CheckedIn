package com.checkedin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.checkedin.R;
import com.checkedin.model.Photos;
import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

public class TagPhotoAdapter extends RecyclerView.Adapter<TagPhotoAdapter.ViewHolder> implements OnClickListener {
    private Context context;
    private int targetWidth;
    private ArrayList<Photos> items;

    public TagPhotoAdapter(Context context, ArrayList<Photos> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tag_post_image, parent, false);
        targetWidth = parent.getWidth();
        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = items.get(position).getImageUrl();

        if (TextUtils.isEmpty(items.get(position).getId())) {
            Utility.loadImageGlide( holder.ivTagPhoto,    new File(imageUrl));
//                    Picasso.with(context).load(new File(imageUrl)).transform(transformation).error(R.drawable.ic_placeholder).into(holder.ivTagPhoto);
        } else {
            Utility.loadImageGlide( holder.ivTagPhoto  ,WebServiceCall.IMAGE_BASE_PATH + Photos.ACTIVITY_PATH + Photos.ORIGINAL_SIZE_PATH + imageUrl);
//            Picasso.with(context).load(WebServiceCall.IMAGE_BASE_PATH + Photos.ACTIVITY_PATH + Photos.ORIGINAL_SIZE_PATH + imageUrl).transform(transformation).error(R.drawable.ic_placeholder).into(holder.ivTagPhoto);
        }

        holder.btnDelete.setTag("" + position);
        holder.myCustomEditTextListener.updatePosition(position);
        holder.etCaption.setText(items.get(position).getCaption());
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            if (v instanceof ImageView) {
                if (v.getTag() != null) {
                    int position = Integer.parseInt(v.getTag().toString());
                    items.remove(position);
                    notifyDataSetChanged();
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTagPhoto, btnDelete;
        private EditText etCaption;
        MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ivTagPhoto = (ImageView) itemView.findViewById(R.id.adapter_upload_image_iv);
            btnDelete = (ImageView) itemView.findViewById(R.id.adapter_upload_image_iv_delete);

            btnDelete.setOnClickListener(TagPhotoAdapter.this);
        }

        public ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);
            ivTagPhoto = (ImageView) itemView.findViewById(R.id.adapter_upload_image_iv);
            btnDelete = (ImageView) itemView.findViewById(R.id.adapter_upload_image_iv_delete);

            this.etCaption = (EditText) v.findViewById(R.id.adapter_upload_edt_caption);
            this.myCustomEditTextListener = myCustomEditTextListener;
            btnDelete.setOnClickListener(TagPhotoAdapter.this);
            this.etCaption.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            items.get(position).setCaption(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };
}
