package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.Asteroid;
import java.util.ArrayList;
import java.util.List;

import kepler.Vec3;
import kepler.World;

public class Level {

    World _world;

    public ArrayList<Asteroid> asteroids = new ArrayList<>();
    public ArrayList<Star> stars = new ArrayList<>();

    private  ArrayList<Vec3> potencialStarPositions = new ArrayList<>();
    public int[][] table = new int[levelSizeHorCount][levelSizeVertCount];
    public boolean[][] mustdel = new boolean[levelSizeHorCount][levelSizeVertCount];

   // ArrayList<ArrayList<Integer>> listOfLists = new ArrayList<>(levelSizeHorCount);

    static public int levelSizeHorCount = 300;
    static public int levelSizeVertCount = 300;
    static public  float elementSize = 40;


    public Level(World world) {
        _world = world;
    }


    private final int  G_LEFT = -1;
    private final int  G_RIGHT = 1;
    private final int  G_FORWARD = -2;
    private final int  G_BACK = 2;

    private int[] dirs = { G_LEFT, G_RIGHT, G_FORWARD, G_BACK};

    private final int  G_DIRSCOUNT = 4;

    public void load(Pixmap pixmap) {


        clear();
        Color pixColor = new Color();
        for(int lx = 0; lx < levelSizeHorCount; lx++) {
            for(int ly = 0; ly<levelSizeVertCount; ly ++) {


                Color.rgba8888ToColor(pixColor, pixmap.getPixel(lx, levelSizeVertCount-1-ly));
                if(pixColor.r == 0.0f && pixColor.g == 0.0f && pixColor.b == 0.0f)
                {
                    Asteroid asteroid = new Asteroid();
                    asteroid.setPos(new Vec3(lx * elementSize, ly * elementSize, 0));

                    asteroid.setRadius(elementSize / 2);
                    asteroid.setMassa(1000);
                    asteroid.setKinematic(true);
                  //  _world.createBody(asteroid);

                    asteroids.add(asteroid );

                    table[lx][ly] = asteroids.size()-1;
                }
                else {
                    table[lx][ly] = 0;
                }
            }
        }
    }

    boolean isValidElement(int i, int j) {
        return (i>=0 && j>=0 && i<levelSizeHorCount && j<levelSizeVertCount);
    }

    public Vec3 generate(float excludeAreaRadius) {
        clear();


        int i_cut = levelSizeHorCount / 2;
        int j_cut = levelSizeVertCount / 2;


        int dir =  G_LEFT;
        int stepWeight = 1;
        int oldDir = 0;
        for(int step = 0; step < levelSizeHorCount * levelSizeVertCount; step ++) {


            if((step % stepWeight) == 0) {
                stepWeight = Math.round(3 + (float) Math.random() * 10);
                dir = dirs[(int)Math.floor((float) Math.random() * (float) G_DIRSCOUNT)];
            }


            switch(dir) {
                case G_LEFT:
                    if(i_cut>7)
                        i_cut--;
                break;

                case G_RIGHT:
                    if(i_cut+7<levelSizeHorCount)
                        i_cut++;
                break;

                case G_FORWARD:
                    if(j_cut>7)
                        j_cut++;
                break;

                case G_BACK:
                    if(j_cut+7<levelSizeVertCount)
                        j_cut--;
                break;

            }

            if(isValidElement(i_cut, j_cut)) {
                table[i_cut][j_cut] = 0;
                Vec3 tmpPos = new Vec3(i_cut * elementSize, j_cut * elementSize, 0);
                if(!potencialStarPositions.contains( tmpPos))
                    potencialStarPositions.add(tmpPos);
            }


            int w =  2+(int)(Math.random()  > 0.5 ? 1:0);
            for(int i = i_cut - w; i< i_cut ; i++) {
                for(int j = j_cut - w; j< j_cut ; j++) {
                    if(isValidElement(i, j))
                        table[i][j] = 0;
                }
            }

//            if(oldDir != dir) {
//
//            }

            oldDir = dir;
        }

        int start_I = levelSizeHorCount / 2;
        int start_J = levelSizeVertCount / 2;
        int off = (int) (excludeAreaRadius / elementSize);

        for(int i = start_I - off; i < start_I + off; i++) {
            for (int j = start_J - off; j < start_J + off; j++) {
                table[i][j] = 0;
            }
        }

        for(int lx = 2; lx < levelSizeHorCount-2; lx++) {
            for (int ly = 2; ly < levelSizeVertCount-2; ly++) {
                if(    table[lx+1][ly] == 1
                    && table[lx-1][ly] == 1
                    && table[lx][ly+1] == 1
                    && table[lx][ly-1] == 1
                     && table[lx+2][ly] == 1
                        && table[lx-2][ly] == 1
                        && table[lx][ly+2] == 1
                        && table[lx][ly-2] == 1


                ) {
                    mustdel[lx][ly] = true;
                }
            }
        }

        for(int lx = 1; lx < levelSizeHorCount-1; lx++) {
            for (int ly = 1; ly < levelSizeVertCount-1; ly++) {
                if(  mustdel[lx][ly]  ) {
                    table[lx][ly] = 0;
                }
            }
        }

        for(int lx = 0; lx < levelSizeHorCount; lx++) {
            for (int ly = 0; ly < levelSizeVertCount; ly++) {
                Vec3 tmpPos = new Vec3(lx * elementSize, ly * elementSize, 0);
                 if(table[lx][ly] != 0) { // if(Math.random() > 0.9f && kepler.Math.farThen(tmpPos, excludeAreaCenterPos, excludeAreaRadius )) {
                    Asteroid asteroid = new Asteroid();
                    asteroid.setPos(tmpPos);
                    asteroid.setRadius(elementSize / 2);
                    asteroid.setMassa(1000);
                    asteroid.setKinematic(true);
                    asteroid.setTakesLifeCount(Math.random() > 0.5f ? 0 : 100);
                    asteroids.add(asteroid);
                    table[lx][ly] = asteroids.size()-1;
                }
            }
        }


        for(int starIter = 0; starIter < 10; starIter ++) {
            int n = (int)(Math.random() * potencialStarPositions.size());
            stars.add(new Star(potencialStarPositions.get(n)));
            potencialStarPositions.remove(n);
        }


        return  new Vec3(start_I * elementSize, start_J * elementSize);
    }


    public void clear() {
        asteroids.clear();
        stars.clear();
        potencialStarPositions.clear();

        for(int lx = 0; lx < levelSizeHorCount; lx++) {
            for (int ly = 0; ly < levelSizeVertCount; ly++) {
                table[lx][ly] = 1;
                mustdel[lx][ly] = false;
            }
        }
    }

}
