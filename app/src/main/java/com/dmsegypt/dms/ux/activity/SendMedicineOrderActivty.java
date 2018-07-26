package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseProviders;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;
import com.dmsegypt.dms.ux.dialogs.PharmacyDialogFragment;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 18/04/2018.
 */

public class SendMedicineOrderActivty extends BaseActivity implements PharmacyDialogFragment.OnPharmcyClickListner {
    private static int time = 3000;

    @BindView(R.id.prescription_upload)
    ImageView prescriptionImgv;

    @BindView(R.id.city_spinner)
    ProgressableSpinner citySpinner;

    @BindView(R.id.area_spinner)
    ProgressableSpinner areaSpinner;
    @BindView(R.id.pharm_spinner)
    ProgressableSpinner pharmSpinner;

    @BindView(R.id.all_radio)
    RadioButton allRadio;

    @BindView(R.id.single_radio)
    RadioButton singleRadio;

    @BindView(R.id.pharm_radio)
    RadioGroup pharmRadioGroup;

    @BindView(R.id.btnSend)
    Button sendBtn;
    @BindView(R.id.container)
    View container;
    @BindView(R.id.notesEdit)
    EditText noteEdit;

    List<City> cityArray=new ArrayList<>();
    List<AreaItem> areaArray=new ArrayList<>();
    List<String> pharmaciesArray=new ArrayList<>();



    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;
    private Uri mCapturedImageURI;
    private Uri imageUri;
    private Provider provider;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        areaSpinner.getSpinner().setEnabled(false);
        areaSpinner.getSpinner().setText(R.string.area);
        citySpinner.getSpinner().setText(R.string.city);
        pharmSpinner.getSpinner().setText(R.string.label_select_pharmcy);

        pharmSpinner.getSpinner().setEnabled(false);
   pharmSpinner.getSpinner().setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           PharmacyDialogFragment fragment=PharmacyDialogFragment.newInstance(Constants.providerCities.get(citySpinner.getSpinner().getSelectedIndex()).getId(),
                   Constants.providerAreas.get(areaSpinner.getSpinner().getSelectedIndex()).getId()
           );
           fragment.setListner(SendMedicineOrderActivty.this);
           fragment.show(getSupportFragmentManager(),"pharmdialog");

       }
   });

        pharmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.all_radio:
                        container.setVisibility(View.GONE);

                        break;
                    case R.id.single_radio:
                        container.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

       citySpinner.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
               areaSpinner.getSpinner().setText(R.string.area);
               areaSpinner.getSpinner().setEnabled(true);
               pharmSpinner.getSpinner().setEnabled(false);
               pharmSpinner.getSpinner().setText(R.string.label_select_pharmcy);
               provider=null;
               getAreas(i);
           }
       });
       areaSpinner.getSpinner().setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
               pharmSpinner.getSpinner().setText(R.string.label_select_pharmcy);
               pharmSpinner.getSpinner().setEnabled(true);
               provider=null;
           }
       });



       getCities();
    }



    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_send_prescription;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.send_prescription;
    }

    @OnClick(R.id.btnSend)
    public void sendPrescription(){

        if (singleRadio.isChecked()){

            if (provider==null){
                Snackbar.make(findViewById(android.R.id.content),R.string.err_select_provider,Snackbar.LENGTH_LONG).show();
                return;
            }
        }

            DialogUtils.showDialog(SendMedicineOrderActivty.this, true);

            Uploader uploader = new Uploader();
            uploader.UploadPrescription(this, App.getInstance().getPrefManager().getUser().getCardId(), imageUri
                    , Uploader.PRESCRIPTION_FOLDER, App.getInstance().getPrefManager().getUser().getFirstName()
                    , singleRadio.isChecked()?provider.getName():"All",
                    "m", singleRadio.isChecked()?provider.getAddress():"All", noteEdit.getText().toString(),  singleRadio.isChecked()?provider.getId():"-1", singleRadio.isChecked()?provider.getId():"-1", "1",
                    new Callback<ResponseUpdateProfileImage>() {
                        @Override
                        public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                            DialogUtils.showDialog(SendMedicineOrderActivty.this, false);

                            if (response.body().getDetails() != null) {
                                new MaterialDialog.Builder(SendMedicineOrderActivty.this)
                                        .content(R.string.prescription_sent)
                                        .positiveText(getString(R.string.ok))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                finish();

                                            }
                                        })
                                        .cancelable(false)
                                        .show();
                            } else {
                                Toast.makeText(SendMedicineOrderActivty.this, R.string.error_inernet_connection, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable t) {
                            DialogUtils.showDialog(SendMedicineOrderActivty.this, false);
                            Toast.makeText(SendMedicineOrderActivty.this, R.string.error_inernet_connection, Toast.LENGTH_SHORT).show();

                        }
                    });
        }


    @OnClick(R.id.prescription_upload)
    public void choosePrescription(){
      selectImage(this);

    }


        //region  get Provider Cities
        public void getCities(){
        citySpinner.showLoading();
            App.getInstance().getService().getCities(getAppLanguage(), Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseCities>() {
                @Override
                public void onResponse(Call<ResponseCities> call, Response<ResponseCities> response) {

                    if (response.body() != null) {
                        Message message = response.body().getMessage();
                        if (message.getCode() != 1) {
                            citySpinner.showFailure();
                            citySpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                    getCities();
                                }
                            });
                        }else {
                            citySpinner.showSucess();
                            citySpinner.getSpinner().setItems(response.body().getList());
                            Constants.providerCities.addAll(response.body().getList());


                        }
                    }else {
                        //retry
                        citySpinner.showFailure();
                        citySpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                            @Override
                            public void onRetry() {
                            getCities();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ResponseCities> call, Throwable t) {
                    citySpinner.showFailure();
                    citySpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getCities();
                        }
                    });

                }
            });
        }


    void getAreas(final int position){
        areaSpinner.showLoading();
         App.getInstance().getService().getAreas(Constants.providerCities.get(position).getId(),getAppLanguage(), Constants.KEY_SEARCH_ID).enqueue(new Callback<ResponseAreas>() {
            @Override
            public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        Constants.providerAreas = response.body().getList();
                        areaSpinner.showSucess();
                        areaSpinner.getSpinner().setItems(Constants.providerAreas);
                    } else {
                       areaSpinner.showFailure();
                       areaSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                           @Override
                           public void onRetry() {
                               getAreas(position);
                           }
                       });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAreas> call, Throwable t) {
                areaSpinner.showFailure();
                areaSpinner.setOnRetryListener(new ProgressableSpinner.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getAreas(position);
                    }
                });
            }
        });

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
                        ActivityCompat.requestPermissions(SendMedicineOrderActivty.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(SendMedicineOrderActivty.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().cameraIntent(SendMedicineOrderActivty.this);
                else
                    SuperToast.create(SendMedicineOrderActivty.this, getString(R.string.permission_denied), 3000).show();

                break;
            case REQUESTCODE_STORAGE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    ImagesHandler.getInstance().galleryIntent(this);
                else
                    SuperToast.create(SendMedicineOrderActivty.this, getString(R.string.permission_denied), 3000).show();

                break;
        }
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
                    prescriptionImgv.setImageBitmap(previewImage);


                }

            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {
                    Bitmap previewImage=ImagesHandler.resizeAndCompressImageBeforeSend(this,
                            ImagesHandler.getInstance().getPath(this, imageUri));
                    prescriptionImgv.setImageBitmap(previewImage);

                }

            }
        }
    }

    @Override
    public void OnPharmcyClicked(Provider item) {
        pharmSpinner.getSpinner().setText(item.getName());
        this.provider=item;
    }
}
