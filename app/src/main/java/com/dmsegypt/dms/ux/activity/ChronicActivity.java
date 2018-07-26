package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.MedicineDetails;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.Response.ResponseMedicineDetails;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.adapter.ChronicAdapter;
import com.dmsegypt.dms.ux.adapter.ChronicEmpAdapter;
import com.dmsegypt.dms.ux.adapter.EmployeesAdapter;
import com.dmsegypt.dms.ux.adapter.RequestAdapter;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class ChronicActivity extends BaseActivity {


  //region Declare Variables and Views
    private static final String EXTRA_USER_TYPE ="usertype" ;
    private ChronicAdapter chronicAdapter;
    private List<MedicineDetails> medicaldetails;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tv_last)
    TextView tv_last;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.med_details)
    RecyclerView mRecyclerViewmedicine;
    @BindView(R.id.relative_details)
    RelativeLayout relative_details;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.mprogress)
    ProgressBar image_progress;
    @BindView(R.id.image_view)
    ImageView image_view;
    @BindView(R.id.reload_btn)
    Button reloadbtn;
   @BindView(R.id.progress_bar)
   ProgressBar mainprogress;
    private boolean isReload=false;

    @BindView(R.id.no_medicine_tv)
    TextView noMedicineTv;
    @BindView(R.id.detail_card_container)
    View detailcard;
    @BindView(R.id.user_chronic_layout)
    View userlayout;
    @BindView(R.id.hr_chronic_layout)
    View hrlayout;
    @BindView(R.id.hr_recyclerView)
    RecyclerView hrRecyleView;
    @BindView(R.id.hr_progress_bar)
    ProgressBar hrProgress;
    @BindView(R.id.hr_emtpy_tv)
    TextView hrEmptyTv;

    @BindView(R.id.iBtnCloseBtn)
    ImageButton iBtnCloseBtn;

    @BindView(R.id.fab_add)
    FloatingActionButton fab_list;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private Uri mCapturedImageURI;
    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;
    private Uri imageUri;
      List<User>employees;
    boolean isHR;
    boolean isfloat;
    private int lastIndex=0;
    private ChronicEmpAdapter employeesAdapter;

    @BindView(R.id.iBtnGuide)
    ImageButton iBtnGuide;

    private static final String SHOWCASE_ID = "chronicHelper";
    Call call;
    Call nestedCall;
//endregion

    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isHR=App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_HR);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        //image helper on Click
        iBtnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentShowcaseView();
            }
        });
        userlayout.setVisibility(View.VISIBLE);
        hrlayout.setVisibility(View.GONE);
        SharedPreferences prefs = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE);

        if(prefs.getString("Enable","No name defined").equals("showed")){
            iBtnGuide.setVisibility(View.GONE);

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isfloat=true;
                onBackPressed();
            }
        });

        //Collapsing Declaration
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colormain));
        collapsingToolbarLayout.setTitle(getString(R.string.label_upload));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewmedicine.setLayoutManager(layoutManager);
        mRecyclerViewmedicine.setNestedScrollingEnabled(false);


        //float Action to select new_image Chronic medical
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(ChronicActivity.this);
            }
        });


        //retrive user data
        retriveData();


        //check if User HR to show his Employees with monthly medical
        if (!isHR) {
            fab_list.setVisibility(View.GONE);

        }else {
            fab_list.setVisibility(View.VISIBLE);

        }


        //fab on Click
        fab_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveEmployees(0);
                isfloat=true;
                fab_list.setVisibility(View.GONE);

                userlayout.setVisibility(View.GONE);
                hrlayout.setVisibility(View.VISIBLE);
                hrRecyleView.setHasFixedSize(true);
                hrRecyleView.setLayoutManager(new LinearLayoutManager(ChronicActivity.this,LinearLayoutManager.VERTICAL,false));
            }
        });


        //close layout
        iBtnCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isfloat=true;
                fab_list.setVisibility(View.VISIBLE);
                iBtnGuide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presentShowcaseView();
                    }
                });
                userlayout.setVisibility(View.VISIBLE);
                hrlayout.setVisibility(View.GONE);
                SharedPreferences prefs = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE);

                if(prefs.getString("Enable","No name defined").equals("showed")){
                    iBtnGuide.setVisibility(View.GONE);

                }

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colormain));
                collapsingToolbarLayout.setTitle(getString(R.string.label_upload));
                layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerViewmedicine.setLayoutManager(layoutManager);
                mRecyclerViewmedicine.setNestedScrollingEnabled(false);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectImage(ChronicActivity.this);
                    }
                });

                retriveData();


            }
        });
    }



