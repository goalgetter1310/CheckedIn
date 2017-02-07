package com.checkedin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.fragment.CheckinLoungeFrg;
import com.checkedin.utility.Utility;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.material.widget.Button;
import com.material.widget.RadioButton;
import com.material.widget.Spinner;
import com.material.widget.Spinner.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("deprecation")

public class CheckinLoungeFilterDialog extends Dialog implements View.OnClickListener {
    private TextView tvName, tvAge, tvCity, tvGender, tvOccupation, tvEduInst, tvCompany, tvInterest;//tvCountries,
    private ImageView ivBack;

    private EditText etCity, etCompany, etInterests, etOccupation, etEduInst;
    private Spinner spAge;//spCountries,
    private AutoCompleteTextView actName;
    private RadioButton rbMale, rbFemale;

    private Button btnApply;

    private Context context;
    private CompoundButton.OnCheckedChangeListener listener;
    private String[] friendString;
    private CheckinLoungeFrg checkinLoungeFrg;
    private boolean isAgeSelected;
    private View viewCity;


    private String[] ageFilter = {"15-20", "20-25", "25-30", "30-35", "35-40", "40-45", "45-50", "50-55", "55-60"};

    public CheckinLoungeFilterDialog(Context context, String[] friendString, CheckinLoungeFrg checkinLoungeFrg) {
        super(context, R.style.Theme_AppTheme);
        this.context = context;
        this.friendString = friendString;
        this.checkinLoungeFrg = checkinLoungeFrg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_server_filter);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initViews();

        filterSelect();

        tvName.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter_selected));
        tvName.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        tvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        actName.setVisibility(View.VISIBLE);

        spAge.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ageFilter));
//        spCountries.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getAllCountryName()));
        actName.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, friendString));


        spAge.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                isAgeSelected = true;
                return true;
            }
        });

//        spCountries.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public boolean onItemClick(Spinner parent, View view, int position, long id) {
//                isCountrySelected = true;
//                return true;
//            }
//        });

        etCity.setEnabled(false);
        tvName.setOnClickListener(this);
        tvAge.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvGender.setOnClickListener(this);
//        tvCountries.setOnClickListener(this);
        tvOccupation.setOnClickListener(this);
        tvEduInst.setOnClickListener(this);
        tvCompany.setOnClickListener(this);
        tvInterest.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        viewCity.setOnClickListener(this);
        rbMale.setOnCheckedChangeListener(listener);
        rbFemale.setOnCheckedChangeListener(listener);
    }

    private void initViews() {


        tvName = (TextView) findViewById(R.id.tv_server_filter_name);
        tvAge = (TextView) findViewById(R.id.tv_server_filter_age);
        tvCity = (TextView) findViewById(R.id.tv_server_filter_city);
        tvGender = (TextView) findViewById(R.id.tv_server_filter_gender);
//        tvCountries = (TextView) findViewById(R.id.tv_server_filter_country);
        tvOccupation = (TextView) findViewById(R.id.tv_server_filter_occupation);
        tvEduInst = (TextView) findViewById(R.id.tv_server_filter_edu_inst);
        tvCompany = (TextView) findViewById(R.id.tv_server_filter_company);
        tvInterest = (TextView) findViewById(R.id.tv_server_filter_interest);


        ivBack = (ImageView) findViewById(R.id.iv_back);

        etCity = (EditText) findViewById(R.id.et_server_filter_city);
        viewCity = findViewById(R.id.view_city);
        etCompany = (EditText) findViewById(R.id.et_server_filter_company);
        etInterests = (EditText) findViewById(R.id.et_server_filter_interest);
        etOccupation = (EditText) findViewById(R.id.et_server_filter_occupation);
        etEduInst = (EditText) findViewById(R.id.et_server_filter_eduInst);

//        spCountries = (Spinner) findViewById(R.id.sp_server_filter_country);
        spAge = (Spinner) findViewById(R.id.sp_server_filter_age);

        actName = (AutoCompleteTextView) findViewById(R.id.act_server_filter_name);

        rbMale = (RadioButton) findViewById(R.id.rb_server_filter_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_server_filter_female);


        btnApply = (Button) findViewById(R.id.btn_server_filter_apply);
        listener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbMale.setChecked(rbMale == buttonView);
                    rbFemale.setChecked(rbFemale == buttonView);
                }

            }

        };
    }

    private void filterSelect() {
        tvName.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvAge.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvCity.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvGender.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
//        tvCountries.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvOccupation.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
//        tvCountries.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvEduInst.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvCompany.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        tvInterest.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);

        tvName.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
        tvAge.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
        tvCity.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
        tvGender.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
//        tvCountries.setBackgroundColor(Utility.colorRes(getContext(),R.color.server_filter));
        tvOccupation.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
//        tvCountries.setBackgroundColor(Utility.colorRes(getContext(),R.color.server_filter));
        tvEduInst.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
        tvCompany.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));
        tvInterest.setBackgroundColor(Utility.colorRes(getContext(), R.color.server_filter));

        tvName.setTextColor(Color.WHITE);
        tvAge.setTextColor(Color.WHITE);
        tvCity.setTextColor(Color.WHITE);
        tvGender.setTextColor(Color.WHITE);
