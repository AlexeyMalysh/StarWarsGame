package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Kid {

    public static final int SPRITE_SIZE_WIDTH =200;
    public static final int SPRITE_SIZE_HEIGTH=200;

    private float speed;
    private float positionX;
    private float positionY;
    private Bitmap spriteKid;

    private float screenWidth;
    Random random = new Random();


    public Kid (Context context, float screenWidth, float screenHeigth){
        speed = 6;
        positionX = screenWidth;
        positionY = random.nextInt((int)screenHeigth-SPRITE_SIZE_HEIGTH-100)+100;
        //Getting bitmap from resource
        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.kid);
        spriteKid = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);

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

    public Bitmap getSpriteKid() {
        return spriteKid;
    }

    public void setSpriteKid(Bitmap spriteKid) {
        this.spriteKid = spriteKid;
    }


    /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo () {
        this.positionX-= speed;
    }
}

