package com.dmsegypt.dms.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;


import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.ProgressRequestBody;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.UploadClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by amr on 08/10/2017.
 */

public class Uploader {
    public static final String PROFILE_FOLDER="profile";
    public static final String REQUEST_FOLDER="request";
    public static final String CHRONIC_FOLDER="chronic";
    public static final String PRINT_FOLDER="print";
    public static final String REPLY_FOLDER ="reply" ;
    public static final String SINGLE_FOLDER ="single" ;
    public static final String PRESCRIPTION_FOLDER ="prescription" ;

    //image profile  && indeminty
    public  void UploadImage(final Context context,Uri selectedImageUri, String cardIDORSubFoldr,String folder_type,Callback<ResponseUpdateProfileImage> callback){
        if(folder_type.equals(Uploader.PROFILE_FOLDER)) {
            String LANGUAGE = LocaleHelper.getLanguage(context).equals("ar") ? Constants.Language.AR : Constants.Language.EN;
            String path = getRealPathFromURI(context, selectedImageUri);
            File imageFile = resizeAndCompressImageBeforeSend(context, path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image1", imageFile.getName(), fileBody);

            RequestBody cardIdbody =
                    RequestBody.create(MediaType.parse("text/plain"), cardIDORSubFoldr);
            RequestBody fplderbody =
                    RequestBody.create(MediaType.parse("text/plain"), folder_type);
            RequestBody langBody =
                    RequestBody.create(MediaType.parse("text/plain"), LANGUAGE);
            /////////////////////////////////////////////////////////////////////////////////////////
            new UploadClient().getApiService().updateProfileImage(cardIdbody, fplderbody, langBody, filePart)
                    .enqueue(callback);
        }else if(folder_type.equals(Uploader.SINGLE_FOLDER)){
            String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
            String path=/*getRealPathFromURI(context,selectedImageUri)*/
                    selectedImageUri.toString();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            File imageFile =resizeAndCompressImageBeforeSend(context,path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image1", imageFile.getName(), fileBody);

            RequestBody subfolderbody =
                    RequestBody.create(MediaType.parse("text/plain"), cardIDORSubFoldr);
            RequestBody fplderbody =
                    RequestBody.create(MediaType.parse("text/plain"), folder_type);
            RequestBody langBody =
                    RequestBody.create(MediaType.parse("text/plain"), LANGUAGE);

            RequestBody filenameBody =
                    RequestBody.create(MediaType.parse("text/plain"), fileName);


            /////////////////////////////////////////////////////////////////////////////////////////
            new UploadClient().getApiService().updateIndemnityImage(filenameBody,fplderbody,subfolderbody ,langBody,filePart)
                    .enqueue(callback);

        }

    }


    //chronic
    public  void UploadImage(final Context context,Uri selectedImageUri, String cardID,String username,String folder_type,Callback<ResponseUpdateProfileImage> callback){

        String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        String path=getRealPathFromURI(context,selectedImageUri);
        File imageFile =resizeAndCompressImageBeforeSend(context,path);
        ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image1", imageFile.getName(), fileBody);

        /////////////////////////////////////////////////////////////////////////////////////////
        new UploadClient().getApiService().updateProfileImage(RequestBody.create(MediaType.parse("text/plain"), cardID),RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE),RequestBody.create(MediaType.parse("text/plain"), username) ,filePart)
                .enqueue(callback);

    }


    //Reply
    public  void UploadImage(final Context context,Uri selectedImageUri,String folder_type,String reply_flag,String reason,String req_id,Callback<ResponseUpdateProfileImage> callback){
        String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        if(selectedImageUri != null) {
            String path = getRealPathFromURI(context, selectedImageUri);
            File imageFile = resizeAndCompressImageBeforeSend(context, path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileBody);
            new UploadClient().getApiService().updateProfileImage(RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), reason),RequestBody.create(MediaType.parse("text/plain"), reply_flag),RequestBody.create(MediaType.parse("text/plain"), req_id),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE) ,filePart)
                    .enqueue(callback);

        }else {
            new UploadClient().getApiService().updateProfileImage(RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), reply_flag),RequestBody.create(MediaType.parse("text/plain"), reason),RequestBody.create(MediaType.parse("text/plain"), req_id) ,null)
                    .enqueue(callback);


        }

        /////////////////////////////////////////////////////////////////////////////////////////

