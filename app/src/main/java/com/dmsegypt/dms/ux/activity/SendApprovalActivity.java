package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.dmsegypt.dms.ux.dialogs.CustomEmployeeFragment;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;


public class SendApprovalActivity extends BaseActivity implements CustomEmployeeFragment.OnCompleteListener{
    //region Constants
    private final String TAG=this.getClass().getSimpleName();
    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;
    private static final String SHOWCASE_ID = "requestHelper";
    //endregion
    //region references

    Uri imageUri;
    boolean isHR;
    boolean isDMS;
    private Uri mCapturedImageURI;
    //endregion
    //region Init Views
    @BindView(R.id.activity_container)
    CoordinatorLayout container;
    @BindView(R.id.user_request_container)
    View requestContainer;
    @BindView(R.id.activity_swipeBackView)
    NestedScrollView swipeBackView;
    @BindView(R.id.iBtnGuide)
    ImageButton iBtnGuide;
    @BindView(R.id.img_imgview)
    ImageView imgView;

    @BindView(R.id.editText_description)
    EditText editTextDescription;

    @BindView(R.id.add_requestcontainer)
    FrameLayout addrequestcontainer;

    @BindView(R.id.btnaddrequest)
    Button btnaddrequest;
    @BindView(R.id.typespinner)
    MaterialSpinner mtype;
    @BindView(R.id.employee_spinner)
    MaterialSpinner employeeSpinner;

    @BindView(R.id.editText_title)
    EditText editTextTitle;
    Call call;
    AnimUtils anim;
    @BindArray(R.array.service_type)
    String[]servicesArray;
    private String EmpcardId=null;
    //endregion

        @Override
        public void onBackPressed() {
            anim.unRevealActivity();
        }

@OnClick(R.id.employee_spinner)
void hsowEmployeeDialoge(){

    final CustomEmployeeFragment cusdialog=new CustomEmployeeFragment();
    FragmentManager fragmentManager=getFragmentManager();
    cusdialog.show(fragmentManager,"Features");

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
               ButterKnife.bind(this);

               isHR=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_HR);
               isDMS=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
              if (isHR){
                  employeeSpinner.setVisibility(View.VISIBLE);
              }else {
                  employeeSpinner.setVisibility(View.GONE);
              }


                 startRevealAnimation();
                   btnaddrequest = (Button) findViewById(R.id.btnaddrequest);
                   mtype = (MaterialSpinner) findViewById(R.id.typespinner);
                   imgView = (ImageView) findViewById(R.id.img_imgview);
                   editTextDescription = (EditText) findViewById(R.id.editText_description);
                   editTextTitle = (EditText) findViewById(R.id.editText_title);
                   addrequestcontainer = (FrameLayout) findViewById(R.id.add_requestcontainer);
                   swipeBackView = (NestedScrollView) findViewById(R.id.activity_swipeBackView);

