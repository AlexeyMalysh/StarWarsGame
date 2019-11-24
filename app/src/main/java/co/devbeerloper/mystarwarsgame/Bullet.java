package co.devbeerloper.mystarwarsgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Bullet implements Sprite {
    public static final float INIT_X = 100;
    private final float INIT_Y = 100;
    private int initSizeWidth;
    private int initSizeHeight;

    private float maxY;
    private float maxX;

    private float speed;
    private float speedbase;
    private float positionX;
    private float positionY;
    private Bitmap[] sprites;
    private int actualSprite;
    Random random = new Random();


    public Bullet(Context context, float screenWidth, float screenHeigth, float positionX, float positionY, boolean isPlayer) {
        initSizeWidth = (int) screenWidth * 2 / 1000 * 6;
        initSizeHeight = (int) screenWidth * 2 / 1000 * 30;
        speed = isPlayer?-10:10;
        speedbase = speed;
        sprites = new Bitmap[3];
        actualSprite = 0;

        //Getting bitmap from resource
        Bitmap bullet1Bitmap = BitmapFactory.decodeResource(context.getResources(), isPlayer ? R.drawable.redlaser1 : R.drawable.greenlaser1);
        Bitmap bullet2Bitmap = BitmapFactory.decodeResource(context.getResources(), isPlayer ? R.drawable.redlaser2 : R.drawable.greenlaser2);
        Bitmap bullet3Bitmap = BitmapFactory.decodeResource(context.getResources(), isPlayer ? R.drawable.redlaser3 : R.drawable.greenlaser3);
        sprites[0] = Bitmap.createScaledBitmap(bullet1Bitmap, initSizeWidth, initSizeHeight, false);
        sprites[1] = Bitmap.createScaledBitmap(bullet2Bitmap, initSizeWidth, initSizeHeight, false);
        sprites[2] = Bitmap.createScaledBitmap(bullet3Bitmap, initSizeWidth, initSizeHeight, false);

        this.positionX = positionX;
        this.positionY = positionY;
        this.maxX = screenWidth - sprites[0].getWidth();
    }


    public void updateInfo(long actualTime) {
        positionY += speed;
        if (actualTime % 180 < 60)
            actualSprite = 0;
        else if (actualTime % 180 < 120)
            actualSprite = 1;
        else
            actualSprite = 2;
    }

    public void setSpeed(float speed) {
        this.speed = speedbase<0? -speed + speedbase:speed + speedbase;
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
        return true;
    }
}
