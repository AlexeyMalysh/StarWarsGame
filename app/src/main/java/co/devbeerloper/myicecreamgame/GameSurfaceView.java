package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean isPlaying;
    private IceCreamCar icecreamCar;
    private ArrayList<Cloud> clouds;
    private ArrayList<Kid> kids;
    private ArrayList<Adult> adults;
    private Paint paint;
    private Canvas canvas;
    private Context context;
    private float screenWith;
    private float screenHeight;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;
    Random random = new Random();

    /**
     * Contructor
     * @param context
     */
    public GameSurfaceView(Context context, float screenWith, float screenHeight) {
        super(context);
        this.context = context;
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;
        icecreamCar = new IceCreamCar(context, screenWith, screenHeight);
        clouds = new ArrayList<Cloud>();
        kids = new ArrayList<Kid>();
        adults = new ArrayList<Adult>();
        paint = new Paint();
        holder = getHolder();
        isPlaying = true;
    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            updateInfo();
            paintFrame();

        }

    }

    private void updateInfo() {
        if(random.nextInt(100) < 10)
            clouds.add(new Cloud(context,screenWith,screenHeight));
        for (Cloud c: clouds)
            c.updateInfo();
        for (int i=0; i < clouds.size(); i++){
            if(clouds.get(i).getPositionX()<-clouds.get(i).SPRITE_SIZE_WIDTH)
                clouds.remove(i--);
        }

        if(random.nextInt(100) < 3)
            kids.add(new Kid(context,screenWith,screenHeight));
        for (Kid k: kids)
            k.updateInfo();
        for (int i=0; i < kids.size(); i++){
            if(kids.get(i).getPositionX()<-kids.get(i).SPRITE_SIZE_WIDTH)
                kids.remove(i--);
        }

        if(random.nextInt(100) < 1)
            adults.add(new Adult(context,screenWith,screenHeight));
        for (Adult a: adults)
            a.updateInfo();
        for (int i=0; i < adults.size(); i++){
            if(adults.get(i).getPositionX()<-adults.get(i).SPRITE_SIZE_WIDTH)
                adults.remove(i--);
        }



        icecreamCar.updateInfo ();



    }

    private void paintFrame() {
        if (holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.CYAN);
            for(Cloud c: clouds){
                canvas.drawBitmap(c.getSpriteCloud(),c.getPositionX(),c.getPositionY(),paint);
            }
            for(Kid k: kids){
                canvas.drawBitmap(k.getSpriteKid(),k.getPositionX(),k.getPositionY(),paint);
            }
            for(Adult a: adults){
                canvas.drawBitmap(a.getSpriteAdult(),a.getPositionX(),a.getPositionY(),paint);
            }
            canvas.drawBitmap(icecreamCar.getSpriteIcecreamCar(),icecreamCar.getPositionX(),icecreamCar.getPositionY(),paint);
            holder.unlockCanvasAndPost(canvas);
        }

    }


    public void pause() {
        isPlaying = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void resume() {

        isPlaying = true;
        gameplayThread = new Thread(this);
        gameplayThread.start();
    }

    /**
     * Detect the action of the touch event
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                System.out.println("TOUCH UP - STOP JUMPING");
                icecreamCar.setJumping(false);
                break;
            case MotionEvent.ACTION_DOWN:
                System.out.println("TOUCH DOWN - JUMP");
                icecreamCar.setJumping(true);
                break;
        }
        return true;
    }

}
