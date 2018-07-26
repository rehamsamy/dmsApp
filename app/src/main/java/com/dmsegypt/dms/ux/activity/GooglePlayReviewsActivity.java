package com.dmsegypt.dms.ux.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.Response.GooglePlayReviewsResponse;
import com.dmsegypt.dms.rest.model.Response.RefreashTokenResponse;
import com.dmsegypt.dms.rest.model.Response.ReviewReplyResponse;
import com.dmsegypt.dms.rest.model.Review;
import com.dmsegypt.dms.rest.model.ReviewReply;
import com.dmsegypt.dms.utils.DialogUtils;
import com.dmsegypt.dms.ux.adapter.StoreReviewsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlayReviewsActivity extends BaseActivity {

    StoreReviewsAdapter reviewsAdapter;
    List<Review> reviewsList;
    @BindView(R.id.store_reviews_recyclerview)
    RecyclerView storeReviewsRecyclerview;
    String accessToken="";
    @BindView(R.id.empty_view)
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        reviewsList=new ArrayList<>();
        storeReviewsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter=new StoreReviewsAdapter(R.layout.review_item,reviewsList);

        if (reviewsAdapter!=null)
        {
            reviewsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    if (view.getId() == R.id.replyImageBtn){
                     EditText editText= (EditText) baseQuickAdapter.getViewByPosition(storeReviewsRecyclerview,i,R.id.replyIdEditText);
                    String text= editText.getText().toString();
                        if (TextUtils.isEmpty(text))
                        {
                            editText.setError("Required !");
                        }else {

                            editText.setError(null);
                            editText.setText("");
                            String id=reviewsList.get(i).getReviewId();
                            submitReview(id,text);

                        }


                    }
                }
            });
        }


        App.getInstance().getGooglePlayService().getRefreashToken(RequestBody.create(MediaType.parse("text/plain"), "refresh_token"),RequestBody.create(MediaType.parse("text/plain"), "911905717460-3r11i36rmuq1bkadi3amd4vhuua2i9r0.apps.googleusercontent.com"),RequestBody.create(MediaType.parse("text/plain"), "ZUdXmhs276XHkUioca1g93Nh"),
                RequestBody.create(MediaType.parse("text/plain"), "1/NvNGHkvFZJctOUXAkfx03RcXuoCZN2HE8PB50L0yZ0Q")).enqueue(new Callback<RefreashTokenResponse>() {
            @Override
            public void onResponse(Call<RefreashTokenResponse> call, Response<RefreashTokenResponse> response) {

                if (response.body().getAccessToken()!=null)
                {
                    accessToken=response.body().getAccessToken();
                    getReviews(response.body().getAccessToken());

                }else {
                    Toast.makeText(getApplicationContext(),"Cannot refreash token !",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RefreashTokenResponse> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Failure refreash token !",Toast.LENGTH_LONG).show();

            }
        });



    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public int getResLayout() {
        return R.layout.activity_google_play_reviews;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.reviews;
    }

    void getReviews(String token)
    {
        DialogUtils.showDialog(GooglePlayReviewsActivity.this,true);
          emptyView.setVisibility(View.GONE);
          storeReviewsRecyclerview.setVisibility(View.VISIBLE);
        App.getInstance().getGooglePlayService().getStoreReviews("com.dmsegypt.dms",token)
                .enqueue(new Callback<GooglePlayReviewsResponse>() {
                    @Override
                    public void onResponse(Call<GooglePlayReviewsResponse> call, Response<GooglePlayReviewsResponse> response) {

                        if (response.body().getReviews()!=null)
                        {
                            for (int i=0;i<response.body().getReviews().size();i++)
                            {
                                reviewsList.add(response.body().getReviews().get(i));
                            }
                            storeReviewsRecyclerview.setAdapter(reviewsAdapter);
                            reviewsAdapter.notifyDataSetChanged();
                        }else {
                            emptyView.setVisibility(View.VISIBLE);
                            storeReviewsRecyclerview.setVisibility(View.GONE);


                        }
                        DialogUtils.showDialog(GooglePlayReviewsActivity.this,false);

                    }

                    @Override
                    public void onFailure(Call<GooglePlayReviewsResponse> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(),R.string.error_inernet_connection,Toast.LENGTH_LONG).show();
                        DialogUtils.showDialog(GooglePlayReviewsActivity.this,false);

                    }
                });
    }

    void submitReview(String reviewId,String replyMsg)
    {

        ReviewReply reviewReply=new ReviewReply();
        reviewReply.setReplyText(replyMsg);
        DialogUtils.showDialog(GooglePlayReviewsActivity.this,true);

        App.getInstance().getGooglePlayService()
                .replyOnReview("com.dmsegypt.dms",reviewId,accessToken,reviewReply)
                .enqueue(new Callback<ReviewReplyResponse>() {
            @Override
            public void onResponse(Call<ReviewReplyResponse> call, Response<ReviewReplyResponse> response) {
                DialogUtils.showDialog(GooglePlayReviewsActivity.this,false);

                if (response.body().getResult()!=null)
                {
                    Toast.makeText(getApplicationContext(),"Reply Success",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Reply Failed",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ReviewReplyResponse> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Failure to get response",Toast.LENGTH_LONG).show();
                DialogUtils.showDialog(GooglePlayReviewsActivity.this,false);

            }
        });
    }
}
