package com.dmsegypt.dms.ux.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.ImagesHandler;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHrRequestActivity extends BaseActivity implements SwipeBackCoordinatorLayout.OnSwipeListener {

    //region Init Views

    @BindView(R.id.activity_settings_swipeBackView)
    SwipeBackCoordinatorLayout swipeBackView;

    @BindView(R.id.fblock1)
    EditText block1;

    @BindView(R.id.fblock2)
    EditText block2;

    @BindView(R.id.etdate)
    EditText EtDate;

    @BindView(R.id.fblock3)
    EditText block3;

    @BindView(R.id.fblock4)
    EditText block4;

    @BindView(R.id.codegenerated)
    TextView generatedCode;

    @BindView(R.id.search_id)
    Button searchid;

    @BindView(R.id.reasonimage)
    ImageButton reasonimage;

    @BindView(R.id.imageurl)
    TextView imageurl;



    @BindView(R.id.name_tv)
    TextView tv_name;


    @BindView(R.id.activity_editCard_container)
    CoordinatorLayout container;


    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.optionlinear)
    LinearLayout optionlinear;


    @BindArray(R.array.edit_card_option)
    String[]option_list;

    @BindArray(R.array.edit_card_reason)
    String[] reason_list;

    @BindView(R.id.linearCode)
    LinearLayout linearCode;

    @BindView(R.id.categoryspinner)
    MaterialSpinner mcategory;


    @BindView(R.id.reasonspinner)
    MaterialSpinner reasonspinner;

    @BindView(R.id.linearprintcard)
    LinearLayout linearprintcard;

    @BindView(R.id.linearreopen)
    LinearLayout linearreopen;

    @BindView(R.id.block1)
    EditText blockedit1;

    @BindView(R.id.tinputCardIdblock4)
    TextInputLayout tinputCardIdblock4;

    @BindView(R.id.block2)
    EditText blockedit2;

    @BindView(R.id.block3)
    EditText blockedit3;

    @BindView(R.id.block4)
    EditText blockedit4;

    @BindView(R.id.start_date)
    TextView tvstart_date;

    @BindView(R.id.end_date)
    TextView tvend_date;

    @BindView(R.id.printlinear)
    LinearLayout printlinear;
    @BindView(R.id.linearname)
    LinearLayout nameLinear;
    @BindView(R.id.et_e_name)
    EditText enameEdit;
    @BindView(R.id.et_a_name)
    EditText anameEdit;
    @BindView(R.id.input_e_name)
    TextInputLayout enameInput;
    @BindView(R.id.input_a_name)
    TextInputLayout anameInput;
    private static String block1st = "";
    private static String block2st = "";
    private static String block3st = "";
    private static String block4st = "";
    private Uri mCapturedImageURI;
    private static final int REQUESTCODE_CAMERA = 91;
    private static final int REQUESTCODE_STORAGE = 92;

    private static String cardId="";
    private static String start_date="";
    private static String end_date="";
    Uri imageUri;
    Call call;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ButterKnife.bind(this);

        initView();

    }


    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }


    //region toolbar declare
    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_hr_edit_card;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.action_edit_card;
    }


    //endregion

    //region request change card number
    private void sendRequestChange() {

        if (blockedit3.getText().toString().isEmpty()){
            blockedit3.setError(getString(R.string.err_empty_field));
            blockedit3.requestFocus();
            return;
        }

        DialogUtils.showDialog(this,true);
        final String NewCardId=blockedit1.getText().toString().trim()+"-"+blockedit2.getText().toString().trim()+"-"+blockedit3.getText().toString().trim()+"-"+blockedit4.getText().toString().trim();
        final String UserEmail=App.getInstance().getPrefManager().getUsername();
        if(NewCardId != null || !NewCardId.equals("") && cardId != null || !cardId.equals("") && UserEmail != null || !UserEmail.equals("")){
            call= App.getInstance().getService().changeEmployeeCard(cardId,NewCardId,getAppLanguage(),UserEmail);
            call.enqueue(new Callback<ResponseItem>() {
                @Override
                public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                    if(response != null && response.message() != null){
                        Snackbar.make(findViewById(android.R.id.content), response.body().getMessage().getDetails().toString(), Snackbar.LENGTH_LONG).show();
                        generatedCode.setText(response.body().getMessage().getUserid().toString());
                        linearCode.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }else {
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }

                }


                @Override
                public void onFailure(Call<ResponseItem> call, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_SHORT).show();
                    DialogUtils.showDialog(AddHrRequestActivity.this,false);
                }
            });
        }


    }
    //endregion


    //region reopne Card
    private void sendRequestReopen() {


        if (EtDate.getText().toString().isEmpty()){
            EtDate.setError(getString(R.string.err_empty_field));
            EtDate.requestFocus();
            return;
        }
        DialogUtils.showDialog(this,true);
        final String Etdate=EtDate.getText().toString().trim();
        final String UserEmail=App.getInstance().getPrefManager().getUsername();
        if(Etdate != null || !Etdate.equals("") && cardId != null || !cardId.equals("") && UserEmail != null || !UserEmail.equals("")){
            call= App.getInstance().getService().reopenEmployeeCard(cardId,Etdate.replaceAll("/", "-"),getAppLanguage(),UserEmail);
            call.enqueue(new Callback<ResponseItem>() {
                @Override
                public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {

                    if(response != null && response.message() != null){
                        Toast.makeText(getApplicationContext(), response.body().getMessage().getDetails().toString(), Toast.LENGTH_LONG).show();
                        generatedCode.setText(response.body().getMessage().getUserid().toString());
                        linearCode.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }else {
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }

                }

                @Override
                public void onFailure(Call<ResponseItem> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), throwable+"", Toast.LENGTH_LONG).show();

                    DialogUtils.showDialog(AddHrRequestActivity.this,false);

                }
            });
        }else {
            DialogUtils.showDialog(AddHrRequestActivity.this,false);
        }

    }

    //endregion

    private void sendRequestChangeName() {


        if (enameEdit.getText().toString().isEmpty()){
            enameEdit.setError(getString(R.string.err_empty_field));
            enameEdit.requestFocus();
            return;
        }
         if (enameEdit.getText().toString().trim().split(" ").length < 3) {
            enameEdit.setError(getString(R.string.error_invalid_fullname));
            enameEdit.requestFocus();
            return;
        }


        if (anameEdit.getText().toString().isEmpty()){
            anameEdit.setError(getString(R.string.err_empty_field));
            anameEdit.requestFocus();
            return;
        }

        if (anameEdit.getText().toString().trim().split(" ").length < 3) {
            anameEdit.setError(getString(R.string.error_invalid_fullname));
            anameEdit.requestFocus();
            return;
        }

        DialogUtils.showDialog(this,true);
        final String Ename=enameEdit.getText().toString().trim();
        final String Aname=anameEdit.getText().toString().trim();
        if(Ename != null || !Aname.equals("") && cardId != null || !cardId.equals("") ){
            call= App.getInstance().getService().changeEmployeeName(cardId,Ename,Aname,App.getInstance().getPrefManager().getUsername(),getAppLanguage());
            call.enqueue(new Callback<ResponseItem>() {
                @Override
                public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {

                    if(response != null && response.message() != null){
                        Toast.makeText(getApplicationContext(), response.body().getMessage().getDetails().toString(), Toast.LENGTH_LONG).show();
                        generatedCode.setText(response.body().getMessage().getUserid().toString());
                        linearCode.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }else {
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }

                }

                @Override
                public void onFailure(Call<ResponseItem> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), throwable+"", Toast.LENGTH_LONG).show();
                    DialogUtils.showDialog(AddHrRequestActivity.this,false);

                }
            });
        }else {
            DialogUtils.showDialog(AddHrRequestActivity.this,false);
        }

    }

    //region datePicker to get date from pickerDialog
    @OnTouch({R.id.etdate,R.id.tinputdate})
    boolean datePicker(MotionEvent e) {
        if (e.getAction()==MotionEvent.ACTION_DOWN) {

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            String newDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Calendar c = Calendar.getInstance();
                            int nowyear = c.get(Calendar.YEAR);
                            int nowmonth = c.get(Calendar.MONTH);
                            int nowday = c.get(Calendar.DAY_OF_MONTH);
                            if (nowyear < year) {
                                EtDate.setText(newDate);
                                EtDate.setError(null);
                            } else if (nowyear == year) {
                                if (nowmonth < monthOfYear) {
                                    EtDate.setText(newDate);
                                    EtDate.setError(null);
                                } else if (nowmonth == monthOfYear) {
                                    if (nowday < dayOfMonth) {
                                        EtDate.setText(newDate);
                                        EtDate.setError(null);
                                    } else {
                                        EtDate.setError(getString(R.string.invalid_date));
                                    }
                                } else {
                                    EtDate.setError(getString(R.string.invalid_date));
                                }
                            } else {
                                EtDate.setError(getString(R.string.invalid_date));
                            }
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setFirstDayOfWeek(Calendar.SATURDAY);
            dpd.showYearPickerFirst(true);
            dpd.setTitle(getString(R.string.birthdate_pick));
            dpd.show(getFragmentManager(), "datePickerDialog");
            return true;
        }
        return false;
    }

    //endregion


    //region print new_image card
    private void sendRequestprint() {
        if (reason_list[reasonspinner.getSelectedIndex()].equals(reason_list[0])){
            reasonspinner.setError(getString(R.string.err_empty_field));
            reasonspinner.requestFocus();
            return;
        }
        DialogUtils.showDialog(this,true);
        final String UserEmail=App.getInstance().getPrefManager().getUsername();
        if(UserEmail != null || !UserEmail.equals("") && cardId != null || !cardId.equals("")){

            if(reason_list[reasonspinner.getSelectedIndex()].equals(reason_list[1])){

                Uploader uploader=new Uploader();
                uploader.UploadImageReprint(getApplicationContext(), null, cardId, Uploader.PRINT_FOLDER,UserEmail,Constants.EditCardPrintOption.get(reasonspinner.getSelectedIndex()), new Callback<ResponseUpdateProfileImage>() {
                    @Override
                    public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                        Toast.makeText(getApplicationContext(), response.body().getDetails()+"", Toast.LENGTH_LONG).show();
                        generatedCode.setText(response.body().getImageURL().toString());
                        linearCode.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }

                    @Override
                    public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);

                    }
                });


            }else if(reason_list[reasonspinner.getSelectedIndex()].equals(reason_list[2])){
                Uploader uploader=new Uploader();
                uploader.UploadImageReprint(getApplicationContext(), imageUri, cardId, Uploader.PRINT_FOLDER,UserEmail,Constants.EditCardPrintOption.get(reasonspinner.getSelectedIndex()),new Callback<ResponseUpdateProfileImage>() {
                    @Override
                    public void onResponse(Call<ResponseUpdateProfileImage> call, Response<ResponseUpdateProfileImage> response) {
                        Toast.makeText(getApplicationContext(), response.body().getDetails()+"", Toast.LENGTH_LONG).show();
                        generatedCode.setText(response.body().getImageURL().toString());
                        linearCode.setVisibility(View.VISIBLE);
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);
                    }

                    @Override
                    public void onFailure(Call<ResponseUpdateProfileImage> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                        DialogUtils.showDialog(AddHrRequestActivity.this,false);

                    }
                });


            }else {
                SuperToast.create(getApplicationContext(), getString(R.string.select_reason), 3000).show();
                DialogUtils.showDialog(AddHrRequestActivity.this,false);

            }
        }else {
            DialogUtils.showDialog(AddHrRequestActivity.this,false);
        }

    }

    //endregion



    //region selecct Image
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
                        ActivityCompat.requestPermissions(AddHrRequestActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_CAMERA);
                } else if (items[item].equals(getString(R.string.open_gallery))) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        ImagesHandler.getInstance().galleryIntent(context);
                    else
                        ActivityCompat.requestPermissions(AddHrRequestActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_STORAGE);

                }
            }
        });
        builder.show();
    }


    //endregion



    //region select Image on Activity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isCorrectImage = false;
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IntentManager.KEY_PICK_IMAGE) {
                imageUri =data.getData();
                if (imageUri != null) {
                    imageurl.setText(imageUri.toString());
                    optionlinear.setVisibility(View.VISIBLE);
                    printlinear.setVisibility(View.VISIBLE);
                    linearprintcard.setVisibility(View.VISIBLE);

                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);
                    tvstart_date.setText(start_date);
                    tvend_date.setText(end_date);
                }



            } else if (requestCode == ImagesHandler.CAMERA_INTENT) {
                imageUri =mCapturedImageURI;
                if (imageUri != null) {
                    imageurl.setText(imageUri.toString());
                    optionlinear.setVisibility(View.VISIBLE);
                    printlinear.setVisibility(View.VISIBLE);
                    linearprintcard.setVisibility(View.VISIBLE);
                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);
                    tvstart_date.setText(start_date);
                    tvend_date.setText(end_date);

                }

            }
        }
    }

    //endregion



    private void initView() {

        swipeBackView.setOnSwipeListener(this);
        mcategory.setItems(option_list);
        reasonspinner.setItems(reason_list);
//region select Category

        mcategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int position, long l, Object o) {
                if(position ==2){//reopen Card
                    linearprintcard.setVisibility(View.GONE);
                    linearreopen.setVisibility(View.VISIBLE);
                    linearCode.setVisibility(View.GONE);
                    nameLinear.setVisibility(View.GONE);
                    blockedit3.setEnabled(false);
                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);
                }else if(position == 3){ // change Card number
                    linearprintcard.setVisibility(View.GONE);
                    linearreopen.setVisibility(View.GONE);
                    linearCode.setVisibility(View.GONE);
                    nameLinear.setVisibility(View.GONE);
                    blockedit3.setEnabled(true);
                    blockedit3.requestFocus();
                    showSoftKeyboard();
                    blockedit3.setTextColor(getResources().getColor(R.color.colorAccentRed));



                }else if(position == 1){ //reprint Card
                    nameLinear.setVisibility(View.GONE);
                    linearprintcard.setVisibility(View.VISIBLE);
                    linearreopen.setVisibility(View.GONE);
                    linearCode.setVisibility(View.GONE);
                    blockedit3.setEnabled(false);
                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);



                }else if(position == 0){ // no thing selected
                    nameLinear.setVisibility(View.GONE);
                    linearprintcard.setVisibility(View.GONE);
                    linearreopen.setVisibility(View.GONE);
                    linearCode.setVisibility(View.GONE);
                    blockedit3.setEnabled(false);
                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);

                }else if (position==4){
                    nameLinear.setVisibility(View.VISIBLE);
                    linearprintcard.setVisibility(View.GONE);
                    linearreopen.setVisibility(View.GONE);
                    linearCode.setVisibility(View.GONE);
                    blockedit3.setEnabled(false);
                    blockedit1.setText(block1st);
                    blockedit2.setText(block2st);
                    blockedit3.setText(block3st);
                    blockedit4.setText(block4st);

                }
            }
        });


        //resion for reprint card
        reasonspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int position, long l, Object o) {
                if(position== 0){ //nothing selected
                    reasonimage.setVisibility(View.GONE);

                }else if(position == 1){//
                    reasonimage.setVisibility(View.GONE);

                }else if(position == 2){//
                    reasonimage.setVisibility(View.VISIBLE);
                }

            }
        });


        reasonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(AddHrRequestActivity.this);
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mcategory.getSelectedIndex() == 0){

                }else if(mcategory.getSelectedIndex() ==1){
                    sendRequestprint();
                }else if(mcategory.getSelectedIndex() == 2){
                    sendRequestReopen();

                }else if(mcategory.getSelectedIndex() == 3){
                    sendRequestChange();
                }else if (mcategory.getSelectedIndex()==4){

                  sendRequestChangeName();
                }

            }
        });



    }


    //region swipBack
    @Override
    public boolean canSwipeBack(int dir) {
        return true;
    }

    @Override
    public void onSwipeProcess(float percent) {
        container.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));

    }

    @Override
    public void onSwipeFinish(int dir) {
        finishActivity(dir);
    }

    @OnClick({R.id.search_id})
    public void HrEditCardOnClick(View view){
        switch (view.getId()){
            case R.id.search_id:
                SearchUserByID();

                break;
        }

    }
