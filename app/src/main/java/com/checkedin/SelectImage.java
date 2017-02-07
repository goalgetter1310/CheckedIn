package com.checkedin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.checkedin.activity.GalleryActivity;
import com.checkedin.utility.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;

@SuppressLint("StaticFieldLeak")
public class SelectImage {

    public static final char GALLERY_SELECTED = 'G';
    public static final char CAMERA_SELECTED = 'C';

    private Uri fileUri;
    private Activity activity;
    private ActivityResult activityResult;
    private static SelectImage mInstance;
    private boolean isMutipleSelect;

    public SelectImage(ActivityResult activityResult, Activity activity, char cameraOrGallery) {
        mInstance = this;
        this.activity = activity;
        this.activityResult = activityResult;

        if (cameraOrGallery == CAMERA_SELECTED) {
            cameraSelected();
        } else if (cameraOrGallery == GALLERY_SELECTED) {
            gallerySelected();
        }
    }

    public SelectImage(ActivityResult activityResult, Activity activity, char cameraOrGallery, boolean isMutipleSelect) {
        this.activity = activity;
        this.activityResult = activityResult;
        this.isMutipleSelect = isMutipleSelect;
        mInstance = this;
        if (cameraOrGallery == CAMERA_SELECTED) {
            cameraSelected();
        } else if (cameraOrGallery == GALLERY_SELECTED) {
            gallerySelected();
        }
    }

    public static SelectImage getInstance() {
        return mInstance;
    }

    private boolean isDeviceSupportCamera() {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void cameraSelected() {
        if (isDeviceSupportCamera()) {
            captureImage();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, 100);
    }

    private void gallerySelected() {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra("multi_selectable", isMutipleSelect);
        activity.startActivityForResult(intent, 1);
    }


    private File getOutputMediaFile() {
        String folderName = activity.getString(R.string.app_name);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Utility.logUtils(folderName + " directory Error");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator + "img_" + timeStamp + ".jpg");
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            mInstance = null;
            if (requestCode == 100) {
                if (resultCode == Activity.RESULT_OK) {
                    fileUri = Uri.fromFile(Compressor.getDefault(activity).compressToFile(new File(fileUri.getPath())));
                    activityResult.onResult(fileUri);
                }
            } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && isMutipleSelect) {
                ArrayList<String> allPath = data.getStringArrayListExtra("images_path");
                ArrayList<Uri> allUri = new ArrayList<>();
                for (String string : allPath) {
                    allUri.add(Uri.fromFile(Compressor.getDefault(activity).compressToFile(new File(string))));
                }
                activityResult.onResult(allUri);
            } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> allPath = data.getStringArrayListExtra("images_path");

                fileUri = Uri.fromFile(Compressor.getDefault(activity).compressToFile(new File(allPath.get(0))));
                activityResult.onResult(fileUri);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public interface ActivityResult {
        void onResult(Uri uriImagePath);

        void onResult(ArrayList<Uri> allUriImagePath);
    }
}