//updateeeeeeeeeeeeeeeeed
    }

    //Add request
    public  void UploadImage(final Context context,Uri selectedImageUri, String cardID,String folder_type,String title,String description,String type,String username,Callback<ResponseUpdateProfileImage> callback){
        String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        if(selectedImageUri != null) {

            String path = getRealPathFromURI(context, selectedImageUri);
            File imageFile = resizeAndCompressImageBeforeSend(context, path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileBody);
            new UploadClient().getApiService().updateProfileImage(RequestBody.create(MediaType.parse("text/plain"),
                    cardID),RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), title),RequestBody.create(MediaType.parse("text/plain"), description),RequestBody.create(MediaType.parse("text/plain"), username),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE),RequestBody.create(MediaType.parse("text/plain"), type) ,filePart)
                    .enqueue(callback);
        }else {
            new UploadClient().getApiService().updateProfileImage(RequestBody.create(MediaType.parse("text/plain"), cardID),RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), title),RequestBody.create(MediaType.parse("text/plain"), description),RequestBody.create(MediaType.parse("text/plain"), type),RequestBody.create(MediaType.parse("text/plain"), username),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE) ,null)
                    .enqueue(callback);

        }

        /////////////////////////////////////////////////////////////////////////////////////////


    }


    //rosheta
    public  void UploadPrescription(Context context,
                             String id,Uri selectedImageUri,String folder_type,String username,String PharmacyNAme,String type,String branchName,String notes,String pharmcayCode,String branchCode,String subFolder,Callback<ResponseUpdateProfileImage> callback){
        String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        MultipartBody.Part filePart=null;
            if (selectedImageUri!=null){
            String path = getRealPathFromURI(context, selectedImageUri);
            File imageFile = resizeAndCompressImageBeforeSend(context, path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
         filePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileBody);
            }
            new UploadClient().getApiService().updateRoshetaImage(
                    RequestBody.create(MediaType.parse("text/plain"), id),
                    filePart,
                    RequestBody.create(MediaType.parse("text/plain"), folder_type),
                    RequestBody.create(MediaType.parse("text/plain"), LANGUAGE),
                    RequestBody.create(MediaType.parse("text/plain"), username),
                    RequestBody.create(MediaType.parse("text/plain"), PharmacyNAme),
                    RequestBody.create(MediaType.parse("text/plain"), type),
                    RequestBody.create(MediaType.parse("text/plain"), subFolder) ,
                    RequestBody.create(MediaType.parse("text/plain"), pharmcayCode) ,
                    RequestBody.create(MediaType.parse("text/plain"), branchName),
                    RequestBody.create(MediaType.parse("text/plain"), branchCode),
                    RequestBody.create(MediaType.parse("text/plain"), notes)).enqueue(callback);


        /////////////////////////////////////////////////////////////////////////////////////////


    }


    //print
    public  void UploadImageReprint(final Context context,Uri selectedImageUri, String cardID,String folder_type,String createdby,String reason,Callback<ResponseUpdateProfileImage> callback){
        String LANGUAGE=LocaleHelper.getLanguage(context).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        if(selectedImageUri != null) {

            String path = getRealPathFromURI(context, selectedImageUri);
            File imageFile = resizeAndCompressImageBeforeSend(context, path);
            ProgressRequestBody fileBody = new ProgressRequestBody(imageFile, null, null);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image1", imageFile.getName(), fileBody);
            new UploadClient().getApiService().updateProfileImagePrint(RequestBody.create(MediaType.parse("text/plain"), cardID),RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), createdby),RequestBody.create(MediaType.parse("text/plain"), reason),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE) ,filePart)
                    .enqueue(callback);
        }else {


            new UploadClient().getApiService().updateProfileImagePrint(RequestBody.create(MediaType.parse("text/plain"), cardID),RequestBody.create(MediaType.parse("text/plain"), folder_type),RequestBody.create(MediaType.parse("text/plain"), createdby),RequestBody.create(MediaType.parse("text/plain"), reason),RequestBody.create(MediaType.parse("text/plain"), LANGUAGE),null)
                    .enqueue(callback);

        }

        /////////////////////////////////////////////////////////////////////////////////////////


    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static File createOutputFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //handle case of no SDCARD present
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "DMS";
            //create folder
            File folder = new File(dir); //folder name
            folder.mkdirs();

            //create file
            return new File(dir, "img" + System.currentTimeMillis() + ".png");
        }

        return null;
    }

    public static File resizeAndCompressImageBeforeSend(Context context, String filePath){
        File outputfile=null;

        final int MAX_IMAGE_SIZE = 500 * 500; // max final file size in kilobytes
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
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
        try {
            outputfile=createOutputFile();
            //save the resized and compressed file to disk cache
            FileOutputStream bmpFile = new FileOutputStream(outputfile);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
        }
        return  outputfile;
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
}
