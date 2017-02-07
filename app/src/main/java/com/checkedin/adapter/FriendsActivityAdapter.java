package com.checkedin.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.activity.WelcomeActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.AdapterFriendsActivityBinding;
import com.checkedin.dialog.BottomActionDialog;
import com.checkedin.dialog.ImageListDialog;
import com.checkedin.dialog.ProgressDialog;
import com.checkedin.dialog.ReportContentDialog;
import com.checkedin.dialog.ViewPhotoPagerDialog;
import com.checkedin.fragment.CommentFrg;
import com.checkedin.fragment.PostActivityEditFrg;
import com.checkedin.fragment.PostCheckinEditFrg;
import com.checkedin.fragment.PostPlanEditFrg;
import com.checkedin.fragment.StarViewUserFrg;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.Friend;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.MyActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.Place;
import com.checkedin.model.PostActivity;
import com.checkedin.model.TagUser;
import com.checkedin.model.response.AddStarViewModel;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.CropSquareTransformation;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class FriendsActivityAdapter extends RecyclerView.Adapter<FriendsActivityAdapter.MyViewHolder> implements VolleyStringRequest.AfterResponse {

    private Fragment fragment;
    private Activity activity;
    private ArrayList<FriendsActivity> items;
    private String playStoreUrl="";

    private WebServiceCall webServiceCall;
    private int position;
    private String image="";
    private ProgressDialog progress;

    public FriendsActivityAdapter(Activity activity, ArrayList<FriendsActivity> items, Fragment fragment) {
        this.activity = activity;
        this.items = items;
        playStoreUrl="https://play.google.com/store/apps/details?id=com.checkedin";
        this.fragment = fragment;
        webServiceCall = new WebServiceCall(this);
        progress=new ProgressDialog((Context) activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        AdapterFriendsActivityBinding mBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.adapter_friends_activity, parent, false);


        return new MyViewHolder(mBinding);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mBinding.tvTitle.setActivity(activity);
        holder.mBinding.setItems(items.get(position));

        holder.mBinding.tvTitle.setText(items.get(position).getTitle());
        if (items.get(position).getAlWordAction().size() > 0)
            holder.mBinding.tvTitle.addSpanClick(items.get(position), items.get(position).getAlWordAction(), items.get(position).getTitle());

        // Tag Activity Photos
        ArrayList<Photos> alPhotos = (ArrayList<Photos>) items.get(position).getTagPhotos();
        if (alPhotos != null && alPhotos.size() != 0) {
            showPhotos(holder, alPhotos);
        } else {
            holder.mBinding.llPhotos.setVisibility(LinearLayout.GONE);
            holder.mBinding.llImgtop.setVisibility(LinearLayout.GONE);
        }

        holder.mBinding.tvStarOMeter.setTag(position);
        holder.mBinding.tvStarOMeterCount.setTag(position);
        holder.mBinding.tvCommentCount.setTag(position);
        holder.mBinding.ivMore.setTag(position);
        holder.mBinding.tvComment.setTag(position);
        holder.mBinding.llPhotos.setTag(position);
        holder.mBinding.tvShare.setTag(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private AdapterFriendsActivityBinding mBinding;
        private ImageView[] ivPhoto;

        MyViewHolder(AdapterFriendsActivityBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            ivPhoto = new ImageView[5];
            ivPhoto[0] = mBinding.ivImg1;
            ivPhoto[1] = mBinding.ivImg2;
            ivPhoto[2] = mBinding.ivImg3;
            ivPhoto[3] = mBinding.ivImg4;
            ivPhoto[4] = mBinding.ivImg5;

            mBinding.ivMore.setOnClickListener(this);
            mBinding.llPhotos.setOnClickListener(this);
            mBinding.tvComment.setOnClickListener(this);
            mBinding.tvStarOMeter.setOnClickListener(this);
            mBinding.tvStarOMeterCount.setOnClickListener(this);
            mBinding.tvCommentCount.setOnClickListener(this);
            mBinding.tvShare.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                try {
                    position = (Integer) v.getTag();
                    switch (v.getId()) {
                        case R.id.tv_starOMeter:
                            if (items.get(position).getStarByme()) {
                                items.get(position).setStarByme(0);
                                items.get(position).setStarOMeterCount(items.get(position).getStarOMeterCount() - 1);
                            } else {
                                items.get(position).setStarByme(1);
                                items.get(position).setStarOMeterCount(items.get(position).getStarOMeterCount() + 1);
                            }
                            notifyItemChanged(position);
                            webServiceCall.addStarViewOMeterWsCall(activity, items.get(position).getPostId(), true);

                            break;
                        case R.id.tv_starOMeterCount:
                            StarViewUserFrg starViewUserFrg = new StarViewUserFrg();
                            Bundle argument = new Bundle();
                            argument.putBoolean("is_star", true);
                            argument.putString("post_id", items.get(position).getPostId());
                            starViewUserFrg.setArguments(argument);

                            if (DialogFragmentContainer.isDialogOpen) {
                                DialogFragmentContainer.getInstance().fragmentTransition(starViewUserFrg, true);
                            } else {
                                DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                                dialogFrgContainer.init(starViewUserFrg);
                                dialogFrgContainer.show(((MainActivity) activity).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                            }
                            break;
                        case R.id.iv_more:
                            final FriendsActivity friendsActivity = items.get(position);
                            if (friendsActivity.getUserId().equals(UserPreferences.fetchUserId(activity))) {
                                boolean isGroufie=items.get(position).getCategoryName().equals("Groufie");
                                new BottomActionDialog(activity,!isGroufie,new BottomActionDialog.PostAction() {
                                    @Override
                                    public void onEditSelect() {
                                        editPost();

                                    }

                                    @Override
                                    public void onDeleteSelect() {
                                        webServiceCall.deletePostWsCall(activity, friendsActivity.getPostId(), friendsActivity.getPostType());
                                    }
                                }).show();
                            } else {
                                new ReportContentDialog(activity, items.get(position).getPostId()).show();
                            }


                            break;
                        case R.id.tv_commentCount:
                        case R.id.tv_comment:
                            CommentFrg activityComment = new CommentFrg();
                            Bundle argumentC = new Bundle();
                            argumentC.putParcelable("friend_activity", items.get(position));
                            argumentC.putParcelable("comment_post_listener", new CommentFrg.CommentPost() {
                                @Override
                                public int describeContents() {
                                    return 0;
                                }

                                @Override
                                public void writeToParcel(Parcel dest, int flags) {

                                }

                                @Override
                                public void onCommentPosted(boolean isPosted) {
                                    if (isPosted) {
                                        notifyDataSetChanged();
                                    }
                                }
                            });

                            activityComment.setArguments(argumentC);
                            if (DialogFragmentContainer.isDialogOpen) {
                                DialogFragmentContainer.getInstance().fragmentTransition(activityComment, true);
                            } else {
                                DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                                dialogFrgContainer.init(activityComment);
                                dialogFrgContainer.show(((MainActivity) activity).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                            }
                            break;
                        case R.id.ll_photos:
                            if (items.get(position).getTagPhotos().size() == 1) {
                                ViewPhotoPagerDialog dialog = new ViewPhotoPagerDialog(activity, 0, (ArrayList<Photos>) items.get(position).getTagPhotos());
                                dialog.show();
                            } else {
                                ImageListDialog activityPhoto = new ImageListDialog(activity, (ArrayList<Photos>) items.get(position).getTagPhotos());
                                activityPhoto.show();
                            }
                            break;
                        case R.id.tv_share:

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                PermissionEverywhere.getPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101).enqueue(new PermissionResultCallback() {
                                    @Override
                                    public void onComplete(PermissionResponse permissionResponse) {
                                        if (permissionResponse.isGranted()) {


                                            if(items.get(position).getTagPhotos().size()>0){
                                                progress.show();
                                                new DownloadImage().execute( Environment.getExternalStorageDirectory().getAbsolutePath()+"/share.png");
                                            }
                                            else {
                                                Intent shareIntent = new Intent();
                                                shareIntent.setAction(Intent.ACTION_SEND);
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, items.get(position).getTitle()+" Share Via CheckedIn\n"+playStoreUrl);
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(mBinding.ivImg1));
                                                shareIntent.setType("text/plain");
                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                activity.startActivity(Intent.createChooser(shareIntent, "Share"));
                                            }
                                        }
                                    }
                                });
                            }
                            else{


                                if(items.get(position).getTagPhotos().size()>0){
                                    progress.show();
                                    new DownloadImage().execute( Environment.getExternalStorageDirectory().getAbsolutePath()+"/share.png");
                                }
                                else {
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, items.get(position).getTitle()+" Share Via CheckedIn\n"+playStoreUrl);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(mBinding.ivImg1));
                                    shareIntent.setType("text/plain");
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    activity.startActivity(Intent.createChooser(shareIntent, "Share"));
                                }
                                }


                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showPhotos(MyViewHolder holder, ArrayList<Photos> alPhotes) {
        int countPhotos = alPhotes.size();
        holder.mBinding.llPhotos.setVisibility(LinearLayout.VISIBLE);
        holder.mBinding.llImgtop.setVisibility(LinearLayout.VISIBLE);

        switch (countPhotos) {
            case 1:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.GONE);
                holder.ivPhoto[2].setVisibility(LinearLayout.GONE);
                holder.ivPhoto[3].setVisibility(LinearLayout.GONE);
                holder.ivPhoto[4].setVisibility(LinearLayout.GONE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.GONE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.GONE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(1, alPhotes, holder);
                image=alPhotes.get(0).getOriginalSizePath();
                break;
            case 2:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.GONE);
                holder.ivPhoto[2].setVisibility(LinearLayout.GONE);
                holder.ivPhoto[3].setVisibility(LinearLayout.GONE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.GONE);
                holder.ivPhoto[4].setVisibility(LinearLayout.GONE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(2, alPhotes, holder);
                break;
            case 3:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[3].setVisibility(LinearLayout.GONE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.GONE);
                holder.ivPhoto[4].setVisibility(LinearLayout.GONE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(3, alPhotes, holder);
                break;
            case 4:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.GONE);
                holder.ivPhoto[4].setVisibility(LinearLayout.GONE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.GONE);
                imageLoader(4, alPhotes, holder);
                break;
            case 5:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[4].setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.GONE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.VISIBLE);
                imageLoader(5, alPhotes, holder);
                break;

            default:
                holder.ivPhoto[0].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[1].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[2].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[3].setVisibility(LinearLayout.VISIBLE);
                holder.ivPhoto[4].setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.llImgbottom.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.tvImgmore.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.flImg5.setVisibility(LinearLayout.VISIBLE);
                holder.mBinding.tvImgmore.setText(String.format(Locale.getDefault(), "+%d", countPhotos - 4));
                imageLoader(5, alPhotes, holder);
                break;
        }

    }

    private void editPost() {
        FriendsActivity friendsActivity = items.get(position);
        switch (items.get(position).getPostType()) {
            case PostActivity.POST_TYPE_CHECKIN:
                CheckinActivity checkinActivity = new CheckinActivity();
                checkinActivity.setId(friendsActivity.getPostId());
                checkinActivity.setType(friendsActivity.getPostType());
                checkinActivity.setPrivacy(friendsActivity.getPostPrivacy());
                checkinActivity.setDescription(friendsActivity.getPostDescription());
                checkinActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                Place place = new Place();
                place.setId(friendsActivity.getLocationId());
                place.setName(friendsActivity.getLocationName());
                place.setLongitude(Double.parseDouble(friendsActivity.getLongitude()));
                place.setLatitude(Double.parseDouble(friendsActivity.getLatitude()));
                place.setCity(friendsActivity.getCity());
                place.setCountry(friendsActivity.getCountry());
                checkinActivity.setPlace(place);

                ArrayList<Friend> alTagFriends = new ArrayList<>();
                for (int counter = 0; counter < friendsActivity.getTagUsers().size(); counter++) {
                    TagUser tagUser = friendsActivity.getTagUsers().get(counter);
                    Friend friend = new Friend();
                    friend.setId(tagUser.getiTaggedUserID());
                    friend.setFirstName(tagUser.getvFirstName());
                    friend.setLastName(tagUser.getvLastName());
                    friend.setImageUrl(tagUser.getvImage());
                    alTagFriends.add(friend);
                }
                checkinActivity.setTagFriends(alTagFriends);

                PostCheckinEditFrg postCheckinEditFrg = new PostCheckinEditFrg();
                Bundle argumentC = new Bundle();
                argumentC.putParcelable("checkin_post", checkinActivity);
                postCheckinEditFrg.setArguments(argumentC);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(postCheckinEditFrg, true);

                break;
            case PostActivity.POST_TYPE_ACTIVITY:
                MyActivity myActivity = new MyActivity();
                myActivity.setCategoryName(friendsActivity.getCategoryName());
                myActivity.setSubCategoryName(friendsActivity.getSubcategoryName());
                myActivity.setId(friendsActivity.getPostId());
                myActivity.setType(friendsActivity.getPostType());
                myActivity.setPrivacy(friendsActivity.getPostPrivacy());
                myActivity.setDescription(friendsActivity.getPostDescription());
                myActivity.setEdit(true);

                myActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                ArrayList<Friend> alTagFriend = new ArrayList<>();
                for (int counter = 0; counter < friendsActivity.getTagUsers().size(); counter++) {
                    TagUser tagUser = friendsActivity.getTagUsers().get(counter);
                    Friend friend = new Friend();
                    friend.setId(tagUser.getiTaggedUserID());
                    friend.setFirstName(tagUser.getvFirstName());
                    friend.setLastName(tagUser.getvLastName());
                    friend.setImageUrl(tagUser.getvImage());
                    alTagFriend.add(friend);
                }
                myActivity.setTagFriends(alTagFriend);


                PostActivityEditFrg postActivityEditFrg = new PostActivityEditFrg();
                Bundle argument = new Bundle();
                argument.putParcelable("my_activity_post", myActivity);
                postActivityEditFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(postActivityEditFrg, true);


                break;
            case PostActivity.POST_TYPE_PLANNING:
                FutureActivity futureActivity = new FutureActivity();
                futureActivity.setId(friendsActivity.getPostId());
                futureActivity.setType(friendsActivity.getPostType());
                futureActivity.setPrivacy(friendsActivity.getPostPrivacy());
                futureActivity.setDescription(friendsActivity.getPlanningPostDescription());
                futureActivity.setEventStart(friendsActivity.getPlanningPostDate());
                futureActivity.setCategoryName(friendsActivity.getCategoryName());
                futureActivity.setSubCategoryName(friendsActivity.getSubcategoryName());
                futureActivity.setAlTagPhoto((ArrayList<Photos>) friendsActivity.getTagPhotos());

                PostPlanEditFrg postPlanEditFrg = new PostPlanEditFrg();
                Bundle argumentP = new Bundle();
                argumentP.putParcelable("plan_activity_post", futureActivity);
                postPlanEditFrg.setArguments(argumentP);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(postPlanEditFrg, true);

                break;
        }
    }


    private void imageLoader(int size, ArrayList<Photos> alPhotes, MyViewHolder holder) {
        for (int i = 0; i < size; i++) {

            Utility.loadImageGlide(holder.ivPhoto[i], alPhotes.get(i).getOriginalSizePath(), new CropSquareTransformation(activity));
        }

    }

    @Override
    public void onResponseReceive(int requestCode) {
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_ADD_STAR_VIEW_METER:
                AddStarViewModel mAddStarView = (AddStarViewModel) webServiceCall.volleyRequestInstatnce().getModelObject(AddStarViewModel.class, AddStarViewModel.class.getSimpleName());
                if (mAddStarView != null) {
                    items.get(position).setStarOMeterCount(mAddStarView.getAddStarView().getStarOMeterCount());
                    items.get(position).setStarByme(mAddStarView.getAddStarView().getStarOMeter());
                }
                break;
            case WebServiceCall.REQUEST_CODE_DELETE_POST:
                BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (baseModel != null) {
                    if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount() - position);
                    } else {
                        Utility.showSnackBar(activity.findViewById(android.R.id.content), baseModel.getMessage());
                    }
                } else {
                    Utility.showSnackBar(activity.findViewById(android.R.id.content), activity.getString(R.string.server_connect_error));
                }

                break;
        }

    }

    @Override
    public void onErrorReceive() {

    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    class DownloadImage extends AsyncTask<String, Integer, String>
    {

        protected String doInBackground(String...arg0) {
            try {
                return DownloadFromUrl(arg0[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "";

            }



        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                progress.dismiss();
                if(!s.equals("")) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, items.get(position).getTitle()+" Share Via CheckedIn\n"+playStoreUrl);
                    String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), s, "sample", null);
                    Uri screenshotUri = Uri.parse(path);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(Intent.createChooser(shareIntent, "Share"));
                }
            }
            catch (Exception ae){ae.printStackTrace();}
        }



    }

    public String DownloadFromUrl(String fileName) {
        try {
            URL url = new URL(image); //you can write here any link
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];
            int   count=0;
            while ((count = input.read(data)) != -1) {

                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }


}

