package com.dmsegypt.dms.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Eslam .
 */

public class ImagesHandler extends AppCompatActivity {
    private static ImagesHandler mInstance;
    private static final int GALLERY_INTENT = 2;
    public static final int CAMERA_INTENT = 1;
    private static int RESIZE_RATIO = 2;
    private static int IMAGE_MAX_SIZE = 5;
    private Uri imageUri;

    private static final int REQUESTCODE_STORAGE =99 ;
    private static final int REQUESTCODE_CAMERA =100 ;

    public static ImagesHandler getInstance() {
        if (mInstance == null) {
            mInstance = new ImagesHandler();
        }
        return mInstance;
    }

    public ImagesHandler() {

    }

    public String convertBitmapImageToString(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /*
public  Uri cameraIntentFromFragment(EditProfileFragment fragment){

    Uri mCapturedImageURI=null;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    String fileName = "temp.jpg";
    ContentValues values = new ContentValues();
    values.put(MediaStore.Images.Media.TITLE, fileName);
    mCapturedImageURI = fragment.getActivity().getContentResolver()
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
    takePictureIntent
            .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
    fragment.startActivityForResult(takePictureIntent, CAMERA_INTENT);


    return mCapturedImageURI;

}*/




    public Uri cameraIntent(Activity context) {
        // Intent intent = new_image Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mCapturedImageURI=null;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = context.getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                takePictureIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                context.startActivityForResult(takePictureIntent, CAMERA_INTENT);


        return mCapturedImageURI;


    }

    public static Bitmap resizeAndCompressImageBeforeSend(Context context, String filePath){
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes
       // String fileName="dms"+System.currentTimeMillis()+".jgp";
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
        }while (streamLength >= MAX_IMAGE_SIZE);


        //return the path of resized and compressed file
        return  bmpPic;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


/*
    // When the button of "Take a Photo with Camera" is pressed.
public void cameraIntent(Activity context){
    Intent intent = new_image Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if(intent.resolveActivity((getPackageManager())) != null) {
        // Save the photo taken to a temporary file.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File file = File.createTempFile("IMG_", ".jpg", storageDir);
            imageUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_INTENT);
        } catch (IOException e) {
            return;
        }
    }

}*/
/*
    // Deal with the result of selection of the photos.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case CAMERA_INTENT:
            case GALLERY_INTENT:
                if (resultCode == RESULT_OK) {
                    Uri imageuri;
                    if (data == null || data.getUserRquest() == null) {
                        imageuri = imageUri;
                    } else {
                        imageUri = data.getUserRquest();
                    }
                    Intent intent = new_image Intent();
                    intent.setData(imageUri);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }*/

    public void galleryIntent(Activity activity) {
     /*   Intent intent = new_image Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_INTENT);*/
     Intent openGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(openGallery,"open Gallery"),IntentManager.KEY_PICK_IMAGE);
    }
/*
    public void galleryIntentfragment(final EditProfileFragment activity) {
        Intent intent = new_image Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_INTENT);
        Intent openGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(openGallery,"open Gallery"),IntentManager.KEY_PICK_IMAGE);
    }
*/
    public boolean onSelectFromGalleryResult(Context context, Intent data, ImageView imageView) {
        if (data != null) {
            Boolean isCorrectImage = setImageBitmap(context, data.getData(), imageView);
            if (isCorrectImage != null) return isCorrectImage;
        }
        return false;
    }

    @Nullable
    private Boolean setImageBitmap(Context context, Uri data, ImageView imageView) {
        Bitmap image;
        try {
            String imagePath = getPath(context, data);
            File file = new File(imagePath);
            float fileSize = getSizeInMB(file.length());
            if (fileSize < IMAGE_MAX_SIZE) {
                try {
                    image = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data);
                    if (getSizeInMB(image.getByteCount()) > IMAGE_MAX_SIZE) {
                        image = getResizedBitmap(imagePath, RESIZE_RATIO);
                    }
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    image = getResizedBitmap(imagePath, RESIZE_RATIO);
                }

                imageView.setImageBitmap(image);

                return true;
            } else {
                Toast.makeText(context, "Image size must not exceed 5MB", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //To convert byte into MegaBytes
    private float getSizeInMB(long byteCount) {
        return (byteCount / 1024) / 1024;
    }

    private Bitmap getResizedBitmap(String filePath, int sampleSize) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        try {
            bitmap = BitmapFactory.decodeFile(filePath, options);
            if (getSizeInMB(bitmap.getByteCount()) > IMAGE_MAX_SIZE) {
                getResizedBitmap(filePath, sampleSize * RESIZE_RATIO);
            }
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            getResizedBitmap(filePath, sampleSize * RESIZE_RATIO);
        }
        return bitmap;
    }


    public String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }


    public boolean onCaptureImageResult(Context context, Intent data, ImageView imageView) {
        Boolean isCorrectImage = setImageBitmap(context, imageUri, imageView);
        if (isCorrectImage != null) return isCorrectImage;
        return false;
    }
}