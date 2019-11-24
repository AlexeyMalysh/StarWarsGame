package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class EnemyShip implements Sprite {    public static final float INIT_X = 100;
    private final float INIT_Y = 100;
    private int initSizeWidth = 150;
    private int initSizeHeight = 150;

    private float maxY;
    private float maxX;

    private boolean canCollide;
    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap[] sprites;
    private int actualSprite;
    Random random = new Random();


    public EnemyShip(Context context, float screenWidth, float screenHeigth) {
        initSizeWidth = initSizeHeight = (int) screenWidth * 15 / 100;
        speed = (random.nextInt(3) + 1) * 3;

        sprites = new Bitmap[2];
        actualSprite = 0;
        canCollide =true;

        //Getting bitmap from resource
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tiefighter);
        sprites[0] = Bitmap.createScaledBitmap(originalBitmap, initSizeWidth, initSizeHeight, false);
        Bitmap kaboom1Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kaboom1);
        sprites[1] = Bitmap.createScaledBitmap(kaboom1Bitmap, initSizeWidth, initSizeHeight, false);

        positionX = random.nextInt((int) screenWidth - initSizeWidth);
        positionY = -initSizeHeight;
        this.maxY = screenHeigth;
    }


    public void updateInfo() {

            positionY += speed;
        if(!canCollide){
            actualSprite++;
        }
        if(actualSprite>=sprites.length) {
            positionY += maxY + speed;
            actualSprite--;
            return;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void disableCollide() {
        canCollide = false;
    }

    @Override
    public float positionX() {
        return positionX;
    }

    @Override
    public float positionY() {
        return positionY;
    }

    @Override
    public int spriteSizeHeigth() {
        return initSizeHeight;
    }

    @Override
    public int spriteSizeWidth() {
        return initSizeWidth;
    }

    @Override
    public float speed() {
        return speed;
    }

    @Override
    public Bitmap spriteImage() {
        return sprites[actualSprite];
    }

    @Override
    public boolean canCollide() {
        return canCollide;
    }


}
