package br.edu.ifsp.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
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

    private tipo tipoAlvo;
    private int cor;

    /**
     * Enum com os tipos possíveis de alvos
     */
    public enum tipo {
        // NAME(MIN%, MAX%, PONTOS)
        PEQUENO(0,30, 7),
        MEDIO(31,60, 5),
        GRANDE(61,90, 3),
        VIDA(91,95, 0),
        BOMBA(96,100, 20); // Pontos somados se a bomba acertar o chão

        int chanceMin;
        int chanceMax;
        int pontos;

        private tipo(int chanceMin, int chanceMax, int pontos) {
            this.chanceMin = chanceMin;
            this.chanceMax = chanceMax;
            this.pontos = pontos;
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
        int vel = r.nextInt(6) + 2;

        switch(tipoAlvo) {
            case PEQUENO:
                setAlvo(xValue, yValue, 30, 0, vel, Color.GREEN, tipo.PEQUENO);
                break;
            case MEDIO:
                setAlvo(xValue, yValue, 40, 0, vel, Color.GREEN, tipo.MEDIO);
                break;
            case GRANDE:
                setAlvo(xValue, yValue, 50, 0, vel, Color.GREEN, tipo.GRANDE);
                break;
            case VIDA:
                setAlvo(xValue, yValue, 30, 0, vel, Color.RED, tipo.VIDA);
                break;
            case BOMBA:
                setAlvo(xValue, yValue, 30, 0, vel, Color.BLACK, tipo.BOMBA);
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
        this.tipoAlvo = tipo.sorteiaTipo();
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
        this.tipoAlvo = tipo.sorteiaTipo();
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
    public void setAlvo(float x, float y, float raio, float velocidadeX, float velocidadeY, int cor, tipo tipoAlvo) {
        this.x = x;
        this.y = y;
        this.raio = raio;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.cor = cor;
        this.atrito = 0.99f;
        this.elasticidade = 0.9f;
        this.gravidade = 1f;
        this.tipoAlvo = tipoAlvo;
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

    public boolean colidir(Projetil projetil) {
        double a = Math.pow(this.getX() - projetil.getX(), 2);
        double b = Math.pow(this.getY() - projetil.getY(), 2);
        double h = Math.sqrt(a + b);

        return h <= (this.getRaio() + projetil.getRaio());
    }

    public boolean acertouChao() {

        if(this.getYFim() > -Cenario.TAMANHO_GRAMADO && !(this.tipoAlvo.equals(tipo.VIDA) || this.tipoAlvo.equals(tipo.BOMBA)) ) {
            Log.i("[acertouChao]", "Acertou o chaou");
            return true;
        }

        return false;
    }

    public int getPontos() {
        return this.tipoAlvo.pontos;
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

    public tipo getTipoAlvo() {
        return tipoAlvo;
    }

    public void setTipoAlvo(tipo tipoAlvo) {
        this.tipoAlvo = tipoAlvo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alvo alvo = (Alvo) o;

        if (Float.compare(alvo.x, x) != 0) return false;
        if (Float.compare(alvo.y, y) != 0) return false;
        if (Float.compare(alvo.raio, raio) != 0) return false;
        if (Float.compare(alvo.velocidadeX, velocidadeX) != 0) return false;
        if (Float.compare(alvo.velocidadeY, velocidadeY) != 0) return false;
        if (Float.compare(alvo.atrito, atrito) != 0) return false;
        if (Float.compare(alvo.elasticidade, elasticidade) != 0) return false;
        if (Float.compare(alvo.gravidade, gravidade) != 0) return false;
        if (cor != alvo.cor) return false;
        return tipoAlvo == alvo.tipoAlvo;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (raio != +0.0f ? Float.floatToIntBits(raio) : 0);
        result = 31 * result + (velocidadeX != +0.0f ? Float.floatToIntBits(velocidadeX) : 0);
        result = 31 * result + (velocidadeY != +0.0f ? Float.floatToIntBits(velocidadeY) : 0);
        result = 31 * result + (atrito != +0.0f ? Float.floatToIntBits(atrito) : 0);
        result = 31 * result + (elasticidade != +0.0f ? Float.floatToIntBits(elasticidade) : 0);
        result = 31 * result + (gravidade != +0.0f ? Float.floatToIntBits(gravidade) : 0);
        result = 31 * result + (tipoAlvo != null ? tipoAlvo.hashCode() : 0);
        result = 31 * result + cor;
        return result;
    }
}
