package com.example.deluge3d;
public class Water
{

	public int SquaresOnWidth = Settings.SquaresOnWidth;
	public int SquaresOnDepth = Settings.SquaresOnDepth;
	public ColumnOfWater[][] columns;

	public Water()
	{
		columns =new ColumnOfWater[SquaresOnWidth][SquaresOnDepth];
		for(int i=0;i<SquaresOnWidth;i++)
			for(int j=0;j<SquaresOnDepth; j++)
				columns[i][j] = new ColumnOfWater();
	}

	//	public float AmountOfWater(){
	//		float summ = 0;
	//		for(int i=0;i<SquaresOnWidth;i++)
	//			for(int j=0;j<SquaresOnDepth; j++)
	//				summ += columns[i][j].level;
	//		return summ;
	//	}
	public void Move(Ground ground)
	{
		for(int i=1;i<SquaresOnWidth-1;i++)
			for (int j = 1; j < SquaresOnDepth-1; j++) {
				//перемещение в стороны
				columns[i-1][j].IncreaseLevel(columns[i][j].getLeftSpeed());
				columns[i+1][j].IncreaseLevel(columns[i][j].getRightSpeed());
				columns[i][j-1].IncreaseLevel(columns[i][j].getForwardSpeed());
				columns[i][j+1].IncreaseLevel(columns[i][j].getBackSpeed());

				columns[i][j].DecreaseLevel();
				//впитывание в землю
			}
		if (Settings.PermeabilityOfBoundaries){
			for (int i=0;i<SquaresOnWidth;i++){
				columns[i][SquaresOnDepth-1].setLevel(0);
				columns[i][0].setLevel(0);
			}
			for (int j =0 ; j< SquaresOnDepth;j++){
				columns[SquaresOnWidth-1][j].setLevel(0);
				columns[0][j].setLevel(0);
			}
		}
	}

	public void MoveIntoGround(Ground ground) {
		for (int i = 0; i < SquaresOnWidth; i++)
			for (int j = 0; j < SquaresOnDepth; j++)
				if (ground.cubs[i][j][ground.upperIndex[i][j]].water < ground.capacity)
					if (columns[i][j].level >= ground.speed) {
						ground.cubs[i][j][ground.upperIndex[i][j]].water += ground.speed;
						columns[i][j].level -= ground.speed;
					}
					else {
						ground.cubs[i][j][ground.upperIndex[i][j]].water += columns[i][j].level;
						columns[i][j].setLevel(0);
					}
	}
	public void CalculateSpeed(int[][] ground)
	{
		int k; //количество квадратов куда может потечь вода (включая сам квадратик на котором она находится) для расчета среднего значения высоты
		float mean; //среднее значение уровней
		boolean leftLower; //слева ниже
		boolean rightLower; //справа ниже
		boolean forwardLower; //спереди ниже
		boolean backLower; //сзади ниже
		boolean selfLower; //сам ниже
		float summ; //сумма уровней
		for(int i=1;i<SquaresOnWidth-1;i++) { //БЕЗ ГРАНИЧНЫХ СЛУЧАЕВ!!!!
			for (int j = 1; j < SquaresOnDepth-1; j++) { //перебираем все квадраты кроме граничных
				if (columns[i][j].level==0) //если уровень воды=0, то перетекать она никуда не может, скорости=0
				{
					columns[i][j].setLeftSpeed(0);
					columns[i][j].setRightSpeed(0);
					columns[i][j].setForwardSpeed(0);
					columns[i][j].setBackSpeed(0);
					continue;
				}
				k = 1;
				leftLower = false;
				rightLower = false;
				forwardLower = false;
				backLower = false;
				selfLower = true;

				//определяем направления куда может течь вода
				if (ground[i - 1][j] + columns[i - 1][j].level < ground[i][j] + columns[i][j].level) { //если на левом уровень ниже
					leftLower = true; //то он действительно ниже
					k++; //вода может на него потечь
				}
				if (ground[i + 1][j] + columns[i + 1][j].level < ground[i][j] + columns[i][j].level) {
					rightLower = true;
					k++;
				}
				if (ground[i][j - 1] + columns[i][j - 1].level < ground[i][j] + columns[i][j].level) {
					forwardLower = true;
					k++;
				}
				if (ground[i][j + 1] + columns[i][j + 1].level < ground[i][j] + columns[i][j].level) {
					backLower = true;
					k++;
				}

				//если никуда потечь нельзя, то и скорости = 0
				if (k==1){
					columns[i][j].setLeftSpeed(0);
					columns[i][j].setRightSpeed(0);
					columns[i][j].setForwardSpeed(0);
					columns[i][j].setBackSpeed(0);
					continue;
				}

				//а если куда-то можно
				//считаем сумму уровней для учитываемых квадратов
				summ = columns[i][j].level + ground[i][j]; //самого себя тоже считаем
				if (leftLower) summ+= ground[i - 1][j] + columns[i - 1][j].level; //если можно потечь влево левый тоже считаем
				if (rightLower) summ +=ground[i + 1][j] + columns[i + 1][j].level;
				if (forwardLower) summ +=ground[i][j-1] + columns[i][j-1].level;
				if (backLower) summ +=ground[i][j+1] + columns[i][j+1].level;

				mean = summ / k; //считаем средний уровень

				for (int l = 0; l<4; l++ ) {
					// если на текущем квадрате уровень земли выше чем средний уровень, то на этот квадрат вода течь не может,
					// но она может стекать с него, поэтому воду с него следует учитывать
					if(selfLower && ground[i][j] >= mean){
						selfLower = false;
						summ -= ground[i][j];
						k--;
					}
					//аналогично для всех соседей, но для них вычитаем и землю и воду
					if (leftLower && (ground[i - 1][j] + columns[i - 1][j].level > mean)) {
						leftLower = false;
						summ -= ground[i - 1][j] + columns[i - 1][j].level;
						k--;
					}
					if (rightLower && (ground[i + 1][j] + columns[i + 1][j].level > mean)) {
						rightLower = false;
						summ -= ground[i + 1][j] + columns[i + 1][j].level;
						k--;
					}
					if (forwardLower && (ground[i][j - 1] + columns[i][j - 1].level > mean)) {
						forwardLower = false;
						summ -= ground[i][j - 1] + columns[i][j - 1].level;
						k--;
					}
					if (backLower && (ground[i][j + 1] + columns[i][j + 1].level > mean)) {
						backLower = false;
						summ -= ground[i][j + 1] + columns[i][j + 1].level;
						k--;
					}
					if (mean == summ/k) // если ничего не изменилось можно больше не пересчитывать
						break;
					else
						mean = summ / k; //пересчитываем средний уровень
				}

				// присваиваем скорости
				if(leftLower) columns[i][j].setLeftSpeed(mean - ground[i - 1][j] - columns[i - 1][j].level );
				else columns[i][j].setLeftSpeed(0);
				if(rightLower) columns[i][j].setRightSpeed(mean - ground[i + 1][j] - columns[i + 1][j].level );
				else columns[i][j].setRightSpeed(0);
				if(forwardLower) columns[i][j].setForwardSpeed(mean - ground[i][j-1] - columns[i][j-1].level);
				else columns[i][j].setForwardSpeed(0);
				if(backLower) columns[i][j].setBackSpeed(mean - ground[i][j+1] - columns[i][j+1].level);
				else columns[i][j].setBackSpeed(0);

				if (!columns[i][j].CheckSpeeds())
					System.err.println("Скорости больше объёма");
			}
		}
	}
}