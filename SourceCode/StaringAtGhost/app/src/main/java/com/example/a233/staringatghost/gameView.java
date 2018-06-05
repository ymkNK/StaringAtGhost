package com.example.a233.staringatghost;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class gameView extends View {

    private sprite littleMan = null;
    //工具
    private Paint paint;
    private Paint textPaint;
    private float density = getResources().getDisplayMetrics().density;//屏幕密度

    private AssetManager mgr=getContext().getAssets();//Assets管理器
    private Typeface tf=Typeface.createFromAsset(mgr, "fonts/SHOWG.TTF");;//默认的字体
    private float fontSize = 0x1e*3;//默认的字体大小
    private float fontSize2 = 0x1e*2;//用于在Game Over的时候绘制Dialog中的文本
    private float borderSize = 2;//Game Over的Dialog的边框
    private Rect continueRect = new Rect();//"继续"、"重新开始"按钮的Rect
    private Rect backHome = new Rect();
    private Rect upload = new Rect();
    //头部状态
    public static final int TURN_LEFT = 1;
    public static final int TURN_FRONT = 2;
    public static final int TURN_RIGHT = 3;
    public static final int TURN_TOO_MUCH = 0;
    public static final int STATUS_GAME_STARTED = 1;//游戏开始
    public static final int STATUS_GAME_PAUSED = 2;//游戏暂停
    public static final int STATUS_GAME_OVER = 3;//游戏结束
    public static final int STATUS_GAME_DESTROYED = 4;//游戏销毁
    private int status = STATUS_GAME_DESTROYED;//初始为销毁状态

    private Bitmap pause = BitmapFactory.decodeResource(getResources(),R.drawable.pause1);
    private Bitmap man = null;// 小黑人
    private Bitmap gui1 = BitmapFactory.decodeResource(getResources(),R.drawable.gui1);
    private Bitmap gui2 = BitmapFactory.decodeResource(getResources(),R.drawable.gui2);
    private Bitmap gui3 = BitmapFactory.decodeResource(getResources(),R.drawable.gui3);
    private sprite leftGhost = new sprite(gui1);
    private sprite frontGhost = new sprite(gui2);
    private sprite rightGhost = new sprite(gui3);
    private int currentDir = 2;
    private int currentGho;
    private long frame = 0;
    private long endFrame = 40;
    private long score = 0;
    private int liveTime = 40;//动画时间
    game activity;

    //构造
    public gameView(Context context){
        super(context);
        activity = (game)context;
        //initsomething?
        init(null);
    }
    public gameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (game)context;
        init(attrs);
    }
    private void init(AttributeSet attrs){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        //设置textPaint，设置为抗锯齿，且是粗体
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        textPaint.setColor(0xff000000);
        fontSize = textPaint.getTextSize();
        fontSize *= density;
        textPaint.setTextSize(fontSize);
        textPaint.setTypeface(tf);
        if((int)(Math.random()*2+1)==1){
            currentGho = 1;
        }
        else{
            currentGho = 3;
        }
    }
    private void restart(){
        score = 0;
        frame = 0;
        endFrame = 40;
        liveTime = 40;
        currentDir = TURN_FRONT;
        status = STATUS_GAME_STARTED;
        if((int)(Math.random()*2+1)==1){
            currentGho = 1;
        }
        else{
            currentGho = 3;
        }
        postInvalidate();
    }

    public void pause(){
        //将游戏设置为暂停状态
        status = STATUS_GAME_PAUSED;
    }

    private void resume(){
        //将游戏设置为运行状态
        status = STATUS_GAME_STARTED;
        postInvalidate();
    }
    //游戏状态转换
    public void start(){
        littleMan = new sprite(man);
        status = STATUS_GAME_STARTED;
    }

    //调用接口
    public void clockwise(){
        currentDir = (currentDir + 1) % 4;
        if(currentDir == TURN_TOO_MUCH){
            status = STATUS_GAME_OVER;
        }
    }

    public void anticlock(){
        currentDir = (currentDir +3 ) % 4;
        if(currentDir == TURN_TOO_MUCH){
            status = STATUS_GAME_OVER;
        }
    }

    //draw
    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);

        if(status == STATUS_GAME_STARTED){
            drawGameStarted(canvas);
        }else if(status == STATUS_GAME_PAUSED){
            drawGamePaused(canvas);
        }else if(status == STATUS_GAME_OVER){
            drawGameOver(canvas);
        }
    }

    private void drawGameStarted(Canvas canvas){
        drawScore(canvas);
        if(currentDir == TURN_FRONT){
            man = BitmapFactory.decodeResource(getResources(),R.drawable.front);
        }
        else if(currentDir == TURN_LEFT){
            man = BitmapFactory.decodeResource(getResources(),R.drawable.left);
        }
        else if(currentDir == TURN_RIGHT){
            man = BitmapFactory.decodeResource(getResources(),R.drawable.right);
        }
        //死掉要画吗？
        if(score < 100){
            liveTime = 40 - (int)score/5;
        }
        else{
            liveTime = 20;
        }
        littleMan.update(man);
        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() - littleMan.getHeight() / 2;
        littleMan.centerTo(centerX,centerY);
        littleMan.draw(canvas, paint);//this不知道是否要传

        if(currentDir == currentGho){
            renewGhost();
        }
        else{
            if(frame > endFrame){
                status = STATUS_GAME_OVER;
            }
        }

        if(currentGho == 1){
            float X = canvas.getWidth()/4;
            float Y = getHeight()/4;
            leftGhost.centerTo(X,Y);
            leftGhost.draw(canvas, paint);//this不知道是否要传
        }
        else if(currentGho == 2){
            float X = canvas.getWidth()/2;
            float Y = getHeight()/2;
            frontGhost.centerTo(X,Y);
            frontGhost.draw(canvas, paint);//this不知道是否要传
        }
        else if(currentGho == 3){
            float X = canvas.getWidth()*3/4;
            float Y = getHeight()*2/7;
            rightGhost.centerTo(X,Y);
            rightGhost.draw(canvas, paint);//this不知道是否要传
        }

        frame++;
        postInvalidate();

    }
    private void drawGamePaused(Canvas canvas){
        drawScore(canvas);

        //调用Sprite的onDraw方法，而非draw方法，这样就能渲染静态的Sprite，而不让Sprite改变位置
        littleMan.draw(canvas, paint);
        //绘制Dialog，显示得分
        drawScoreDialog1(canvas, "Continue");

//        if(lastSingleClickTime > 0){
//            postInvalidate();
//        }
    }

    //绘制结束状态的游戏
    private void drawGameOver(Canvas canvas){
        //Game Over之后只绘制弹出窗显示最终得分
        drawScoreDialog(canvas, "Again","Final Score:");
        littleMan.draw(canvas, paint);
//        if(lastSingleClickTime > 0){
//            postInvalidate();
//        }
    }
    private void drawScoreDialog1(Canvas canvas, String operation){
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //存储原始值
        float originalFontSize = textPaint.getTextSize();
        Paint.Align originalFontAlign = textPaint.getTextAlign();
        int originalColor = paint.getColor();
        Paint.Style originalStyle = paint.getStyle();
        int w1 = (int)(20.0 / 360.0 * canvasWidth);
        int w2 = canvasWidth - 2 * w1;
        int buttonWidth = (int)(140.0 / 360.0 * canvasWidth);

        int h1 = (int)(150.0 / 558.0 * canvasHeight);
        int h2 = (int)(60.0 / 558.0 * canvasHeight);
        int h3 = (int)(124.0 / 558.0 * canvasHeight);
        int h4 = (int)(76.0 / 558.0 * canvasHeight);
        int buttonHeight = (int)(42.0 / 558.0 * canvasHeight);

        canvas.translate(w1, h1);
        //绘制背景色
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFD7DDDE);
        Rect rect1 = new Rect(0, 0, w2, canvasHeight - 2 * h1);
        canvas.drawRect(rect1, paint);
        //绘制边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF515151);
        paint.setStrokeWidth(borderSize);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRect(rect1, paint);
        //绘制文本
        textPaint.setTextSize(fontSize2);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Score:", w2 / 2, (h2 - fontSize2) / 2 + fontSize2, textPaint);
        //绘制下面的横线
        canvas.translate(0, h2);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制实际的分数
        String allScore = String.valueOf(score);
        canvas.drawText(allScore, w2 / 2, (h3 - fontSize2) / 2 + fontSize2, textPaint);
        //绘制分数下面的横线
        canvas.translate(0, h3);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制按钮边框
        Rect rect2 = new Rect();
        rect2.left = (w2 - buttonWidth) / 2;
        rect2.right = w2 - rect2.left;
        rect2.top = (h4 - buttonHeight) / 2;
        rect2.bottom = h4 - rect2.top;
        canvas.drawRect(rect2, paint);
        //绘制文本"继续"或"重新开始"
        canvas.translate(0, rect2.top);
        canvas.drawText(operation, w2 / 2, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
        continueRect = new Rect(rect2);
        continueRect.left = w1 + rect2.left;
        continueRect.right = continueRect.left + buttonWidth;
        continueRect.top = h1 + h2 + h3 + rect2.top;
        continueRect.bottom = continueRect.top + buttonHeight;

        //重置
        textPaint.setTextSize(originalFontSize);
        textPaint.setTextAlign(originalFontAlign);
        paint.setColor(originalColor);
        paint.setStyle(originalStyle);
    }
    private void drawScoreDialog(Canvas canvas, String operation,String zhuangtai){
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //存储原始值
        float originalFontSize = textPaint.getTextSize();
        Paint.Align originalFontAlign = textPaint.getTextAlign();
        int originalColor = paint.getColor();
        Paint.Style originalStyle = paint.getStyle();
        Bitmap dman;
        String text;
        if(currentDir == TURN_TOO_MUCH) {
            dman = BitmapFactory.decodeResource(getResources(), R.drawable.toomuch1);
            text = "Screwed to death by yourself!";
        }
        else{
            dman = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
            text = "YOU ARE KILLED!";
        }

        int w1 = (int)(20.0 / 360.0 * canvasWidth);
        int w2 = canvasWidth - 2 * w1;
        int dmanleft = (w2-dman.getWidth())/2;
        int buttonWidth = (int)(140.0 / 360.0 * canvasWidth);

        int h1 = (int)(150.0 / 558.0 * canvasHeight);
        int h2 = (int)(60.0 / 558.0 * canvasHeight);
        int h3 = (int)(124.0 / 558.0 * canvasHeight);
        int h4 = (int)(76.0 / 558.0 * canvasHeight);
        int buttonHeight = (int)(42.0 / 558.0 * canvasHeight);

        canvas.translate(w1, h1);
        //绘制背景色
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFD7DDDE);
        Rect rect1 = new Rect(0, 0, w2, canvasHeight - 2 * h1);
        canvas.drawRect(rect1, paint);
        //绘制边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF515151);
        paint.setStrokeWidth(borderSize);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRect(rect1, paint);
        Rect rect2 = new Rect();
        rect2.left = w2 - buttonWidth/2;
        rect2.right = w2;
        rect2.top = (h2 - buttonHeight) / 2;
        rect2.bottom = h2 - rect2.top;
        canvas.drawRect(rect2, paint);
        canvas.drawText("Upload", rect2.left+buttonWidth/16, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
        upload.left = w1 + rect2.left;
        upload.right = upload.left + buttonWidth/2;
        upload.top = h1 + rect2.top;
        upload.bottom = upload.top + buttonHeight;
        textPaint.setTextSize(fontSize2);
        textPaint.setTextAlign(Paint.Align.CENTER);
        String allScore = String.valueOf(score);
        canvas.drawText(zhuangtai+":"+allScore, w2 / 2, (h2 - fontSize2) / 2 + fontSize2, textPaint);
        canvas.drawBitmap(dman,dmanleft,0,paint);
        //绘制下面的横线
        canvas.translate(0, h2);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制实际的分数
        canvas.drawText(text, w2 / 2, 0 + fontSize2, textPaint);
        //绘制分数下面的横线
        canvas.translate(0, h3);
        canvas.drawLine(0, 0, w2, 0, paint);
        //绘制按钮边框
        rect2.left = (w2 - buttonWidth) / 2;
        rect2.right = w2/2;
        rect2.top = (h4 - buttonHeight) / 2;
        rect2.bottom = h4 - rect2.top;
        Rect rect3 = new Rect();
        rect3.left = rect2.right;
        rect3.right = buttonWidth/2 + rect3.left;
        rect3.top = (h4 - buttonHeight) / 2;
        rect3.bottom = h4 - rect3.top;
        canvas.drawRect(rect2, paint);
        canvas.drawRect(rect3, paint);
        //绘制文本"继续"或"重新开始"
        canvas.translate(0, rect2.top);
        canvas.drawText(operation, w2 / 2 - buttonWidth / 4, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
        canvas.drawText("Menu", w2 / 2 + buttonWidth / 4, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
        continueRect = new Rect(rect2);
        continueRect.left = w1 + rect2.left;
        continueRect.right = continueRect.left + buttonWidth/2;
        continueRect.top = h1 + h2 + h3 + rect2.top;
        continueRect.bottom = continueRect.top + buttonHeight;
        backHome.left = continueRect.right;
        backHome.right = backHome.left + buttonWidth/2;
        backHome.top = h1 + h2 + h3 + rect2.top;
        backHome.bottom = continueRect.top + buttonHeight;
        //重置
        textPaint.setTextSize(originalFontSize);
        textPaint.setTextAlign(originalFontAlign);
        paint.setColor(originalColor);
        paint.setStyle(originalStyle);
    }

    private void renewGhost(){
        if(currentGho == 1){
            currentGho = currentGho + (int)(Math.random() * 2 + 1);
        }
        else if(currentGho == 2){
            if((int)(Math.random()*2+1)==1){
                currentGho = currentGho - 1;
            }
            else{
                currentGho = currentGho + 1;
            }
        }
        else if(currentGho == 3){
            if((int)(Math.random()*2+1)==1){
                currentGho = currentGho - 1;
            }
            else{
                currentGho = currentGho - 2;
            }
        }
        //dosomething
        score++;
        endFrame = frame + liveTime;
    }

    //暂时没用
    private RectF getPauseBitmapDstRecF(){
        Bitmap pauseBitmap = pause;
        RectF recF = new RectF();
        recF.left = 15 * density;
        recF.top = 15 * density;
        recF.right = recF.left + pauseBitmap.getWidth();
        recF.bottom = recF.top + pauseBitmap.getHeight();
        return recF;
    }

    private void drawScore(Canvas canvas){
        //绘制左上角的暂停按钮
        Bitmap pauseBitmap = pause;
        RectF pauseBitmapDstRecF = getPauseBitmapDstRecF();
        float pauseLeft = pauseBitmapDstRecF.left;
        float pauseTop = pauseBitmapDstRecF.top;
        canvas.drawBitmap(pauseBitmap, pauseLeft, pauseTop, paint);
        //绘制左上角的总得分数
        float scoreLeft = pauseLeft + pauseBitmap.getWidth() + 20 * density;
        float scoreTop = fontSize + pauseTop + pauseBitmap.getHeight() / 2 - fontSize / 2;
        canvas.drawText(score + "", scoreLeft, scoreTop, textPaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){
            onSingleClick(event.getX(),event.getY());
        }
        return true;
    }
    private void onSingleClick(float x, float y){
        if(status == STATUS_GAME_STARTED){
            if(isClickPause(x, y)){
                //单击了暂停按钮
                pause();
            }
        }else if(status == STATUS_GAME_PAUSED){
            if(isClickContinueButton(x, y)){
                //单击了“继续”按钮
                resume();
            }
        }else if(status == STATUS_GAME_OVER){
            if(isClickRestartButton(x, y)){
                //单击了“重新开始”按钮
                restart();
            }
            else if(isClickBack(x,y)){
                activity.backHome();
            }
            else if(isClickUpload(x,y)){
                activity.upload(score);
                Toast.makeText(activity,"UPLOAD SCORE SUCCESSFULLY",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //是否单击了左上角的暂停按钮
    private boolean isClickPause(float x, float y){
        RectF pauseRecF = getPauseBitmapDstRecF();
        return pauseRecF.contains(x, y);
    }
    private boolean isClickBack(float x, float y){
        return backHome.contains((int)x, (int)y);
    }
    private boolean isClickUpload(float x,float y){
        return upload.contains((int)x,(int)y);
    }
    //是否单击了暂停状态下的“继续”那妞
    private boolean isClickContinueButton(float x, float y){
        return continueRect.contains((int)x, (int)y);
    }

    //是否单击了GAME OVER状态下的“重新开始”按钮
    private boolean isClickRestartButton(float x, float y){
        return continueRect.contains((int)x, (int)y);
    }

}


