package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Player implements Sprite {

    public static final float INIT_X = 100;
    private final float INIT_Y = 100;
    private int initSizeWidth = 150;
    private int initSizeHeight = 150;

    private float maxY;
    private float maxX;

    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap[] sprites ;
    Random random = new Random();


    public Player(Context context, float screenWidth, float screenHeigth) {
        initSizeWidth = initSizeHeight = (int) screenWidth * 30 / 100;
        speed = 0;
        sprites = new Bitmap[3];

        //Getting bitmap from resource
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);
        sprites[0] = Bitmap.createScaledBitmap(originalBitmap, initSizeWidth, initSizeHeight, false);
        Bitmap leftBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing1);
        sprites[1] = Bitmap.createScaledBitmap(leftBitmap, initSizeWidth, initSizeHeight, false);
        Bitmap rightBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing2);
        sprites[2] = Bitmap.createScaledBitmap(rightBitmap, initSizeWidth, initSizeHeight, false);

        positionX = screenWidth / 2 - sprites[0].getWidth() / 2;
        positionY = screenHeigth * 90 / 100 - sprites[0].getHeight();
        this.maxX = screenWidth - sprites[0].getWidth();
    }


    public void updateInfo() {
        if (positionX + speed > 0 && positionX + speed < maxX)
            positionX += speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

        if(speed==0)
            return sprites[0];
        long time = System.currentTimeMillis()%100;
        if(time>50)
            return sprites[1];
        else
            return sprites[2];
    }

    @Override
    public boolean canCollide() {
        return true;
    }
}
