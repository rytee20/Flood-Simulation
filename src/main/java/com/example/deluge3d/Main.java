package com.example.deluge3d;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static Water water;
	private static Scene3D scene3D;
	private static Scene2D scene2D;
	private static ControlFrame controlFrame;
	private static Ground ground;
	private static OriginsOfWater origins;
	private static AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long l) {
			timerTick();
		}
	};

	public static void stopTimer(){
		timer.stop();
	}

	public static void RunTimer(){
		timer.start();
	}


	@Override
	public void start(Stage stage) {
		try {
			Settings.Read_Settings();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		ground = new Ground();
		water = new Water();
		if (Settings.show3d)
			scene3D = new Scene3D(ground , water, stage);
		if (Settings.show2d)
			scene2D = new Scene2D();
		if (!Settings.show2d && !Settings.show3d) {
			System.err.println("Не выбран ни один из способов визуализации!");
		}
		controlFrame = new ControlFrame();
		origins = new OriginsOfWater();
		for(int i=0;i<Settings.amountOfSources;i++)
			origins.AddSource(Settings.locationOfSourceX[i], Settings.locationOfSourceY[i],Settings.sizeOfSourceX[i],Settings.sizeOfSourceY[i],Settings.powerOfSource[i]);
		timer.start();
	}

	private static void timerTick() {

		if (controlFrame.waterComing())
			origins.AddWater(water);
		if (Settings.show3d)
			scene3D.DrawWater(water, ground);
		if (Settings.show2d)
			scene2D.DrawWater(water,ground);
		if (Settings.absorptionIntoGround) {
			water.MoveIntoGround(ground);
			ground.CalculateSpeed();
			ground.Move();
		}

		water.CalculateSpeed(ground.levelOfGround);
		water.Move(ground);




	}

	public static void main(String[] args) {
		launch();
	}
}