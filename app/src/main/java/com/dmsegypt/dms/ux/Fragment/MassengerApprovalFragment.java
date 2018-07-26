package com.dmsegypt.dms.ux.Fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Order;
import com.dmsegypt.dms.rest.model.Response.ResponseOrder;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.activity.MainMassengerActivity;
import com.dmsegypt.dms.ux.custom_view.ToolbarScrollHelper;
import com.dmsegypt.dms.ux.custom_view.VerticalTextView;
import com.dmsegypt.dms.ux.dialogs.RatingBarDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.sephiroth.android.library.bottomnavigation.BottomBehavior;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import it.sephiroth.android.library.bottomnavigation.MiscUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MassengerApprovalFragment extends Fragment {
    private static final String TAG = MainMassengerActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    ProgressBar mprogress;
    TextView error_occured;
    CoordinatorLayout mCoordinatorLayout;
    Adapter adapter;
    ViewGroup mRoot;
    private SystemBarTintManager.SystemBarConfig config;
    private ToolbarScrollHelper scrollHelper;
    Boolean loadMore=false;
    List<Order> list=new ArrayList<>();
    private String  username;

    public MassengerApprovalFragment() { }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_massenger_approval, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView01);
        mprogress=(ProgressBar)view.findViewById(R.id.loading_progress);
        error_occured=(TextView)view.findViewById(R.id.tv_error);



    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username=App.getInstance().getPrefManager().getCurrentUser().getUserType().equals(Constants.USER_TYPE_DMS)?"-1":App.getInstance().getPrefManager().getUsername();

        final MainMassengerActivity activity = (MainMassengerActivity) getActivity();
        config = activity.getSystemBarTint().getConfig();
        mRoot = (ViewGroup) activity.findViewById(R.id.CoordinatorLayout01);


        if (mRoot instanceof CoordinatorLayout) {
            mCoordinatorLayout = (CoordinatorLayout) mRoot;
        }

        final int navigationHeight;
        final int actionbarHeight;

        if (activity.hasTranslucentNavigation()) {
            navigationHeight = config.getNavigationBarHeight();
        } else {
            navigationHeight = 0;
        }

        if (activity.hasTranslucentStatusBar()) {
            actionbarHeight = config.getActionBarHeight();
        } else {
            actionbarHeight = 0;
        }

        MiscUtils.log(TAG, Log.VERBOSE, "navigationHeight: " + navigationHeight);
        MiscUtils.log(TAG, Log.VERBOSE, "actionbarHeight: " + actionbarHeight);

        final BottomNavigation navigation = activity.getBottomNavigation();
        if (null != navigation) {
            navigation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    navigation.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    final ViewGroup.LayoutParams params = navigation.getLayoutParams();
                    final CoordinatorLayout.Behavior behavior;

                    if (params instanceof CoordinatorLayout.LayoutParams) {
                        final CoordinatorLayout.LayoutParams coordinatorLayoutParams = (CoordinatorLayout.LayoutParams) params;
                        behavior = coordinatorLayoutParams.getBehavior();
                    } else {
                        behavior = null;
                    }

                    if (behavior instanceof BottomBehavior) {
                        final boolean scrollable = ((BottomBehavior) behavior).isScrollable();
                        int systemBottomNavigation = activity.hasTranslucentNavigation() ? activity.getNavigationBarHeight() : 0;

                        MiscUtils.log(TAG, Log.VERBOSE, "scrollable: " + scrollable);

                        int totalHeight;

                        if (scrollable) {
                            if (systemBottomNavigation > 0) {
                                totalHeight = systemBottomNavigation;
                            } else {
                                totalHeight = navigationHeight;
                            }
                        } else {
                            totalHeight = navigation.getNavigationHeight();
                        }

                        createAdater(totalHeight, activity.hasManagedToolbarScroll());
                    } else {
                        createAdater(navigationHeight, activity.hasAppBarLayout());
                    }
                }
            });
        } else {
            createAdater(navigationHeight, activity.hasAppBarLayout());
        }

        if (!activity.hasManagedToolbarScroll()) {
            scrollHelper = new ToolbarScrollHelper(activity, activity.getToolbar());
            scrollHelper.initialize(mRecyclerView);
        }
    }

    private void createAdater(final int height, final boolean hasAppBarLayout) {
        MiscUtils.log(getClass().getSimpleName(), Log.INFO, "createAdapter(" + height + ")");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getData(height,hasAppBarLayout,"0");
        error_occured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(height,hasAppBarLayout,"0");
            }
        });
    }

    private void getData(final int height, final boolean hasAppBarLayout,final String index) {
        mprogress.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        error_occured.setVisibility(View.GONE);
        if(list != null){
            list.clear();
        }else {
            list=new ArrayList<>();
        }

        App.getInstance().getService().getAllMassengerOrder(username,LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,"Approval",index).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                mprogress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                error_occured.setVisibility(View.GONE);
                if(response != null && response.body().getList() != null){
                    if (response.body().getList().isEmpty()){
                        if(response.body().getMessage().getCode()!= 1) {
                            if (index.equals("0")) {
                                if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                    getData(height, hasAppBarLayout, "0");
                                } else {
                                    error_occured.setText("No Data Available");
                                    mRecyclerView.setVisibility(View.GONE);
                                }

                            }else if(!index.equals("0")){
                                if((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                                }
                            }else if(!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))){
                                getData(height,hasAppBarLayout,index);
                            }
                        }
                    }else {
                        if (index.equals("0")) {

                            error_occured.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            loadMore = (response.body().getList().size() % 20) == 0;
                            if(loadMore) {
                                loadMoreRequest(height, hasAppBarLayout, String.valueOf(adapter.getItemCount() - 1));
                            }
                            list.addAll(response.body().getList());
                            adapter=new MassengerApprovalFragment.Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed
                        }else {


                            list.addAll(response.body().getList());
                            adapter=new MassengerApprovalFragment.Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed

                            loadMore = (response.body().getList().size() % 20) == 0;
                            if(loadMore) {
                                loadMoreRequest(height, hasAppBarLayout, String.valueOf(adapter.getItemCount() - 1));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable throwable) {
                mprogress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                error_occured.setVisibility(View.VISIBLE);
            }
        });
    }


    void loadMoreRequest(final int height, final boolean hasAppBarLayout,final String index){
        mprogress.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        error_occured.setVisibility(View.GONE);
        if(list != null){
            list.clear();
        }else {
            list=new ArrayList<>();
        }

        App.getInstance().getService().getAllMassengerOrder(username,LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,"Approval",index).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                mprogress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                error_occured.setVisibility(View.GONE);
                if(response != null){
                    if (response.body().getList().isEmpty()){
                        if(response.body().getMessage().getCode()!= 1) {
                            if (index.equals("0")) {
                                if (response.body().getMessage().getDetails().equals("Can't connect to server")) {
                                    getData(height, hasAppBarLayout, "0");
                                } else {
                                    error_occured.setText("No Data Available");
                                    mRecyclerView.setVisibility(View.GONE);
                                }

                            }else if(!index.equals("0")){
                                if((response.body().getMessage().getDetails().toString().trim().equals(getString(R.string.error_no_data_available)) || response.body().getMessage().getDetails().toString().trim().equals(R.string.error_no_data_branch_available))){
                                }
                            }else if(!index.equals("0") && (response.body().getMessage().getDetails().equals("Can't connect to server"))){
                                getData(height,hasAppBarLayout,index);
                            }
                        }
                    }else {
                        if (index.equals("0")) {


                            error_occured.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            loadMore = (response.body().getList().size() % 20) == 0;
                            if(loadMore) {
                                loadMoreRequest(height, hasAppBarLayout, String.valueOf(adapter.getItemCount() - 1));
                            }
                            list.addAll(response.body().getList());
                            adapter=new MassengerApprovalFragment.Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed
                        }else {


                            list.addAll(response.body().getList());
                            adapter=new MassengerApprovalFragment.Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed

                            loadMore = (response.body().getList().size() % 20) == 0;
                            if(loadMore) {
                                loadMoreRequest(height, hasAppBarLayout, String.valueOf(adapter.getItemCount() - 1));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable throwable) {
                mprogress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                error_occured.setVisibility(View.VISIBLE);
            }
        });
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    static class TwoLinesViewHolder extends RecyclerView.ViewHolder {

        final TextView comp_name;
        final TextView order_date;
        final TextView order_notes;
        final TextView comp_location;
        final TextView comp_person;
        final TextView comp_phone;
        final int marginBottom;
        final LinearLayout linear_state;
        final VerticalTextView verticaltrackorder;
        final AppCompatButton button_status;
        final TextView runner_id;
        final TextView approval_date;
        final TextView confirm_order;
        public ImageView vipImgv;

        public TwoLinesViewHolder(final View itemView) {
            super(itemView);

            comp_name=(TextView)itemView.findViewById(R.id.comp_name_tv);
            order_date=(TextView)itemView.findViewById(R.id.order_date);
            order_notes=(TextView)itemView.findViewById(R.id.tv_order_notes);
            comp_location=(TextView)itemView.findViewById(R.id.tv_comp_location);
            comp_person=(TextView)itemView.findViewById(R.id.tv_comp_person);
            comp_phone=(TextView)itemView.findViewById(R.id.tv_comp_phone);
            marginBottom = ((ViewGroup.MarginLayoutParams) itemView.getLayoutParams()).bottomMargin;
            linear_state=(LinearLayout)itemView.findViewById(R.id.linear_state);
            verticaltrackorder=(VerticalTextView) itemView.findViewById(R.id.track_order);
            button_status=(AppCompatButton)itemView.findViewById(R.id.button_status);
            runner_id=(TextView)itemView.findViewById(R.id.tv_runner_id);
            approval_date=(TextView)itemView.findViewById(R.id.tv_approval_date);
            confirm_order=(TextView)itemView.findViewById(R.id.confirm_order);
            vipImgv=(ImageView)itemView.findViewById(R.id.vip_imgv);

        }
    }

    private class Adapter extends RecyclerView.Adapter<MassengerApprovalFragment.TwoLinesViewHolder> {
        private final int navigationHeight;
        private final List<Order> data;
        private final boolean hasAppBarLayout;

        public Adapter(final Context context, final int navigationHeight, final boolean hasAppBarLayout, final List<Order> data) {
            this.navigationHeight = navigationHeight;
            this.data = data;
            this.hasAppBarLayout = hasAppBarLayout;
        }

        @Override
        public MassengerApprovalFragment.TwoLinesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.card_item_approval, parent, false);
            final MassengerApprovalFragment.TwoLinesViewHolder holder = new MassengerApprovalFragment.TwoLinesViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final MassengerApprovalFragment.TwoLinesViewHolder holder, final int position) {
            ((ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams()).topMargin = 0;
            if (position == getItemCount() - 1) {
                ((ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams()).bottomMargin = holder.marginBottom + navigationHeight;
            } else if (position == 0 && !hasAppBarLayout) {
                ((ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams()).topMargin = scrollHelper.getToolbarHeight();
            } else {
                ((ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams()).bottomMargin = holder.marginBottom;
            }

            final Order item = data.get(position);
            if(item.getComp_id() != null && item.getComp_name() != null) {
                holder.comp_name.setText(item.getComp_name() +"-"+ item.getComp_id());
            }
            if(item.getOrder_notes() != null) {
                holder.order_notes.setText(item.getOrder_notes());
            }
            if(item.getComp_area() != null || item.getComp_gover() != null || item.getComp_address() != null) {
                holder.comp_location.setText(item.getComp_gover() + "," + item.getComp_area() + "," + item.getComp_address());
            }
            if(item.getComp_phone1() != null || item.getComp_phone2() != null) {
                holder.comp_phone.setText(item.getComp_phone1() + "-" + item.getComp_phone2());
            }
            if(item.getComp_person() != null) {
                holder.comp_person.setText(item.getComp_person());
            }
            if(item.getCreated_date() != null){
                holder.order_date.setText(item.getCreated_date());
            }
            if(item.getRun_id() != null){
                holder.runner_id.setText(item.getRun_id());
            }
            if (item.getVip().equals("1")){
                holder.vipImgv.setVisibility(View.VISIBLE);
            }else {
                holder.vipImgv.setVisibility(View.GONE);
            }

            if(item.getUpdated_date() != null){
                holder.approval_date.setText(item.getUpdated_date());
            }
            holder.confirm_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RatingBarDialog ratingBarFragment = RatingBarDialog.newInstance(item.getOrder_no(),item.getRun_id());
                    ratingBarFragment.show(getActivity().getFragmentManager(), "dialog");
                }
            });

/*
            Drawable mDrawableperson = holder.comp_person.drawableR.getResources().getDrawable(R.drawable.ic_person);
            mDrawableperson.setColorFilter(new
                    PorterDuffColorFilter(itemView.getResources().getColor(R.color.colorAccentyellow), PorterDuff.Mode.MULTIPLY));

            Drawable mDrawablelocation = itemView.getResources().getDrawable(R.drawable.ic_my_location);
            mDrawablelocation.setColorFilter(new
                    PorterDuffColorFilter(itemView.getResources().getColor(R.color.colorAccentyellow), PorterDuff.Mode.MULTIPLY));

            Drawable mDrawablephone = itemView.getResources().getDrawable(R.drawable.ic_phone);
            mDrawablephone.setColorFilter(new
                    PorterDuffColorFilter(itemView.getResources().getColor(R.color.colorAccentyellow), PorterDuff.Mode.MULTIPLY));

*/


        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }



}
