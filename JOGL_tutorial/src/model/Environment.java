package model;

import java.util.ArrayList;

import javax.vecmath.Point3f;

/**
 * @author snowgoon88ATgmailDOTcom
 *
 */
public class Environment {

	/** List of every walls */
	ArrayList<Wall> _walls;
	
	public Environment() {
		_walls = new ArrayList<Wall>();
		init();
	}
	
	public void init() {
		Wall w;
		// Outer Walls
		w = new Wall( new Point3f(new float [] {-5.0f, 5.0f, 0.0f}),
		                  new Point3f(new float [] {5.0f, 5.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {5.0f, 5.0f, 0.0f}),
                new Point3f(new float [] {5.0f, 3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {5.0f, 3.0f, 0.0f}),
                new Point3f(new float [] {4.5f, 3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {4.5f, 3.0f, 0.0f}),
                new Point3f(new float [] {4.5f, -3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {4.5f, -3.0f, 0.0f}),
                new Point3f(new float [] {3.0f, -3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.0f, -3.0f, 0.0f}),
                new Point3f(new float [] {3.0f, -5.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.0f, -5.0f, 0.0f}),
                new Point3f(new float [] {-1.0f, -5.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {-1.0f, -5.0f, 0.0f}),
                new Point3f(new float [] {-1.0f, -2.5f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {-1.0f, -2.5f, 0.0f}),
                new Point3f(new float [] {-5.0f, -2.5f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {-5.0f, -2.5f, 0.0f}),
                new Point3f(new float [] {-5.0f, 5.0f, 0.0f}));
		_walls.add( w );
		
		// Inner Walls
		w = new Wall( new Point3f(new float [] {-3.0f, 3.0f, 0.0f}),
                new Point3f(new float [] {3.0f, 3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.0f, 3.0f, 0.0f}),
                new Point3f(new float [] {3.5f, 3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.5f, 3.0f, 0.0f}),
                new Point3f(new float [] {3.5f, -2.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.5f, -2.0f, 0.0f}),
                new Point3f(new float [] {3.0f, -2.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.0f, -2.0f, 0.0f}),
                new Point3f(new float [] {3.0f, -1.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {3.0f, -1.0f, 0.0f}),
                new Point3f(new float [] {-3.0f, -1.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {-3.0f, -1.0f, 0.0f}),
                new Point3f(new float [] {-3.0f, 3.0f, 0.0f}));
		_walls.add( w );
		w = new Wall( new Point3f(new float [] {-1.0f, -2.0f, 0.0f}),
                new Point3f(new float [] {-5.0f, 5.0f, 0.0f}));
		_walls.add( w );
	}
	
	public ArrayList<Wall> getWalls() {
		return _walls;
	}
}
