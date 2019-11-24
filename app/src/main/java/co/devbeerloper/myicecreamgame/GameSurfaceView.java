package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private Context context;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;
    private float screenWidth;
    private float screenHeight;

    Random random = new Random();
    int frameCount;
    long initTime;
    long actualTime;
    long deathTime;

    private boolean isPlaying;
    private boolean isGaming;
    private boolean isDead;
    private int playerSpeed;

    private Player player;
    private ArrayList<Bullet> playerBullets;
    private ArrayList<EnemyShip> enemyShips;
    private ArrayList<Bullet> enemyBullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Cloud> clouds;

    private Thread gameplayThread = null;
    private int highscore;
    private int score;
    private int lifes;
    private int nexTop;
    private int speed;

    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWidth, float screenHeight) {
        super(context);

        highscore = PreferenceManager.getDefaultSharedPreferences(context).getInt("HIGH SCORE", 0);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        initTime = System.currentTimeMillis();
        frameCount = 0;
        isPlaying = true;
        isGaming = false;
        isDead = false;
        speed = 0;
        score = 0;
        lifes = 3;
        nexTop = 30;

        player = new Player(context, screenWidth, screenHeight);
        playerSpeed = 7;
        playerBullets = new ArrayList<Bullet>();
        enemyShips = new ArrayList<EnemyShip>();
        enemyBullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        clouds = new ArrayList<Cloud>();


        paint = new Paint();
        paint.setTextSize(screenWidth * 3 / 100);
        paint.setColor(Color.YELLOW);
        holder = getHolder();
    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {

            frameCount++;
            frameCount %= Integer.MAX_VALUE - 10000;
            actualTime = System.currentTimeMillis();
            updateBackGround();
            if (actualTime - initTime > 500) {
                if(isDead)
                    updateDeadInfo();
                else if (isGaming)
                    updateInfo();
                paintFrame();
            }
            if (score > highscore) {
                highscore = score;
                PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("HIGH SCORE", highscore).apply();
            }

        }
    }

    private void updateDeadInfo() {
        speed--;
        speed=Math.max(speed, 0);
        player.updateInfo(actualTime);
        for (Bullet pb : playerBullets) {
            pb.setSpeed(speed);
            pb.updateInfo(actualTime);
        }
        for (Bullet pb : enemyBullets) {
            pb.setSpeed(speed);
            pb.updateInfo(actualTime);
        }
        for (Asteroid a : asteroids) {
            a.setSpeed(speed);
            a.updateInfo();
        }        for (EnemyShip e : enemyShips){
            e.setSpeed(speed);
            e.updateInfo();
        }
    }

    private void updateBackGround() {
        if (random.nextInt(100) < 20 || (isGaming && random.nextInt(100) < 70))
            clouds.add(new Cloud(context, screenWidth, screenHeight, isGaming));
        for (Cloud c : clouds) {
            c.setSpeed(speed);
            c.updateInfo(isGaming);
        }
        for (int i = 0; i < clouds.size(); i++)
            if (clouds.get(i).positionY() > screenHeight + clouds.get(i).spriteSizeHeigth())
                clouds.remove(i--);
    }

    private void updateInfo() {
        if (frameCount%100 == 0)
            speed++;
        if (frameCount%200 == 0)
            playerSpeed++;
        speed=Math.min(speed,100);
        playerSpeed=Math.min(playerSpeed,15);

        player.updateInfo(actualTime);
        for (Bullet pb : playerBullets) {
            pb.setSpeed(speed);
            pb.updateInfo(actualTime);
        }
        if (frameCount % 50 == 0 && !isDead)
            playerBullets.add(new Bullet(context, screenWidth, screenHeight, player.positionX(), player.positionY(), true));
        if (frameCount % 50 == 25 && !isDead)
            playerBullets.add(new Bullet(context, screenWidth, screenHeight, player.positionX() + player.spriteSizeWidth() - (screenWidth * 2 / 1000 * 6), player.positionY(), true));
        for (int i = 0; i < playerBullets.size(); i++) {
            if (playerBullets.get(i).positionY() < -playerBullets.get(i).spriteSizeHeigth())
                playerBullets.remove(i--);
        }


        for (EnemyShip e : enemyShips) {
            if (random.nextInt(100) < 1 || frameCount%120 == 0)
                enemyBullets.add(new Bullet(context, screenWidth, screenHeight, e.positionX() + e.spriteSizeWidth() / 2, e.positionY() + e.spriteSizeHeigth(), false));
        }
        for (Bullet pb : enemyBullets) {
            pb.setSpeed(speed);
            pb.updateInfo(actualTime);
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (enemyBullets.get(i).positionY() < -enemyBullets.get(i).spriteSizeHeigth())
                enemyBullets.remove(i--);
        }

        if (random.nextInt(1000) < 7 || frameCount%150 == 0)
            enemyShips.add(new EnemyShip(context, screenWidth, screenHeight));
        for (EnemyShip e : enemyShips){
            e.setSpeed(speed);
            e.updateInfo();
        }
        for (int i = 0; i < enemyShips.size(); i++)
            if (enemyShips.get(i).positionY() > screenHeight + enemyShips.get(i).spriteSizeHeigth()) {
                enemyShips.remove(i--);
            } else if (collide(player, enemyShips.get(i))) {
                enemyShips.get(i).disableCollide();
                score += 2;
                lifes--;
            }

        if (random.nextInt(1000) < 10|| frameCount%120 == 0)
            asteroids.add(new Asteroid(context, screenWidth, screenHeight));
        for (Asteroid a : asteroids) {
            a.setSpeed(speed);
            a.updateInfo();
        }
        for (int i = 0; i < asteroids.size(); i++)
            if (asteroids.get(i).positionY() > screenHeight + asteroids.get(i).spriteSizeHeigth()) {
                score --;
                asteroids.remove(i--);
            } else if (collide(player, asteroids.get(i))) {
                asteroids.get(i).disableCollide();
                lifes--;
            }

        checkBulletsCollitions();


        if (score >= nexTop) {
            lifes++;
            nexTop += 40;
        }

        if(lifes<1){
            lifes=0;
            player.disableCollide();
            isDead = true;
            isGaming=false;
            deathTime = System.currentTimeMillis();
        }
    }

    private void checkBulletsCollitions() {

        loop:
        for (int i = 0; i < playerBullets.size(); i++) {

            for (int j = 0; j < enemyShips.size(); j++) {
                if (collide(playerBullets.get(i), enemyShips.get(j))) {
                    score+=3;
                    enemyShips.get(j).disableCollide();
                    playerBullets.remove(i--);
                    continue loop;
                }
            }

            for (int j = 0; j < asteroids.size(); j++) {
                if (collide(playerBullets.get(i), asteroids.get(j))) {
                    score+=2;
                    asteroids.get(j).disableCollide();
                    playerBullets.remove(i--);
                    continue loop;
                }
            }
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            if (collide(enemyBullets.get(i), player)) {
                lifes--;
                enemyBullets.remove(i--);
            }
        }


    }

    private boolean collide(Sprite a, Sprite b) {
        if (!a.canCollide() || !b.canCollide())
            return false;

        if (a.positionX() > b.positionX() + b.spriteSizeWidth() || b.positionX() > a.positionX() + a.spriteSizeWidth() ||
                a.positionY() > b.positionY() + b.spriteSizeHeigth() || b.positionY() > a.positionY() + a.spriteSizeHeigth()) {
            return false;
        }
        return true;
    }


    private void paintFrame() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            for (Cloud c : clouds)
                canvas.drawBitmap(c.spriteImage(), c.positionX(), c.positionY(), paint);
            for (Asteroid a : asteroids)
                canvas.drawBitmap(a.spriteImage(), a.positionX(), a.positionY(), paint);
            for (EnemyShip e : enemyShips)
                canvas.drawBitmap(e.spriteImage(), e.positionX(), e.positionY(), paint);
            for (Bullet pb : playerBullets)
                canvas.drawBitmap(pb.spriteImage(), pb.positionX(), pb.positionY(), paint);
            for (Bullet pb : enemyBullets)
                canvas.drawBitmap(pb.spriteImage(), pb.positionX(), pb.positionY(), paint);
            canvas.drawBitmap(player.spriteImage(), player.positionX(), player.positionY(), paint);
            canvas.drawText("Score: " + score, screenWidth / 100 * 5, screenWidth / 100 * 10, paint);
            canvas.drawText("Lifes: " + lifes, screenWidth / 100 * 70, screenWidth / 100 * 10, paint);
            canvas.drawText("High Score: " + highscore, screenWidth / 100 * 30, screenWidth / 100 * 10, paint);
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
                player.setSpeed(0);
                break;
            case MotionEvent.ACTION_DOWN:
                float xValue = motionEvent.getX();
                if (xValue <= screenWidth / 2 && !isDead) {
                    player.setSpeed(-playerSpeed);
                } else if (!isDead)
                    player.setSpeed(playerSpeed);

                break;
        }
        if(isDead){
            if(actualTime-deathTime>1000){
                initTime = System.currentTimeMillis();
                frameCount = 0;
                isPlaying = true;
                isGaming = false;
                isDead = false;
                speed = 0;
                score = 0;
                lifes = 3;
                nexTop = 30;

                player = new Player(context, screenWidth, screenHeight);
                playerSpeed = 7;
                playerBullets = new ArrayList<Bullet>();
                enemyShips = new ArrayList<EnemyShip>();
                enemyBullets = new ArrayList<Bullet>();
                asteroids = new ArrayList<Asteroid>();

            }

        }

        return true;
    }

}
