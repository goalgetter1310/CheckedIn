package com.checkedin.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.checkedin.R;
import com.checkedin.utility.Utility;

public class CountryListDialog extends Dialog implements OnItemClickListener {

	private Context context;
	private ListView lvCountryName;
	private Object countryName;
	private ArrayList<String> alCountries;

	public CountryListDialog(Context context, Object countryName) {
		super(context);
		this.context = context;
		this.countryName = countryName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_country_list);

		initViews();
		getAllCountryName();
		lvCountryName.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, alCountries));
		lvCountryName.setOnItemClickListener(this);
	}

	private void initViews() {
		alCountries = new ArrayList<>();
		lvCountryName = (ListView) findViewById(R.id.lv_country_name);
	}

	private void getAllCountryName() {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			if (country.trim().length() > 0 && !alCountries.contains(country)) {
				alCountries.add(country);
			}
		}
		Collections.sort(alCountries);
	}

	public interface CountryName {
		void onCountrySeleted(String countryName);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
			Utility.doubleTapTime = System.currentTimeMillis();
			((CountryName) countryName).onCountrySeleted(alCountries.get(position));
			dismiss();
		}
	}
}
