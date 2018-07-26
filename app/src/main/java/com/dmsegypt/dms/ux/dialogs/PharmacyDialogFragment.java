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
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseProviders;
import com.dmsegypt.dms.ux.adapter.AreaAdapter;
import com.dmsegypt.dms.ux.adapter.InfiniteRecyclerViewAdapter;
import com.dmsegypt.dms.ux.adapter.PharmacyAdapter;
import com.dmsegypt.dms.ux.custom_view.ProgressableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PharmacyDialogFragment extends DialogFragment implements PharmacyAdapter.OnItemClickListener, InfiniteRecyclerViewAdapter.LoadMoreListener {
    private static final String EXTRA_CITY ="extra_city" ;
    private static final String EXTRA_AREA ="extra_area" ;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ArrayList<Provider> areaItems;
    PharmacyAdapter areaAdapter;
    OnPharmcyClickListner listner;

    public static PharmacyDialogFragment newInstance(String city,String area) {

        Bundle args = new Bundle();
        PharmacyDialogFragment fragment = new PharmacyDialogFragment();
        args.putString(EXTRA_CITY,city);
        args.putString(EXTRA_AREA,area);
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
        View view=inflater.inflate(R.layout.dialog_pharmacy,container,false);
        ButterKnife.bind(this,view);
        iniView();

        return view;
    }

    private void iniView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        areaItems =new ArrayList<>();
        areaAdapter =new PharmacyAdapter(getContext(), areaItems,this);
        areaAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(areaAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        getProvider("0");

    };

    @Override
    public void OnItemClicked(int pos) {
        Provider provider= areaItems.get(pos);
        listner.OnPharmcyClicked(provider);
        dismiss();
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.title_Area);
        return dialog;
    }

    @Override
    public void onMoreRequested() {
        getProvider(areaItems.size()+"");
    }

    public interface OnPharmcyClickListner{
        void OnPharmcyClicked(Provider item);
    }

    public void setListner(OnPharmcyClickListner listner) {
        this.listner = listner;
    }

    private void getProvider(final String index) {
        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
        App.getInstance().getService().getProviders(
                getArguments().getString(EXTRA_CITY),
                "-1",
                "2",
                "En",
                index,
                "-1",
                getArguments().getString(EXTRA_AREA),
                Constants.KEY_SEARCH_ID
                , "-1"
        ).enqueue(new Callback<ResponseProviders>() {
            @Override
            public void onResponse(Call<ResponseProviders> call, Response<ResponseProviders> response) {
                if (response.body()!=null){
                    if (response.body().getMessage().getCode()==1){
                        if (response.body().getList().isEmpty()){
                            areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);

                        }else {
                            areaItems.addAll(response.body().getList());
                            areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
                        }
                    }else {
                        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                    }

                }else {

                    areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                }

            }

            @Override
            public void onFailure(Call<ResponseProviders> call, Throwable t) {
                areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
            }
        });
    }




}
