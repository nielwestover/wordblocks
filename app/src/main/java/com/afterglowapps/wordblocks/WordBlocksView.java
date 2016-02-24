package com.afterglowapps.wordblocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by a2558 on 2/5/2016.
 */
public class WordBlocksView extends SurfaceView implements SurfaceHolder.Callback {

    private Context myContext;
    private SurfaceHolder mySurfaceHolder;
    private float scaleW;
    private float scaleH;
    private float drawScaleW;
    private float drawScaleH;
    private int screenW = 1;
    private int screenH = 1;
    private float screenWDP = 1.0f;
    private boolean running = false;
    private int myMode;
    private int fingerX, fingerY;

    private float boxGap;
    private float boxRounding;
    private float boxDim;
    private WordBlocksDrawer wordBlocksDrawer;
    private WordBlocksUpdater wordBlocksUpdater;
    private Game game = new Game();

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }
    }

    private WordBlocksThread thread;

    public WordBlocksView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new WordBlocksThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {

            }
        });

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                wordBlocksUpdater.fingerPress = true;
                wordBlocksUpdater.X = motionEvent.getX();
                wordBlocksUpdater.Y = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                wordBlocksUpdater.fingerMoving = true;
                wordBlocksUpdater.X = motionEvent.getX();
                wordBlocksUpdater.Y = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                wordBlocksUpdater.fingerPress = false;
                break;
        }
        return true;
    }

    public WordBlocksThread getThread() {
        return thread;
    }

    class WordBlocksThread extends Thread {

        public WordBlocksThread(SurfaceHolder surfaceHolder, Context context,
                                Handler handler) {
            mySurfaceHolder = surfaceHolder;
            myContext = context;
        }

        @Override
        public void run() {
            while (running) {
                update();
                draw();

            }
        }

        private void draw() {

            Canvas canvas = null;
            try {
                canvas = mySurfaceHolder.lockCanvas(null);
                synchronized (mySurfaceHolder) {
                   wordBlocksDrawer.draw(game, canvas);
                }
            } finally {
                if (canvas != null) {
                    mySurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        private void update(){
            if (wordBlocksUpdater != null)
                wordBlocksUpdater.update(game);
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (mySurfaceHolder) {
                wordBlocksDrawer = new WordBlocksDrawer(getContext(), width, height);
                wordBlocksUpdater = new WordBlocksUpdater();
            }
        }

        public void setRunning(boolean b) {
            running = b;
        }

    }
}
