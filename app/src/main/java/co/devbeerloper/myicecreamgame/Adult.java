package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Adult {

    public int spriteSizeWidth = 200;
    public int spriteSizeHeigth = 400;

    private float speed;
    private float positionX;
    private float positionY;
    private Bitmap spriteKid;

    private float screenWidth;
    Random random = new Random();


    public Adult(Context context, float screenWidth, float screenHeigth) {
        speed = 6;
        positionX = screenWidth;
        positionY = random.nextInt((int) screenHeigth - spriteSizeHeigth - 100) + 100;
        //Getting bitmap from resource
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kid);
        spriteKid = Bitmap.createScaledBitmap(originalBitmap, spriteSizeWidth, spriteSizeHeigth, false);

        this.screenWidth = screenWidth;
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

    public Bitmap getSpriteAdult() {
        return spriteKid;
    }

    public void setSpriteAdult(Bitmap spriteKid) {
        this.spriteKid = spriteKid;
    }

    public int getSpriteSizeWidth() {
        return spriteSizeWidth;
    }

    public void setSpriteSizeWidth(int spriteSizeWidth) {
        this.spriteSizeWidth = spriteSizeWidth;
    }

    public int getSpriteSizeHeigth() {
        return spriteSizeHeigth;
    }

    public void setSpriteSizeHeigth(int spriteSizeHeigth) {
        this.spriteSizeHeigth = spriteSizeHeigth;
    }


    /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo() {
        this.positionX -= speed;
    }
}

