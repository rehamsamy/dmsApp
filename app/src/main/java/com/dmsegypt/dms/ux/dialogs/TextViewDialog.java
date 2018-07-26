package com.dmsegypt.dms.ux.dialogs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmsegypt.dms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mahmoud on 29/11/17.
 */


public class TextViewDialog extends DialogFragment {
    public static final String EXTRA_TEXT="extra_text";
    private View rootView;
    private Unbinder unbinder;

    @BindView(R.id.tv_alltext)
    TextView tv_alltext;

    public static TextViewDialog newInstance(String textString) {

        Bundle args = new Bundle();
        TextViewDialog fragment = new TextViewDialog();
        args.putString(EXTRA_TEXT,textString);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_text_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        String text=getArguments().getString(EXTRA_TEXT);
        if(!text.equals("") || text != null){
            tv_alltext.setText(text);
        }

        return  rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
