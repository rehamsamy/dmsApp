package com.dmsegypt.dms.ux.dialogs;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.app.Constants;
import com.dmsegypt.dms.rest.model.DeveloperComment;
import com.dmsegypt.dms.rest.model.Pharmacy;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.Response.GetPharmaciesReponse;
import com.dmsegypt.dms.rest.model.Response.ResponseProviders;
import com.dmsegypt.dms.ux.adapter.InfiniteRecyclerViewAdapter;
import com.dmsegypt.dms.ux.adapter.PharmacyAdapter;
import com.dmsegypt.dms.ux.adapter.ProviderAdminAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderDailogFragment  extends DialogFragment implements ProviderAdminAdapter.OnItemClickListener, InfiniteRecyclerViewAdapter.LoadMoreListener {
    private static final String EXTRA_Type = "extra_type";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ArrayList<Pharmacy> areaItems;
    ProviderAdminAdapter areaAdapter;
    OnPharmcyClickListner listner;
    @BindView(R.id.searchEdit)
     EditText searchEt;

    public static ProviderDailogFragment newInstance( String type) {

        Bundle args = new Bundle();
        ProviderDailogFragment fragment = new ProviderDailogFragment();
        args.putString(EXTRA_Type, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.75), (int) (size.y * 0.75));
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_area, container, false);
        ButterKnife.bind(this, view);
        iniView();

        return view;
    }
   @OnClick(R.id.searchButton)
   void searchProvider(){
        getProvider(searchEt.getText().toString().trim(),"0");
   }
    private void iniView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        areaItems = new ArrayList<>();
        areaAdapter = new ProviderAdminAdapter(getContext(), areaItems, this);
        areaAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(areaAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        getProvider("-1","0");

    }

    ;

    @Override
    public void OnItemClicked(int pos) {
        Pharmacy provider = areaItems.get(pos);
        listner.OnPharmcyClicked(provider);
        dismiss();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.title_Area);
        return dialog;
    }

    @Override
    public void onMoreRequested() {
        getProvider(!searchEt.getText().toString().trim().isEmpty()?searchEt.getText().toString().trim():"-1",areaItems.size() + "");
    }

    public interface OnPharmcyClickListner {
        void OnPharmcyClicked(Pharmacy item);
    }

    public void setListner(OnPharmcyClickListner listner) {
        this.listner = listner;
    }

    private void getProvider(String query,String index) {
        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        App.getInstance().getService().getPharmacies(query, getArguments().getString(EXTRA_Type), index).enqueue(new Callback<GetPharmaciesReponse>() {
            @Override
            public void onResponse(Call<GetPharmaciesReponse> call, Response<GetPharmaciesReponse> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().getCode() == 1) {
                        if (response.body().getList().isEmpty()) {
                            areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);

                        } else {
                            areaItems.addAll(response.body().getList());
                            areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
                        }
                    } else {
                        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                    }

                } else {

                    areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                }

            }

            @Override
            public void onFailure(Call<GetPharmaciesReponse> call, Throwable t) {
                areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            }
        });
    }


}