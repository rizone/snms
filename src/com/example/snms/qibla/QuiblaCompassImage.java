package com.example.snms.qibla;

import com.example.snms.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Matrix;
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
	        	
	         
	          	System.out.println("x:" + x + " y:" +y);
	       	android.graphics.Matrix scaleMatrix = new android.graphics.Matrix();
	          	scaleMatrix.postTranslate((float)canvas.getWidth()/2, (float)canvas.getHeight()/2);
	      canvas.setMatrix(scaleMatrix);
	          	//	scaleMatrix.postTranslate(x, y);
	   
	          	Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
	          			R.drawable.ic_qibla);
	          	p.setColor(Color.RED);
	           	canvas.drawCircle(x, y, 10, p);
	          	canvas.drawBitmap(icon, x, y, p);
	         	
	        	canvas.drawLine(0, 0, x,y, p);
	        	canvas.drawCircle(x, y, 10, p);
	        	//canvas.drawl
	        }

	    }
	    
	    
	    public void setAngle(Float angle){
	    	this.angle = angle; 
	    }
	
	    
	
}
