package com.dmsegypt.dms.ux.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dmsegypt.dms.R;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.utils.DataUtils;
import com.dmsegypt.dms.utils.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed Abdallah on 09/03/2017.
 **/

public class ProviderDetailsView extends FrameLayout {
    @BindView(R.id.tvMoreDetails)
    TextView tvMoreDetails;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvType)
    TextView tvType;
    private View container;

    public ProviderDetailsView(@NonNull Context context) {
        super(context);
        this.initialize();
    }

    public ProviderDetailsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public ProviderDetailsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProviderDetailsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.container_provider_details, null);
        addView(v);
        ButterKnife.bind(this);
    }

    public void initData(Provider provider, View container) {
        tvAddress.setText((!provider.getAddress().isEmpty()) ?
                provider.getAddress() :
                getContext().getString(R.string.not_available_address));
        tvEmail.setText((!provider.getEmail().isEmpty()) ?
                provider.getEmail() :
                getContext().getString(R.string.not_available_email));
        tvPhone.setText((!provider.getTel().isEmpty()) ?
                provider.getTel() :
                getContext().getString(R.string.not_available_phone));
        tvType.setText(DataUtils.getTypeNameById(provider.getProviderType()));
        this.container = container;
    }

    @OnClick({R.id.tvMoreDetails, R.id.tvAddress, R.id.tvEmail, R.id.tvPhone, R.id.tvType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMoreDetails:
                NotificationUtils.showSnackbar(getContext(), container,
                        getContext().getString(R.string.info_more_details) + " : " + tvMoreDetails.getText().toString(), Snackbar.LENGTH_SHORT);
                break;
            case R.id.tvAddress:
                NotificationUtils.showSnackbar(getContext(), container,
                        getContext().getString(R.string.info_address) + " : " + tvAddress.getText().toString(), Snackbar.LENGTH_SHORT);
                break;

            case R.id.tvEmail:
                NotificationUtils.showSnackbar(getContext(), container,
                        getContext().getString(R.string.info_email) + " : " + tvEmail.getText().toString(), Snackbar.LENGTH_SHORT);
                break;
            case R.id.tvPhone:
                NotificationUtils.showSnackbar(getContext(), container,
                        getContext().getString(R.string.info_phone_number) + " : " + tvPhone.getText().toString(), Snackbar.LENGTH_SHORT);
                break;
            case R.id.tvType:
                NotificationUtils.showSnackbar(getContext(), container,
                        getContext().getString(R.string.info_type) + " : " + tvType.getText().toString(), Snackbar.LENGTH_SHORT);
                break;
        }
    }
}
