package kino.cache.BB;

import java.util.Random;

import kino.cache.Entity;
import kino.util.SelfRenderingEntity;

import org.lwjgl.opengl.GL11;

public class BBRenderDebug
{
	public static class Cube extends Entity implements SelfRenderingEntity {
		double scaleX = 2;
		double scaleY = 2;
		double scaleZ = 2;
		public Cube(double sx, double sy, double sz)
		{
			scaleX = sx;
			scaleY = sy;
			scaleZ = sz;
			debugBoundingBox = new AxisAlignedBoundingBox(this,scaleX,scaleY,scaleZ);
			//debugBoundingBox = new RadialBoundingBox(this,scale/2);
		}
		@Override
		public String toString() {
			return "BBRenderDebug.Cube("+scaleX+","+scaleY+","+scaleZ+")";
		}

		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glTranslated(position.getX(),position.getY(),position.getZ());
			GL11.glScaled(scaleX,scaleY,scaleZ);
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glColor3f(1.0f,0.0f,0.5f);
			GL11.glVertex3f( 0.5f, -0.5f,-0.5f);
			GL11.glVertex3f( 0.5f, -0.5f, 0.5f);
			GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
			GL11.glVertex3f(-0.5f, -0.5f,-0.5f);
			
			GL11.glColor3f(0.0f,1.0f,0.0f);
			GL11.glVertex3f( 0.5f, 0.5f,-0.5f);
			GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
			GL11.glVertex3f(-0.5f, 0.5f,-0.5f);
			
			GL11.glColor3f(1.0f,0.0f,0.0f);
			GL11.glVertex3f( 0.5f, 0.5f,-0.5f);
			GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
			GL11.glVertex3f( 0.5f,-0.5f, 0.5f);
			GL11.glVertex3f( 0.5f,-0.5f,-0.5f);
			
			GL11.glColor3f(0.0f,1.0f,1.0f);
			GL11.glVertex3f(-0.5f, 0.5f,-0.5f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
			GL11.glVertex3f(-0.5f,-0.5f, 0.5f);
			GL11.glVertex3f(-0.5f,-0.5f,-0.5f);
			
			GL11.glColor3f(0.0f,0.0f,1.0f);
			GL11.glVertex3f( 0.5f,-0.5f, 0.5f);
			GL11.glVertex3f( 0.5f, 0.5f, 0.5f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
			GL11.glVertex3f(-0.5f,-0.5f, 0.5f);
			
			GL11.glColor3f(1.0f,1.0f,0.0f);
			GL11.glVertex3f( 0.5f,-0.5f, -0.5f);
			GL11.glVertex3f( 0.5f, 0.5f, -0.5f);
			GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
			GL11.glVertex3f(-0.5f,-0.5f, -0.5f);
			
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	public static class Sphere extends Entity implements SelfRenderingEntity {
		double radius;
		public Sphere(double diameter)
		{
			radius = diameter/2;
			debugBoundingBox = new RadialBoundingBox(this,radius);
			Random r = new Random();
			for(int i=0;i<color.length;i++)
				color[i] = r.nextFloat();
		}
		@Override
		public String toString() {
			return "BBRenderDebug.Sphere("+radius+")";
		}
		float[] color = new float[5000*3];
		@Override
		public void render() {
			GL11.glPushMatrix();
			GL11.glTranslated(position.getX(),position.getY(),position.getZ());
			GL11.glRotatef(90, 1,0,0);
			//sphere.draw((float) (scale/2), 10, 10);
			int slices=50,stacks=50;
			int vertexCount = 0;
			float radius = (float)this.radius;
			
			float rho, drho=(float)(Math.PI/stacks), theta, dtheta=(float)(2.0f*Math.PI/slices);
            float x, y, z;
            int i, j, imin, imax;
            float nsign = 1;
			
			 
			drho = (float) (Math.PI / stacks);
			dtheta = (float) (2.0f * Math.PI / slices);
            
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			
			GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
			vertexCount+=3;
            GL11.glVertex3f(0.0f, 0.0f, nsign * radius);
            
            for (j = 0; j <= slices; j++) {
                    theta = (j == slices) ? 0.0f : j * dtheta;
                    x = (float) (-Math.sin(theta) * Math.sin(drho));
                    y = (float) (Math.cos(theta) * Math.sin(drho));
                    z = (float) (nsign * Math.cos(drho));
                    GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
        			vertexCount+=3;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
            }
            GL11.glEnd();
            
            imin = 1;
            imax = stacks-1;
            
            for (i = imin; i < imax; i++)
            {
                rho = i * drho;
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (j = 0; j <= slices; j++) {
                        theta = (j == slices) ? 0.0f : j * dtheta;
                        x = (float) (-Math.sin(theta) * Math.sin(rho));
                        y = (float) (Math.cos(theta) * Math.sin(rho));
                        z = (float) (nsign * Math.cos(rho));
                        
                        GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
            			vertexCount+=3;
                        GL11.glVertex3f(x * radius, y * radius, z * radius);
                        x = (float) (-Math.sin(theta) * Math.sin(rho + drho));
                        y = (float) (Math.cos(theta) * Math.sin(rho + drho));
                        z = (float) (nsign * Math.cos(rho + drho));
                        GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
            			vertexCount+=3;
                        GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
			vertexCount+=3;
            GL11.glVertex3f(0.0f, 0.0f, -radius * nsign);
            rho = (float) (Math.PI - drho);
            for (j = slices; j >= 0; j--) {
                    theta = (j == slices) ? 0.0f : j * dtheta;
                    x = (float) (-Math.sin(theta) * Math.sin(rho));
                    y = (float) (Math.cos(theta) * Math.sin(rho));
                    z = (float) (nsign * Math.cos(rho));
                    GL11.glColor3f(color[vertexCount+0],color[vertexCount+1],color[vertexCount+2]);
        			vertexCount+=3;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
            }
            GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
}
