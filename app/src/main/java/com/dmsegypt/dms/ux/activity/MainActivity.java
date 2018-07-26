package com.dmsegypt.dms.ux.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseChangePassword;
import com.dmsegypt.dms.utils.AnimUtils;
import com.dmsegypt.dms.utils.DataUtils;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.utils.NotificationUtils;
import com.dmsegypt.dms.utils.Uploader;
import com.dmsegypt.dms.ux.Fragment.ProfileFragment;
import com.dmsegypt.dms.ux.custom_view.CircleImageView;
import com.dmsegypt.dms.ux.custom_view.CropCircleTransform;
import com.dmsegypt.dms.ux.custom_view.SwipeBackCoordinatorLayout;
import com.dmsegypt.dms.ux.custom_view.coordinatorView.StatusBarView;
import com.dmsegypt.dms.ux.dialogs.SwitchUserDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmsegypt.dms.utils.utils.meetLengthValidation;
import static com.dmsegypt.dms.utils.utils.playAnimation;
import static com.dmsegypt.dms.utils.utils.promptToClose;

/**
 * Created by Mohamed Abdallah on 1/03/2017.
 **/

// First Update At BitBucket
// the second one

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Menu nav_Menu;
    //region Views binding
    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;
    @BindView(R.id.llHeaderContainer)
    LinearLayout llHeaderContainer;
    @BindView(R.id.flMainContent)
    FrameLayout flMainContent;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    LinearLayout llHomeHeader;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.fragment_home_appBar)
    AppBarLayout appBar;
    @BindView(R.id.mprogress)
    ProgressBar mprogress;
    @BindView(R.id.toolbar_full_user_name)
    TextView username;
    @BindView(R.id.iBtnCloseBtn)
    ImageView closeButton;
    @BindView(R.id.linearcontact)
    LinearLayout linearcontact;

    @BindView(R.id.user_image)
    CircleImageView userimage;


    @BindView(R.id.tv_number)
    TextView tv_number;

    ObjectAnimator objectAnimator;
    //region changePassword Views
    @BindView(R.id.changePassSwipeBackView)
    SwipeBackCoordinatorLayout changePassSwipeBackView;
    @BindView(R.id.layoutChangePassContainer)
    CoordinatorLayout layoutChangePassContainer;
    @BindView(R.id.etOldPass)
    EditText etOldPass;
    @BindView(R.id.etNewPass)
    EditText etNewPass;
    @BindView(R.id.changePassStatusBar)
    StatusBarView changePassStatusBar;
    @BindView(R.id.tinputOldPass)
    TextInputLayout tinputOldPass;
    @BindView(R.id.tinputNewPass)
    TextInputLayout tinputNewPass;
    //endregion




    private @SuppressLint("InflateParams") View view;
    Call call;
    private TextView notifCountTv;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        NotificationUtils.showNotificationAlert(this);
        //region Introduction Helper view
        if (App.getInstance().getPrefManager().isLoggedIn()&&!(App.getInstance().getPrefManager().isUpdateViewed() == 1)){
            App.getInstance().getPrefManager().setViewUpdate();
            IntentManager.startActivity(this, IntentManager.ACTIVITY_VIEW_UPDATE);
            finish();
            return;
        }
        //endregion


        //region check login
        if (!App.getInstance().getPrefManager().isLoggedIn()) {
            IntentManager.startActivityAndFinishMe(this, IntentManager.ACTIVITY_AUTH);
        }
        //endregion

        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_PROVIDER))
        {

            if (! App.getInstance().getPrefManager().getProviderFirstLogin())
            {
              handleChangePassOpen();
                App.getInstance().getPrefManager().setProviderFirstLogin();//set to true
                closeButton.setVisibility(View.GONE);
            }
        }




        initView();
    }

    @OnClick(R.id.btnSubmit)
    public void ChangePassOnClick(View view) {
        switch (view.getId()) {

            case R.id.btnSubmit:
                boolean cancel = false;
                View focusView = null;

                String oldPass = etOldPass.getText().toString().trim();
                String newPass = etNewPass.getText().toString().trim();

                //region Check for a valid new_image password, if the user entered one.
                if (TextUtils.isEmpty(newPass)) {
                    tinputNewPass.setError(getString(R.string.error_field_required));
                    focusView = etNewPass;
                    cancel = true;
                } else {
                    switch (meetLengthValidation(newPass)) {
                        case 0:
                            tinputNewPass.setError(getString(R.string.error_invalid_short_password));
                            focusView = etNewPass;
                            cancel = true;
                            break;
                        case 1:
                            tinputNewPass.setError(getString(R.string.error_invalid_long_password));
                            focusView = etNewPass;
                            cancel = true;
                            break;
                    }
                }
                //endregion
                //region Check for a valid old password, if the user entered one.
                if (TextUtils.isEmpty(oldPass)) {
                    tinputOldPass.setError(getString(R.string.error_field_required));
                    focusView = etOldPass;
                    cancel = true;
                } else {
                    switch (meetLengthValidation(oldPass)) {
                        case 0:
                            tinputOldPass.setError(getString(R.string.error_invalid_short_password));
                            focusView = etOldPass;
                            cancel = true;
                            break;
                        case 1:
                            tinputOldPass.setError(getString(R.string.error_invalid_long_password));
                            focusView = etOldPass;
                            cancel = true;
                            break;
                    }
                }
                //endregion

                if (cancel) focusView.requestFocus();
                else {
                    call= App.getInstance().getService().changePassword(App.getInstance().getPrefManager().getUser().getCardId(),
                            oldPass, newPass,getAppLanguage() );
                    call.enqueue(new Callback<ResponseChangePassword>() {
                        @Override
                        public void onResponse(Call<ResponseChangePassword> call, Response<ResponseChangePassword> response) {
                            if (response.body() != null) {
                                Message message = response.body();
                                if (message.getCode() == 1) {
                                    new MaterialDialog.Builder(MainActivity.this)
                                            .content(getString(R.string.password_changed_success))
                                            .positiveText(getString(R.string.Ok))
                                            .onPositive(null)
                                            .negativeText(null)
                                            .onNegative(null)
                                            .cancelable(true)
                                            .show();

                                    App.getInstance().getPrefManager().makeLogout();
                                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                } else {
                                    new MaterialDialog.Builder(MainActivity.this)
                                            .content(message.getDetails())
                                            .positiveText(getString(R.string.Ok))
                                            .onPositive(null)
                                            .negativeText(null)
                                            .onNegative(null)
                                            .cancelable(true)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseChangePassword> call, Throwable t) {
                            Log.e("Api", "API ForgetPassword error : " + t.toString());
                            new MaterialDialog.Builder(MainActivity.this)
                                    .content(getString(R.string.unknown_error))
                                    .positiveText("Ok")
                                    .onPositive(null)
                                    .negativeText(null)
                                    .onNegative(null)
                                    .cancelable(true)
                                    .show();
                        }
                    });
                }
                break;
        }
    }

    private void handleChangePassOpen() {
//        Toast.makeText(this, "pass  clicked", Toast.LENGTH_SHORT).show();
        changePassSwipeBackView.setOnSwipeListener(new SwipeBackCoordinatorLayout.OnSwipeListener() {
            @Override
            public boolean canSwipeBack(int dir) {
                return layoutChangePassContainer.getVisibility() == View.VISIBLE;
            }

            @Override
            public void onSwipeProcess(float percent) {
                changePassStatusBar.setAlpha(1 - percent);
                layoutChangePassContainer.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent));
            }

            @Override
            public void onSwipeFinish(int dir) {
                handleChangePassClose(dir);
            }
        });
        etOldPass.setText("");
        etNewPass.setText("");
        etOldPass.requestFocus();
        layoutChangePassContainer.setVisibility(View.VISIBLE);
        playAnimation(this, layoutChangePassContainer, R.anim.activity_in);
    }

    private void handleChangePassClose(int dir) {
        switch (dir) {
            case SwipeBackCoordinatorLayout.UP_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_top);
                break;

            case SwipeBackCoordinatorLayout.DOWN_DIR:
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                break;
        }
        layoutChangePassContainer.setVisibility(View.GONE);
        changePassSwipeBackView.setOnSwipeListener(null);
    }




    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    //region toolbar declatation
    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_main;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    //endregion




    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter(Constants.ACTION_UPDATE_LANGUAGE);
        registerReceiver(updateLanguageReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(updateLanguageReceiver);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isTaskRoot()) promptToClose(this);
        else {
            promptToClose(this);
        }
    }

    //update language
    BroadcastReceiver updateLanguageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_UPDATE_LANGUAGE)) {
                recreate();
            }
        }
    };

    //region Init
    private void initView() {


        objectAnimator=ObjectAnimator.ofFloat(tv_number,"x",50f);
        objectAnimator.setDuration(4000);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.reverse();
        objectAnimator.start();
        linearcontact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+201205566050"));
                startActivity(intent);
                return false;
            }
        });

        AnimUtils.animProfileImage(userimage);
        setSupportActionBar(mainToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimUtils.animProfileImage(userimage);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(this);
        notifCountTv=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.action_notifications));
        initUserData(App.getInstance().getPrefManager().getCurrentUser().getUserType());
        ViewProfileInit();
    }

    void updateNotificationCount(){
        int count=  App.getInstance().getPrefManager().getNotificationCount();
        if (count==0){
            notifCountTv.setVisibility(View.GONE);
        }else {
            notifCountTv.setVisibility(View.VISIBLE);
            SpannableString sColored = new SpannableString(" "+count+"+ ");
            sColored.setSpan(new BackgroundColorSpan( Color.RED ),0,sColored.length(),0);
            sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), 0,sColored.length(),0);
            notifCountTv.setText(sColored);
            notifCountTv.setGravity(Gravity.CENTER_VERTICAL);
            notifCountTv.setTypeface(null, Typeface.BOLD);
        }



    }

    private void ViewProfileInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setElevation(0);
        }
        Fragment fragment = new ProfileFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flMainContent, fragment)
                .commit();
    }

    private void initUserData(String usertype) {
        if (!App.getInstance().getPrefManager().isLoggedIn()) return;

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.TvNavUsername))
                .setText(App.getInstance().getPrefManager().getUsername());
        setLoggedMessage((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_type),usertype);
        username.setText(getString(R.string.label_welcome)+" "+App.getInstance().getPrefManager().getUsername());
        String cardId="";
        if(App.getInstance().getPrefManager().getCurrentUser().getCardId().toString() != null) {
            cardId = App.getInstance().getPrefManager().getCurrentUser().getCardId().toString();
        }
        String image_url=Constants.BASE_IMAGES_URL+ Uploader.PROFILE_FOLDER+"/"+cardId+".jpg";

       //    call=App.getInstance().getService().getImagebase54(cardId,Constants.PROFILE_TYPE_PATH);
     //     call.enqueue(new_image ImageProfileCallBack(userimage,mprogress));



        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_NORMAL))
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_normal_user);
        }
        else if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS))
        {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_drawer_dms);

        }

        else if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_HR))
        {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_drawer_hr);

        }
        else if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER))
        {
            navigationView.getMenu().clear();

            navigationView.inflateMenu(R.menu.activity_main_drawer_provider);
            /*if (App.getInstance().getPrefManager().getCurrentUser().getPrvType().equals(Constants.PHARMCY_TYPE)){
              */
            navigationView.getMenu().findItem(R.id.action_orders).setVisible(true);
            /*}else {
                navigationView.getMenu().findItem(R.id.action_orders).setVisible(false);

            }*/

        }


        /********************/
        nav_Menu = navigationView.getMenu();
        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS)) {
            //todo approval request edit num 9
            if (App.getInstance().getPrefManager().getCurrentUser().getDepartmentId().equalsIgnoreCase(Constants.USER_TYPE_DMS_MEDICAL)) {
                nav_Menu.findItem(R.id.action_requests).setVisible(true);
            } else {
                nav_Menu.findItem(R.id.action_requests).setVisible(false);
            }
        }



    }



    @Override
    protected void onResume() {
        super.onResume();

        String cardId=App.getInstance().getPrefManager().getCurrentUser().getCardId().toString();
        String image_url=Constants.BASE_IMAGES_URL+Uploader.PROFILE_FOLDER+"/"+cardId+".jpg";
        //   call=App.getInstance().getService().getImagebase54(cardId,Constants.PROFILE_TYPE_PATH);
        //   call.enqueue(new_image ImageProfileCallBack(userimage,mprogress));
        SharedPreferences prefs = getSharedPreferences("ImageUpdated", MODE_PRIVATE);
        String state = null;
        if(prefs != null){
            state=prefs.getString("state","No name defined");
        }
        if(state.equals("true") && state != null) {
            Glide.with(this).load(image_url).asBitmap().transform(new CropCircleTransform(getApplicationContext())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.userprofile).centerCrop()
                    .error(R.drawable.userprofile).into(userimage);
            final SharedPreferences.Editor editor = getSharedPreferences("ImageUpdated", MODE_PRIVATE).edit();
            editor.putString("state","false");
            editor.apply();


        }else {
            Glide.with(this).load(image_url).asBitmap().transform(new CropCircleTransform(getApplicationContext())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.userprofile).centerCrop()
                    .error(R.drawable.userprofile).into(userimage);
        }
        /*  Picasso.Builder builder=new_image Picasso.Builder(this);
                       builder.listener(new_image Picasso.Listener() {
                           @Override
                           public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                               exception.printStackTrace();
                           }
                       });
                        builder.build().load(image_url).placeholder(R.drawable.no_image).networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.no_image).into(userimage);
*/

        updateNotificationCount();

    }
    //endregion



    //region Handle options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem switchItem=menu.findItem(R.id.action_switch_user);
        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_NORMAL)){

            switchItem.setVisible(true);

        }else if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER)){
            switchItem.setVisible(false);
        }else {
            if (DataUtils.hasCardID(App.getInstance().getPrefManager().getCurrentUser().getCardId())){
                switchItem.setVisible(true);

            }else {
                switchItem.setVisible(false);
            }
        }



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_switch_user) {
            showUsers();
            return true;
        }else if (id==R.id.action_settings){
            IntentManager.startSettingsActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUsers() {

        SwitchUserDialog switchUserDialog = new SwitchUserDialog();
        switchUserDialog.show(getSupportFragmentManager(), getString(R.string.label_switch_user));
        switchUserDialog.setUserListener(new SwitchUserDialog.UserListener() {
            @Override
            public void setOnUserSelectListener(String userType) {
                initUserData(App.getInstance().getPrefManager().getCurrentUser().getUserType());
                ViewProfileInit();
                String message="Logged As ";
                if (userType.equals(Constants.USER_TYPE_DMS)){
                    message+="DMS Member";

                }else if (userType.equals(Constants.USER_TYPE_HR)){
                    message+="HR";

                }else if (userType.equals(Constants.USER_TYPE_PROVIDER)){
                    message+="Provider";
                }else {
                    message+="user";

                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


            }
        });


    }

    private void setLoggedMessage(TextView usertypetv,String userType){
        String message="";
        if (userType.equals(Constants.USER_TYPE_DMS)){
            message+="DMS Member";

        }else if (userType.equals(Constants.USER_TYPE_HR)){
            message+="HR";

        }else if (userType.equals(Constants.USER_TYPE_PROVIDER)){
            message+="Provider";
        }else {
            message+="user";

        }
        usertypetv.setText(message);
        //Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


    }

    //endregion

    //region Handle Navigation interactions
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view Item clicks here.
        setChildView(item.getItemId(), item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean networkCollapsed=false;
    boolean employeeCollapsed=false;
    boolean requestsCollapsed=false;

    @SuppressWarnings("UnusedParameters")
    private void setChildView(@IdRes int id, CharSequence title) {
        setHeader(id);
        Fragment fragment = null;
        switch (id) {

            case R.id.action_network:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBar.setElevation(8);
                }
//                fragment = new_image SearchFragment();
                //  IntentManager.startNetwork(this);

                if (networkCollapsed==false)
                {
                    nav_Menu.findItem(R.id.sub_action_network_search).setVisible(true);

                    nav_Menu.findItem(R.id.action_addprovider).setVisible(true);
                    nav_Menu.findItem(R.id.action_network).setIcon(R.drawable.arrow_up);


                    networkCollapsed=true;
                }else {
                    nav_Menu.findItem(R.id.sub_action_network_search).setVisible(false);

                    nav_Menu.findItem(R.id.action_addprovider).setVisible(false);
                    nav_Menu.findItem(R.id.action_network).setIcon(R.drawable.arrow_down_black);
                    networkCollapsed=false;
                }

                  nav_Menu.findItem(R.id.action_network).setChecked(true);
                nav_Menu.findItem(R.id.action_addprovider).setChecked(true);

                break;

            case R.id.sub_action_network_search:
                IntentManager.startNetwork(this);
                nav_Menu.findItem(R.id.sub_action_network_search).setChecked(true);
                break;

            case R.id.action_notifications:
                IntentManager.startActivity(this, IntentManager.ACTIVITY_NOTIFICATION);
                nav_Menu.findItem(R.id.action_notifications).setChecked(true);

                break;
            case R.id.action_about:
                IntentManager.startBaseBackActivity(this, IntentManager.FRAGMENT_ABOUT,null);
                //IntentManager.startAddProviderActivity(this);
                //  startActivity(new_image Intent(this,UpdateViewerActivity.class));
                nav_Menu.findItem(R.id.action_about).setChecked(true);

                break;
            case R.id.action_complaints:
                if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER)) {
                    IntentManager.startComplaintsActivity(MainActivity.this, Constants.KEY_COMPLAINTS_SERVICE );
                }else {
                    chooseComplainsType();

                }

                nav_Menu.findItem(R.id.action_complaints).setChecked(true);

                break;
            case R.id.action_requests:
                if(App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_DMS)){
                    IntentManager.startRequestslistActivity(this);
                }
                else {
                    String card_id = App.getInstance().getPrefManager().getCurrentUser().getCardId();
                    IntentManager.startListUserRequests(this, card_id);
                }
                nav_Menu.findItem(R.id.action_requests).setChecked(true);

                // waitingMessage();
                break;
            case R.id.action_family:
                IntentManager.startFamilyMembers(this);
                nav_Menu.findItem(R.id.action_family).setChecked(true);

                break;
            case R.id.action_employees:

                if (employeeCollapsed==false)
                {
                    nav_Menu.findItem(R.id.action_view_employees).setVisible(true);
                    nav_Menu.findItem(R.id.action_editcard).setVisible(true);
                    nav_Menu.findItem(R.id.action_employees).setIcon(R.drawable.arrow_up);

                    employeeCollapsed=true;
                }else {
                    nav_Menu.findItem(R.id.action_view_employees).setVisible(false);
                    nav_Menu.findItem(R.id.action_editcard).setVisible(false);
                    nav_Menu.findItem(R.id.action_employees).setIcon(R.drawable.arrow_down_black);
                    employeeCollapsed=false;
                }

                break;

            case R.id.action_view_employees:
                IntentManager.startCompanyEmployees(this,false);
                nav_Menu.findItem(R.id.action_view_employees).setChecked(true);

                break;

            case R.id.action_check_status:
                IntentManager.startCheckStatus(this);
                nav_Menu.findItem(R.id.action_check_status).setChecked(true);

                break;

            case R.id.requestsMenu:
                if (requestsCollapsed==false)
                {
                    nav_Menu.findItem(R.id.action_requests).setVisible(true);
                    nav_Menu.findItem(R.id.action_approve).setVisible(true);
                    nav_Menu.findItem(R.id.requestsMenu).setIcon(R.drawable.arrow_up);
                    requestsCollapsed=true;
                }else {
                    nav_Menu.findItem(R.id.action_requests).setVisible(false);
                    nav_Menu.findItem(R.id.action_approve).setVisible(false);
                    nav_Menu.findItem(R.id.requestsMenu).setIcon(R.drawable.arrow_down_black);
                    requestsCollapsed=false;
                }
                nav_Menu.findItem(R.id.requestsMenu).setChecked(true);

                break;
            case R.id.action_chat:
                startActivity(new Intent(this,ChatActivity.class));
                nav_Menu.findItem(R.id.action_chat).setChecked(true);

                break;

            case R.id.action_addprovider:
                IntentManager.startAddProviderActivity(this);
                nav_Menu.findItem(R.id.action_addprovider).setChecked(true);

                break;

            case R.id.action_approve:
                IntentManager.startApprovalActivity(this);
                nav_Menu.findItem(R.id.action_approve).setChecked(true);

                break;

            case R.id.action_chronic_medication:
                IntentManager.startChronicActivity(this);
                nav_Menu.findItem(R.id.action_chronic_medication).setChecked(true);

                break;
            case R.id.action_editcard:
                IntentManager.startHREditCard(this);
                nav_Menu.findItem(R.id.action_editcard).setChecked(true);

                break;
            case R.id.action_batch:
                IntentManager.startBatchActivity(this);
                nav_Menu.findItem(R.id.action_batch).setChecked(true);

                break;
            case R.id.action_indmnity:
                IntentManager.startIndemnityActivity(this);
                nav_Menu.findItem(R.id.sub_action_network_search).setChecked(true);

                break;

            case R.id.action_card_checker:
             startActivity(new Intent(this,CardValidationActivity.class));
                nav_Menu.findItem(R.id.action_card_checker).setChecked(true);
                break;

            case R.id.action_prescription:
                IntentManager.startPharmacyOrdersActivity(this);
                nav_Menu.findItem(R.id.action_prescription).setChecked(true);

                break;
            case R.id.action_massenger:
                IntentManager.StartMassengerActivity(this);
                nav_Menu.findItem(R.id.action_massenger).setChecked(true);

                break;
            case R.id.action_tele_sales:
                startActivity(new Intent(this,SearchTeleSalesActivity.class));
                nav_Menu.findItem(R.id.action_tele_sales).setChecked(true);

                break;
            case R.id.action_orders:
                IntentManager.startOrderActivity(this);
                nav_Menu.findItem(R.id.action_orders).setChecked(true);
                break;
            case R.id.action_admin:
                IntentManager.startAdminActivity(this);
                nav_Menu.findItem(R.id.action_admin).setChecked(true);

                break;
            case R.id.action_after_sales:
                startActivity(new Intent(this,SearchAfterSalesActivity.class));
                nav_Menu.findItem(R.id.action_after_sales).setChecked(true);
                break;




            case R.id.action_contract:
                startActivity(new Intent(this,SummrayContractActivity.class));
                nav_Menu.findItem(R.id.action_contract).setChecked(true);

                //startActivity(new Intent(this,ReviewsActivity.class));


        }
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMainContent, fragment, fragment.getClass().getName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private void setHeader(@IdRes int id) {
        switch (id) {
            case R.id.action_network:
                break;
            case R.id.action_complaints:


                break;
            case -1:
                mainToolbar.setTitle("");
                llHeaderContainer.removeAllViews();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.container_nav_header:
                navigationView.getMenu().getItem(0).setChecked(false);
                setHeader(-1);
                ViewProfileInit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @OnClick({R.id.tvSignOut, R.id.tvSignOutContainer})
    void signOut() {
//        throw new_image RuntimeException("This is a crash");
        App.getInstance().getPrefManager().makeLogout();
        IntentManager.startActivityAndFinishMe(this, IntentManager.ACTIVITY_AUTH);
    }
    //endregion



    //region choose Complain
    private void chooseComplainsType() {

        if (App.getInstance().getPrefManager().getCurrentUser().getUserType().equalsIgnoreCase(Constants.USER_TYPE_PROVIDER) ||
                App.getInstance().getPrefManager().getCurrentUser().getCardId().equals("")) {
            setTitle(getString(R.string.action_complaints));
            IntentManager.startComplaintsActivity(MainActivity.this, Constants.KEY_COMPLAINTS_SERVICE );
        }
        else {
            DialogUtils.chooseComplainsDialog(this, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    setTitle(getString(R.string.action_complaints));
                    IntentManager.startComplaintsActivity(MainActivity.this, Constants.KEY_COMPLAINTS_PROVIDER );

                }
            }, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    setTitle(getString(R.string.action_complaints));
                    IntentManager.startComplaintsActivity(MainActivity.this, Constants.KEY_COMPLAINTS_SERVICE );
                }
            });
        }
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
