package br.edu.ifsp.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;

/**
 * Created by fcorbano on 02/05/15.
 */
public class Alvo {

    private float x;
    private float y;
    private float raio;

    private float velocidadeX;
    private float velocidadeY;

    private float atrito;
    private float elasticidade;
    private float gravidade;

    private int cor;

    /**
     * Enum com os tipos possíveis de alvos
     */
    public enum tipo {
        PEQUENO(0,30),
        MEDIO(31,60),
        GRANDE(61,90),
        VIDA(91,95),
        BOMBA(96,100);

        int chanceMin;
        int chanceMax;

        private tipo(int chanceMin, int chanceMax) {
            this.chanceMin = chanceMin;
            this.chanceMax = chanceMax;
        }

        public static tipo sorteiaTipo(){
            Random rand = new Random();
            int sorteio = rand.nextInt(100);

            if(sorteio >= PEQUENO.chanceMin && sorteio <= PEQUENO.chanceMax) return tipo.PEQUENO;
            if(sorteio >= MEDIO.chanceMin && sorteio <= MEDIO.chanceMax) return tipo.MEDIO;
            if(sorteio >= GRANDE.chanceMin && sorteio <= GRANDE.chanceMax) return tipo.GRANDE;
            if(sorteio >= VIDA.chanceMin && sorteio <= VIDA.chanceMax) return tipo.VIDA;
            if(sorteio >= BOMBA.chanceMin && sorteio <= BOMBA.chanceMax) return tipo.BOMBA;

            return null;
        }
    }

    /**
     * Construtor com base no tipo de alvo
     */
    public Alvo(Context context) {

        Random r = new Random();

        // Sorteia alvo
        Alvo.tipo tipoAlvo = tipo.sorteiaTipo();

        // Sorteia posicao X e seta posicao Y
        DisplayMetrics m = context.getResources().getDisplayMetrics();
        int yValue = -m.heightPixels + 200;

        float max = m.widthPixels - this.getRaio()*2;
        float min = 0 + this.getRaio()*2;
        float xValue = r.nextFloat() * (max - min) + min;

        // Sorteia a velocidade do alvo
        //TODO

        switch(tipoAlvo) {
            case PEQUENO:
                setAlvo(xValue, yValue, 20, 8, 8, Color.GREEN);
                break;
            case MEDIO:
                setAlvo(xValue, yValue, 30, 8, 8, Color.GREEN);
                break;
            case GRANDE:
                setAlvo(xValue, yValue, 50, 8, 8, Color.GREEN);
                break;
            case VIDA:
                setAlvo(xValue, yValue, 30, 8, 8, Color.RED);
                break;
            case BOMBA:
                setAlvo(xValue, yValue, 30, 8, 8, Color.BLACK);
                break;
        }
    }

    /**
     * Construtor default
     */
    public Alvo(){
        this.raio = 10;
        this.velocidadeX = 8;
        this.velocidadeY = 8;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
        this.cor = Color.BLACK;
    }

    /**
     * Construtor Alvo
     *
     * @param x
     * @param y
     * @param raio
     * @param velocidadeX
     * @param velocidadeY
     * @param cor
     */
    public Alvo(float x, float y, float raio, float velocidadeX, float velocidadeY, int cor) {
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.cor = cor;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
    }

    /**
     * Setar um alvo
     *
     * @param x
     * @param y
     * @param raio
     * @param velocidadeX
     * @param velocidadeY
     * @param cor
     */
    public void setAlvo(float x, float y, float raio, float velocidadeX, float velocidadeY, int cor) {
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.cor = cor;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
    }

    /**
     * Método que desenha um alvo na view
     * @param canvas
     * @param paint
     */
    public void desenhar( Canvas canvas, Paint paint ) {
        paint.setStyle( Paint.Style.FILL );
        paint.setColor(cor);
        canvas.drawCircle( x, y, raio, paint );
    }

    public float getYIni(){
        return y - raio;
    }

    public float getYFim(){
        return y + raio;
    }

    public float getXIni(){
        return x - raio;
    }

    public float getXFim(){
        return x + raio;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRaio() {
        return raio;
    }

    public void setRaio(float raio) {
        this.raio = raio;
    }

    public float getVelocidadeX() {
        return velocidadeX;
    }

    public void setVelocidadeX(float velocidadeX) {
        this.velocidadeX = velocidadeX;
    }

    public float getVelocidadeY() {
        return velocidadeY;
    }

    public void setVelocidadeY(float velocidadeY) {
        this.velocidadeY = velocidadeY;
    }

    public float getAtrito() {
        return atrito;
    }

    public void setAtrito(float atrito) {
        this.atrito = atrito;
    }

    public float getElasticidade() {
        return elasticidade;
    }

    public void setElasticidade(float elasticidade) {
        this.elasticidade = elasticidade;
    }

    public float getGravidade() {
        return gravidade;
    }

    public void setGravidade(float gravidade) {
        this.gravidade = gravidade;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

}
