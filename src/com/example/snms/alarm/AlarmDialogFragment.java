package com.example.snms.alarm;


import com.actionbarsherlock.app.SherlockDialogFragment;
import com.example.snms.PreyOverView;
import com.example.snms.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class AlarmDialogFragment extends SherlockDialogFragment implements OnClickListener  {
	
		String prey; 
		PreyOverView preyActivity;
		Button okButton; 
		
		
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
	        preyActivity = (PreyOverView) getActivity();
	        View view = inflater.inflate(R.layout.alarmdialog, null);
	        okButton = (Button)view.findViewById(R.id.okAlarm);
	        okButton.setOnClickListener(this);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
	  
	        builder.setView(view);
	        return builder.create();
	    }

	@Override
	public void onClick(View v) {
		if(v.equals(okButton)){
			preyActivity.setAlarm(prey, 10);
			this.dismiss();
		}
		
	}



}
