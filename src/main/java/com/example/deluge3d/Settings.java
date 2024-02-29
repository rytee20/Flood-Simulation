package com.example.deluge3d;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class Settings {
	public static boolean show3d; // Показать 2Д
	public static boolean show2d; // Показать 3Д
	public static boolean absorptionIntoGround = false; // впитывание ещё не готово
	public static boolean PermeabilityOfBoundaries; //Проницаемость границ
	public static float minLevel; // Минимальный уровень воды
	public static float error_rate;//допустимая погрешность

	public static float capacity;
	public static int widthOfFrame; // Ширина окна
	public static int heightOfFrame; // Высота окна
	public static int widthOfScene; // Ширина сцены
	public static int heightOfScene; // Высота сцены
	public static int depthOfScene; // глубина сцены

	public static int SquaresOnHeight; // Квадраты по высоте
	public static int SquaresOnWidth; // Квадраты по ширине
	public static int SquaresOnDepth; // Квадраты по глубине

	public static int amountOfSources; // Количество источников
	public static float[] powerOfSource; // Сила источников
	public static float powerOfPrecipitation; // Сила осадков
	public static int[] locationOfSourceX; // Место источников по Х
	public static int[] locationOfSourceY; // Место источников по У
	public static int[] sizeOfSourceX; // Размер источников по Х
	public static int[] sizeOfSourceY; // Размер источников по У
	public static String sourceFileName; // Имя файла, где хранятся данные об источниках
	public static String landscapeFileName; // Имя файла, где хранятся данные о ландшафте
	private static int[][] Landscape; // Ландшафт

	// Чтение настроек из конфигурационного файла
	public static void Read_Settings() throws IOException, IllegalArgumentException, NegativeArgumentException {
		// Открываем конфигурационный файл
		Properties props = new Properties();
		props.load(new FileInputStream("Settings.ini"));

		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add("txt");
		extensions.add("jpg");
		landscapeFileName = ReadFileName(props,"landscapeFileName",extensions);

		show2d = ReadBool(props,"show2d", "true");
		show3d = ReadBool(props,"show3d", "true");
		absorptionIntoGround = ReadBool(props, "absorptionIntoGround", "false");
		PermeabilityOfBoundaries = ReadBool(props,"PermeabilityOfBoundaries", "false");
		minLevel = ReadFloat(props,"minLevel","5");
		error_rate = ReadFloat(props,"error_rate","0.001");
		widthOfFrame = ReadInt(props, "widthOfFrame","400");
		heightOfFrame = ReadInt(props, "heightOfFrame","400");
		widthOfScene = ReadInt(props, "widthOfScene", "800");
		heightOfScene = ReadInt(props, "heightOfScene", "800");
		depthOfScene = ReadInt(props, "depthOfScene", "800");
		SquaresOnHeight = ReadInt(props, "SquaresOnHeight", "10");
		powerOfPrecipitation = ReadFloat(props,"powerOfPrecipitation", "0");
		amountOfSources = ReadInt(props,"amountOfSources", "0");
		capacity = ReadFloat(props, "CapacityOfGround", "10");




		if ((widthOfFrame < SquaresOnWidth)||(heightOfFrame < SquaresOnDepth))
			throw new IOException("Размеры окна меньше размеров ланшафта");

		if (Objects.equals(landscapeFileName.split("\\.")[1], "txt"))
			CreateTestLandscape();
		if (Objects.equals(landscapeFileName.split("\\.")[1], "jpg"))
			CreateLandscapeByPicture();
		if (amountOfSources>0)
			ReadDataAboutSources(props);
	}

	public static void ReadDataAboutSources(Properties props) throws IOException
	{
			ArrayList<String> extensions=new ArrayList<>();
			extensions.add("txt");
			sourceFileName = ReadFileName(props, "sourceFileName",extensions);

			powerOfSource = new float[amountOfSources];
			locationOfSourceX = new int[amountOfSources];
			locationOfSourceY = new int[amountOfSources];
			sizeOfSourceX = new int[amountOfSources];
			sizeOfSourceY = new int[amountOfSources];

			File file = new File(sourceFileName);
			Scanner scanner = new Scanner(file);
			scanner.nextLine();

			String[] line;
			for (int i=0;i<amountOfSources;i++)
			{
				if (scanner.hasNextLine()) {
					line = scanner.nextLine().split("\\s+");
					if (line.length < 5) {
						throw new IllegalArgumentException("Некорректные данные в файле " + sourceFileName);
					} else {
						try {
							locationOfSourceX[i] = Integer.parseInt(line[0]);
							locationOfSourceY[i] = Integer.parseInt(line[1]);
							powerOfSource[i] = Float.parseFloat(line[2]);
							sizeOfSourceX[i] = Integer.parseInt(line[3]);
							sizeOfSourceY[i] = Integer.parseInt(line[4]);
							if((locationOfSourceX[i] > SquaresOnWidth)||(locationOfSourceY[i] > SquaresOnDepth))
								throw new IOException(i+1+ "й источник имеет координаты: " +locationOfSourceX[i] +", "+locationOfSourceY[i] + " что выходит за размеры размеры карты: " + SquaresOnWidth+"x"+SquaresOnDepth+"\n");
							if((locationOfSourceX[i]+sizeOfSourceX[i] > SquaresOnWidth)||(locationOfSourceY[i]+sizeOfSourceY[i] > SquaresOnDepth))
								throw new IOException(i+1+ "й источник имеет координаты: " +locationOfSourceX[i] +", "+locationOfSourceY[i]+" и размеры "+sizeOfSourceX[i]+"x"+sizeOfSourceY[i] + " что выходит за размеры размеры карты: " + SquaresOnWidth+"x"+SquaresOnDepth+"\n");

						} catch (IllegalArgumentException e) {
							throw new IllegalArgumentException("Некорректные данные в файле " + sourceFileName);
						}
					}
				}
				else {
					System.err.println("Количество источников указано как "+ amountOfSources+
							" однако файл "+sourceFileName+" содержит информацию только о "+i+" источниках");
					break;
				}
			}
			scanner.close();

	}

	public static int ReadInt(Properties props, String field, String standardValue) throws IllegalArgumentException, NegativeArgumentException{
		try {
			int result = Integer.parseInt(props.getProperty(field, standardValue));
			if (result<0)
				throw new NegativeArgumentException("Отрицательное значение в поле " + field);
			return result;
		}
		catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Некорректные данные в поле " + field);
		}
	}
	public static float ReadFloat(Properties props, String field, String standardValue) throws IllegalArgumentException, NegativeArgumentException{
		try {
			float result = Float.parseFloat(props.getProperty(field, standardValue));
			if (result<0)
				throw new NegativeArgumentException("Отрицательное значение в поле " + field);
			return result;
		}
		catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Некорректные данные в поле " + field);
		}
	}
	public static boolean ReadBool(Properties props, String field, String standardValue) throws IllegalArgumentException{
		try {
			return Boolean.parseBoolean(props.getProperty(field, standardValue));
		}
		catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Некорректные данные в поле " + field);
		}
	}
	public static String ReadFileName(Properties props, String field, List<String> extensions) throws IOException{
		String FileName = props.getProperty(field);
		Path path = Paths.get(FileName);

		if (!FileName.contains("."))
			throw new IOException("Не указано расширение файла "+FileName);
		else if (!(extensions.contains(FileName.split("\\.")[1]))) {
			String message = "Недопустимый формат файла " + FileName;
			message += "\nДопустимые форматы:\n";
			for(String extension : extensions)
				message += extension+'\n';
			throw new IOException(message);
		}
		else if (Files.notExists(path))
			throw new FileNotFoundException("Файл "+FileName+" не найден" );
		else return FileName;
	}

	// Создание тестового ландшафта
	public static void CreateTestLandscape() throws IOException {
		// Читаем его из файла
		// Сначала узнаем его масштаб для инициализации массива
		File file = new File(landscapeFileName);
		Scanner scanner_count_lines = new Scanner(file);
		int lines=0;
		int row=0;
		while (scanner_count_lines.hasNextLine()) {
			String[] line = scanner_count_lines.nextLine().split(" ");
			lines++;
			row = line.length;
		}
		scanner_count_lines.close();
		Landscape = new int[lines][row];
		// Проинициализировали массив и теперь считываем сами данные и заодно преобразовываем их в инт
		Scanner scanner = new Scanner(file);
		int count_line=0;
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\\s+");
			for (int i = 0; i < line.length; i++) {
				Landscape[count_line][i]=Integer.parseInt(line[i]);
			}
			count_line++;
		}
		// Количество квадратов по ширине и глубине соответственно равно количеству строк и столбцов
		SquaresOnWidth = lines;
		SquaresOnDepth = row;
	}

	public static void CreateLandscapeByPicture() throws IOException{

			// Открываем изображение
			File file = new File(landscapeFileName);
			BufferedImage SourceMap = ImageIO.read(file);

			Settings.SquaresOnWidth= SourceMap.getWidth();
			Settings.SquaresOnDepth=SourceMap.getHeight();

			Landscape = new int[SquaresOnWidth][SquaresOnDepth];
			// Делаем двойной цикл, чтобы обработать каждый пиксель
			for (int x = 0; x < SourceMap.getWidth(); x++) {
				for (int y = 0; y < SourceMap.getHeight(); y++) {

					// Получаем цвет текущего пикселя
					Color color = new Color(SourceMap.getRGB(x, y));

					// Получаем каналы этого цвета
					int blue = color.getBlue();
					int red = color.getRed();
					int green = color.getGreen();

					// Применяем стандартный алгоритм для получения черно-белого изображения
					int grey = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
					Landscape[x][y] = grey;
				}
			}
	}
	public static int[][] getLandscape(){
		return Landscape;
	}
}