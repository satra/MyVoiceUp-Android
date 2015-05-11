package edu.mit.voicesurvey.androidapplication.controllers.registrationflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * http://stackoverflow.com/questions/19654179/getting-signature-from-the-user-as-input-in-android-eclipse
 */
public class SignatureView extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback {

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setBackgroundColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        this.setOnTouchListener(this);
        this.getHolder().addCallback(this);
    }

    public void setStrokeWidth(float width){
        mPaint.setStrokeWidth(width);
        this.invalidate();
    }

    public void setColor(int color){
        mPaint.setColor(color);
        this.invalidate();
    }

    public void clear(){
        mDots = new ArrayList<List<Dot>>();
        //To prevent an exception
        mDots.add(new ArrayList<Dot>());
        this.invalidate();
    }

    private List<List<Dot>> mDots = new ArrayList<List<Dot>>();

    private class Dot{

        public float X = 0;
        public float Y = 0;

        public Dot(float x, float y){
            X = x;
            Y = y;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDots.add(new ArrayList<Dot>());
                mDots.get(mDots.size() - 1).add(new Dot(event.getX(), event.getY()));
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mDots.get(mDots.size() - 1).add(new Dot(event.getX(), event.getY()));
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mDots.get(mDots.size() - 1).add(new Dot(event.getX(), event.getY()));
                this.invalidate();
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

    public Paint mPaint;
    @Override
    protected void onDraw(Canvas canvas) {
        for(List<Dot> dots : mDots){
            for(int i = 0; i < dots.size(); i++){
                if(i - 1 == -1)
                    continue;
                canvas.drawLine(dots.get(i - 1).X, dots.get(i - 1).Y, dots.get(i).X, dots.get(i).Y, mPaint);
            }
        }
    }

    public Bitmap getBitmap(){
        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        this.draw(c);
        return b;
    }

    public boolean exportFile(String pathString, String fileString){
        File path = new File(pathString);
        path.mkdirs();
        if(!fileString.toLowerCase(Locale.ENGLISH).contains(".png")){
            fileString += ".png";
        }
        File file = new File(path, fileString);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            this.getBitmap().compress(Bitmap.CompressFormat.PNG, 90, out);
            return true;
        } catch (FileNotFoundException e) {
        }
        return false;
    }
}