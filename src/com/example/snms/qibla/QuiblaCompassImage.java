package com.example.snms.qibla;

import com.example.snms.R;

import android.annotation.SuppressLint;
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
	   


	    @SuppressLint("ResourceAsColor")
		@Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        if(angle!=null) {
	        	angle = 133.0f;
	        	canvas.translate((float)(canvas.getWidth()/2.0), (float)(canvas.getHeight()/2.0));
	        	Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
	        	float x = (float) ((canvas.getWidth()/2.0)*Math.cos(angle*(Math.PI/180)));
	        	float y = (float) ((canvas.getWidth()/2.0)*Math.sin(angle*(Math.PI/180)));

	          	//	scaleMatrix.postTranslate(x, y);
	          	Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
	          			R.drawable.ic_qibla);
	          	p.setColor(Color.BLUE);
	           	canvas.drawCircle(x-(-25), y, 30, p);
	          	canvas.drawBitmap(icon, x, y, p);
	        	canvas.drawLine(0, 0, x,y, p);
	        	
	        	canvas.restore();
	        	//canvas.drawl
	        }

	    }
	    
	    
	    public void setAngle(Float angle){
	    	this.angle = angle; 
	    }
	
	    
	
}
