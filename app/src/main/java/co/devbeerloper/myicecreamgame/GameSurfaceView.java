package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean isPlaying;
    private IceCreamCar icecreamCar;
    private ArrayList<Cloud> clouds;
    private ArrayList<Kid> kids;
    private ArrayList<Adult> adults;
    private ArrayList<PowerUp> powerUps;
    private Paint paint;
    private Canvas canvas;
    private Context context;
    private float screenWith;
    private float screenHeight;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;
    Random random = new Random();
    private int score;

    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWith, float screenHeight) {
        super(context);

        this.context = context;
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;

        icecreamCar = new IceCreamCar(context, screenWith, screenHeight);
        clouds = new ArrayList<Cloud>();
        kids = new ArrayList<Kid>();
        adults = new ArrayList<Adult>();
        powerUps = new ArrayList<PowerUp>();
        paint = new Paint();
        paint.setTextSize(150);
        holder = getHolder();
        isPlaying = true;
        score = 0;
    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            updateInfo();
            paintFrame();

        }

    }

    private void updateInfo() {
        if (random.nextInt(1000) < 5 && powerUps.isEmpty())
            powerUps.add(new PowerUp(context, screenWith, screenHeight));
        for (PowerUp p : powerUps)
            p.updateInfo();
        for (int i = 0; i < powerUps.size(); i++) {
            if (powerUps.get(i).getPositionX() < -powerUps.get(i).getSpriteSizeWidth())
                powerUps.remove(i--);
            else if (collitedWhithIceCreamCar(powerUps.get(i).getPositionX(), powerUps.get(i).getPositionY(), powerUps.get(i).getSpriteSizeWidth(), powerUps.get(i).getSpriteSizeHeigth())) {
                powerUps.remove(i--);
                for(Adult a : adults){
                    a.setPowerUp(true);
                }
            }
        }


        if (random.nextInt(100) < 2)
            clouds.add(new Cloud(context, screenWith, screenHeight));
        for (Cloud c : clouds)
            c.updateInfo();
        for (int i = 0; i < clouds.size(); i++) {
            if (clouds.get(i).getPositionX() < -clouds.get(i).SPRITE_SIZE_WIDTH)
                clouds.remove(i--);
        }

        if (random.nextInt(100) < 2) {
            Kid toAdd = new Kid(context, screenWith, screenHeight);
            kids.add(toAdd);
        }
        for (Kid k : kids)
            k.updateInfo();
        for (int i = 0; i < kids.size(); i++) {
            if (kids.get(i).getPositionX() < -kids.get(i).spriteSizeWidth) {
                kids.remove(i--);
                score--;
            } else if (collitedWhithIceCreamCar(kids.get(i).getPositionX(), kids.get(i).getPositionY(), kids.get(i).getSpriteSizeWidth(), kids.get(i).getSpriteSizeHeigth())) {
                kids.remove(i--);
                score += 2;
            }

        }

        if (random.nextInt(100) < 1)
            adults.add(new Adult(context, screenWith, screenHeight));
        for (Adult a : adults)
            a.updateInfo();
        for (int i = 0; i < adults.size(); i++) {
            if (adults.get(i).getPositionX() < -adults.get(i).spriteSizeWidth)
                adults.remove(i--);
            else if (collitedWhithIceCreamCar(adults.get(i).getPositionX(), adults.get(i).getPositionY(), adults.get(i).spriteSizeWidth, adults.get(i).spriteSizeHeigth)) {
                if(adults.get(i).isPowerUp()) {
                    score++;
                }
                else {
                    score -= 2;
                }
                adults.remove(i--);
            }
        }


        icecreamCar.updateInfo();
    }

    private boolean collitedWhithIceCreamCar(float x, float y, int xlen, int ylen) {
        if (icecreamCar.getPositionX() > x + xlen || x > icecreamCar.getPositionX() + icecreamCar.SPRITE_SIZE_WIDTH) {
            return false;
        } else if (icecreamCar.getPositionY() > y + ylen || y > icecreamCar.getPositionY() + icecreamCar.SPRITE_SIZE_HEIGTH) {
            return false;
        }
        return true;
    }


    private void paintFrame() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            for (Cloud c : clouds) {
                canvas.drawBitmap(c.getSpriteCloud(), c.getPositionX(), c.getPositionY(), paint);
            }
            for (Kid k : kids) {
                canvas.drawBitmap(k.getSpriteKid(), k.getPositionX(), k.getPositionY(), paint);
            }
            for (Adult a : adults) {
                canvas.drawBitmap(a.getSpriteAdult(), a.getPositionX(), a.getPositionY(), paint);
            }
            for (PowerUp p : powerUps) {
                canvas.drawBitmap(p.getSpriteKid(), p.getPositionX(), p.getPositionY(), paint);
            }
            canvas.drawBitmap(icecreamCar.getSpriteIcecreamCar(), icecreamCar.getPositionX(), icecreamCar.getPositionY(), paint);
            canvas.drawText("" + score, 100, 70, paint);
            holder.unlockCanvasAndPost(canvas);
        }

    }


    public void pause() {
        isPlaying = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {

        isPlaying = true;
        gameplayThread = new Thread(this);
        gameplayThread.start();
    }

    /**
     * Detect the action of the touch event
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                System.out.println("TOUCH UP - STOP JUMPING");
                icecreamCar.setJumping(false);
                break;
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOUCH DOWN - JUMP");
                icecreamCar.setJumping(true);
                break;
        }
        return true;
    }

}
