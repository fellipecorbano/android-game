package br.edu.ifsp.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by fcorbano on 02/05/15.
 */
public class Projetil {

    // Configurações do Projétil
    public static final int POS_X = 540;
    public static final int POS_Y = -150;

    private float x;
    private float y;
    private float raio;

    private float velocidadeX;
    private float velocidadeY;

    private float atrito;
    private float elasticidade;
    private float gravidade;

    private float angulo;
    private boolean atirou;
    private int tempo;

    private int cor;

    /**
     * Construtor default
     */
    public Projetil(){
        this.raio = 10;
        this.velocidadeX = 8f;
        this.velocidadeY = 8f;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
        this.tempo = 0;
        this.atirou = false;
        this.cor = Color.BLACK;
    }



    /**
     * Construtor Projetil
     *
     * @param x
     * @param y
     * @param raio
     * @param cor
     */
    public Projetil(float x, float y, float raio, int cor, float angulo) {
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.cor = cor;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
        this.angulo = angulo;
        this.velocidadeX = 8f;
        this.velocidadeY = 8f;

    }

    /**
     * Método que desenha uma objeto tipo bola na view
     * @param canvas
     * @param paint
     */
    public void desenhar( Canvas canvas, Paint paint ) {
        paint.setStyle( Paint.Style.FILL );
        paint.setColor(cor);
        canvas.drawCircle( x, y, raio, paint );
    }

    public void movimento(){
        // calculando a velocidade atual
        double vX = velocidadeX * Math.cos( Math.toRadians(angulo) ) * tempo;

        // precisa inverter y por causa do sistema de coordenadas.
        double vY = - velocidadeY * Math.sin(Math.toRadians(angulo)) * tempo;

        //projetil.setVelocidadeX((float) velocidadeX);
        //projetil.setVelocidadeY((float) velocidadeY);

        x = (float) (x + vX);
        y = (float) (y + vY);

        tempo += 1;

    }

    public boolean isAtirou() {
        return atirou;
    }

    public void setAtirou(boolean atirou) {
        this.atirou = atirou;
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
