package com.example.a233.staringatghost;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class sprite {
    private Bitmap bitmap = null;
    private float x = 0;
    private float y = 0;
    private boolean visible = true;

    public sprite(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public void update(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public float getWidth(){
        if(bitmap != null){
            return bitmap.getWidth();
        }
        return 0;
    }
    public float getHeight(){
        if(bitmap != null){
            return bitmap.getHeight();
        }
        return 0;
    }

    public RectF getRectF(){
        float left = x;
        float top = y;
        float right = left + getWidth();
        float bottom = top + getHeight();
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }
    public Rect getBitmapSrcRec(){
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = (int)getWidth();
        rect.bottom = (int)getHeight();
        return rect;
    }
    public void centerTo(float centerX, float centerY){
        float w = getWidth();
        float h = getHeight();
        x = centerX - w / 2;
        y = centerY - h / 2;
    }
    public void draw(Canvas canvas, Paint paint){
        if(visible && this.bitmap != null){
            //将Sprite绘制到Canvas上
            Rect srcRef = getBitmapSrcRec();
            RectF dstRecF = getRectF();
            //canvas.drawBitmap(this.bitmap, x, y, paint);
            canvas.drawBitmap(bitmap, srcRef, dstRecF, paint);
        }
    }
    public void hidden(){
        visible= false;
    }
}
