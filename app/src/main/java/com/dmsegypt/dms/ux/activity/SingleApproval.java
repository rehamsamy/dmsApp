package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.SingleRequestImage;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.adapter.IndemnityRequestAdapter;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleApproval extends BaseActivity {

    public static final int INTENT_REQUEST_GET_IMAGES = 22;
    public static final String EXTRA_SUB_FOLDER ="extra_subfolder" ;
    private static final int STORAGE_PERMISSION_CODE =55 ;
    @BindView(R.id.recyclerimages)
    RecyclerView recycleview;
    @BindView(R.id.add_image_imgv)
    ImageView addImageImgv;

    @BindView(R.id.editText_note)
    EditText editText_note;

    @BindView(R.id.editText_title)
    EditText editText_title;

    @BindView(R.id.codegenerated)
    TextView codegenerated;

    @BindView(R.id.Codelinear)
    LinearLayout Codelinear;

    @BindView(R.id.send_Replay)
    Button send_Replay;

    boolean isDMSUser;


    IndemnityRequestAdapter adapter;
    ArrayList<SingleRequestImage>singleRequesImagetList;
    String subFolder="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        iniView();

    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_single_approval;
    }


    @OnClick(R.id.send_Replay)
    public void SendRequest(){
        isDMSUser=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS);
        if (editText_title.getText().toString().isEmpty()){
            editText_title.setError(getString(R.string.err_empty_field));
            editText_title.requestFocus();
            return;
        }

        if (editText_note.getText().toString().isEmpty()){
            editText_note.setError(getString(R.string.err_empty_field));
            editText_note.requestFocus();
            return;
        }
        String title=editText_title.getText().toString().trim();
        String description=editText_note.getText().toString().trim();
        String cardId="";
        if(isDMSUser){
            cardId=App.getInstance().getPrefManager().getUser().getCardId();

        }else {
            cardId="DMS_member";
        }
        String Username=App.getInstance().getPrefManager().getCurrentUser().getEmail();
        String LANGUAGE = LocaleHelper.getLanguage(this).equals("ar") ? Constants.Language.AR : Constants.Language.EN;
        DialogUtils.showDialog(this,true);
        App.getInstance().getService().indvidualApproval(LANGUAGE,title,description,cardId,Username,subFolder).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if(response != null && response.body().getCode() != null){
                    if(response.body().getUserid() != null){
                        codegenerated.setText(response.body().getUserid());
                        Codelinear.setVisibility(View.VISIBLE);
                    }

                    SuperToast.create(SingleApproval.this, response.body().getDetails(), 3000).show();
                }
                DialogUtils.showDialog(SingleApproval.this,false);
            }
            @Override
            public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                DialogUtils.showDialog(SingleApproval.this,false);
                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public int getToolbarTitle() {
        return R.string.indemnity;
    }

    void iniView(){
        recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recycleview.setHasFixedSize(true);
        singleRequesImagetList=new ArrayList<>();
        singleRequesImagetList.add(new SingleRequestImage());
        subFolder=getIntent().getStringExtra(EXTRA_SUB_FOLDER);
        adapter=new IndemnityRequestAdapter(singleRequesImagetList,subFolder,this);
        recycleview.setAdapter(adapter);
        addImageImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isReadStorageAllowed()){
                    openGallery();
                    return;
                }
                requestStoragePermission();

            }
        });

    }

    private void openGallery() {
        Config config = new Config();
        config.setToolbarTitleRes(R.string.custom_title);
        config.setSelectionLimit(8);
        ImagePickerActivity.setConfig(config);
        Intent intent  = new Intent(SingleApproval.this, ImagePickerActivity.class);
        startActivityForResult(intent, SingleApproval.INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            ArrayList<Uri>  image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            int size=singleRequesImagetList.size();
            if (size<=8){
            if(size !=0){
                singleRequesImagetList.remove(size-1);
                addImageImgv.setVisibility(View.GONE);
                recycleview.setVisibility(View.VISIBLE);
                send_Replay.setVisibility(View.VISIBLE);

            }
            for (Uri uri:image_uris){
                SingleRequestImage image=new SingleRequestImage();
                image.setLocal_path(uri);
                singleRequesImagetList.add(image);
            }
            singleRequesImagetList.add(new SingleRequestImage());
            adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(this,R.string.max_image, Toast.LENGTH_SHORT).show();

            }
            //send_Replay.setVisibility(View.VISIBLE);
            //do something
        }
    }
    private boolean isReadStorageAllowed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
