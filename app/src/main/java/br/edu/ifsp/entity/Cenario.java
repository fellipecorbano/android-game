package br.edu.ifsp.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
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
    private SoundPool background;
    private int backgroundId;

    /**
     * Construtor default
     * @param context
     */
    public Cenario(Context context, View view) {

        // Grama
        grama = BitmapFactory.decodeResource(context.getResources(), R.drawable.grama);

        // Armazena a view
        this.view = view;
    }

    /**
     * Init background sound
     */
    public void initBackgroundSound(Context context) {
        // Carregar som de fundo
        background = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        backgroundId = background.load(context, R.raw.background, 1);
        background.play(backgroundId, 5, 5, 1, 0, 1f);
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
