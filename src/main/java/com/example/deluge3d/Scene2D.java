package com.example.deluge3d;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;

public class Scene2D {
    public int SquaresOnWidth = Settings.SquaresOnWidth;
    public int SquaresOnDepth = Settings.SquaresOnDepth;
    private final int widthOfSquare;
    private final int depthOfSquare;
    private final int height  = Settings.heightOfFrame;
    private final int width = Settings.widthOfFrame;
    private final JFrame frame;
    private final float minLevel = Settings.minLevel;

    public Scene2D()
    {
        frame = new JFrame();
        widthOfSquare = width / SquaresOnWidth;
        depthOfSquare = height / SquaresOnDepth;
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void DrawWater(Water water, Ground ground)
    {
        JPanel panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
            super.paintComponent(g);
            for (int i = 0;i<SquaresOnWidth;i++)
                for (int j = 0; j<SquaresOnDepth;j++) {
                    if (water.columns[i][j].level>minLevel) {
                        int blue = (int)(255 - water.columns[i][j].level);
                        if (blue<30)
                            blue = 30;
                        //g.setColor(new Color(248*blue/255,24*blue/255,148*blue/255));
                        g.setColor(new Color(0,0,blue));
                    }
                    else {
                        int color = (int)(250*(1 - (float)ground.levelOfGround[i][j]/(float)ground.maxLevel));
                        g.setColor(new Color(color,color,color));
                    }
                    g.fillRect(i * widthOfSquare, height - (j+1) * depthOfSquare, widthOfSquare, depthOfSquare);
				}
            }
        };
        frame.add(panel);
        frame.setVisible(true);
    }
}
