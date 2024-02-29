package com.example.deluge3d;

public class Ground {
	public int SquaresOnWidth = Settings.SquaresOnWidth;
	public int SquaresOnDepth = Settings.SquaresOnDepth;
	public int SquaresOnHeight = Settings.SquaresOnHeight;
	public int heightOfGroundCube = Settings.heightOfScene / SquaresOnHeight;
	public int[][] levelOfGround = Settings.getLandscape();
	public CubeOfGround[][][] cubs;
	public int[][] upperIndex = new int[SquaresOnWidth][SquaresOnDepth];
	public int maxLevel;

	public float capacity = Settings.capacity;
	public float speed = 0.1f;

	public Ground() {

		//находим максимальную высоту ландшафта
		maxLevel = 0;
		for (int i = 0; i < SquaresOnWidth; i++)
			for (int j = 0; j < SquaresOnDepth; j++)
				if (levelOfGround[i][j] > maxLevel)
					maxLevel = levelOfGround[i][j];

		if (Settings.absorptionIntoGround)
			CreateDiscreteMap();
	}

	public void CalculateSpeed() {
		for (int i = 0; i < SquaresOnWidth; i++) {
			for (int j = 0; j < SquaresOnDepth; j++) {
				if (cubs[i][j][0].water >= capacity)
						cubs[i][j][0].speedDown = speed;
				for (int k = 1; k < upperIndex[i][j]; k++) {
					if ((cubs[i][j][k - 1].water < capacity) && (cubs[i][j][k].water >= capacity))
						cubs[i][j][k].speedDown =  speed;
					else cubs[i][j][k].speedDown = 0;
				}
				//lust
			}
		}
	}

	public void Move() {
		for (int i = 0; i < SquaresOnWidth; i++) {
			for (int j = 0; j < SquaresOnDepth; j++) {
				cubs[i][j][0].water -= cubs[i][j][0].speedDown;
				for (int k = 1; k < upperIndex[i][j]; k++) {
					cubs[i][j][k - 1].water += cubs[i][j][k].speedDown;
					cubs[i][j][k].water -= cubs[i][j][k].speedDown;
				}
			}
		}

	}
	public void CreateDiscreteMap(){
		//создаём дискретную карту земли
		boolean HaveMaxHeight;
		cubs = new CubeOfGround[SquaresOnWidth][SquaresOnDepth][SquaresOnHeight];

		for (int i = 0; i < SquaresOnWidth; i++)
			for (int j = 0; j < SquaresOnDepth; j++) {
				HaveMaxHeight = true;
				for (int k = 0; k < SquaresOnHeight; k++)
					if ((k + 1) * heightOfGroundCube <= levelOfGround[i][j])
						cubs[i][j][k] = new CubeOfGround(true); // true означает наличие земли
					else {
						upperIndex[i][j] = k ;
						HaveMaxHeight = false;
						for (int m = k; m < SquaresOnHeight; m++)
							cubs[i][j][m] = new CubeOfGround(false); // false означает отсутствие земли
						break;
					}
				if (HaveMaxHeight)
					upperIndex[i][j] = SquaresOnHeight - 1;
			}

		//делаем карту высот так же дискретной
		for (int i = 0; i < SquaresOnWidth; i++)
			for (int j = 0; j < SquaresOnDepth; j++) {
				HaveMaxHeight = true;
				for (int k = 0; k < SquaresOnHeight; k++)
					if (!cubs[i][j][k].isGround) {
						levelOfGround[i][j] = k * heightOfGroundCube;
						HaveMaxHeight = false;
						break;
					}
				if (HaveMaxHeight)
					levelOfGround[i][j] = heightOfGroundCube * SquaresOnHeight;
			}
	}
}
