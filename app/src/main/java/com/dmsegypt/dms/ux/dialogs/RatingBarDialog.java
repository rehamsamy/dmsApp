package com.dmsegypt.dms.ux.dialogs;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.app.IntentManager;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.activity.AddOrderActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mahmoud on 2/01/18.
 */

public class RatingBarDialog extends DialogFragment {
    public static final String EXTRA_ORDER_ID="orderId";
    public static final String EXTRA_RUNNER_ID="runnerId";
    RatingBar ratingBar;
    Button getRating;
    EditText et_any_comment;
    String order_no;
    String runner_id;
    Call call;



    public static RatingBarDialog newInstance(String order_id,String runner_id){
        Bundle args = new Bundle();
        args.putString(EXTRA_ORDER_ID,order_id);
        args.putString(EXTRA_RUNNER_ID,runner_id);
        RatingBarDialog ratingBarDialog=new RatingBarDialog();
        ratingBarDialog.setArguments(args);
        return ratingBarDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        getRating = (Button) view.findViewById(R.id.btn_add);
        et_any_comment=(EditText)view.findViewById(R.id.et_any_comment);
        getRating.setOnClickListener(new OnGetRatingClickListener());
        order_no=getArguments().getString(EXTRA_ORDER_ID);
        runner_id=getArguments().getString(EXTRA_RUNNER_ID);
        return view;
    }

    private class OnGetRatingClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            DialogUtils.showDialog(getActivity(),true);

            //RatingBar$getRating() returns float value, you should cast(convert) it to string to display in a view
            String rating = String.valueOf(ratingBar.getRating());
            String comment=et_any_comment.getText().toString().trim();
            String createdBy= App.getInstance().getPrefManager().getCurrentUser().getCardId();
            call=App.getInstance().getService().ConfirmMassengerOrder(order_no,runner_id,comment,rating,createdBy, LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN);
            call.enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    DialogUtils.showDialog(getActivity(),false);
                    if (response != null && response.body() != null) {
                        if (response.body().getCode() != null && response.body().getCode() == 1) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.msg_request_sent, Snackbar.LENGTH_LONG).show();
                            dismiss();

                        }

                    }else {
                        DialogUtils.showDialog(getActivity(),false);
                        Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                    DialogUtils.showDialog(getActivity(),false);
                    Snackbar.make(getActivity().findViewById(android.R.id.content),R.string.err_data_load_failed, Snackbar.LENGTH_LONG)
                            .show();

                }
            });


            Toast.makeText(getActivity(),"Rated "+rating+" stars", Toast.LENGTH_SHORT).show();
        }
    }
}
