package co.devbeerloper.myicecreamgame;

import android.graphics.Bitmap;

public class EnemyShip implements Sprite {
    @Override
    public float positionX() {
        return 0;
    }

    @Override
    public float positionY() {
        return 0;
    }

    @Override
    public int spriteSizeHeigth() {
        return 0;
    }

    @Override
    public int spriteSizeWidth() {
        return 0;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public Bitmap spriteImage() {
        return null;
    }

    @Override
    public boolean canCollide() {
        return false;
    }


}
