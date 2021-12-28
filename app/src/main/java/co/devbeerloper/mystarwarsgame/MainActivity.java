package co.devbeerloper.mystarwarsgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Main menu of the game
 */
public class MainActivity extends AppCompatActivity {


    MediaPlayer backgroundMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.music_menu);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.6f, 0.6f);
        backgroundMusic.start();
    }




    @Override
    protected void onResume() {
        backgroundMusic.release();
        backgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.music_menu);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.6f, 0.6f);
        backgroundMusic.start();
        super.onResume();
    }

    @Override
    protected void onStop() {
        backgroundMusic.stop();
        super.onStop();
    }

    /**
     * Launches the gameplay
     * @param view
     */
    public void playGame (View view){
        backgroundMusic.stop();
        startActivity(new Intent(this, GamePlay.class));
    }

    public void exit(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        backgroundMusic.release();
        super.onDestroy();
    }
}
