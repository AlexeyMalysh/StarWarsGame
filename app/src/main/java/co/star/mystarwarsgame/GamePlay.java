package co.star.mystarwarsgame;

import android.graphics.Point;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class GamePlay extends AppCompatActivity {

    private GameSurfaceView gameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        // Force the screen to use the landscape orintation
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getRealSize(screenSize);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        gameSurfaceView = new GameSurfaceView(this, screenSize.x, screenSize.y);
        setContentView(gameSurfaceView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.resume();
    }


}
