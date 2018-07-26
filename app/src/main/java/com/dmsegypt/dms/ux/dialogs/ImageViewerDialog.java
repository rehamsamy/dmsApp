package com.dmsegypt.dms.ux.dialogs;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.graphics.Palette;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dmsegypt.dms.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by amr on 19/10/2017.
 */

public class ImageViewerDialog extends DialogFragment {

    private View rootView;
    private Unbinder unbinder;
    @BindView(R.id.button_close)
    Button closebtn;
    @BindView(R.id.img_imgview)
    ImageView imgView;

    @BindView(R.id.download_btn)
    TextView downloadbtn;
    public static final String EXTRA_IMG="extra_image";
    public static final String IS_URL="extra_is_url";
    public static Palette newpalette;
    private static final int REQUESTCODE_STORAGE = 92;


public static ImageViewerDialog newInstance(String imagedata, boolean isUrl) {

    Bundle args = new Bundle();
    ImageViewerDialog fragment = new ImageViewerDialog();
    args.putString(EXTRA_IMG,imagedata);
    args.putBoolean(IS_URL,isUrl);
    fragment.setArguments(args);
    return fragment;

}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_img_viewer, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        String imagedata=getArguments().getString(EXTRA_IMG);
        if (getArguments().getBoolean(IS_URL)){

            Glide.with(getActivity()).load(imagedata).placeholder(R.drawable.no_image).error(R.drawable.no_image).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                    if(downloadbtn != null) {
                        downloadbtn.setVisibility(View.GONE);
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                    if(downloadbtn != null) {
                        downloadbtn.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            }).into(imgView);

        }else {
        byte[] decodedString = Base64.decode(imagedata, Base64.DEFAULT);
        if (decodedString!=null){
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgView.setImageBitmap(decodedByte);
        }
        }

        downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.buildDrawingCache();
                Bitmap bitmap = imgView.getDrawingCache();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SaveImage(bitmap);
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);
                }

            }
        });

        return rootView;
    }

    @OnClick(R.id.button_close)
    void closeDialog(){
        dismiss();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void SaveImage(Bitmap bitmap) {

        OutputStream fOut = null;
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fileName = "Image-"+ n +".png";
        final String appDirectoryName = "DMS_Request";
        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        imageRoot.mkdirs();
        final File file = new File(imageRoot, fileName);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"DMS-Replys");
        values.put(MediaStore.Images.Media.DESCRIPTION, "DMS description");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put("_data", file.getAbsolutePath());
        Toast.makeText(getContext(),
                "Downloading...",
                Toast.LENGTH_LONG).show();
        ContentResolver cr = getActivity().getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG).show();
    }


}
