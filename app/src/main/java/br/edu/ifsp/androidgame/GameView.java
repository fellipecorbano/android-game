package br.edu.ifsp.androidgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private static final int CONFIG_VIDAS = 5;
    private static final int CONFIG_PONTOS = 0;
    private static final int CONFIG_ANGULO = 90;
    private static final int LEVEL_INICIAL = 1;

    private Paint paint = new Paint();

    private boolean executando;
    private int largura;
    private int altura;

    private float angulo;

    private static int vidas;
    private static int pontos;
    private static int level;

    private Cenario cenario;
    private Projetil projetil;
    private Canhao canhao;

    private List<Alvo> alvos = new ArrayList<Alvo>();
    private List<Alvo> alvosAtingidos = new ArrayList<Alvo>();
    private List<Projetil> projetils = new ArrayList<Projetil>();
    private List<Projetil> projetilsAtingidos = new ArrayList<Projetil>();

    private boolean atingiuSprite;

    private float xClique;
    private boolean acertouBomba=false;

    private Bitmap desenhoVida;
    private Bitmap desenhoExplosao;



    public GameView(Context context) {
        super(context);
        paint.setAntiAlias(true);

        angulo = CONFIG_ANGULO;
        vidas = CONFIG_VIDAS;
        pontos = CONFIG_PONTOS;
        level = LEVEL_INICIAL;

        atingiuSprite = false;

        desenhoVida = BitmapFactory.decodeResource(context.getResources(), R.drawable.vida);
        desenhoExplosao = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_transparent);

        cenario = new Cenario(context, this);
        canhao =  new Canhao(context);
        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 20, Color.BLACK , angulo);



        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (executando) {
                            projetil = new Projetil(Projetil.POS_X, Projetil.POS_Y, 20, Color.BLACK, angulo);
                            xClique = event.getX();
                        } else {
                            restartGame();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (executando) {
                            projetils.add(projetil);
                            canhao.disparar();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        float d = -(event.getX() - xClique) / 50;
                        angulo += d;
                        if (angulo < 0) {
                            angulo = 0;
                        } else if (angulo > 180) {
                            angulo = 180;
                        }
                        canhao.setAngulo(angulo);
                        projetil = new Projetil(Projetil.POS_X, Projetil.POS_Y, 20, Color.BLACK, angulo);
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

        // Projetil na ponta do canhão
        float catAdj = (float) (Math.cos(Math.toRadians(angulo)) * 80);
        float catOp = (float) (Math.sin(Math.toRadians(angulo)) * 80) - 10;
        projetil.setX(Projetil.POS_X + catAdj);
        projetil.setY(Projetil.POS_Y - catOp);

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
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, -1550, getWidth(), -1450, paint);
        paint.setTextSize(46);
        paint.setColor(Color.WHITE);
        canvas.drawText("Pontos: " + pontos, 30, -1475, paint);
        canvas.drawText("Level: " + level, (getWidth() / 2) - 100, -1475, paint);
        canvas.drawText("X " + vidas, getWidth() - 100, -1475, paint);

        desenhoVida = Bitmap.createScaledBitmap(desenhoVida, 77, 77, false);
        canvas.drawBitmap( desenhoVida, getWidth()-185, -1530, paint );
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

                            verificaPontos();
                            limparAlvosAtingidos();
                            limparProjeteisAtingidos();

                            // Sorteia com 3% de chances de sair um alvo
                            // Podem ter no máximo 5 alvos por vez na tela
                            Random r = new Random();
                            if(alvos.size() < 5) {
                                if (r.nextInt(100) < 3) {
                                    alvos.add(new Alvo(getContext(), level));
                                }
                            }

                            for (Alvo alvo : alvos) {
                                // Alterar a velocidade
                                alvo.setY(alvo.getY() + alvo.getVelocidadeY());
                                //alvo.setVelocidadeY(alvo.getVelocidadeY() * alvo.getAtrito() + alvo.getGravidade());
                            }

                            if (!projetils.isEmpty()) {

                                for (Projetil projetil : projetils) {
                                    projetil.movimento();

                                    if (projetil.getXFim() >= largura ||
                                            projetil.getXIni() <= 0 ||
                                            projetil.getYIni() >= altura) {
                                        projetilsAtingidos.add(projetil);
                                    }

                                    // Para o projetil corrente, verificar se colidiu com algum dos alvos
                                    if(!alvos.isEmpty()) {
                                        for (Alvo alvo : alvos) {

                                            // Checar a colisao
                                            if(projetil.colidir(alvo)) {
                                                if(!alvosAtingidos.contains(alvo)) {
                                                    alvosAtingidos.add(alvo);
                                                    projetilsAtingidos.add(projetil);
                                                }
                                                checkAlvosEspeciais(alvo);

                                            } else if(alvo.acertouChao()){
                                                if(alvo.getTipoAlvo().equals(Alvo.tipo.BOMBA)) {
                                                    pontos += alvo.getPontos();
                                                }
                                                if(!alvosAtingidos.contains(alvo)){
                                                    alvosAtingidos.add(alvo);
                                                    vidas--;
                                                }
                                                checkEndGame();

                                            }
                                        }
                                    }
                                }
                            }
                            else {

                                if(!alvos.isEmpty()) {
                                    for (Alvo alvo : alvos) {

                                        if (alvo.acertouChao()) {
                                            alvosAtingidos.add(alvo);
                                            vidas--;
                                            checkEndGame();
                                        }
                                    }
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

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(vidas==0){
                        Toast.makeText(getContext(), "Fim de jogo", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();

        }

    }

    private void checkAlvosEspeciais(Alvo alvo) {
        if(alvo.getTipoAlvo().equals(Alvo.tipo.VIDA)) {
            vidas++;
        }
        if (alvo.getTipoAlvo().equals(Alvo.tipo.BOMBA)) {

            vidas--;
        }
        else
            pontos += alvo.getPontos();
    }

    private void checkEndGame() {
        if(vidas <= 0) {
            parar();
            level = LEVEL_INICIAL;
        }
    }

    private void restartGame() {
        pontos = CONFIG_PONTOS;
        vidas = CONFIG_VIDAS;
        angulo = CONFIG_ANGULO;
        acertouBomba = false;

        alvos = new ArrayList<Alvo>();
        alvosAtingidos = new ArrayList<Alvo>();
        projetils = new ArrayList<Projetil>();
        projetilsAtingidos = new ArrayList<Projetil>();

        projetil = new Projetil( Projetil.POS_X, Projetil.POS_Y, 20, Color.BLACK , angulo);

        canhao.setAngulo(angulo);

        iniciar();
    }

    private void limparAlvosAtingidos() {
        for(Alvo alvoAtingido : alvosAtingidos){
            alvos.remove(alvoAtingido);
        }
    }

    private void limparProjeteisAtingidos(){
            for(Projetil projetilAtingido : projetilsAtingidos){
                projetils.remove(projetilAtingido);
            }
    }

    private void verificaPontos(){
        if(pontos >= 50)
            level = 2;
        if(pontos >= 100)
            level = 3;
        if(pontos >= 150)
            level = 4;
        if(pontos >= 200)
            level = 5;
    }

    public void parar() {
        executando = false;
    }
}
