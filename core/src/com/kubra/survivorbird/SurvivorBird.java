package com.kubra.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture bird;
    Texture stone1;
    Texture stone2;
    Texture stone3;
    float birdX = 0;
    float birdY = 0;
    int gameState = 0;
    float velocity = 0;
    float gravity = 0.1f;
    float stoneVelocity = 2;
    Random random;
    int score = 0;
    int scoreStone = 0;
    BitmapFont font;
    BitmapFont font2;

    Circle birdCircle;

    ShapeRenderer shapeRenderer;

    int numberOfStone = 4;
    float[] stoneX = new float[numberOfStone];
    float[] stoneOffSet = new float[numberOfStone];
    float[] stoneOffSet2 = new float[numberOfStone];
    float[] stoneOffSet3 = new float[numberOfStone];
    float distance = 0;

    Circle[] stoneCircle;
    Circle[] stoneCircle2;
    Circle[] stoneCircle3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("aksam.png");
        bird = new Texture("frame-1.png");
        stone1 = new Texture("stone.png");
        stone2 = new Texture("stone.png");
        stone3 = new Texture("stone.png");

        distance = Gdx.graphics.getWidth() / 2;
        random = new Random();

        birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight();
        birdY = Gdx.graphics.getHeight() / 4;

        shapeRenderer = new ShapeRenderer();

        birdCircle = new Circle();
        stoneCircle = new Circle[numberOfStone];
        stoneCircle2 = new Circle[numberOfStone];
        stoneCircle3 = new Circle[numberOfStone];

        font = new BitmapFont();
        font.setColor(Color.DARK_GRAY);
        font.getData().setScale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.DARK_GRAY);
        font2.getData().setScale(6);

        for (int i = 0; i < numberOfStone; i++) {

            stoneOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            stoneOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            stoneOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            //her olusan stone setinin distance göre otomatik ayarı sağlanmış oldu
            stoneX[i] = Gdx.graphics.getWidth() - stone1.getWidth() / 2 + (i * distance);

            stoneCircle[i] = new Circle();
            stoneCircle2[i] = new Circle();
            stoneCircle3[i] = new Circle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //oyun basladı
        if (gameState == 1) {

            if (stoneX[scoreStone] < Gdx.graphics.getWidth() / 2 - bird.getHeight()) {
                score++;

                //Score ataması
                if(scoreStone < (numberOfStone - 1)){
                    scoreStone++;
                }else{
                    scoreStone = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                //push etmek için - değer verdim
                velocity = -8;
            }

            for (int i = 0; i < numberOfStone; i++) {

                //stoneların devamlı gelmesini sağladım
                if (stoneX[i] < Gdx.graphics.getWidth() / 10) {
                    stoneX[i] += numberOfStone * distance;
                    //y her seferinde farklı olur
                    stoneOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    stoneOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    stoneOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                } else {
                    stoneX[i] -= stoneVelocity;
                }
                //aynı x düzleminde 3 farklı stone olur
                batch.draw(stone1, stoneX[i], Gdx.graphics.getHeight() / 2 + stoneOffSet[i], Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 8);
                batch.draw(stone2, stoneX[i], Gdx.graphics.getHeight() / 2 + stoneOffSet2[i], Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 8);
                batch.draw(stone3, stoneX[i], Gdx.graphics.getHeight() / 2 + stoneOffSet3[i], Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 8);

                stoneCircle[i] = new Circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
                stoneCircle2[i] = new Circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet2[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
                stoneCircle3[i] = new Circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet3[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
            }

            if (birdY > 0) {
                //kus dussun oyun basladıgında
                velocity += gravity;
                birdY = birdY - velocity;
            } else {
                //bird y nin altında ise oyunu bitir
                gameState = 2;
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {

            font2.draw(batch,"Game Over! Click to Try Again",100, 900);

            //tekrar tıkaldı
            if (Gdx.input.justTouched()) {
                gameState = 1;

                birdY = Gdx.graphics.getHeight() / 4;
                for (int i = 0; i < numberOfStone; i++) {
                    stoneOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    stoneOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    stoneOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                    //her olusan stone setinin distance göre otomatik ayarı sağlanmış oldu
                    stoneX[i] = Gdx.graphics.getWidth() - stone1.getWidth() / 2 + (i * distance);

                    stoneCircle[i] = new Circle();
                    stoneCircle2[i] = new Circle();
                    stoneCircle3[i] = new Circle();
                }
                velocity = 0;
                scoreStone = 0;
                score = 0;
            }
        }

        batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();

        //bird circle
        birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLDENROD);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        */
        //stone circles
        for (int i = 0; i < numberOfStone; i++) {
           /* shapeRenderer.circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
            shapeRenderer.circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet2[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
            shapeRenderer.circle(stoneX[i] + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 2 + stoneOffSet3[i] + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 20);
           */
            if (Intersector.overlaps(birdCircle, stoneCircle[i]) || Intersector.overlaps(birdCircle, stoneCircle2[i]) || Intersector.overlaps(birdCircle, stoneCircle3[i])) {
                //oyun biterse
                gameState = 2;
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {


    }
}
