package com.dmsegypt.dms.ux.activity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Item;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.dmsegypt.dms.utils.utils.isEmailValid;
import static com.dmsegypt.dms.utils.utils.isMobileNumberValid;


public class AddMemberActivity extends BaseActivity
        implements SwipeBackCoordinatorLayout.OnSwipeListener {

    //region Signup Views
    @BindView(R.id.tv_add_title)
    TextView tvAddTitle;
    String message="";
    @BindView(R.id.root)
    View rootLayout;



    @BindView(R.id.rowtinputRelationCard)
    TableRow rowtinputRelationCard;

    @BindView(R.id.tinputRelationCard)
    TextInputLayout tinputRelationCard;

    @BindView(R.id.tvAddress)
    EditText etaddress;

    @BindView(R.id.tinputAddress)
    TextInputLayout tinputAddress;

    @BindView(R.id.tinputFullName)
    TextInputLayout tinputFullName;

    @BindView(R.id.tinputFullNamearab)
    TextInputLayout tinputFullNamearab;

    @BindView(R.id.tinputNationalID)
    TextInputLayout tinputNationalID;

    @BindView(R.id.iBtnGuide)
    ImageButton iBtnGuide;

    @BindView(R.id.csspinnerrelation)
    LinearLayout csspinnerrelation;

    @BindView(R.id.tinputBirthDate)
    TextInputLayout tinputBirthDate;

    @BindView(R.id.tinputMobileNumber)
    TextInputLayout tinputMobileNumber;

    @BindView(R.id.tinputConfirmMobileNumber)
    TextInputLayout tinputConfirmMobileNumber;

    @BindView(R.id.tinputSignupEmail)
    TextInputLayout tinputSignupEmail;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.etFullNamearab)
    EditText etFullNamearab;

    @BindView(R.id.etNationalID)
    EditText etNationalID;

    @BindView(R.id.etSignupCardID)
    EditText etSignupCardID;

    @BindView(R.id.etBirthDate)
    EditText etBirthDate;

    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.etConfirmMobileNumber)
    EditText etConfirmMobileNumber;

    @BindView(R.id.etSignupEmail)
    AutoCompleteTextView etSignupEmail;

    @BindView(R.id.etRelationCard)
    EditText etRelationCard;


    @BindView(R.id.btn_add)
    Button btnAdd;

    //endregion


    final int PLACE_PICKER_REQUEST=1;


    //region Values references.
    @BindInt(R.integer.loginImeActionID)
    int loginImeActionID;
    @BindInt(R.integer.registerImeActionID)
    int registerImeActionID;
    //region spinner view
    @BindView(R.id.degreespinner)
    MaterialSpinner degreespinner;
    @BindView(R.id.genderspinner)
    MaterialSpinner genderspinner;
    @BindView(R.id.csspinner)
    MaterialSpinner csspinner;
    @BindArray(R.array.gender_list)
      String[]genderList;
    @BindArray(R.array.family_member_list)
            String[]familymemberList;
    //endregion
    private static final String SHOWCASE_ID = "addMemberHelper";

    int type;
    boolean user_type;
    String customerservice;

    private View mFormView;
    private int count = 0;
    private String TAG = this.getClass().getSimpleName();
    private boolean isloaded;
    Call call;
    AnimUtils a;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        startRevealAnimation();
        initview();

    }

    private void startRevealAnimation(){
        Intent intent = this.getIntent();
        a= new AnimUtils(rootLayout, intent, this, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                user_type=App.getInstance().getPrefManager().getUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_NORMAL);
                if(user_type){
                    csspinnerrelation.setVisibility(View.GONE);
                    customerservice="Normal User";

                }else {
                    if(Constants.CustomerServiceList == null) {
                        getCustomerService();

                    }
                }
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
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_add_member;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.add_new_member;
    }

    @OnClick(R.id.iBtnCloseBtn)
     void close(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        }else {
            Intent intent = new Intent(this, EmployeesActivity.class);
            ActivityOptions options=ActivityOptions.makeCustomAnimation(this,R.anim.slide_in_right,R.anim.slide_out_left);
            this.startActivity(intent,options.toBundle());
            finish();
        }
     }


    private void getCustomerService() {
        DialogUtils.showDialog(this,true);

        call=App.getInstance().getService().getcustomerservice(App.getInstance().getPrefManager().getUsername(),getAppLanguage());
                call.enqueue(new GetCustomServiceCallback(this,csspinner));


    }

    public void initview(){

        SharedPreferences prefs = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE);
        if(prefs.getString("Enable","No name defined").equals("showed")){
            iBtnGuide.setVisibility(View.GONE);

        }


        type = getIntent().getIntExtra(Constants.KEY_ADD_TYPE, Constants.KEY_TYPE_MEMBER);

        if (type == Constants.KEY_TYPE_EMPLOYEE) {
            etSignupCardID.setVisibility(View.GONE);
            tvAddTitle.setText(R.string.add_new_employees);
            btnAdd.setText(R.string.add_new_employees);

        }

        genderspinner.setItems(genderList);
        degreespinner.setItems(familymemberList);
        degreespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                if(i != 0){
                    tinputRelationCard.setVisibility(View.VISIBLE);
                    rowtinputRelationCard.setVisibility(View.VISIBLE);
                }else {
                    tinputRelationCard.setVisibility(View.GONE);
                    rowtinputRelationCard.setVisibility(View.GONE);


                }
            }
        });




    }


    @OnTouch(R.id.tvAddress)
        public boolean onAddressTouched(View v, MotionEvent event) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (etaddress.getRight() -    etaddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                    Intent intent;
                    try {
                        intent=builder.build(AddMemberActivity.this);
                        startActivityForResult(intent,PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            return false;
        }





    @Override
    protected void onResume() {
        super.onResume();


        if(Constants.CustomerServiceList != null){
            if(Constants.CustomerServiceList.size() != 0){
                csspinner.setItems(Constants.CustomerServiceList);

            }
        }
    }

    @Override
    public void onBackPressed() {
    a.unRevealActivity();


    }

    @OnTextChanged(R.id.etFullName)
    public void handleFullNameTextChange(Editable editable) {
        tinputFullName.setError(null);
    }

    @OnTextChanged(R.id.tvAddress)
    public void handleAddressTextChange(Editable editable){
        tinputAddress.setError(null);
    }

    @OnTextChanged(R.id.etNationalID)
    public void handleNationalIDTextChange(Editable editable) {
        tinputNationalID.setError(null);
    }

    @OnTextChanged(R.id.etMobileNumber)
    public void handleMobileNumberTextChange(Editable editable) {
        tinputMobileNumber.setError(null);
    }

    @OnTextChanged(R.id.etSignupEmail)
    public void handleSignupEmailTextChange(Editable editable) {
        tinputSignupEmail.setError(null);
    }


    //endregion

    //region OnEditorAction handle
    @OnEditorAction(R.id.etSignupEmail)
    public boolean onEditorActionSignin(TextView textView, int id, KeyEvent keyEvent) {
        int imeActionID = textView.getImeActionId();
        if (imeActionID == loginImeActionID && id == EditorInfo.IME_ACTION_DONE) {
            addMember(type);
            return true;
        }
        return false;
    }

    //endregion

    //region onClick handle
    @OnClick({R.id.btn_add ,R.id.iBtnGuide})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                addMember(type);
                break;
            case R.id.iBtnGuide:
                presentShowcaseView();
                break;


        }

    }

    private void presentShowcaseView() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(0); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(iBtnGuide)
                        .setDismissText( getString(R.string.next))
                        .setContentText(getString(R.string.welcomeguide))
                        .withCircleShape()
                        .setDismissOnTouch(true)
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tinputFullName)
                        .setDismissText( getString(R.string.next))
                        .setContentText(getString(R.string.enteryourfullname))
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tinputFullNamearab)
                        .setDismissText( getString(R.string.next))
                        .setContentText(getString(R.string.enteryourfullnameArabic))
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(degreespinner)
                        .setDismissText( getString(R.string.next))
                        .setContentText(getString(R.string.degreecondition))
                        .useFadeAnimation()
                        .setDismissOnTouch(true)
                        .withRectangleShape()
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(tinputAddress)
                        .setDismissText( getString(R.string.next))
                        .setContentText(getString(R.string.AddressHelper))
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(csspinner)
                        .setDismissText( getString(R.string.finish))
                        .setContentText(getString(R.string.costcenterhelper))
                        .withRectangleShape()
                        .setDismissOnTouch(true)
                        .setContentTextColor(getResources().getColor(R.color.mdtp_white))
                        .setMaskColour(getResources().getColor(R.color.colormaintransparent))
                        .build()
        );


        sequence.start();


        SharedPreferences.Editor editor = getSharedPreferences(SHOWCASE_ID, MODE_PRIVATE).edit();
        editor.putString("Enable","showed");
        editor.apply();
        iBtnGuide.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PLACE_PICKER_REQUEST:
                if(resultCode ==RESULT_OK){
                    Place place = PlacePicker.getPlace(this,data);
                    String address=place.getAddress().toString();
                    etaddress.setText(""+address);
                }
        }

    }

    @Override
    public boolean canSwipeBack(int dir) {
        return false;
    }

    @Override
    public void onSwipeProcess(float percent) {

    }

    @Override
    public void onSwipeFinish(int dir) {
        switch (dir) {
            case SwipeBackCoordinatorLayout.UP_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_top);
                break;

            case SwipeBackCoordinatorLayout.DOWN_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                break;
        }
    }
    //endregion

    @OnFocusChange(R.id.etBirthDate)
    void datePickerFocus(boolean hasFocus) {
        if (hasFocus) datePicker();
    }

    @OnClick({R.id.tinputBirthDate, R.id.etBirthDate})
    void datePicker() {

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
                        if(nowyear > year ){
                            etBirthDate.setText(newDate);
                            etBirthDate.setError(null);
                        }else if(nowyear == year) {
                            if(nowmonth > monthOfYear){
                                etBirthDate.setText(newDate);
                                etBirthDate.setError(null);
                            }else if (nowmonth == monthOfYear) {
                                if(nowday > dayOfMonth){
                                    etBirthDate.setText(newDate);
                                    etBirthDate.setError(null);
                                }else {
                                    etBirthDate.setError(getString(R.string.invalid_date));
                                }
                            }else {
                                etBirthDate.setError(getString(R.string.invalid_date));
                            }
                        }else {
                            etBirthDate.setError(getString(R.string.invalid_date));
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

    }

    /**
     * Attempts to add member or add employee.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual add member attempt is made.
     *
     * @param type
     */
    private void addMember(int type) {

        //region Store values at the time of add member attempt.
        String fullName = etFullName.getText().toString().trim();
        String nationalID = etNationalID.getText().toString().trim();
       // String cardID = etSignupCardID.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String mobileNumberConfirm = etConfirmMobileNumber.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String address=etaddress.getText().toString().trim();
        String RelationCardId=etRelationCard.getText().toString().trim();
        if (RelationCardId.isEmpty()){
            RelationCardId="0";
        }
        String gender=Constants.Gender.get(genderspinner.getSelectedIndex()).toString();
        String degree=Constants.FamilyMember.get(degreespinner.getSelectedIndex()).toString();
        if(csspinnerrelation.getVisibility() == View.VISIBLE) {
            if (!Constants.CustomerServiceList.isEmpty())
            customerservice = Constants.CustomerServiceList.get(csspinner.getSelectedIndex()).getName();
        }
        String CreatedBy=App.getInstance().getPrefManager().getCurrentUser().getCardId();
        String fullNamearab=etFullNamearab.getText().toString().trim();
        //endregion


        boolean cancel = false;
        View focusView = null;
        if(Constants.CustomerServiceList != null) {
            if (Constants.CustomerServiceList.size() == 0) {
                Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.msg_reolad, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCustomerService();
                    }
                }).setActionTextColor(Color.GREEN).show();
                return;
            }
        }
        //region Check for valid Request Type
        if(csspinnerrelation.getVisibility() == View.VISIBLE) {
            if (csspinner.getSelectedIndex() == 0) {
                Snackbar.make(findViewById(android.R.id.content), R.string.error_not_select_requets, Snackbar.LENGTH_LONG).show();
                focusView = csspinner;
                cancel = true;
            }
        }
        //endregion

        //region Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            tinputSignupEmail.setError(getString(R.string.error_field_required));
            focusView = etSignupEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tinputSignupEmail.setError(getString(R.string.error_invalid_email));
            focusView = etSignupEmail;
            cancel = true;
        }

        if(TextUtils.isEmpty(address)){
            tinputAddress.setError(getString(R.string.error_field_required));
            focusView=etaddress;
            cancel=true;
        }
        //endregion

        //region  Check for a valid mobile number confirmation.
        if (TextUtils.isEmpty(mobileNumberConfirm)) {
            tinputConfirmMobileNumber.setError(getString(R.string.error_field_required));
            focusView = etConfirmMobileNumber;
            cancel = true;
        } else if (!mobileNumberConfirm.equals(mobileNumber)) {
            tinputConfirmMobileNumber.setError(getString(R.string.error_not_match_mobile));
            focusView = etConfirmMobileNumber;
            cancel = true;
        }
        //endregion

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

        //region Check for a valid BirthDate.
        if (TextUtils.isEmpty(birthDate)) {
            tinputBirthDate.setError(getString(R.string.error_field_required));
            focusView = etBirthDate;
            cancel = true;
        }
        //endregion

        //region Check for a valid card ID.
        if (this.type == Constants.KEY_TYPE_MEMBER) {

        }
        //endregion

        //region Check for a valid national ID.
        if (TextUtils.isEmpty(nationalID)) {
            tinputNationalID.setError(getString(R.string.error_field_required));
            focusView = etNationalID;
            cancel = true;
        } else if (14 != nationalID.length()) {
            tinputNationalID.setError(getString(R.string.error_invalid_nationalID));
            focusView = etNationalID;
            cancel = true;
        }
        //endregion

        //region Check for a valid fullName.
        if (TextUtils.isEmpty(fullName)) {
            tinputFullName.setError(getString(R.string.error_field_required));
            focusView = etFullName;
            cancel = true;
        } else if (fullName.split(" ").length < 3) {
            tinputFullName.setError(getString(R.string.error_invalid_fullname));
            focusView = etFullName;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullNamearab)) {
            etFullNamearab.setError(getString(R.string.error_field_required));
            focusView = etFullNamearab;
            cancel = true;
        } else if (fullName.split(" ").length < 3) {
            etFullNamearab.setError(getString(R.string.error_invalid_fullname));
            focusView = etFullNamearab;
            cancel = true;
        }

        //endregion

        if (cancel) {
            // There was an error; don't attempt send data and focus the first
            // form field with an error.
            if (focusView == etBirthDate) datePicker();
            else focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform sne request attempt.
            DialogUtils.showDialog(AddMemberActivity.this,true);
            if (type == Constants.KEY_TYPE_MEMBER) {
                  // region add Employee Request
                call=App.getInstance().getService().addEmployeeRequest("4",fullName, nationalID,
                        birthDate.replaceAll("/", "-"),gender,degree,customerservice,email ,address,mobileNumber
                        ,App.getInstance().getPrefManager().getUser().getCardId(),"1","m", getAppLanguage(),CreatedBy,fullNamearab);
                        call.enqueue(new AddEmployeeRequestCallBack(this));
                //endregion

            } else {
                //region add Member Rquest
                call=App.getInstance().getService().addEmployeeRequest("5",fullName, nationalID,birthDate.replaceAll("/", "-"),gender,
                        degree,
                        customerservice,email,address,mobileNumber,
                        RelationCardId,"1","m"
                        ,getAppLanguage(),CreatedBy,fullNamearab);
                        call.enqueue(new AddEmployeeRequestCallBack(this));
                //endregion


            }
        }
    }