                   SharedPreferences prefs = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE);
                   if(prefs.getString("Enable","No name defined").equals("showed")){
                       iBtnGuide.setVisibility(View.GONE);

                   }


        btnaddrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDescription.setError(null);
                editTextTitle.setError(null);
                final String Title=editTextTitle.getText().toString().trim();
                final String Description=editTextDescription.getText().toString().trim();
                final String card_Id=App.getInstance().getPrefManager().getCurrentUser().getCardId();
                final String username=App.getInstance().getPrefManager().getUsername();
                if (imageUri==null){
                    Snackbar.make(findViewById(android.R.id.content),R.string.msg_upload_img,Snackbar.LENGTH_LONG).show();
                     return;
                }
                if (Title.length()==0){
                    editTextTitle.setError(getString(R.string.err_empty_field));
                    editTextTitle.requestFocus();
                    return;
                }
                if (Description.length()==0){
                    editTextDescription.setError(getString(R.string.err_empty_field));
                    editTextDescription.requestFocus();
                    return;
                }
               /* if (Constants.requestTypeList==null||Constants.requestTypeList.size()==0){
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_empty_category,Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.msg_reolad, new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v) {
                                    getRequestType();
                                }
                            }).setActionTextColor(Color.GREEN).show();

                    return;
                }*/
               if (isHR){
                   if (EmpcardId==null){
                       Snackbar.make(findViewById(android.R.id.content),R.string.msg_select_employee,Snackbar.LENGTH_LONG).show();

                       return;
                   }
               }
                if (mtype.getSelectedIndex()==0){
                    Snackbar.make(findViewById(android.R.id.content),R.string.error_not_select_requets,Snackbar.LENGTH_LONG).show();
                    return;
                }


               final MaterialDialog dialog= DialogUtils.progressDialog(SendApprovalActivity.this,R.string.msg_send_request);
                Uploader uploader=new Uploader();
                uploader.UploadImage(getApplicationContext(), imageUri, card_Id, Uploader.REQUEST_FOLDER, Title, Description, servicesArray[mtype.getSelectedIndex()]+"", username, new Callback<ResponseUpdateProfileImage>() {
                    @Override
                    public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(),R.string.mesg_reuqest_send, Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable throwable) {
                        Snackbar.make(findViewById(android.R.id.content),R.string.label_no_conenction,Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });




            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(SendApprovalActivity.this);
            }
        });

               iBtnGuide.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       presentShowcaseView();

                   }
               });


    }

    private void startRevealAnimation() {

        anim= new AnimUtils(container, getIntent(), this, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getRequestType();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_requests;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_requests;
    }

    private void presentShowcaseView() {
        new MaterialShowcaseView.Builder(this)
                .setTarget(iBtnGuide)
                .setDismissText( getString(R.string.finish))
                .setContentText(getString(R.string.requestHelper))
                .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                .show();


        SharedPreferences.Editor editor = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE).edit();
        editor.putString("Enable","showed");
        editor.apply();
        iBtnGuide.setVisibility(View.GONE);

    }



    @Override
    protected void onResume() {
               super.onResume();
           }

           private void getRequestType() {
               mtype.setItems(servicesArray);



/*

             DialogUtils.showDialog(this,true);
               call=App.getInstance().getService().getRequestType(getAppLanguage(),1);
               call.enqueue(new Callback<ResponseRequestType>() {
            @Override
            public void onResponse(Call<ResponseRequestType> call, Response<ResponseRequestType> response) {
                if (response.body() != null) {
                    DialogUtils.showDialog(SendApprovalActivity.this,false);
                    Message message = response.body().getMessage();
                    if (Constants.requestTypeList == null)
                        Constants.requestTypeList = new ArrayList<RequestType>();
                    if (message.getCode() != 1){

                    }

                    else {
                        Constants.requestTypeList.add(new RequestType("0",getString(R.string.select_request_type)));
                        Constants.requestTypeList.addAll(response.body().getList());
                        mtype.setItems(Constants.requestTypeList);
                        DialogUtils.showDialog(SendApprovalActivity.this,false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRequestType> call, Throwable throwable) {
                DialogUtils.showDialog(SendApprovalActivity.this,false);
                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getRequestType();
                            }
                        }).setActionTextColor(Color.GREEN).show();
            }
        });
*/


    }





    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
            switch (dir) {
                case SwipeBackCoordinatorLayout.UP_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_top);
                    break;

                case SwipeBackCoordinatorLayout.DOWN_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                    break;
            }
        }
    }


    public void selectImage(final Activity context) {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.add_new_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                       mCapturedImageURI= ImagesHandler.getInstance().cameraIntent(context);
                    else
                        ActivityCompat.requestPermissions(SendApprovalActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(SendApprovalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isCorrectImage = false;
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IntentManager.KEY_PICK_IMAGE) {
                imageUri =data.getData();
                    if (imageUri != null) {

                        Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                                ImagesHandler.getInstance().getPath(this, imageUri));
                        imgView.setImageBitmap(previewImage);
                        btnaddrequest.setVisibility(View.VISIBLE);


                    }



            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                    ImagesHandler.getInstance().getPath(this, imageUri));
                    imgView.setImageBitmap(previewImage);
                    btnaddrequest.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().cameraIntent(SendApprovalActivity.this);
                else
              //      Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(SendApprovalActivity.this, getString(R.string.permission_denied), 3000).show();

                break;
            case REQUESTCODE_STORAGE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().galleryIntent(this);
                else
                //    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    SuperToast.create(SendApprovalActivity.this, getString(R.string.permission_denied), 3000).show();

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){call.cancel();}
    }
    @Override
    public void onComplete(String cardid) {
        EmpcardId=cardid;
        employeeSpinner.setText(cardid);
    }
}
