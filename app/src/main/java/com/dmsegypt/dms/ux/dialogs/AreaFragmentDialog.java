package com.dmsegypt.dms.ux.dialogs;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.app.App;
import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.ux.adapter.AreaAdapter;
import com.dmsegypt.dms.ux.adapter.InfiniteRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amr on 29/03/2018.
 */


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
     * Created by amr on 16/02/2018.
     */

    public class AreaFragmentDialog extends DialogFragment implements AreaAdapter.OnItemClickListener {
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        ArrayList<AreaItem> areaItems;
        AreaAdapter areaAdapter;
        @BindView(R.id.searchEdit)
        EditText searchEdit;
    OnAreaClickListner listner;


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
            View view=inflater.inflate(R.layout.dialog_area,container,false);
            ButterKnife.bind(this,view);
            iniView();

            return view;
        }

        private void iniView(){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            areaItems =new ArrayList<>();
            areaAdapter =new AreaAdapter(getContext(), areaItems,null);
            areaAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(areaAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
            getAreas();

        };

        @Override
        public void OnItemClicked(int pos) {
            AreaItem country= areaItems.get(pos);
            listner.OnAreaClicked(country);
            dismiss();
        }

        void getAreas(){
            areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_PROGRESS);
            App.getInstance().getService().getAreas("-1","En",0).enqueue(new Callback<ResponseAreas>() {
                @Override
                public void onResponse(Call<ResponseAreas> call, Response<ResponseAreas> response) {
                    List<AreaItem> list= response.body().getList();
                    if (!list.isEmpty()){
                        areaItems.addAll(list);
                        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_LIST);
                    }else {
                        areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);

                    }
                }

                @Override
                public void onFailure(Call<ResponseAreas> call, Throwable t) {
                    areaAdapter.setModeAndNotify(InfiniteRecyclerViewAdapter.MODE_EMPTY);
                }
            });
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog= super.onCreateDialog(savedInstanceState);
            dialog.setTitle(R.string.title_Area);
            return dialog;
        }
        public interface OnAreaClickListner{
        void OnAreaClicked(AreaItem item);
    }

    public void setListner(OnAreaClickListner listner) {
        this.listner = listner;
    }
}

