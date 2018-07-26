package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfile;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.isEmailValid;
import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;

public class EditProfileActivity extends BaseActivity {

    //region declare View and Variabels
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tinputEmail)
    TextInputLayout tinputEmail;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.tinputMobileNumber)
    TextInputLayout tinputMobileNumber;
    Unbinder unbinder;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.civAvatar)
    CircleImageView civAvatar;
    private User user;
    Call call;
    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;

    @BindView((R.id.mprogress))
    ProgressBar imageProgress;
    private Uri imageUri;
    Uri mCapturedImageURI;
    private Context context;
    OnImageUpdateListener onImageUpdateListener;
    String langugue="";

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();


    }

    private void initView() {
        langugue= LocaleHelper.getLanguage(this).equals("ar")? Constants.Language.AR:Constants.Language.EN;
        user = App.getInstance().getPrefManager().getCurrentUser();
        unbinder = ButterKnife.bind(this);
        etEmail.setText(user.getEmail());
        etMobileNumber.setText(user.getMobile());


        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            Picasso.with(this)
                    .load(user.getImageUrl())
                    .priority(Picasso.Priority.HIGH)
                    .into(civAvatar);
        }

        btnSubmit.setEnabled(false);
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }


    //on Change text setEnable Bnt Sumbit Change
    @OnTextChanged({R.id.etMobileNumber, R.id.etEmail})
    public void onTextChanged(Editable editable) {
        btnSubmit.setEnabled(true);
    }


    //on Activity Result for select image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            civAvatar=(CircleImageView) findViewById(R.id.civAvatar);
            imageProgress=(ProgressBar) findViewById(R.id.mprogress);
            btnSubmit=(Button)findViewById(R.id.btnSubmit);

            //if select form Gallery
            if (requestCode == IntentManager.KEY_PICK_IMAGE) {


                imageUri =data.getData();
                if (imageUri != null) {
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    civAvatar.setImageBitmap(previewImage);
                    btnSubmit.setEnabled(true);
                    uploadImageProfile();




                }


                //if select Camera
            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {

                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    civAvatar.setImageBitmap(previewImage);
                    btnSubmit.setEnabled(true);

                    uploadImageProfile();

                }

            }
        }


    }



    //region Upload Profile
    public void uploadImageProfile(){

        final Uploader uploader=new Uploader();
        imageProgress.setVisibility(View.VISIBLE);
        final SharedPreferences.Editor editor = this.getSharedPreferences("ImageUpdated", MODE_PRIVATE).edit();
        uploader.UploadImage(this, imageUri,App.getInstance().getPrefManager().getUser().getCardId()
                , Uploader.PROFILE_FOLDER,
                new Callback<ResponseUpdateProfileImage>() {
                    @Override
                    public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                        if (onImageUpdateListener!=null){
                            onImageUpdateListener.onImageUpdate();
                        }
                        imageProgress.setVisibility(View.GONE);
                        if(response.body().getDetails() != null) {
                            Toast.makeText(EditProfileActivity.this,R.string.sucess_upload, Toast.LENGTH_SHORT).show();
                            //           Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.sucess_upload, Snackbar.LENGTH_LONG).show();
                            editor.putString("state","true");
                            editor.apply();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable t) {
                        imageProgress.setVisibility(View.GONE);
                 /*   Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.GREEN)
                            .setAction(R.string.msg_upload, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  uploadImageProfile();
                                }
                            }).show();*/
                        Toast.makeText(EditProfileActivity.this,R.string.error_inernet_connection, Toast.LENGTH_SHORT).show();

                    }
                });

    }

//endregion

    public interface OnImageUpdateListener{
        void onImageUpdate();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (call!=null){
            call.cancel();
        }
    }

    //on Click close btn & submit & image to change profile
    @OnClick({R.id.iBtnCloseBtn, R.id.btnSubmit, R.id.civAvatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iBtnCloseBtn:
                this.onBackPressed();
                break;
            case R.id.civAvatar:
                selectImage(EditProfileActivity.this);
                // IntentManager.pickImg(EditProfileFragment.this);
                break;
            case R.id.btnSubmit:
                if (user.getMobile().equals(etMobileNumber.getText().toString()))
                    attemptData(false);
                else {
                    new MaterialDialog.Builder(this)
                            .content(R.string.edit_mobile)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    attemptData(true);
                                }
                            })
                            .negativeText(R.string.cancel)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
        }
    }


    //select image for change Image
    public void selectImage(final Activity context) {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_new_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        mCapturedImageURI= ImagesHandler.getInstance().cameraIntent(context);
                    else
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }



    private void attemptData(final boolean logout) {
        DialogUtils.showDialog(this,true);

        //region Store values at the time of the login attempt.
        String mobileNumber = etMobileNumber.getText().toString();
        String email = etEmail.getText().toString();
        //endregion
        boolean cancel = false;
        View focusView = null;

        //region  Check for a valid mobile number.
        if (TextUtils.isEmpty(mobileNumber)) {
            tinputMobileNumber.setError(getString(R.string.error_field_required));
            focusView = etMobileNumber;
            cancel = true;
        } else if (!isMobileNumberValid(mobileNumber)) {
            tinputMobileNumber.setError(getString(R.string.error_invalid_mobile));
            focusView = etMobileNumber;
            cancel = true;
        }
        //endregion
        //region Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            tinputEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tinputEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }
        //endregion

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            DialogUtils.showDialog(this,false);
        } else {
            call= App.getInstance().getService().updateProfile(user.getCardId(), etEmail.getText().toString(),
                    etMobileNumber.getText().toString(),langugue);
            call.enqueue(new Callback<ResponseUpdateProfile>() {
                @Override
                public void onResponse(Call<ResponseUpdateProfile> call, Response<ResponseUpdateProfile> response) {
                    DialogUtils.showDialog(EditProfileActivity.this,false);
                    if (response.body() != null) {
                        if (response.body().getCode() == 1) {
                            if (logout) {
                                App.getInstance().getPrefManager().makeLogout();
                                IntentManager.startActivityAndFinishMe(EditProfileActivity.this, IntentManager.ACTIVITY_AUTH);
                            }else
                                Snackbar.make(findViewById(android.R.id.content),R.string.sucess_edit,Snackbar.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseUpdateProfile> call, Throwable t) {
                    DialogUtils.showDialog(EditProfileActivity.this,false);
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_LONG)
                            .show();

                }
            });
        }
    }



    @Override
    public int getResLayout() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

}