//region on Back Pressed And Toolbar Declaration
    @Override
    public void onBackPressed() {
        if(userlayout.getVisibility() == View.GONE){
            isfloat=true;
            fab_list.setVisibility(View.VISIBLE);
            iBtnGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentShowcaseView();
                }
            });
            userlayout.setVisibility(View.VISIBLE);
            hrlayout.setVisibility(View.GONE);
            SharedPreferences prefs = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE);

            if(prefs.getString("Enable","No name defined").equals("showed")){
                iBtnGuide.setVisibility(View.GONE);

            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colormain));
            collapsingToolbarLayout.setTitle(getString(R.string.label_upload));
            layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerViewmedicine.setLayoutManager(layoutManager);
            mRecyclerViewmedicine.setNestedScrollingEnabled(false);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage(ChronicActivity.this);
                }
            });

            retriveData();



        } else {
          super.onBackPressed();
        }

    }

    @Override
    public boolean hasActionBar() {
        return isfloat;
    }
    @Override
    public int getResLayout() {
        return R.layout.activity_chronic;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_chronic;
    }

    //endregion



    //region ShowcaseView helper visible only once
    private void presentShowcaseView() {


        new MaterialShowcaseView.Builder(this)
                .setTarget(floatingActionButton)
                .setDismissText( getString(R.string.finish))
                .setContentText(getString(R.string.chronicHelper))
                .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                .setDismissOnTouch(true)
                .show();

        SharedPreferences.Editor editor = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE).edit();
        editor.putString("Enable","showed");
        editor.apply();
        iBtnGuide.setVisibility(View.GONE);

    }

    //endregion



    //region Select Image
    public void selectImage(final Activity context) {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.add_new_prescription));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        mCapturedImageURI= ImagesHandler.getInstance().cameraIntent(context);
                    else
                        ActivityCompat.requestPermissions(ChronicActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(ChronicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }

    //endregion




    //region Select Image on Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IntentManager.KEY_PICK_IMAGE) {
                imageUri =data.getData();
                //when image Selected
                if (imageUri != null) {

                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    image_view.setImageBitmap(previewImage);

                    Uploader uploader=new Uploader();
                    image_progress.setVisibility(View.VISIBLE);
                    String card_id=App.getInstance().getPrefManager().getUser().getCardId();
                    String username=App.getInstance().getPrefManager().getUsername();
                    uploader.UploadImage(getApplicationContext(), imageUri,card_id,username
                            , Uploader.CHRONIC_FOLDER,
                            new retrofit2.Callback<ResponseUpdateProfileImage>() {
                                @Override
                                public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {

                                    image_progress.setVisibility(View.GONE);
                                    Snackbar.make(findViewById(android.R.id.content),response.body().getDetails().toString(),Snackbar.LENGTH_SHORT)
                                            .show();                                        }

                                @Override
                                public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable t) {
                                    image_progress.setVisibility(View.GONE);
                                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_SHORT)
                                            .show();

                                }
                            });


                }



            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    image_view.setImageBitmap(previewImage);

                    Uploader uploader=new Uploader();
                    image_progress.setVisibility(View.VISIBLE);
                    String card_id=App.getInstance().getPrefManager().getUser().getCardId();
                    String username=App.getInstance().getPrefManager().getUsername();
                    uploader.UploadImage(getApplicationContext(), imageUri,card_id,username
                            , Uploader.CHRONIC_FOLDER,
                            new retrofit2.Callback<ResponseUpdateProfileImage>() {
                                @Override
                                public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                                    Bitmap bitmap = ((BitmapDrawable) image_view.getDrawable()).getBitmap();
                                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                        public void onGenerated(Palette palette) {
                                            applyPalette(palette);
                                        }
                                    });
                                    image_progress.setVisibility(View.GONE);
                                    Snackbar.make(findViewById(android.R.id.content),response.body().getDetails().toString(),Snackbar.LENGTH_SHORT)
                                            .show();                                        }

                                @Override
                                public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable t) {
                                    image_progress.setVisibility(View.GONE);
                                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_SHORT)
                                            .show();

                                }
                            });

                }

            }
        }



    }

    //endregion


    //region Reload when Connection Faied
    @OnClick(R.id.reload_btn)
    void reloadchronic(){
        retriveData();
//
    }

    //endregion



    //region get last Chronic Image For Ures by card Id and Folder Type
    void getChronicImage(){
       call= App.getInstance().getService().getImagebase54(App.getInstance().getPrefManager().getCurrentUser().getCardId().toString(),"CHRONIC");
        call.enqueue(new retrofit2.Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if(response.body() != null) {

                    //append image url from server
                    String image_url = response.body().getDetails();


                    if(image_url!= null) {

                        byte[] decodedString = Base64.decode(image_url, Base64.DEFAULT);
                        if (decodedString!=null){
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_view.setImageBitmap(decodedByte);



                        }







                    }else {
                        image_view.setImageResource(R.drawable.no_image);
                    }
                }else {
                    image_view.setImageResource(R.drawable.no_image);

                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable throwable) {

            }
        });

            Bitmap bitmap = ((BitmapDrawable) image_view.getDrawable()).getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    applyPalette(palette);
                }
            });

    }

    //endregion





    @Override
    protected void onResume() {
        super.onResume();
        if (!isReload) {
            isReload = true;

                getChronicImage();



        }

    }


    //region Retrive Employee which have medical monthely
    private void retrieveEmployees(final int lastIndex){
         if (lastIndex==0){
        hrProgress.setVisibility(View.VISIBLE);
        hrEmptyTv.setVisibility(View.GONE);
        hrRecyleView.setVisibility(View.GONE);
         }
       call= App.getInstance().getService().getMedEmployee(App.getInstance().getPrefManager().getUsername(),lastIndex+"",
                10+"", getAppLanguage());
                call.enqueue(new retrofit2.Callback<ResponseMembers>() {
                    @Override
                    public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {

                        if (response.body() != null) {
                            Message message = response.body().getMessage();
                            if (message.getCode() != 1) {
                                if ((lastIndex == 0)) {
                                    hrEmptyTv.setVisibility(View.VISIBLE);
                                    hrRecyleView.setVisibility(View.GONE);
                                    hrProgress.setVisibility(View.GONE);
                                    SuperToast.create(ChronicActivity.this, message.getDetails(), 3000).show();

                                } else if ((lastIndex != 0)) {
                                    employeesAdapter.setEnableLoadMore(false);
                                }
                            } else {

                                if (lastIndex == 0) {
                                    hrEmptyTv.setVisibility(View.GONE);
                                    if (userlayout.getVisibility()!=View.VISIBLE){
                                    hrRecyleView.setVisibility(View.VISIBLE);
                                    hrlayout.setVisibility(View.VISIBLE);
                                    }
                                    hrProgress.setVisibility(View.GONE);

                                    employees = response.body().getList();
                                    employeesAdapter = new ChronicEmpAdapter(R.layout.list_item_chronic_emp, employees);
                                    hrRecyleView.setAdapter(employeesAdapter);
                                    employeesAdapter.notifyDataSetChanged();  // data set changed


                                    employeesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                        @Override
                                        public void onLoadMoreRequested() {
                                            retrieveEmployees(employeesAdapter.getItemCount() - 1);
                                        }
                                    }, hrRecyleView);


                                } else {
                                    if ((response.body().getList().size() % 20) != 0)
                                        employeesAdapter.setEnableLoadMore(false);
                                    employees.addAll(response.body().getList());
                                }
                                employeesAdapter.notifyDataSetChanged();
                            }

                            if(employeesAdapter != null) {
                                employeesAdapter.loadMoreComplete();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseMembers> call, Throwable t) {
                        if (lastIndex == 0){
                            hrProgress.setVisibility(View.GONE);
                            if(t != null) {
                                Log.d("onFailure: ", t.getMessage().toString());
                                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                                        .setAction(R.string.action_reload, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                retrieveEmployees(0);
                                            }
                                        })
                                        .setActionTextColor(Color.GREEN)
                                        .show();
                            }
                        }
                        else {

                            Toast.makeText(ChronicActivity.this, R.string.label_no_conenction, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    //endregion


    //region getChronicMedication Details and last Transaction
    private void retriveData() {
        mainprogress.setVisibility(View.VISIBLE);
        reloadbtn.setVisibility(View.GONE);
        noMedicineTv.setVisibility(View.GONE);
        tv_last.setVisibility(View.GONE);
        relative_details.setVisibility(View.GONE);
        medicaldetails=new ArrayList<>();
        //get Midecin
        call=App.getInstance().getService().getChronicMedication(App.getInstance().getPrefManager().getUser().getCardId().toString(),getAppLanguage());
                call.enqueue(new retrofit2.Callback<ResponseMedicineDetails>() {
            @Override
            public void onResponse(Call<ResponseMedicineDetails> call, Response<ResponseMedicineDetails> response) {
                if(response.body() != null){
                    medicaldetails=response.body().getList();
                      // data set changed
                    //get last trancation
                    nestedCall=App.getInstance().getService().getChroniclastTransaction(App.getInstance().getPrefManager().getUser().getCardId().toString(),getAppLanguage());
                    nestedCall.enqueue(new retrofit2.Callback<StatusResponse>() {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                            if (response != null && response.body() != null) {
                                mainprogress.setVisibility(View.GONE);
                                reloadbtn.setVisibility(View.GONE);
                                if (medicaldetails.size()>0){
                                    noMedicineTv.setVisibility(View.GONE);
                                    mRecyclerViewmedicine.setVisibility(View.VISIBLE);
                                    chronicAdapter = new ChronicAdapter(R.layout.item_list_chronic, medicaldetails);
                                mRecyclerViewmedicine.setAdapter(chronicAdapter);
                                DividerItemDecoration dividerItemDecoration =
                                        new DividerItemDecoration(mRecyclerViewmedicine.getContext(),
                                        layoutManager.getOrientation());
                                mRecyclerViewmedicine.addItemDecoration(dividerItemDecoration);
                                chronicAdapter.notifyDataSetChanged();
                                }else {
                                    noMedicineTv.setVisibility(View.VISIBLE);
                                    mRecyclerViewmedicine.setVisibility(View.GONE);
                                }
                                tv_last.setText(response.body().getDetails().toString());
                                tv_last.setVisibility(View.VISIBLE);
                                relative_details.setVisibility(View.VISIBLE);
                                detailcard.setTranslationY(100);
                                ViewCompat.animate(detailcard)
                                        .translationY(0)
                                        .setDuration(700)
                                        .setInterpolator(new BounceInterpolator())
                                        .start();
                            }else {
                                reloadbtn.setVisibility(View.VISIBLE);
                                mainprogress.setVisibility(View.GONE);
                                noMedicineTv.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                            reloadbtn.setVisibility(View.VISIBLE);
                            mainprogress.setVisibility(View.GONE);
                            noMedicineTv.setVisibility(View.GONE);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ResponseMedicineDetails> call, Throwable throwable) {
                reloadbtn.setVisibility(View.VISIBLE);
                mainprogress.setVisibility(View.GONE);
                noMedicineTv.setVisibility(View.GONE);



            }
        });

    }

    //endregion




    //region palette library which show bottom color like image color
    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimary);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setTitle(getString(R.string.label_chronic_title));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.mdtp_accent_color));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }


    //endregion

    //distroy CallBack to avoid leak Memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();
        }
        if (nestedCall!=null){nestedCall.cancel();}
    }
}
