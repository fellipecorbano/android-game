package br.edu.ifsp.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;

import br.edu.ifsp.androidgame.R;

/**
 * Created by fcorbano on 02/05/15.
 */
public class Canhao {

    // Configurações do canhão
    public static final int ANGULO_INICIAL = 90;
    public static int POS_X;
    public static final int POS_Y = 235;
    public static final int PIVO_X = 50;
    public static final int PIVO_Y = 100;

    // Configurações da base do canhão
    public static int BASE_POS_X;
    public static final int BASE_POS_Y = -160;

    private Bitmap canhao;
    private Bitmap base_canhao;
    private SoundPool disparo;
    private int disparoId;

    /**
     * Construtor default
     */
    public Canhao(Context context){

        // Carregar canhao e base
        canhao = BitmapFactory.decodeResource(context.getResources(), R.drawable.canhao);
        base_canhao = BitmapFactory.decodeResource(context.getResources(), R.drawable.base_canhao);

        // Encontrar posicao X do canhao
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        BASE_POS_X = (metrics.widthPixels / 2) - (base_canhao.getWidth() / 2);
        POS_X = (metrics.widthPixels / 2) - (canhao.getWidth() / 2) + 24;

        // Carregar som de disparo
        disparo = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        disparoId = disparo.load(context, R.raw.cannon, 1);
    }

    /**
     * Método que desenha o canhao na view
     * @param canvas
     * @param paint
     */
    public void desenhar( Canvas canvas, Paint paint ) {

        canvas.translate( POS_X, -POS_Y );
        canvas.rotate( -ANGULO_INICIAL + 46, PIVO_X, PIVO_Y );
        canvas.drawBitmap(canhao, 0, 0, paint);
        canvas.rotate( ANGULO_INICIAL - 46, PIVO_X, PIVO_Y );
        canvas.translate( -POS_X, POS_Y );
        canvas.drawBitmap(base_canhao, BASE_POS_X, BASE_POS_Y, paint);
    }

    /**
     * Método responsável por fazer o som do disparo
     */
    public void disparar() {
        disparo.play(disparoId, 5, 5, 1, 0, 1f);
    }

}
