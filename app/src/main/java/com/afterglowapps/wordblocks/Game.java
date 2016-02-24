package com.afterglowapps.wordblocks;

import android.content.Context;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a2558 on 2/5/2016.
 */
public class Game {
    public Game(){

        for (int row = 0; row < grid.length; row ++)
            for (int col = 0; col < grid[row].length; col++)
                grid[row][col] = new Cell();
        answers = new ArrayList<>();
        answers.add("CUCUMBER");
        answers.add("MINCE");
        answers.add("TONGUE");
        answers.add("PENCIL");
        initBoard("EOIBPUCMERGUUNENCICETMLCN");
        initDrawableAttributes();
    }

    List<String> answers;
    private void initBoard(String boardString){
        int index = 0;
        for (int j = 0; j < grid.length; ++j){
            for (int i = 0; i < grid[j].length; ++i){
                Block block = new Block();
                block.letter = boardString.charAt(index);
                index++;
                grid[i][j].block = block;
            }
        }
    }

    public String selectedWord = "";
    public List<Pair<Integer,Integer>> selectedChain = new ArrayList<Pair<Integer,Integer>>();

    //Trying my best to have the Game know as little as possible about its own drawable state, but unfortunately this is inevitable
    private void initDrawableAttributes() {
        float width = DeviceDimensionsHelper.getDisplayWidth(MyApplication.getAppContext());
        float scalar = width/600;
        float boxGap = 12 * scalar;
        float padding = 40 * scalar;
        float boxRounding = 10 * scalar;
        boxDim = (width - (padding * 2 + boxGap * (grid.length - 1))) / (grid.length * 1.0f);
        for (int i = 0; i < grid.length; ++i){
            for (int j = 0; j < grid[i].length; ++j){
                if (grid[i][j].block == null)
                    continue;
                grid[i][j].block.x = padding + (boxGap + boxDim) * i;
                grid[i][j].block.y = + (boxGap + boxDim) * j;
                grid[i][j].block.width = boxDim;
                grid[i][j].block.height = boxDim;
            }
        }
    }

    public boolean selectedWordIsAnswer(){
        return answers.contains(selectedWord);
    }

    public void removeWordFound(){
        for (int i = 0; i < selectedChain.size(); ++i){
            Pair<Integer,Integer> curBlock = selectedChain.get(i);
            grid[curBlock.first][curBlock.second].block = null;
        }
    }

    public void deselectAll(){
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (grid[i][j].block != null)
                    grid[i][j].block.selected = false;
            }
        }
        selectedWord = "";
        selectedChain = new ArrayList<Pair<Integer,Integer>>();
    }

    public class Block extends DrawableObject{
        char letter;
        boolean selected;
    }

    public class Cell{
        Block block;
    }

    public float boxDim;
    public Cell [][] grid = new Cell[5][5];
}

