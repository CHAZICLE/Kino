package kino.util;

public class NumericalTools {
	public static double wrapTo(double start, double number, double end)
	{
		while(number<start)
			number += (end-start);
		while(number>end)
			number -= (end-start);
		return number;
	}
	public static double capTo(double start, double number, double end)
	{
		return number<start ? start : (number>end ? end : number);
	}
	public static float wrapTo(float start, float number, float end)
	{
		while(number<start)
			number += (end-start);
		while(number>end)
			number -= (end-start);
		return number;
	}
	public static float capTo(float start, float number, float end)
	{
		return number<start ? start : (number>end ? end : number);
	}
}
