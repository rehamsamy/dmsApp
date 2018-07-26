package com.dmsegypt.dms.ux.Fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.Image;
import com.dmsegypt.dms.rest.model.Order;
import com.dmsegypt.dms.rest.model.Response.ResponseOrder;
import com.dmsegypt.dms.rest.model.Response.ResponseRequests;
import com.dmsegypt.dms.rest.model.User;
import com.dmsegypt.dms.utils.LocaleHelper;
import com.dmsegypt.dms.ux.activity.MainMassengerActivity;
import com.dmsegypt.dms.ux.custom_view.ToolbarScrollHelper;
import com.dmsegypt.dms.ux.custom_view.VerticalTextView;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.sephiroth.android.library.bottomnavigation.BottomBehavior;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import it.sephiroth.android.library.bottomnavigation.MiscUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainMassengerFragment extends Fragment {
    private static final String TAG = MainMassengerActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    ProgressBar mprogress;
    TextView error_occured;
    CoordinatorLayout mCoordinatorLayout;
    Adapter adapter;
    ViewGroup mRoot;
    private SystemBarConfig config;
    private ToolbarScrollHelper scrollHelper;
    Boolean loadMore=false;
    List<Order> list=new ArrayList<>();
    String username;

    public MainMassengerFragment() { }

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
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

        App.getInstance().getService().getAllMassengerOrder(username,LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,"-1",index).enqueue(new Callback<ResponseOrder>() {
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
                            Collections.reverse(list);
                            adapter=new Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed
                        }else {
                            list.addAll(response.body().getList());
                            Collections.reverse(list);
                            adapter=new Adapter(getContext(), height, hasAppBarLayout, list);
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

        App.getInstance().getService().getAllMassengerOrder(username,LocaleHelper.getLanguage(getActivity()).equals("ar")? Constants.Language.AR:Constants.Language.EN,"-1",index).enqueue(new Callback<ResponseOrder>() {
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
                            Collections.reverse(list);
                            adapter=new Adapter(getContext(), height, hasAppBarLayout, list);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();  // data set changed
                        }else {
                            list.addAll(response.body().getList());
                            Collections.reverse(list);
                            adapter=new Adapter(getContext(), height, hasAppBarLayout, list);
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
        final ImageView iv_order_pending;
        final ImageView iv_order_done;
        final ImageView iv_order_start;
        final TextView order_done_date;
        final TextView order_updated_date;
        final TextView order_start_date;
        final int marginBottom;
        final LinearLayout linear_state;
        final VerticalTextView verticaltrackorder;
        final AppCompatButton button_status;
        final ImageView vipImgv;

        public TwoLinesViewHolder(final View itemView) {
            super(itemView);

            comp_name=(TextView)itemView.findViewById(R.id.comp_name_tv);
            order_date=(TextView)itemView.findViewById(R.id.order_date);
            order_notes=(TextView)itemView.findViewById(R.id.tv_order_notes);
            comp_location=(TextView)itemView.findViewById(R.id.tv_comp_location);
            comp_person=(TextView)itemView.findViewById(R.id.tv_comp_person);
            comp_phone=(TextView)itemView.findViewById(R.id.tv_comp_phone);
            iv_order_pending=(ImageView)itemView.findViewById(R.id.iv_order_pending);
            iv_order_done=(ImageView)itemView.findViewById(R.id.iv_order_done);
            iv_order_start=(ImageView)itemView.findViewById(R.id.iv_order_start);
            order_done_date=(TextView)itemView.findViewById(R.id.order_done_date);
            order_updated_date=(TextView)itemView.findViewById(R.id.order_updated_date);
            order_start_date=(TextView)itemView.findViewById(R.id.order_created_date);
            marginBottom = ((MarginLayoutParams) itemView.getLayoutParams()).bottomMargin;
            linear_state=(LinearLayout)itemView.findViewById(R.id.linear_state);
            verticaltrackorder=(VerticalTextView) itemView.findViewById(R.id.track_order);
            button_status=(AppCompatButton)itemView.findViewById(R.id.button_status);
            vipImgv=(ImageView)itemView.findViewById(R.id.vip_imgv);


        }
    }

    private class Adapter extends RecyclerView.Adapter<TwoLinesViewHolder> {
        private final int navigationHeight;
        private final List<Order> data;
        private final boolean hasAppBarLayout;

        public Adapter(final Context context, final int navigationHeight, final boolean hasAppBarLayout, final List<Order> data) {
            this.navigationHeight = navigationHeight;
            this.data = data;
            this.hasAppBarLayout = hasAppBarLayout;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public TwoLinesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.simple_card_item, parent, false);
            final TwoLinesViewHolder holder = new TwoLinesViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final TwoLinesViewHolder holder, final int position) {
            ((MarginLayoutParams) holder.itemView.getLayoutParams()).topMargin = 0;
            if (position == getItemCount() - 1) {
                ((MarginLayoutParams) holder.itemView.getLayoutParams()).bottomMargin = holder.marginBottom + navigationHeight;
            } else if (position == 0 && !hasAppBarLayout) {
                ((MarginLayoutParams) holder.itemView.getLayoutParams()).topMargin = scrollHelper.getToolbarHeight();
            } else {
                ((MarginLayoutParams) holder.itemView.getLayoutParams()).bottomMargin = holder.marginBottom;
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
            if(holder.linear_state.getVisibility() == View.VISIBLE){

            }
            if (item.getVip().equals("1")){
                holder.vipImgv.setVisibility(View.VISIBLE);
            }else {
                holder.vipImgv.setVisibility(View.GONE);
            }

            if(item.isBar_visible()){
                holder.linear_state.setVisibility(View.VISIBLE);
            }else {
                holder.linear_state.setVisibility(View.GONE);
            }
            holder.verticaltrackorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.linear_state.getVisibility() == View.VISIBLE){
                        mRecyclerView.getRecycledViewPool().clear();
                        item.setBar_visible(false);
                        notifyDataSetChanged();


                    }else if(holder.linear_state.getVisibility() == View.GONE) {
                        mRecyclerView.getRecycledViewPool().clear();
                        item.setBar_visible(true);
                        int position = holder.getAdapterPosition();
                        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
                        Order new_item = data.get(position);
                        if (new_item.getOrder_state().toString().equals("D")) {
                            holder.iv_order_start.setImageDrawable(getResources().getDrawable(R.drawable.track_start_done));
                            holder.iv_order_pending.setImageDrawable(getResources().getDrawable(R.drawable.track_pending_done));
                            holder.iv_order_done.setImageDrawable(getResources().getDrawable(R.drawable.track_done_done));
                            if (new_item.getCreated_date() != null) {
                                holder.order_start_date.setVisibility(View.VISIBLE);
                                holder.order_start_date.setText(new_item.getCreated_date());
                            }
                            if (new_item.getUpdated_date() != null) {
                                holder.order_updated_date.setVisibility(View.VISIBLE);
                                holder.order_updated_date.setText(new_item.getUpdated_date());
                            }
                            if (new_item.getOrder_date() != null) {
                                holder.order_done_date.setVisibility(View.VISIBLE);
                                holder.order_done_date.setText(new_item.getOrder_date());
                            }
                            notifyDataSetChanged();

                        } else if (new_item.getOrder_state().toString().equals("P")) {
                            holder.iv_order_start.setImageDrawable(getResources().getDrawable(R.drawable.track_start_done));
                            holder.iv_order_pending.setImageDrawable(getResources().getDrawable(R.drawable.track_pending_done));
                            if (new_item.getCreated_date() != null) {
                                holder.order_start_date.setVisibility(View.VISIBLE);
                                holder.order_start_date.setText(new_item.getCreated_date());
                            }
                            if (new_item.getUpdated_date() != null) {
                                holder.order_updated_date.setVisibility(View.VISIBLE);
                                holder.order_updated_date.setText(new_item.getUpdated_date());
                            }

                            notifyDataSetChanged();

                        } else if (new_item.getOrder_state().toString().equals("A")) {
                            holder.iv_order_start.setImageDrawable(getResources().getDrawable(R.drawable.track_start_done));
                            holder.iv_order_pending.setImageDrawable(getResources().getDrawable(R.drawable.track_pending_done));
                            if (new_item.getCreated_date() != null) {
                                holder.order_start_date.setVisibility(View.VISIBLE);
                                holder.order_start_date.setText(new_item.getCreated_date());
                            }
                            if (new_item.getUpdated_date() != null) {
                                holder.order_updated_date.setVisibility(View.VISIBLE);
                                holder.order_updated_date.setText(new_item.getUpdated_date());
                            }

                            notifyDataSetChanged();

                        } else if (new_item.getOrder_state().toString().equals("H")) {
                            holder.iv_order_start.setImageDrawable(getResources().getDrawable(R.drawable.track_start_done));
                            holder.iv_order_pending.setImageDrawable(getResources().getDrawable(R.drawable.track_pending_done));
                            if (new_item.getCreated_date() != null) {
                                holder.order_start_date.setVisibility(View.VISIBLE);
                                holder.order_start_date.setText(new_item.getCreated_date());
                            }
                            if (new_item.getHold_time() != null) {
                                holder.order_updated_date.setVisibility(View.VISIBLE);
                                holder.order_updated_date.setText(new_item.getHold_time());
                            }
                            notifyDataSetChanged();
                        }
                    }
                }
            });


            if(item.getOrder_state().toString().equals("D")){
                holder.button_status.setBackgroundResource(R.color.colorAccentGreen);
                holder.button_status.setText("DONE");

            }else if(item.getOrder_state().toString().equals("P")){
                holder.button_status.setBackgroundResource(R.color.colorAccentyellow);
                holder.button_status.setText("PENDING");

            }else if(item.getOrder_state().toString().equals("A")){
                holder.button_status.setBackgroundResource(R.color.colorPrimary);
                holder.button_status.setText("APPROVAL");


            }else if(item.getOrder_state().toString().equals("H")){
                holder.button_status.setBackgroundResource(R.color.colorAccentRed);
                holder.button_status.setText("HOLDING");

            }

            if(item.getCreated_date() != null){
                holder.order_date.setText(item.getCreated_date());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }



}
