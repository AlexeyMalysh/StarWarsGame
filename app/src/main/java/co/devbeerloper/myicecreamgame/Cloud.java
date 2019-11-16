package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Cloud {

    public static  int SPRITE_SIZE_WIDTH;
    public static  int SPRITE_SIZE_HEIGTH;

    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteCloud;
    Random random = new Random();


    public Cloud (Context context, float screenWidth, float screenHeigth){
        SPRITE_SIZE_HEIGTH = SPRITE_SIZE_WIDTH = random.nextInt(100) + 100;
        speed = random.nextInt(3) + 3;
        positionX = screenWidth;
        positionY = random.nextInt(100);
        //Getting bitmap from resource
        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud);
        spriteCloud  = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);


    }



    public static int getSpriteSizeWidth() {
        return SPRITE_SIZE_WIDTH;
    }

    public static int getSpriteSizeHeigth() {
        return SPRITE_SIZE_HEIGTH;
    }
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public Bitmap getSpriteCloud() {
        return spriteCloud;
    }

    public void setSpriteCloud(Bitmap spriteIcecreamCar) {
        this.spriteCloud = spriteIcecreamCar;
    }



    /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo () {
        this.positionX -= speed;

    }



}