//endregion

    private static class AddEmployeeRequestCallBack implements Callback<StatusResponse> {
        WeakReference<AppCompatActivity>activity;

        public AddEmployeeRequestCallBack(AppCompatActivity activity) {
            this.activity =new WeakReference<AppCompatActivity>(activity);
        }

        @Override
        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

            AppCompatActivity context=activity.get();
            DialogUtils.showDialog(context,false);

            if (response != null && response.body() != null) {


                if (response.body().getCode() != null && response.body().getCode() == 1) {
                    Snackbar.make(context.findViewById(android.R.id.content), R.string.msg_request_sent, Snackbar.LENGTH_LONG).show();

                }

            }else {
                DialogUtils.showDialog(context,false);
                Snackbar.make(context.findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                        .show();

            }

        }
        @Override
        public void onFailure(Call<StatusResponse> call, Throwable t) {
            //  Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            AppCompatActivity context=activity.get();
            DialogUtils.showDialog(context,false);
            Snackbar.make(context.findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                    .show();
        }



    }
private  static  class GetCustomServiceCallback implements Callback<ResponseItem> {
    WeakReference<AddMemberActivity>activity;
    WeakReference<MaterialSpinner>spinner;

    public GetCustomServiceCallback(AddMemberActivity activity, MaterialSpinner spinner) {
        this.activity = new WeakReference<AddMemberActivity>(activity);
        this.spinner = new WeakReference<MaterialSpinner>(spinner);
    }

    @Override
        public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                MaterialSpinner csspinner=this.spinner.get();
               AddMemberActivity activity=this.activity.get();
        DialogUtils.showDialog(activity,false);
        if(response != null&&response.body()!=null){
                Message message = response.body().getMessage();
                if (Constants.CustomerServiceList == null)
                    Constants.CustomerServiceList = new ArrayList<Item>();
                Constants.CustomerServiceList.clear();

                if (message.getCode() != 1){

                }
                else {
                    Constants.CustomerServiceList.add(new Item("0",activity.getString(R.string.cost_center)));
                    Constants.CustomerServiceList.addAll(response.body().getList());
                    csspinner.setItems(Constants.CustomerServiceList);
                }



            }
        }

        @Override
        public void onFailure(Call<ResponseItem> call, Throwable throwable) {
            final AddMemberActivity activity=this.activity.get();
            DialogUtils.showDialog(activity,false);
            Snackbar.make(activity.findViewById(android.R.id.content),R.string.err_data_load_failed,Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.msg_reolad, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            activity.getCustomerService();
                        }
                    }).setActionTextColor(Color.GREEN).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call!=null){
            call.cancel();

        }
    }
}


