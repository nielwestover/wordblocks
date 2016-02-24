package com.afterglowapps.wordblocks;

import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a2558 on 2/21/2016.
 */
public class WordBlocksUpdater {

    public boolean fingerPress = false;
    public boolean fingerMoving = false;
    public float X;
    public float Y;
    private Pair<Integer,Integer> selectedBlock;
    private Pair<Integer,Integer> previousSelectedBlock;

    enum GameStates{
        INIT,
        WAIT_FOR_PRESS,
        FINGER_DOWN,
        MOVING,
        FINGER_UP,
        WORD_FOUND
    }
    GameStates gameState = GameStates.INIT;
    public void update(Game game){
       switch (gameState){
           case INIT:
               selectedBlock = new Pair<Integer,Integer>(-1,-1);
               previousSelectedBlock = new Pair<Integer,Integer>(-1,-1);
               gameState = GameStates.WAIT_FOR_PRESS;
               break;
           case WAIT_FOR_PRESS:
               if (fingerPress)
                   gameState = GameStates.FINGER_DOWN;
               break;
           case FINGER_DOWN:
               doBlockLogic(game);
               if (fingerMoving || !fingerPress)
                   gameState = GameStates.MOVING;
               break;
           case MOVING:
               if (!fingerPress)
                   gameState = GameStates.FINGER_UP;
               doBlockLogic(game);
               break;
           case FINGER_UP:
               if (game.selectedWordIsAnswer())
                   gameState = GameStates.WORD_FOUND;
               else {
                   gameState = GameStates.INIT;
                   game.deselectAll();
               }
               break;
           case WORD_FOUND:
               game.removeWordFound();
               game.deselectAll();
               //Do some effect here then start over
               gameState = GameStates.INIT;
               break;
       }
    }
    private void doBlockLogic(Game game){
        setSelectedBlock(game);
        if (isSelectedBlockValid(game))
            highlightSelectedBlock(game);
    }
    private boolean isSelectedBlockValid(Game game){
        //initial case
        if (previousSelectedBlock.first == -1) {
            previousSelectedBlock = selectedBlock;
            return true;
        }
        if (selectedBlock == previousSelectedBlock)
            return  false;
        if (game.grid[selectedBlock.first][selectedBlock.second].block.selected)
            return false;
        int xDist = Math.abs(selectedBlock.first - previousSelectedBlock.first);
        int yDist = Math.abs(selectedBlock.second - previousSelectedBlock.second);
        if (xDist > 1 || yDist > 1)
            return false;
        previousSelectedBlock = selectedBlock;
        return true;
    }

    private void setSelectedBlock(Game game)
    {
        for (int i = 0; i < game.grid.length; ++i){
            for (int j = 0; j < game.grid[i].length; ++j){
                if (game.grid[i][j].block == null)
                    continue;
                if (X > game.grid[i][j].block.x && X < (game.grid[i][j].block.x + game.grid[i][j].block.width)){
                    if (Y > game.grid[i][j].block.y && Y < (game.grid[i][j].block.y + game.grid[i][j].block.height)){
                        selectedBlock = new Pair<Integer, Integer>(i,j);
                    }
                }
            }
        }
    }

    private void highlightSelectedBlock(Game game){
        int i = selectedBlock.first;
        int j = selectedBlock.second;
        if (i == -1 || j == -1)
            return;
        if (!game.grid[i][j].block.selected){
            game.grid[i][j].block.selected = true;
            game.selectedWord = game.selectedWord.concat(game.grid[i][j].block.letter + "");
            game.selectedChain.add(new Pair<Integer, Integer>(i,j));
        }
    }
}
