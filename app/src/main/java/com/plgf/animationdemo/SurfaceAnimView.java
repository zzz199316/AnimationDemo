package com.plgf.animationdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * 作者：zzz on 2019/3/11 0011 14:36
 * 邮箱：1038883524@qq.com
 * 功能：
 */
public class SurfaceAnimView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;

    private ArrayList<SurfaceBean> surfaceBeans = new ArrayList<>();
    private Paint p;
    /**
     * 负责绘制的工作线程
     */
    private DrawThread drawThread;

    public SurfaceAnimView(Context context) {
        this(context, null);
    }

    public SurfaceAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setZOrderOnTop(true);
        /**设置画布  背景透明*/
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        p = new Paint();
        p.setAntiAlias(true);
        drawThread = new DrawThread();
    }


    public void addBean(SurfaceBean surfaceBean){
        surfaceBeans.add(surfaceBean);
        if (surfaceBeans.size() > 40) {
            surfaceBeans.remove(0);
        }
        start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (drawThread == null) {
            drawThread = new DrawThread();
        }
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (drawThread != null) {
            drawThread.isRun = false;
            drawThread = null;
        }
    }

    class DrawThread extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            super.run();
            /**绘制的线程 死循环 不断的跑动*/
            while (isRun) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        /**清除画面*/
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        boolean isEnd = true;

                        for (int i = 0; i < surfaceBeans.size(); i++) {
                            isEnd = surfaceBeans.get(i).isEnd;
                            surfaceBeans.get(i).draw(canvas, p);
                        }
                        /**这里做一个性能优化的动作，由于线程是死循环的 在没有心需要的绘制的时候会结束线程*/
                        if (isEnd) {
                            isRun = false;
                            drawThread = null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    /**用于控制绘制帧率*/
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        if (drawThread != null) {

            for (int i = 0; i < surfaceBeans.size(); i++) {
                surfaceBeans.get(i).stop();
            }

            drawThread.isRun = false;
            drawThread = null;
        }

    }

    public void start() {
        if (drawThread == null) {
            drawThread = new DrawThread();
            drawThread.start();
        }
    }
}