package com.example.snms.qibla;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class QuiblaCompassImage extends ImageView{
	
	
	   Float angle = null;  
	
	   public QuiblaCompassImage(Context context,AttributeSet attributeSet) {
	        super(context,attributeSet);
	        // TODO Auto-generated constructor stub
	    }
	   


	    @Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        if(angle!=null) {
	        	Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
	        	float x = (float) ((canvas.getWidth()/2.0)*Math.cos(angle*(Math.PI/180)));
	        	float y = (float) ((canvas.getWidth()/2.0)*Math.sin(angle*(Math.PI/180)));
	          	canvas.drawLine((float)canvas.getWidth()/2, (float)canvas.getHeight()/2, x,y, p);
	        	//canvas.drawl
	        }

	    }
	    
	    
	    public void setAngle(Float angle){
	    	this.angle = angle; 
	    }
	
	    
	
}
