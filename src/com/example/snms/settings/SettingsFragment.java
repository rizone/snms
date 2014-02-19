package com.example.snms.settings;

import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.database.SnmsDAO;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class SettingsFragment extends Fragment implements
		OnItemSelectedListener, OnCheckedChangeListener {

	SnmsDAO dao;

	Spinner location;
	Spinner calcMethod;
	Spinner jurMethod;
	Spinner adjustMethod;
	CheckBox hanaFi;
	CheckBox icc;
	CheckBox avansert;
	RelativeLayout avansertContainer;
	ArrayAdapter<CharSequence> locationAdapter;
	ArrayAdapter<CharSequence> calcAdapter;
	ArrayAdapter<CharSequence> adjustMethodAdapter;
	ArrayAdapter<CharSequence> jurMethodAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		dao = ((PreyOverView) getActivity()).getDAO();
		View root = inflater.inflate(R.layout.settings, null);

		hanaFi = (CheckBox) root.findViewById(R.id.hanafi);
		icc = (CheckBox) root.findViewById(R.id.icc);
		avansert = (CheckBox) root.findViewById(R.id.avansert);

		hanaFi.setOnCheckedChangeListener(this);
		icc.setOnCheckedChangeListener(this);
		avansert.setOnCheckedChangeListener(this);
		avansertContainer = (RelativeLayout) root
				.findViewById(R.id.avansertContainer);

		location = (Spinner) root.findViewById(R.id.location);
		calcMethod = (Spinner) root.findViewById(R.id.calcMethod);
		adjustMethod = (Spinner) root.findViewById(R.id.adjustMethod);
		jurMethod = (Spinner) root.findViewById(R.id.jurMethod);

		locationAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.location, android.R.layout.simple_spinner_item);
		locationAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		location.setAdapter(locationAdapter);

		calcAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.calculation_methods,
				android.R.layout.simple_spinner_item);
		calcAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		calcMethod.setAdapter(calcAdapter);

		adjustMethodAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.adjust_methods, android.R.layout.simple_spinner_item);
		adjustMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adjustMethod.setAdapter(adjustMethodAdapter);

		jurMethodAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.jurstic_methods, android.R.layout.simple_spinner_item);
		jurMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jurMethod.setAdapter(jurMethodAdapter);

		location.setOnItemSelectedListener(this);
		calcMethod.setOnItemSelectedListener(this);
		adjustMethod.setOnItemSelectedListener(this);
		jurMethod.setOnItemSelectedListener(this);

		renderSettingState();

		return root;
	}

	void renderSettingState() {
		if (dao.getSettingsValue("hanfi") != null) {
			hanaFi.setChecked(true);
			icc.setChecked(false);
			avansert.setChecked(false);
		} else if (dao.getSettingsValue("icc") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(true);
			avansert.setChecked(false);
		} else if (dao.getSettingsValue("avansert") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(false);
			avansert.setChecked(true);
			
			if(dao.getSettingsValue("jurmethod")!=null){
				jurMethod.setSelection(jurMethodAdapter.getPosition(dao.getSettingsValue("jurmethod"))); 
			}
			if(dao.getSettingsValue("adjustmethod")!=null){
				adjustMethod.setSelection(adjustMethodAdapter.getPosition(dao.getSettingsValue("adjustmethod"))); 
			}
			if(dao.getSettingsValue("location")!=null){
				location.setSelection(locationAdapter.getPosition(dao.getSettingsValue("location"))); 
			}
			if(dao.getSettingsValue("calcmethod")!=null){
				calcMethod.setSelection(calcAdapter.getPosition(dao.getSettingsValue("calcmethod"))); 
			}
			
		} else {
			hanaFi.setChecked(false);
			icc.setChecked(true);
			avansert.setChecked(false);
			dao.saveSetting("icc", "true");
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		if (arg0 == calcMethod) {
			String calcMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("calcmethod", calcMethod);
		}
		if (arg0 == adjustMethod) {
			String adjustMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("adjustmethod", adjustMethod);
		}
		if (arg0 == jurMethod) {
			String jurMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("jurmethod", jurMethod);
		}
		if (arg0 == location) {
			String locationString = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("location", locationString);
		}
	}
	
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.equals(avansert) && isChecked) {
			dao.saveSetting("avansert", "true");
			dao.deleteSetting("hanfi");
			avansertContainer.setVisibility(View.VISIBLE);
			dao.deleteSetting("icc");
			icc.setChecked(false);
			hanaFi.setChecked(false);
		}
		if (buttonView.equals(hanaFi) && isChecked) {
			dao.saveSetting("hanfi", "true");
			dao.deleteSetting("avansert");
			avansertContainer.setVisibility(View.GONE);
			dao.deleteSetting("icc");
			icc.setChecked(false);
			avansert.setChecked(false);
		}
		if (buttonView.equals(icc) && isChecked) {
			dao.saveSetting("icc", "true");
			dao.deleteSetting("hanfi");
			dao.deleteSetting("avansert");
			avansertContainer.setVisibility(View.GONE);
			hanaFi.setChecked(false);
			avansert.setChecked(false);
		}

	}

}
