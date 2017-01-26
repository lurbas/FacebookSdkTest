package com.lucasurbas.facebooksdktest.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.lucasurbas.facebooksdktest.constants.Constants;
import com.lucasurbas.facebooksdktest.ui.details.DetailsActivity;
import com.lucasurbas.facebooksdktest.ui.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Lucas on 25/01/2017.
 */

public class GalleryNavigator implements GalleryContract.Navigator {

    private WeakReference<GalleryActivity> galleryActivity;
    private String currentPhotoPath;

    @Inject
    public GalleryNavigator(GalleryActivity galleryActivity) {
        this.galleryActivity = new WeakReference<>(galleryActivity);
    }

    @Override
    public String openCamera() {
        dispatchTakePictureIntent();
        return currentPhotoPath;
    }

    @Override
    public void openGalleryItemDetails(String itemId) {
        galleryActivity.get().startActivity(DetailsActivity.getIntent(galleryActivity.get(), itemId));
    }

    @Override
    public void openLoginScreen() {
        galleryActivity.get().startActivity(LoginActivity.getIntent(galleryActivity.get()));
        galleryActivity.get().overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        galleryActivity.get().finish();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = galleryActivity.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(galleryActivity.get().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                currentPhotoPath = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(galleryActivity.get(),
                        Constants.FILE_PROVIDER_AUTHORITY,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                galleryActivity.get().startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO);
            }
        }
    }
}
