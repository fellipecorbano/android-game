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
import android.widget.Toast;

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

    private static int vidas;
    private static int pontos;

    private Cenario cenario;
    private Projetil projetil;
    private Canhao canhao;
    private List<Alvo> alvos = new ArrayList<Alvo>();
    private List<Alvo> alvosAtingidos = new ArrayList<Alvo>();
    private List<Projetil> projetils = new ArrayList<Projetil>();

    private float xClique;

    private boolean atirou=false;


    public GameView(Context context) {
        super(context);
        paint.setAntiAlias(true);

        angulo = 90;
        vidas = 3;
        pontos = 0;

        cenario = new Cenario(context, this);
        canhao =  new Canhao(context);
        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 30, Color.BLACK , angulo);


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        atirou = false;
                        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 30, Color.BLACK , angulo);
                        xClique = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        atirou = true;

                        projetil.setAtirou(true);
                        projetils.add(projetil);
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
                        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 30, Color.BLACK , angulo);
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

        // Projetil em movimento
        for(Projetil p : projetils) {
            p.desenhar(canvas, paint);
        }

        // Canhão
        canhao.desenhar(canvas, paint);

        // Dados do jogo
        paint.setTextSize(46);
        canvas.drawText("Pontos: " + pontos, 30, -30, paint);
        canvas.drawText("Vidas: " + vidas, getWidth() - 200, -30, paint);
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

                            limparAlvosAtingidos();

                            // Sorteia com 3% de chances de sair um alvo
                            // Podem ter no máximo 5 alvos por vez na tela
                            Random r = new Random();
                            if(alvos.size() < 5) {
                                if (r.nextInt(100) < 3) {
                                    alvos.add(new Alvo(getContext()));
                                }
                            }

                            // Se houver alvos
                            if(!alvos.isEmpty()) {
                                for (Alvo alvo : alvos) {

                                    // Alterar a velocidade
                                    alvo.setY(alvo.getY() + alvo.getVelocidadeY());
                                    alvo.setVelocidadeY(alvo.getVelocidadeY() * alvo.getAtrito() + alvo.getGravidade());

                                    // Checar a colisao
                                    if(alvo.colidir(projetil)) {
                                        // remover alvo da lista
                                        alvosAtingidos.add(alvo);
                                        pontos++; // TODO refazer a contagem de pontos para cada tipo de alvo
                                    } else if(alvo.acertouChao()){
                                        // remover alvo da lista
                                        alvosAtingidos.add(alvo);
                                        vidas--;
                                        checkEndGame();
                                    }
                                }
                            }

                            //TODO NECESSARIO CHECAR COLISAO DO PROJETIL COM ALVOS E VALIDAR O TIPO DO ALVO
                            // NECESSARIO REMOVER ELEMENTOS SOMENTE QUANDO TOCAR O CHAO OU FOR ACERTADO, CASO
                            // PASSAR O NUMERO M'AXIMO NA LISTA SOMENTE NAO CRIAR MAIS
                            // NECESSARIO TAMB'EM AJUSTAR OS PARAMETROS DE CONFIG DE VELOCIDADE, ETC


                            if (!projetils.isEmpty()) {
                                for (Projetil projetil : projetils) {
                                   projetil.movimento();

                                    if (projetil.getXFim() >= largura) {
                                        /*projetil.setVelocidadeX(0);
                                        projetil.setVelocidadeY(0);
                                        projetil.setX(Projetil.POS_X);
                                        projetil.setY(Projetil.POS_Y);
                                        atirou = false;*/
                                        projetil.setAtirou(false);
                                    }

                                    if (projetil.getXIni() <= 0) {
                                        /*projetil.setVelocidadeX(0);
                                        projetil.setVelocidadeY(0);
                                        projetil.setX(Projetil.POS_X);
                                        projetil.setY(Projetil.POS_Y);
                                        atirou = false;*/
                                        projetil.setAtirou(false);
                                    }

                                    if (projetil.getYIni() >= altura) {
                                        /*projetil.setVelocidadeX(0);
                                        projetil.setVelocidadeY(0);
                                        projetil.setX(Projetil.POS_X);
                                        projetil.setY(Projetil.POS_Y);
                                        atirou = false;*/
                                        projetil.setAtirou(false);
                                    }
                                }

                                /*// calculando a velocidade atual
                                double velocidadeX = 10f * Math.cos( Math.toRadians(angulo) ) * tempo;

                                // precisa inverter y por causa do sistema de coordenadas.
                                double velocidadeY = - 10f * Math.sin(Math.toRadians(angulo)) * tempo;

                                projetil.setVelocidadeX((float) velocidadeX);
                                projetil.setVelocidadeY((float) velocidadeY);

                                projetil.setX( projetil.getX() + projetil.getVelocidadeX() );
                                projetil.setY( projetil.getY() + projetil.getVelocidadeY() );

                                tempo += 1;*/



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
                    //limparProjetil();
                    invalidate();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(vidas==0){
                        Toast.makeText(getContext(), "Fim de jogo", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();

        }



    }

    private void checkEndGame() {
        if(vidas == 0) {
            parar();
        }
    }

    private void limparAlvosAtingidos() {
        for(Alvo alvoAtingido : alvosAtingidos){
            alvos.remove(alvoAtingido);
        }
    }

    private void limparProjetil(){
        System.out.println(projetils.size());
        for(int i= 0; i < projetils.size(); i++){
            if(!projetils.get(i).isAtirou()){
                projetils.remove(i);
            }
        }
        System.out.println(projetils.size());

    }

    public void parar() {
        executando = false;
    }
}
