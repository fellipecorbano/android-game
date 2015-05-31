package br.edu.ifsp.androidgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ifsp.entity.Alvo;
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

    private float angulo;
    private double tempo;

    private Cenario cenario;
    private Projetil projetil;
    private Canhao canhao;
    private List<Alvo> alvos = new ArrayList<Alvo>();;

    private float xClique;

    private boolean atirou=false;


    public GameView(Context context) {
        super(context);
        paint.setAntiAlias(true);

        angulo = 90;

        cenario = new Cenario(context, this);
        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 30, 0, 0, Color.BLACK );
        canhao =  new Canhao(context);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        if(!atirou)
                            xClique = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        atirou = true;
                        tempo = 0;
                        canhao.disparar();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float d = -(event.getX() - xClique) / 40;
                        angulo += d;


                        if (angulo < 0) {
                            angulo = 0;
                        } else if (angulo > 180) {
                            angulo = 180;
                        }
                        canhao.setAngulo(angulo);
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

        if(!atirou) {
            float catAdj = (float) (Math.cos(Math.toRadians(angulo)) * 80);
            float catOp = (float) (Math.sin(Math.toRadians(angulo)) * 80) - 10;
            projetil.setX(Projetil.POS_X + catAdj);
            projetil.setY(Projetil.POS_Y - catOp);
        }

        // Alvos
        for(Alvo alvo : alvos) {
            alvo.desenhar(canvas, paint);
        }

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

                            // Sorteia com 5% de chances de sair um alvo
                            // Podem ter no máximo 5 alvos por vez na tela
                            Random r = new Random();
                            if(alvos.size() < 5) {
                                if (r.nextInt(100) < 5) {
                                    alvos.add(new Alvo(getContext()));
                                }
                            } else {
                                alvos.remove(0);
                            }

                            // Se houver alvos, alterar a velocidade de cada um
                            if(!alvos.isEmpty()) {
                                for (Alvo alvo : alvos) {
                                    alvo.setY(alvo.getY() + alvo.getVelocidadeY());
                                    alvo.setVelocidadeY(alvo.getVelocidadeY() * alvo.getAtrito() + alvo.getGravidade());
                                }
                            }

                            //TODO NECESSARIO CHECAR COLISAO DO PROJETIL COM ALVOS E VALIDAR O TIPO DO ALVO
                            // NECESSARIO REMOVER ELEMENTOS SOMENTE QUANDO TOCAR O CHAO OU FOR ACERTADO, CASO
                            // PASSAR O NUMERO M'AXIMO NA LISTA SOMENTE NAO CRIAR MAIS
                            // NECESSARIO TAMB'EM AJUSTAR OS PARAMETROS DE CONFIG DE VELOCIDADE, ETC

                            if (atirou) {

                                // calculando a velocidade atual
                                double velocidadeX = 10f * Math.cos( Math.toRadians(angulo) ) * tempo;

                                // precisa inverter y por causa do sistema de coordenadas.
                                double velocidadeY = - 10f * Math.sin(Math.toRadians(angulo)) * tempo;

                                projetil.setVelocidadeX((float) velocidadeX);
                                projetil.setVelocidadeY((float) velocidadeY);

                                projetil.setX( projetil.getX() + projetil.getVelocidadeX() );
                                projetil.setY( projetil.getY() + projetil.getVelocidadeY() );

                                tempo += 1;

                                if (projetil.getXFim() >= largura) {
                                    projetil.setVelocidadeX(0);
                                    projetil.setVelocidadeY(0);
                                    projetil.setX(Projetil.POS_X);
                                    projetil.setY(Projetil.POS_Y);
                                    atirou = false;
                                }

                                if (projetil.getXIni() <= 0) {
                                    projetil.setVelocidadeX(0);
                                    projetil.setVelocidadeY(0);
                                    projetil.setX(Projetil.POS_X);
                                    projetil.setY(Projetil.POS_Y);
                                    atirou = false;
                                }

                                if (projetil.getYFim() >= altura) {
                                    projetil.setVelocidadeX(0);
                                    projetil.setVelocidadeY(0);
                                    projetil.setX(Projetil.POS_X);
                                    projetil.setY(Projetil.POS_Y);
                                    atirou = false;
                                }

                            }

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
