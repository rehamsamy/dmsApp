package com.dmsegypt.dms.ux.dialogs;

import android.animation.Animator;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.ResponseRunner;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.Runner;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.Fragment.MassengerPendFragment;
import com.dmsegypt.dms.ux.activity.AddProviderActivity;
import com.dmsegypt.dms.ux.adapter.RunnerListAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mahmoud on 28/12/17.
 */

public class MoveMassengerDialog extends DialogFragment {
    public static final String EXTRA_ORDER_ID="orderId";
    public static final String EXTRA_ORDER_DATE="orderDate";

    @BindView(R.id.recycler_all_massenger)
    RecyclerView recycler_all_massenger;
    @BindView(R.id.button_first_next)
    AppCompatButton button_first_next;

    @BindView(R.id.rel_all_Massenger)
    RelativeLayout rel_all_Massenger;

    @BindView(R.id.rel_runner_id)
    RelativeLayout rel_runner_id;


    @BindView(R.id.rel_finish)
    RelativeLayout rel_finish;

    @BindView(R.id.et_any_comment)
    EditText optional_note;

    @BindView(R.id.button_finish)
    AppCompatButton button_finish;

    @BindView(R.id.button_second_previos)
    AppCompatButton button_second_previos;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.rel_select)
    RelativeLayout rel_select;

    @BindView(R.id.tv_selected_runner)
    TextView selected_runner;

    @BindView(R.id.tv_selected_order_time)
    TextView selected_order_time;

    @BindView(R.id.button_hold)
    AppCompatButton button_hold;

    @BindView(R.id.button_approve)
    AppCompatButton button_approve;


    public List<Runner> list=new ArrayList<>();

    public RunnerListAdapter runnerListAdapter;

    static String order_date;
    static  String order_time;
    static String order_no;
    static Boolean checkSelectApprove;
    static String order_date_approve;
    static String order_runner_Id;
    static Bundle transfearBundel;

    private DialogInterface.OnDismissListener onDismissListener;


    public static MoveMassengerDialog newInstance (String OrderId,String OrderDate){
        Bundle args = new Bundle();
        args.putString(EXTRA_ORDER_ID,OrderId);
        args.putString(EXTRA_ORDER_DATE,OrderDate);
        MoveMassengerDialog fragment=new MoveMassengerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transfearBundel=savedInstanceState;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_move_massenger,container,false);
        ButterKnife.bind(this,view);
        recycler_all_massenger.setLayoutManager(new LinearLayoutManager(getActivity()));

        runnerListAdapter=new RunnerListAdapter(MoveMassengerDialog.this,R.layout.item_list_runner,list);
        recycler_all_massenger.setAdapter(runnerListAdapter);
        order_no=getArguments().getString(EXTRA_ORDER_ID);
        order_date_approve=getArguments().getString(EXTRA_ORDER_DATE);

        button_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectApprove=true;
                rel_select.setVisibility(View.GONE);
                selectRunner();

            }
        });
        button_hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectApprove=false;
                rel_select.setVisibility(View.GONE);
            //    selectRunner();
                DatePicker();
            }
        });



        return view;

    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        this.onDismissListener=onDismissListener;
    }

    private void selectRunner() {
        rel_all_Massenger.setVisibility(View.VISIBLE);

        App.getInstance().getService().getAllRunner(LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,"1","0").enqueue(new Callback<ResponseRunner>() {
            @Override
            public void onResponse(Call<ResponseRunner> call, Response<ResponseRunner> response) {
                if(response != null){
                    mProgress.setVisibility(View.GONE);
                    if(response.body().getList().size() != 0){
                        list.clear();
                        list.addAll(response.body().getList());
                        runnerListAdapter.notifyDataSetChanged();
                        recycler_all_massenger.setVisibility(View.VISIBLE);
                       // button_first_next.setVisibility(View.VISIBLE);
                        runnerListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                                firstnext();
                                selected_runner.setText(list.get(position).getRunner_ename());
                                order_runner_Id=list.get(position).getRunner_id();
                                rel_runner_id.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseRunner> call, Throwable throwable) {

            }
        });

    }

    void timePicker(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd=TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                              @Override
                                                              public void onTimeSet(TimePickerDialog timePickerDialog, int i, int i1, int i2) {
                                                                  String newTime = i + ":" + (i1);
                                                                  order_time=newTime;
                                                                  if(checkSelectApprove){
                                                                      selected_order_time.setText(order_date_approve + " " + order_time);

                                                                  }else {
                                                                      selected_order_time.setText(order_date + " " + order_time);

                                                                  }
                                                                  rel_finish.setVisibility(View.VISIBLE);

                                                              }
                                                          },
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setTitle("Order Time Picker");
        tpd.setCancelText(R.string.label_prevroise);
        tpd.setCancelColor(getResources().getColor(R.color.colorAccentRed));
        tpd.setOkText(R.string.next);
        tpd.setOkColor(getResources().getColor(R.color.colorAccentGreen));
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               if (checkSelectApprove){
                   rel_all_Massenger.setVisibility(View.VISIBLE);
                   recycler_all_massenger.setVisibility(View.VISIBLE);
                   rel_finish.setVisibility(View.GONE);

               }

            }
        });
        tpd.show(getFragmentManager(),"timePickerDialog");



    }


    void DatePicker(){
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
                            order_date=newDate;
                            timePicker();
                        } else if (nowyear == year) {
                            if (nowmonth < monthOfYear) {
                                order_date=newDate;
                                timePicker();
                            } else if (nowmonth == monthOfYear) {
                                if (nowday < dayOfMonth) {
                                    order_date=newDate;
                                    timePicker();
                                } else {
                                    Toast.makeText(getActivity(), "Wrong Date", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Wrong Date", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Wrong Date", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setFirstDayOfWeek(Calendar.SATURDAY);
        dpd.showYearPickerFirst(true);
        dpd.setTitle("Order Time Picker");
        dpd.setCancelText(R.string.label_prevroise);
        dpd.setCancelColor(getResources().getColor(R.color.colorAccentRed));
        dpd.setOkText(R.string.next);
        dpd.setOkColor(getResources().getColor(R.color.colorAccentGreen));
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                rel_all_Massenger.setVisibility(View.VISIBLE);
                recycler_all_massenger.setVisibility(View.VISIBLE);
               // button_first_next.setVisibility(View.VISIBLE);

            }
        });
        dpd.show(getFragmentManager(), "datePickerDialog");


    }


    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.button_first_next)
    public void firstnext(){
        if(checkSelectApprove){
            timePicker();
        }else {
            DatePicker();
        }
        rel_all_Massenger.setVisibility(View.GONE);

/*
        rel_date.animate().translationY(rel_date.getHeight())
                .alpha(1.0f)
                .setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rel_date.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });*/

    }

    @OnClick(R.id.button_finish)
    public void buttonfinish(){
        String createdBy=App.getInstance().getPrefManager().getCurrentUser().getCardId();
        String last_order_id=order_no;
        String last_order_date=selected_order_time.getText().toString().trim();
        String last_runner_id=order_runner_Id;
        String last_optional_note;
        if(optional_note.getText().toString().equals("")){
            last_optional_note="Defult";
        }else {
            last_optional_note=optional_note.getText().toString().trim();
        }

        if(!createdBy.equals("") && !last_order_id.equals("") && !last_order_date.equals("") && !last_order_id.equals("") && !last_optional_note.equals("")){
            final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.send_msg)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
            if(checkSelectApprove) {
                App.getInstance().getService().ApproveMassengerOrder(last_order_id, last_runner_id, last_order_date.replaceAll("/", "-"), createdBy, LocaleHelper.getLanguage(getActivity()).equals("ar") ? Constants.Language.AR : Constants.Language.EN).enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.body().getCode() == 1) {
                            Toast.makeText(getActivity(), R.string.msg_send_proivder_request, Toast.LENGTH_SHORT).show();
                            dismiss();
                    /*        MassengerPendFragment massengerPendFragment=new MassengerPendFragment();
                            massengerPendFragment.getData(0,true,"0");*/
                            IntentManager.StartMassengerActivity(getActivity());



                        } else {
                            Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();

                    }
                });
            }else {
             App.getInstance().getService().HoldMassengerOrder(last_order_id,last_order_date.replaceAll("/","-"), createdBy, LocaleHelper.getLanguage(getActivity()).equals("ar") ? Constants.Language.AR : Constants.Language.EN).enqueue(new Callback<StatusResponse>() {
                 @Override
                 public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                     dialog.dismiss();
                     if (response.body().getCode() == 1) {
                         Toast.makeText(getActivity(), R.string.msg_send_proivder_request, Toast.LENGTH_SHORT).show();
                         dismiss();
                    /*        MassengerPendFragment massengerPendFragment=new MassengerPendFragment();
                            massengerPendFragment.getData(0,true,"0");*/
                         IntentManager.StartMassengerActivity(getActivity());



                     } else {
                         Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                         dismiss();
                     }

                 }

                 @Override
                 public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                     dialog.dismiss();
                     Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();


                 }
             });
            }
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissListener != null){
            onDismissListener.onDismiss(dialog);
        }
    }

    @OnClick(R.id.button_second_previos)
    public void secondprevios(){
        if(checkSelectApprove){
            timePicker();
        }else {
            DatePicker();
        }

        rel_finish.setVisibility(View.GONE);
       // rel_select.setVisibility(View.VISIBLE);

    }





}
