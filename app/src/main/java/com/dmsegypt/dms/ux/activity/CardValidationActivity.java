package com.dmsegypt.dms.ux.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardValidationActivity extends BaseActivity {
    @BindView(R.id.fblock1)
    EditText block1;

    @BindView(R.id.fblock2)
    EditText block2;


    @BindView(R.id.fblock3)
    EditText block3;

    @BindView(R.id.fblock4)
    EditText block4;
    @BindView(R.id.validate_btn)
    Button ValidateBtn;
    private Call call;
    ImageView stateImgv;
    Button okButton;
    TextView messageTv,detailTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.validate_btn)
    public void validateCard(){
        DialogUtils.showDialog(CardValidationActivity.this, true);

        final String NewCardId=block1.getText().toString().trim()+"-"+block2.getText().toString().trim()+"-"+block3.getText().toString().trim()+"-"+block4.getText().toString().trim();
        if(NewCardId != null || !NewCardId.equals("") ){
            call= App.getInstance().getService().checkCardValdiation(NewCardId);
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    DialogUtils.showDialog(CardValidationActivity.this, false);

                    if(response != null && response.message() != null){
                        if (response.body().getCode()==1) {
                           showDailog(true);

                        }else {
                       showDailog(false);
                        }



                    }else {
                        Snackbar.make(findViewById(android.R.id.content),R.string.error_inernet_connection,Snackbar.LENGTH_SHORT).show();
                        DialogUtils.showDialog(CardValidationActivity.this,false);
                    }

                }


                @Override
                public void onFailure(Call<Message> call, Throwable throwable) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.err_data_load_failed, Snackbar.LENGTH_SHORT).show();
                    DialogUtils.showDialog(CardValidationActivity.this,false);
                }
            });
        }


    }



    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_card_validation;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_Card_validation;
    }
    public void showDailog(boolean isSuccess){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(this).inflate(R.layout.dailog_card_valid,null);
        stateImgv= (ImageView) view.findViewById(R.id.stateImgv);
        okButton=(Button)view.findViewById(R.id.okButton);
        messageTv=(TextView)view.findViewById(R.id.messageTv);
        detailTv=(TextView)view.findViewById(R.id.detailTv);
        detailTv.setVisibility(isSuccess?View.VISIBLE:View.GONE);

         messageTv.setText(isSuccess?R.string.msg_valid_card:R.string.msg_invalid_card);
         stateImgv.setImageResource(isSuccess?R.drawable.ic_correct:R.drawable.ic_false);
         okButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
             }
         });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }
}
