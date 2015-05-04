package br.edu.ifsp.androidgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import br.edu.ifsp.entity.Canhao;
import br.edu.ifsp.entity.Cenario;
import br.edu.ifsp.entity.Projetil;

/**
 * Created by fcorbano on 02/05/15.
 */
public class GameView extends View {

    // Configurações da View
    private static final int QUADROS_POR_SEGUNDO = 20;

    private Paint paint = new Paint();

    private boolean executando;
    private int largura;
    private int altura;

    private Cenario cenario;
    private Projetil projetil;
    private Canhao canhao;


    public GameView(Context context) {
        super(context);
        paint.setAntiAlias(true);

        cenario = new Cenario(context, this);
        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 30, 0, 0, Color.BLACK );
        canhao =  new Canhao(context);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        /*yClique = event.getY();
                        projetil.setVelocidadeX(0);
                        projetil.setVelocidadeY(0);
                        projetil.setX(xIni);
                        projetil.setY(yIni);*/
                        break;

                    case MotionEvent.ACTION_UP:
                        /*velocidadeIniX = 10f;
                        velocidadeIniY = 10f;
                        tempo = 0;
                        atirou = true;
                        disparoCanhao.play(disparoId, 5, 5, 1, 0, 1f);*/
                        canhao.disparar();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        /*float d = -(event.getY() - yClique) / 40;
                        angulo += d;

                        if (angulo < 0) {
                            angulo = 0;
                        } else if (angulo > 90) {
                            angulo = 90;
                        }*/

                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Cenário
        cenario.desenhar(canvas, paint);

        // Projetil
        projetil.desenhar(canvas, paint);

        // Canhão
        canhao.desenhar(canvas, paint);

    }

    public void iniciar() {

        if ( !executando ) {

            executando = true;

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    try {

                        while (executando) {

                            largura = getWidth();
                            altura = getHeight();

                            // TODO lógica do jogo

                            publishProgress();
                            Thread.sleep(1000 / QUADROS_POR_SEGUNDO);

                        }

                    } catch ( InterruptedException exc ) {
                        exc.printStackTrace();
                    }

                    return null;

                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    invalidate();
                }

            }.execute();

        }

    }

    public void parar() {
        executando = false;
    }
}
