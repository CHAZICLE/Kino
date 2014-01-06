package kino.util;

public class Vector3d {
	private double x = 0;
	private double y = 0;
	private double z = 0;
	public Vector3d setX(double paramX) { x = paramX; return this; }
	public Vector3d setY(double paramY) { y = paramY; return this; }
	public Vector3d setZ(double paramZ) { z = paramZ; return this; }
	public Vector3d setXY(double paramX, double paramY) { x = paramX; y = paramY; return this; }
	public Vector3d setYZ(double paramY, double paramZ) { y = paramY; z = paramZ; return this; }
	public Vector3d setXZ(double paramX, double paramZ) { x = paramX; z = paramZ; return this; }
	public Vector3d setXYZ(double paramX, double paramY, double paramZ) { x = paramX; y = paramY; z = paramZ; return this; }
	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }
	public Vector3d(double paramX, double paramY, double paramZ)
	{
		x = paramX;
		y = paramY;
		z = paramZ;
		store();
	}
	public Vector3d() { }
	
	// Add
	public Vector3d add(double paramX, double paramY, double paramZ) { x += paramX; y += paramY; z += paramZ; return this; }
	public Vector3d add(Vector3d paramVect) { return add(paramVect.x,paramVect.y,paramVect.z); }
	public Vector3d addMake(double paramX, double paramY, double paramZ) { return new Vector3d(x+paramX,y+paramY,z+paramZ); }
	public Vector3d addMake(Vector3d paramVect) { return addMake(paramVect.x,paramVect.y,paramVect.z); }
	// Subtract
	public Vector3d subtract(double paramX, double paramY, double paramZ) { x -= paramX; y -= paramY; z -= paramZ; return this; }
	public Vector3d subtract(Vector3d paramVect) { return subtract(paramVect.x,paramVect.y,paramVect.z); }
	public Vector3d subtractMake(double paramX, double paramY, double paramZ) { return new Vector3d(x-paramX,y-paramY,z-paramZ); }
	public Vector3d subtractMake(Vector3d paramVect) { return subtractMake(paramVect.x,paramVect.y,paramVect.z); }
	// Multiply
	public Vector3d multiplex(double paramX, double paramY, double paramZ) { x *= paramX; y *= paramY; z *= paramZ; return this; }
	public Vector3d multiplex(Vector3d paramVect) { return multiplex(paramVect.x,paramVect.y,paramVect.z); }
	public Vector3d multiplexMake(double paramX, double paramY, double paramZ) { return new Vector3d(x*paramX,y*paramY,z*paramZ); }
	public Vector3d multiplexMake(Vector3d paramVect) { return multiplexMake(paramVect.x,paramVect.y,paramVect.z); }
	// Multiplex
	public Vector3d multiply(double param) { x *= param; y *= param; z *= param; return this; }
	public Vector3d multiplyMake(double param) { return new Vector3d(x*param,y*param,z*param); }
	// Dot Product
	public double dot(double paramX, double paramY, double paramZ) { return paramX*x+paramY*y+paramZ*z; }
	public double dot(Vector3d paramVect) { return dot(paramVect.x,paramVect.y,paramVect.z); }
	// Cross Product
	public Vector3d cross(double paramX, double paramY, double paramZ) { double pX = (y*paramZ)-(z*paramY); double pY = (z*paramX)-(x*paramZ); z = (x*paramY)-(y*paramX); x = pX; y = pY; return this; }
	public Vector3d cross(Vector3d paramVect) { return cross(paramVect.x,paramVect.y,paramVect.z); }
	public Vector3d crossMake(double paramX, double paramY, double paramZ) { return new Vector3d( ((y*paramZ)-(z*paramY)), ((z*paramX)-(x*paramZ)), ((x*paramY)-(y*paramX)) ); }
	public Vector3d crossMake(Vector3d paramVect) { return crossMake(paramVect.x,paramVect.y,paramVect.z); }
	// Magnitude
	public double getMagnitude() { return Math.sqrt(getMagnitudeSquared()); }
	public double getMagnitudeSquared() { return x*x+y*y+z*z; }
	public Vector3d setMagnitude(double magnitude) { double mag = getMagnitude(); x *= magnitude/mag; y *= magnitude/mag; z *= magnitude/mag; return this; }
	public Vector3d makeMagnitude(double magnitude) { double mag = getMagnitude(); return new Vector3d( x*magnitude/mag, y*magnitude/mag, z*magnitude/mag ); }
	public Vector3d normalise() { double mag = getMagnitude(); x /= mag; y /= mag; z /= mag; return this; }
	public Vector3d normaliseMake() { double mag = getMagnitude(); return new Vector3d(x/mag,y/mag,z/mag); }
	
	public Vector3d reflect(Vector3d n)
	{
		// v = this
		//-2*(V.N)*N+V
		n.normalise();
		// v-2(v.n^)n^
		return subtract(n.multiply(2*dot(n)));
	}
	public Vector3d flatten(Vector3d n)
	{
		// a-(a.n)n
		n.normalise();
		return subtract(n.multiply(dot(n)));
	}
	
	public static Vector3d establish(Vector3d vec, double x, double y, double z)
	{
		if(vec==null)
			vec = new Vector3d(x,y,z);
		else
			vec.setXYZ(x,y,z);
		return vec;
		
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+","+z+" ::: "+getMagnitude()+")";
	}
	public Vector3d set(Vector3d newVec) {
		setXYZ(newVec.x, newVec.y, newVec.z);
		return this;
	}
	public Vector3d nullify()
	{
		return setXYZ(0, 0, 0);
	}
	
	
	private double storeX,storeY,storeZ;
	public void store()
	{
		storeX=x;
		storeY=y;
		storeZ=z;
	}
	public void load()
	{
		x=storeX;
		y=storeY;
		z=storeZ;
	}
	public void swap()
	{
		double tempX = x;
		double tempY = y;
		double tempZ = z;
		load();
		storeX = tempX;
		storeY = tempY;
		storeZ = tempZ;
	}
}
