package br.edu.ifsp.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;

import br.edu.ifsp.androidgame.R;

/**
 * Created by fcorbano on 02/05/15.
 */
public class Cenario {

    public static final int TAMANHO_GRAMADO = 100;

    private Bitmap grama;
    private View view;
    private MediaPlayer bgsound = new MediaPlayer();
    private SoundPool gameOverSound;
    private int gameOverSoundId;

    /**
     * Construtor default
     * @param context
     */
    public Cenario(Context context, View view) {

        // Grama
        grama = BitmapFactory.decodeResource(context.getResources(), R.drawable.grama);

        // Game over sound
        gameOverSound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        gameOverSoundId = gameOverSound.load(context, R.raw.gameover, 1);

        // Background sound
        bgsound = MediaPlayer.create(context, R.raw.background);
        bgsound.setLooping(true);
        bgsound.setVolume(100, 100);
        bgsound.start();

        // Armazena a view
        this.view = view;
    }

    /**
     * Handle game over sound
     */
    public void playGameOverSound() {
        gameOverSound.play(gameOverSoundId, 5, 5, 1, 0, 1f);
    }

    /**
     * Handle background sound
     */
    public void startBackgroundSound() {
        bgsound.start();
    }
    public void stopBackgroundSound() {
        bgsound.stop();
    }

    /**
     * Método que desenha o cenário na view
     * @param canvas
     * @param paint
     */
    public void desenhar( Canvas canvas, Paint paint ) {

        // Carrega background
        view.setBackgroundResource(R.drawable.ceu);

        // Translação na tela para o eixo (0,0) ser o ponto inferior direito
        canvas.translate(0, view.getHeight());

        // Gramado
        paint.setColor(Color.GRAY);
        //canvas.drawRect( 0, -TAMANHO_GRAMADO, view.getWidth(), 0, paint );
        canvas.drawBitmap(grama, 0, -TAMANHO_GRAMADO, paint);
    }

}
