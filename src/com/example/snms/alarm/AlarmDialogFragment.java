package com.example.snms.alarm;


import com.actionbarsherlock.app.SherlockDialogFragment;
import com.example.snms.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class AlarmDialogFragment extends SherlockDialogFragment   {
	
		String prey; 
		
		AlarmDialogFragment(String prey){
			this.prey = prey;
		}
	 	
	    @SuppressLint("ValidFragment")
		public static AlarmDialogFragment newInstance(String prey) {
	    	AlarmDialogFragment f = new AlarmDialogFragment(prey);
	        return f;
	    }
	 
	 
	   @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        //getting proper access to LayoutInflater is the trick
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        View view = inflater.inflate(R.layout.alarmdialog, null);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
	        builder.setView(view);
	      
	       
	        return builder.create();
	    }

}