//        tvCountries.setTextColor(Color.WHITE);
        tvOccupation.setTextColor(Color.WHITE);
        tvEduInst.setTextColor(Color.WHITE);
        tvCompany.setTextColor(Color.WHITE);
        tvInterest.setTextColor(Color.WHITE);

        etCity.setVisibility(View.GONE);
        etCompany.setVisibility(View.GONE);
        etInterests.setVisibility(View.GONE);
        etOccupation.setVisibility(View.GONE);
        etEduInst.setVisibility(View.GONE);

        spAge.setVisibility(View.GONE);
//        spCountries.setVisibility(View.GONE);

        actName.setVisibility(View.GONE);

        rbMale.setVisibility(View.GONE);
        rbFemale.setVisibility(View.GONE);
    }

    private ArrayList<String> getAllCountryName() {
        ArrayList<String> alCountries = new ArrayList<>();
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !alCountries.contains(country)) {
                alCountries.add(country);
            }
        }
        Collections.sort(alCountries);
        return alCountries;
    }

    private String getValues() {
        String nameFilter, cityFilter, occupationFilter, eduInstFilter, companyFilter, interestFilter;

        Map<String, String> alFilterValue = new HashMap<>();
        nameFilter = actName.getText().toString();
        if (!TextUtils.isEmpty(nameFilter)) {
            alFilterValue.put("name", nameFilter);
        }

        if (isAgeSelected) {
            String age = ((TextView) spAge.getSelectedView()).getText().toString();

            String[] ageFilter = age.split("-");

            alFilterValue.put("minAge", ageFilter[0]);
            alFilterValue.put("maxAge", ageFilter[1]);
        }

        cityFilter = etCity.getText().toString();
        if (!TextUtils.isEmpty(cityFilter)) {
            alFilterValue.put("town", cityFilter);
        }

        if (rbMale.isChecked()) {
            alFilterValue.put("gender", "Male");
        } else if (rbFemale.isChecked()) {
            alFilterValue.put("gender", "Female");
        }

//        if (isCountrySelected) {
//            alFilterValue.put("country", ((TextView) spCountries.getSelectedView()).getText().toString());
//        }
        occupationFilter = etOccupation.getText().toString();
        if (!TextUtils.isEmpty(occupationFilter)) {
            alFilterValue.put("vDesignation", occupationFilter);
        }

        eduInstFilter = etEduInst.getText().toString();
        if (!TextUtils.isEmpty(eduInstFilter)) {
            alFilterValue.put("educationInstitute", eduInstFilter);
        }

        companyFilter = etCompany.getText().toString();
        if (!TextUtils.isEmpty(companyFilter)) {
            alFilterValue.put("company", companyFilter);
        }

        interestFilter = etInterests.getText().toString();
        if (!TextUtils.isEmpty(interestFilter)) {
            alFilterValue.put("interest", interestFilter);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            for (Entry<String, String> entry : alFilterValue.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void placeSelect() {
        ((MainActivity) context).setLoungeAddress(new LoungeAddress() {
            @Override
            public void onLoungeAddresFilter(String address) {
                etCity.setText(address);
            }
        });
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build((Activity) context);
            ((Activity) context).startActivityForResult(intent, 500);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    public interface LoungeAddress {
        void onLoungeAddresFilter(String address);
    }


    private void clearFilter() {
        actName.setText("");
        etCity.setText("");
        rbFemale.setChecked(false);
        rbMale.setChecked(false);
        etOccupation.setText("");
        etEduInst.setText("");
        etCompany.setText("");
        etInterests.setText("");
        isAgeSelected = false;
    }


    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();


            switch (v.getId()) {
                case R.id.tv_server_filter_name:
                    filterSelect();
                    tvName.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvName.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    actName.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_age:
                    filterSelect();
                    tvAge.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvAge.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvAge.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    spAge.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_city:
                    filterSelect();
                    tvCity.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvCity.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvCity.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    etCity.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_gender:
                    filterSelect();
                    tvGender.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvGender.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvGender.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    rbMale.setVisibility(View.VISIBLE);
                    rbFemale.setVisibility(View.VISIBLE);
                    break;
//                case R.id.tv_server_filter_country:
//                    filterSelect();
//                    tvCountries.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
//                    tvCountries.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//                    tvCountries.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
//                    spCountries.setVisibility(View.VISIBLE);
//                    break;
                case R.id.tv_server_filter_occupation:
                    filterSelect();
                    tvOccupation.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvOccupation.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvOccupation.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    etOccupation.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_edu_inst:
                    filterSelect();
                    tvEduInst.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvEduInst.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvEduInst.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    etEduInst.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_company:
                    filterSelect();
                    tvCompany.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvCompany.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvCompany.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    etCompany.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_server_filter_interest:
                    filterSelect();
                    tvInterest.setBackgroundColor(context.getResources().getColor(R.color.server_filter_selected));
                    tvInterest.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tvInterest.getCompoundDrawables()[1].setColorFilter(Utility.colorRes(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    etInterests.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_server_filter_apply:
                    checkinLoungeFrg.onFilterApply(getValues());
                    dismiss();
                    break;
                case R.id.view_city:
                    placeSelect();
                    break;
                case R.id.tv_clear:
                    clearFilter();
                    checkinLoungeFrg.onFilterApply("");
                case R.id.iv_back:
                    dismiss();

                    break;
            }
        }
    }


}
