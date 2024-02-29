package com.example.deluge3d;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.shape.Box;
import java.util.ArrayList;

public class Scene3D {
	public int SquaresOnWidth = Settings.SquaresOnWidth;
	public int SquaresOnDepth = Settings.SquaresOnDepth;
	private final int width;
	private final int height;
	private float minLevel = Settings.minLevel;

	private final ArrayList<Box> Boxes = new ArrayList<>();

	public Scene3D(Ground ground, Water water, Stage stage) {

		height = Settings.heightOfScene;
		width = Settings.widthOfScene;
		int widthOfSquare = width / Settings.SquaresOnWidth;

		//земля

		for (int i = 0; i < ground.SquaresOnWidth; i++) {
			for (int j = 0; j < ground.SquaresOnDepth; j++) {
				if (ground.levelOfGround[i][j] > 0) {
					Box box = new Box(widthOfSquare, ground.levelOfGround[i][j], widthOfSquare);
					box.setTranslateX(i * widthOfSquare);
					box.setTranslateZ(j * widthOfSquare);
					box.setTranslateY(height - (ground.levelOfGround[i][j] / 2));
					float color = (1 - (float)ground.levelOfGround[i][j]/(float)ground.maxLevel);
					PhongMaterial blackMaterial = new PhongMaterial();
					blackMaterial.setDiffuseColor(new Color(color,color,color,1));
					blackMaterial.setSpecularColor(new Color(color,color,color,1));
					box.setMaterial(blackMaterial);
					Boxes.add(box);
				}
			}
		}

		//вода
		PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);
		for (int i = 0; i < SquaresOnWidth; i++) {
			for (int j = 0; j < SquaresOnDepth; j++) {
				water.columns[i][j].box.setTranslateX(i * widthOfSquare);
				water.columns[i][j].box.setTranslateZ(j * widthOfSquare);
				water.columns[i][j].box.setMaterial(blueMaterial);
				Boxes.add(water.columns[i][j].box);
			}
		}
		//камера
		Camera camera = new PerspectiveCamera(true);
		camera.setFarClip(50000.0);
		camera.setTranslateZ(-1400);
		camera.setTranslateY(-1200);
		camera.setTranslateX(300);
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-45);

		PointLight light = new PointLight();
		light.setColor(Color.rgb(255, 255, 255,1));
		light.setTranslateX(camera.getTranslateX());
		light.setTranslateY(camera.getTranslateY());
		light.setTranslateZ(camera.getTranslateZ());

		//собираем всё
		Group group = new Group();
		group.getChildren().addAll(Boxes);
		group.getChildren().addAll(light);
		Scene scene = new Scene(group, 800, 800);

		scene.setCamera(camera);
		stage.setScene(scene);
		stage.show();
	}

	public void DrawWater(Water water, Ground ground) {
		for (int i = 0; i < SquaresOnWidth; i++) {
			for (int j = 0; j < SquaresOnDepth; j++) {
				if (water.columns[i][j].level < minLevel)
					water.columns[i][j].box.setVisible(false);
				else {
					water.columns[i][j].box.setHeight(water.columns[i][j].level);
					water.columns[i][j].box.setTranslateY(height - ground.levelOfGround[i][j] - water.columns[i][j].level / 2);
					water.columns[i][j].box.setVisible(true);
				}
			}
		}
	}
}



