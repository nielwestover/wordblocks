package com.afterglowapps.wordblocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by a2558 on 2/21/2016.
 */
public class WordBlocksDrawer {
    private float width;
    private float height;
    private Paint blockPaint;
    private Paint selectedPaint;
    private Paint textPaint;
    private Paint selectedTextPaint;
    private float boxGap;
    private float boxRounding;
    private float boxDim = 0;
    private Context context;
    private float scalar;

    public WordBlocksDrawer(Context context, float width, float height) {
        this.width = width;
        this.height = height;
        this.context = context;

        scalar = width / 600;

        blockPaint = new Paint();
        blockPaint.setAntiAlias(true);
        blockPaint.setColor(Color.WHITE);
        blockPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blockPaint.setTextAlign(Paint.Align.LEFT);

        selectedPaint = new Paint();
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(Color.YELLOW);
        selectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(scalar * 50.0f);

        selectedTextPaint = new Paint();
        selectedTextPaint.setAntiAlias(true);
        selectedTextPaint.setColor(Color.WHITE);
        selectedTextPaint.setStyle(Paint.Style.FILL);
        selectedTextPaint.setTextAlign(Paint.Align.CENTER);
        selectedTextPaint.setTextSize(scalar * 40.0f);
    }

    public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner


        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    private void computeDims(Game g) {
        boxGap = DeviceDimensionsHelper.convertDpToPixel(7, context);
        boxRounding = DeviceDimensionsHelper.convertDpToPixel(5, context);
        boxDim = (width - (boxGap * (g.grid.length + 1))) / (g.grid.length * 1.0f);
    }

    public void draw(Game game, Canvas canvas) {
        if (boxDim == 0)
            computeDims(game);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                Paint paint = blockPaint;
                if (game.grid[i][j].block.selected)
                    paint = selectedPaint;
                canvas.drawPath(RoundedRect(game.grid[i][j].block.x, game.grid[i][j].block.y, game.grid[i][j].block.x + game.grid[i][j].block.width, game.grid[i][j].block.y + game.grid[i][j].block.height, boxRounding, boxRounding), paint);
                Rect r = new Rect();
                textPaint.getTextBounds(game.grid[i][j].block.letter + "", 0, 1, r);
                canvas.drawText(game.grid[i][j].block.letter + "", game.grid[i][j].block.x + game.grid[i][j].block.width / 2, game.grid[i][j].block.y + game.grid[i][j].block.height/2 + (Math.abs(r.height()))/2, textPaint);
            }
        }
        canvas.drawText(game.selectedWord, width / 2, width + scalar * 40, selectedTextPaint);
    }


}
