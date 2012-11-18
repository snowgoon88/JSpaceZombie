/**
 * 
 */
package test;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import model.Wall;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class TestModels {

	/**
	 * Test l'intersection avec un Wall.
	 */
	public void testIntersect() {
		Wall _wall = new Wall( new Point3f(new float [] {1.0f,-2.0f, 0.0f}),
					           new Point3f(new float [] {1.0f, 1.5f, 0.0f}) );
		
		System.out.println(_wall.toString());
		
		Point3f from = new Point3f(new float [] {0.0f, 0.0f, 0.0f});
		// Un vecteur unitaire tournant
		Vector3f dir = new Vector3f();
		for (double ang = -Math.PI/4; ang <= Math.PI/2.0; ang += Math.PI/20.0) {
			dir.set((float)Math.cos(ang), (float)Math.sin(ang), 0.0f);
			System.out.println("Angle ="+Math.toDegrees(ang));
			Vector3f res = _wall.intersect(from, dir);
			if (res != null) {
				System.out.println(res.toString());
			}
			else {
				System.out.println("No intersect");
			}
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestModels app = new TestModels();
		app.testIntersect();
	}

}
