package kino.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class ConvertModel {
	
	private static long last = 100;
	public static void main(String[] args) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("./res/model/DuplicateModel.obj"));
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("./res/model/kino_model.knm"));
		String line;
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> faces = new ArrayList<Integer>();
		while((line=br.readLine())!=null)
		{
			if(line.startsWith("v "))
			{
				String[] parts = line.split(" ");
				vertices.add(Float.parseFloat(parts[1]));
				vertices.add(Float.parseFloat(parts[2]));
				vertices.add(Float.parseFloat(parts[3]));
			}
			else if(line.startsWith("f "))
			{
				String[] parts = line.split(" ");
				faces.add(Integer.parseInt(parts[1])-1);
				faces.add(Integer.parseInt(parts[2])-1);
				faces.add(Integer.parseInt(parts[3])-1);
			}
		}
		dos.writeByte(0);
		dos.writeInt(vertices.size()/3);
		for(Float f : vertices)
			dos.writeFloat(f);
		System.out.println("Wrote "+vertices.size()+" vertex data points ("+(vertices.size()/3)+" vertices)");
		dos.writeInt(faces.size()/3);
		for(Integer i : faces)
			dos.writeInt(i);
		System.out.println("Wrote "+faces.size()+" face data points ("+(faces.size()/3)+" faces)");
		dos.close();
		br.close();
	}
}
