package com.example.deluge3d;
import javafx.scene.shape.Box;

public class ColumnOfWater {
	public float level;
	private float leftSpeed;
	private float rightSpeed;
	private float forwardSpeed;
	private float backSpeed;

	private static final float error_rate = Settings.error_rate;

	public Box box;
	
	public ColumnOfWater(){
		if (Settings.show3d){
			int widthOfSquare = Settings.widthOfScene / Settings.SquaresOnWidth;
			box = new Box(widthOfSquare, 0, widthOfSquare);
		}
	}

	public void setLeftSpeed(float speed){
		if ((speed<0)&&(speed>-error_rate)) speed = 0;
		if(speed < 0)
			System.err.println("Отрицательная скорость: "+ speed);
		else leftSpeed = speed;
	}
	public void setRightSpeed(float speed){
		if ((speed<0)&&(speed>-error_rate)) speed = 0;
		if(speed < 0)
			System.err.println("Отрицательная скорость: "+ speed);
		else rightSpeed = speed;
	}
	public void setForwardSpeed(float speed){
		if ((speed<0)&&(speed>-error_rate)) speed = 0;
		if(speed < 0)
			System.err.println("Отрицательная скорость: "+ speed);
		else forwardSpeed = speed;
	}
	public void setBackSpeed(float speed){
		if ((speed<0)&&(speed>-error_rate)) speed = 0;
		if(speed < 0)
			System.err.println("Отрицательная скорость: "+ speed);
		else backSpeed = speed;
	}
	public boolean CheckSpeeds(){
		return (leftSpeed+rightSpeed+forwardSpeed+backSpeed<level+0.02);
	}
	public void IncreaseLevel(float increase){
		this.level += increase;
		if (increase<0)
			System.err.println("Отрицательное увеличение уровня воды: "+ level);
	}

	public void DecreaseLevel(){
	this.level -= leftSpeed+rightSpeed+forwardSpeed+backSpeed;
	if((level>-error_rate)&(level<0)) level = 0;
	if (level<0)
		System.err.println("Отрицательный уровень воды: "+ level);
	}

	public void setLevel(float value){
		if (value<0)
			System.err.println("Отрицательный уровень воды: "+ level);
		else
			level = value;
	}

	public float getLeftSpeed() {
		return leftSpeed;
	}
	public float getRightSpeed() {
		return rightSpeed;
	}
	public float getForwardSpeed() {
		return forwardSpeed;
	}
	public float getBackSpeed(){
		return backSpeed;
	}
}
