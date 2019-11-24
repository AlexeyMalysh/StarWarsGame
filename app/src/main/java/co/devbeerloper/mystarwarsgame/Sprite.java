package co.devbeerloper.mystarwarsgame;

import android.graphics.Bitmap;

public interface Sprite {

    int spriteSizeWidth();
    int spriteSizeHeigth();
    float positionX();
    float positionY();
    float speed();
    Bitmap spriteImage();
    boolean canCollide();
}
