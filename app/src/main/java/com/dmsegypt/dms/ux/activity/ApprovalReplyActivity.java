package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Image;
import com.dmsegypt.dms.rest.model.ProgressRequestBody;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.dialogs.ImageViewerDialog;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 18/10/2017.
 */

public class ApprovalReplyActivity extends AppCompatActivity {



    //region Declare Variables And View
    @BindView(R.id.accept_radio)
    RadioButton acceptRadio;
    @BindView(R.id.refuse_radio)
    RadioButton refuseRadio;

    @BindView(R.id.editText_note)
    EditText notesEt;

    @BindView(R.id.btn_select)
    ImageView uploadImgv;

    @BindView(R.id.img_imgview)
    ImageView img_imagview;

    @BindView(R.id.relativeLayout1)
    FrameLayout frameLayoutimage;

    @BindView(R.id.send_Replay)
    TextView send_Replay;

    @BindView(R.id.iBtnCloseBtn)
    ImageButton iBtnCloseBtn;


    @BindView(R.id.button_close)
    Button closeImage;

    boolean isChanged =false;
    String request_id;
    private static final int REQUESTCODE_STORAGE = 99;
    private static final int REQUESTCODE_CAMERA = 100;
    Call call;
    private Uri mCapturedImageURI;
    private Uri imageUri;

    //endregion







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_reply);

        ButterKnife.bind(this);
        //get Request Id from Intent
        Intent intent=getIntent();
        request_id=intent.getStringExtra("REQUEST_ID");
        iBtnCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //when close Image set ImageView equal Null
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayoutimage.setVisibility(View.GONE);
                img_imagview.setImageDrawable(null);
            }
        });


    }




    //region On Click of select Image
    @OnClick(R.id.btn_select)
    void uploadImage() {
        //select Image pass Activity
        selectImage(this);

    }

    //endregion




    //region Select Image Function have two options from Camera or Gallry
    public void selectImage(final Activity context) {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.add_new_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        mCapturedImageURI = ImagesHandler.getInstance().cameraIntent(context);
                    else
                        ActivityCompat.requestPermissions(ApprovalReplyActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(ApprovalReplyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }

    //endregion




    //region Select Image on Avtivity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isCorrectImage = false;
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IntentManager.KEY_PICK_IMAGE) {
                imageUri = data.getData();
                if (imageUri != null) {

                    Bitmap previewImage = ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    img_imagview.setImageBitmap(previewImage);
                    frameLayoutimage.setVisibility(View.VISIBLE);
                    //btnaddrequest.setVisibility(View.VISIBLE);


                }


            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri = mCapturedImageURI;
                if (imageUri != null) {
                    Bitmap previewImage = ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    img_imagview.setImageBitmap(previewImage);
                    frameLayoutimage.setVisibility(View.VISIBLE);
                    // btnaddrequest.setVisibility(View.VISIBLE);
                }

            }
        }
    }


    //endregion



    //region check if there is no permission to Camera Access and Storage
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().cameraIntent(ApprovalReplyActivity.this);
                else
                    //      Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(ApprovalReplyActivity.this, getString(R.string.Permission_Denied), 3000).show();

                break;
            case REQUESTCODE_STORAGE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().galleryIntent(this);
                else
                    //    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(ApprovalReplyActivity.this, getString(R.string.Permission_Denied), 3000).show();

                break;
        }
    }

//endregion




    //region OnClick send Reply
    @OnClick(R.id.send_Replay)
    void sendReply(){

        //check validation
        String notes=notesEt.getText().toString().trim();
        if (notes.isEmpty()){
            notesEt.setError(getString(R.string.err_empty_field));

            return;
        }
        String replyFlag=acceptRadio.isChecked()?"1":"0";
        if (notes.isEmpty()){
            notesEt.setError(getString(R.string.err_empty_field));
            notesEt.requestFocus();
            return;
        }
        if (imageUri==null){
            Snackbar.make(findViewById(android.R.id.content),R.string.msg_upload_img,Snackbar.LENGTH_LONG).show();
            return;
        }




        DialogUtils.showDialog(ApprovalReplyActivity.this,true);
        Uploader uploader=new Uploader();
        String path=uploader.getRealPathFromURI(this,imageUri);
        File imageFile= Uploader.resizeAndCompressImageBeforeSend(this,path);
        ProgressRequestBody fileBody=new ProgressRequestBody(imageFile,null,null);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileBody);

        //uploader.UploadImage pass context/imageurl/FolderName which save image /reply state(Accept or refuse) / notes/request_id
        uploader.UploadImage(getApplicationContext(),imageUri, Uploader.REPLY_FOLDER,replyFlag,notes,request_id, new Callback<ResponseUpdateProfileImage>() {
            @Override
            public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                DialogUtils.showDialog(ApprovalReplyActivity.this,false);

                if (response.body()!=null) {
                    if (response.body().getCode()==1){
                        isChanged =true;
                        Toast.makeText(ApprovalReplyActivity.this,R.string.msg_Reply_sent, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content),R.string.err_send_reply,Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable throwable) {
                DialogUtils.showDialog(ApprovalReplyActivity.this,false);

                Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG).show();

            }
        });

    }

//endregion




    //distroy CallBack to avoid leak Memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
    }
}