//endregion





    //finish Activity
    @Override
    public void finishActivity(int dir) {
        SwipeBackCoordinatorLayout.hideBackgroundShadow(container);
        onBackPressed();

    }


    //region search by Card Id for User
    public void SearchUserByID(){

        //region Validation
        if (block1.getText().toString().isEmpty()){
            block1.setError(getString(R.string.err_empty_field));
            block1.requestFocus();
            return;
        }

        if (block2.getText().toString().isEmpty()){
            block2.setError(getString(R.string.err_empty_field));
            block2.requestFocus();
            return;
        }

        if (block3.getText().toString().isEmpty()){
            block3.setError(getString(R.string.err_empty_field));
            block3.requestFocus();
            return;
        }

        if (block4.getText().toString().isEmpty()){
            block4.setError(getString(R.string.err_empty_field));
            block4.requestFocus();
            return;
        }

        //endregion


        //region Store values at the time of the Search User.
        final String CardId=block1.getText().toString().trim()+"-"+block2.getText().toString().trim()+"-"+block3.getText().toString().trim()+"-"+block4.getText().toString().trim();
        //      User user = App.getInstance().getPrefManager().getCurrentUser();
        String UserPassword= App.getInstance().getPrefManager().getPassword();
        final String UserEmail=App.getInstance().getPrefManager().getUsername();
        //endregion

        // cancel for validation
        boolean cancel = false;
        // View focusView = null;
        if(cancel){
            // There was an error; don't attempt login and focus the first
            // form field with an error.

        }else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .content(R.string.msg_loading)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            //   String version = "android";
            //  String device = "sam" ;
            call=App.getInstance().getService().searchByCardId(CardId,UserEmail,UserPassword,getAppLanguage());
            call.enqueue(new Callback<ResponseMembers>() {
                @Override
                public void onResponse(Call<ResponseMembers> call, Response<ResponseMembers> response) {

                    if(response.body() != null && response.body().getMessage() != null) {
                        Message message = response.body().getMessage();
                        if (message.getCode() != 1) {
                            Snackbar.make(findViewById(android.R.id.content),response.body().getMessage().getDetails().toString(),Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content),message.getDetails().toString(),Snackbar.LENGTH_SHORT).show();
                            optionlinear.setVisibility(View.VISIBLE);
                            printlinear.setVisibility(View.VISIBLE);
                            User user1 = response.body().getUser();
                            cardId=user1.getCardId();
                            start_date=user1.getIns_start_date();
                            end_date=user1.getIns_end_date();
                            String[] splited=user1.getCardId().split("-");
                            block1st=splited[0];
                            block2st=splited[1];
                            block3st=splited[2];
                            block4st=splited[3];
                            blockedit1.setText(block1st);
                            blockedit2.setText(block2st);
                            blockedit3.setText(block3st);
                            blockedit4.setText(block4st);
                            String[]splitedStartDate=start_date.split("\\s");
                            String[]splitedEndDate=end_date.split("\\s");
                            tvstart_date.setText(splitedStartDate[0]);
                            tvend_date.setText(splitedEndDate[0]);
                            tv_name.setText(user1.getFirstName()+" "+user1.getSecondName()+" "+user1.getLastName());

                            dialog.dismiss();

                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseMembers> call, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            //    dialog.dismiss();

        }




    }

    //endregion


    //distroy CallBack to avoid leak Memory
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null)
            call.cancel();

    }
}
