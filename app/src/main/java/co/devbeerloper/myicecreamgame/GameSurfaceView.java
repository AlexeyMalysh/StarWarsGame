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
    private boolean isGaming;
    private IceCreamCar icecreamCar;
    private ArrayList<Cloud> clouds;
    private ArrayList<Kid> kids;
    private ArrayList<Adult> adults;
    private ArrayList<PowerUp> powerUps;
    private Paint paint;
    private Canvas canvas;
    private Context context;
    private float screenWidth;
    private float screenHeight;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;
    Random random = new Random();
    private int score;
    private int lifes;
    private int nexTop;

    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWidth, float screenHeight) {
        super(context);

        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        icecreamCar = new IceCreamCar(context, screenWidth, screenHeight);
        clouds = new ArrayList<Cloud>();
        kids = new ArrayList<Kid>();
        adults = new ArrayList<Adult>();
        powerUps = new ArrayList<PowerUp>();
        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        holder = getHolder();
        isPlaying = true;
        isGaming = false;
        score = 0;
        lifes = 3;
        nexTop = 100;
    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            updateBackGround();
            if (isGaming)
                updateInfo();
            paintFrame();
        }
    }

    private void updateBackGround() {
        if (random.nextInt(100) < 20)
            clouds.add(new Cloud(context, screenWidth, screenHeight));
        for (Cloud c : clouds)
            c.updateInfo();
        for (int i = 0; i < clouds.size(); i++)
            if (clouds.get(i).getPositionX() < -clouds.get(i).SPRITE_SIZE_WIDTH)
                clouds.remove(i--);
    }

    private void updateInfo() {

        if (random.nextInt(1000) < 1 && powerUps.isEmpty())
            powerUps.add(new PowerUp(context, screenWidth, screenHeight));
        for (PowerUp p : powerUps)
            p.updateInfo();
        for (int i = 0; i < powerUps.size(); i++)
            if (powerUps.get(i).getPositionX() < -powerUps.get(i).getSpriteSizeWidth())
                powerUps.remove(i--);
            else if (collitedWhithIceCreamCar(powerUps.get(i).getPositionX(), powerUps.get(i).getPositionY(), powerUps.get(i).getSpriteSizeWidth(), powerUps.get(i).getSpriteSizeHeigth())) {
                powerUps.remove(i--);
                for (Adult a : adults)
                    a.setPowerUp(true);
            }

        if (random.nextInt(100) < 2) {
            Kid toAdd = new Kid(context, screenWidth, screenHeight);
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
            adults.add(new Adult(context, screenWidth, screenHeight));
        for (Adult a : adults)
            a.updateInfo();
        for (int i = 0; i < adults.size(); i++) {
            if (adults.get(i).getPositionX() < -adults.get(i).spriteSizeWidth)
                adults.remove(i--);
            else if (collitedWhithIceCreamCar(adults.get(i).getPositionX(), adults.get(i).getPositionY(), adults.get(i).spriteSizeWidth, adults.get(i).spriteSizeHeigth)) {
                if (adults.get(i).isPowerUp()) {
                    score++;
                } else {
                    lifes--;
                }
                adults.remove(i--);
            }
        }

        if (score >= nexTop) {
            lifes++;
            nexTop += 100;
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
            canvas.drawColor(Color.BLACK);
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
            canvas.drawText("Score: " + score, screenWidth / 100 * 10, 100, paint);
            canvas.drawText("Lifes: " + lifes, screenWidth / 100 * 50, 100, paint);
            holder.unlockCanvasAndPost(canvas);
        }

    }


    public void pause() {
        isPlaying = false;
        isGaming = false;
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
                isGaming = true;
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
