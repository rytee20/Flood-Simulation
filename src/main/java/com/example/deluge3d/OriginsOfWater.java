package com.example.deluge3d;
import java.util.ArrayList;

public class OriginsOfWater {
	private static ArrayList<SourceOfWater> sources;
	private static Precipitation precipitation;


public OriginsOfWater() {
	sources = new ArrayList<>();
	precipitation = new Precipitation();
}
	
public void AddWater(Water water) {
	for (SourceOfWater source : sources) {
		for (int x = source.x; x<source.x+source.sizeX; x++)
			for (int y =source.y; y<source.y+source.sizeY; y++)
				water.columns[x][y].level += source.PowerOfSource;
	}
	for (int i=0;i<Settings.SquaresOnWidth;i++) {
		for (int j=0;j<Settings.SquaresOnDepth;j++) {
			water.columns[i][j].level += precipitation.GetAmountOfPrecipitations();
		}
	}
}

public void AddSource(int x,int y,int sizeX, int sizeY, float power) {
	SourceOfWater source = new SourceOfWater(x,y,sizeX,sizeY,power);
	sources.add(source);
}
}
