import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.RowSpec;

public class Tetris {
	public static Cnst cn;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TetrisFrmSelector frame = new TetrisFrmSelector();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

// /////////////////////////////////////////////

/**
 * @author HASHIM HOSSAM
 * @see <a href="hashimhossam@outlook.com">hashimhossam@outlook.com</a>
 *
 */
@SuppressWarnings("javadoc")
class Box implements Comparable<Box> {
	private int x;
	private int y;
	private int w;
	private Color c;// = RandomShape.getColor();

	/**
	 * @param x
	 * @param y
	 */
	public Box(int x, int y) {
		this(x, y, Tetris.cn.BOX_SIZE);
		// this.c =cn.COLOR;
	}

	/**
	 * @param x
	 * @param y
	 * @param c
	 */
	public Box(int x, int y, Color c) {
		this(x, y, Tetris.cn.BOX_SIZE);
		this.c = c;
	}

	/**
	 * @param x
	 * @param y
	 * @param w
	 */
	public Box(int x, int y, int w) {
		this.x = x;
		this.y = y;
		this.w = w;

	}

	/**
	 * @param that
	 * @return true if collides another box
	 */
	@Deprecated
	public boolean collision(Box that) {
		return that.getBounds().intersects(getBounds()) || stickToBottom();

	}

	@Override
	public int compareTo(Box that) {
		if (x == that.x && c == that.c && y == that.y && w == that.w) {
			return 0;
		} else if (x > that.x || y > that.y) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * @param g
	 */
	public void draw(Graphics g) {
		// g.draw3DRect(x, y, w, w, true);
		g.setColor(c);
		g.fill3DRect(x, y, w, w, true);
	}

	/**
	 * let box falls down by a box length step
	 */
	public void fall() {
		move(0, Tetris.cn.BOX_SIZE);
	}

	/**
	 * @return bounds of the box
	 */
	@Deprecated
	public Rectangle getBounds() {
		return new Rectangle(x, y, Tetris.cn.BOX_SIZE, Tetris.cn.BOX_SIZE);
	}

	/**
	 * @return get color of box
	 */
	public Color getC() {
		return c;
	}

	/**
	 * @return get x by pxls
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return get y by pxls
	 */
	public int getY() {
		return y;
	}

	/**
	 *
	 * <code>this</code> is me the landing bird
	 *
	 * @param that
	 *            is the box that i lands on
	 * @return return true if the box lands on another box
	 */
	public boolean landOn(Box that) {
		// boolean lands = false;
		if (x == that.x) {
			if (that.y + Tetris.cn.BOX_SIZE == y) {
				// System.out.println("reversed land");
				return false;
			} else if (y + Tetris.cn.BOX_SIZE == /* > == */that.y) {
				// System.out.println("land");
				// try {
				// Thread.sleep(cn.sleepAmmount);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				return true;
			}
		}
		return false;
		// return that.getBounds().intersects(getBounds()) ||
		// stickToBottom();

	}

	/**
	 * move box by dx and dy
	 *
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	/**
	 * set x by pxls
	 *
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * set y by pxls
	 *
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
     * FIXME: SLAM IS INTERFERING WITH OUTER
	 * @param that
	 * @return true if box slams another box
	 */
	public boolean slam(Box that) {
		return slamLeft(that) || slamRight(that);
	}

	/**
	 * @param that
	 * @return my box slams another left
	 */
	public boolean slamLeft(Box that) {
		if (y == that.y) {
			if (that.x + Tetris.cn.BOX_SIZE >= x) {
				return true;
			}
		}
		return false;
	}

    public boolean outRight() {
//	    System.out.println(x+","+Tetris.cn.SCREEN_WIDTH*Tetris.cn.BOX_SIZE);
        if (x+Tetris.cn.BOX_SIZE> Tetris.cn.SCREEN_WIDTH*Tetris.cn.BOX_SIZE) {
            return true;
        }
        return false;
    }
    public boolean outLeft() {
        if (x <0) {
            return true;
        }
        return false;
    }
	/**
	 * @param that
	 * @return my box slams another left
	 */
	public boolean slamRight(Box that) {
		if (y == that.y) {
			if (x + Tetris.cn.BOX_SIZE >= that.x) {
				return true;
			}
		}
		return false;
	}

	/**
	 * FIXME
	 *
	 * @return true if box is landed on bottom of the screen
	 */
	public boolean stickToBottom() {//
		// if (y >=cn.SCREEN_HEIGHT *cn.BOX_SIZE) {
		// return true;
		// }
		if (y + Tetris.cn.BOX_SIZE >= Tetris.cn.SCREEN_HEIGHT
				* Tetris.cn.BOX_SIZE) {
			// try {
			// Thread.sleep(cn.sleepAmmount / 2);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return true if box is stick to left wall
	 */
	public boolean stickToLeft() {
		if (x <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return true if box is stick to right wall
	 */
	public boolean stickToRight() {
		if (x >= Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE) {
			return true;
		}
		if (x + Tetris.cn.BOX_SIZE >= Tetris.cn.SCREEN_WIDTH
				* Tetris.cn.BOX_SIZE) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String str = "(";
		str += x / Tetris.cn.BOX_SIZE;
		str += ",";
		str += y / Tetris.cn.BOX_SIZE;
		str += ")";
		return str;
	}

	/**
	 * @return the x coordinated in terms if boxes
	 */
	public int x() {
		return x / Tetris.cn.BOX_SIZE;
	}

	/**
	 * @return the y coordinated in terms if boxes
	 */
	public int y() {
		return y / Tetris.cn.BOX_SIZE;
	}
}

/**
 * @author HASHIM
 * @see <a href="cse.hashim.hossam@gmail.com">cse.hashim.hossam@gmail.com</a>
 *
 */
@SuppressWarnings("javadoc")
class Cnst {

	/**
	 * box size in pxl
	 */
	public/* final */int BOX_SIZE;// = 25;
	/***
	 * screen width measured by number of boxes
	 */
	public/* final */int SCREEN_WIDTH;// = 10;// number of boxes
	/**
	 * screen height measured by number of boxes
	 */
	public/* final */int SCREEN_HEIGHT;// = 20;// number of boxes
	/**
	 * rotate right const
	 */
	public/* final */boolean ROTATE_RIGHT;// = false;
	/**
	 * rotate left const
	 */
	public/* final */boolean ROTATE_LEFT;// = true;

	/**
	 * number of the set of allowed distinct shapes in the game
	 */
	public/* final */int N_SHAPES;// = 7;
	/**
	 * box pool: is a pool contains boxes that is in rest status
	 */
	public LinkedList<Box> pool;// = new LinkedList<>();
	/**
	 * current falling shape
	 */
	public IShape current;
	/**
	 * is game running or paused or stopped
	 */
	public boolean running;// = true;
	/** sleep amount for the fall thread */
	public long sleepAmmount;// = 1000;
	/** is game over or not */
	public boolean gameover;// = false;
	/** grid color */
	public Color gridColor;// = RandomShape.getColor();
	/** background color */
	public Color backGroundColor;// = Const.gridColor.brighter();
	/** frame color */
	public Color frameColor;// = Const.backGroundColor.brighter();

	public Cnst() {
		BOX_SIZE = 25;
		SCREEN_WIDTH = 10;// number of boxes
		SCREEN_HEIGHT = 20;// number of boxes
		ROTATE_RIGHT = false;
		ROTATE_LEFT = true;
		N_SHAPES = 7;
		pool = new LinkedList<>();
		current = null;
		running = true;
		sleepAmmount = 1000;
		gameover = false;
		gridColor = RandomShape.getColor();
		backGroundColor = gridColor.brighter().brighter();// RandomShape.getColor();
		frameColor = backGroundColor.brighter().brighter();// RandomShape.getColor();}
	}

	/**
	 * how to initiate:<br>
	 * <code>Cnst c = new Cnst(new Object[] { 25, 10, 20, 1000, RandomShape.getColor() });</code>
	 *
	 * @param o
	 */
	public Cnst(Object... o) {
		int i = 0;
		BOX_SIZE = (int) o[i++];
		SCREEN_WIDTH = (int) o[i++];
		SCREEN_HEIGHT = (int) o[i++];
		ROTATE_RIGHT = false;
		ROTATE_LEFT = true;
		N_SHAPES = 7;
		pool = new LinkedList<>();
		current = null;
		running = true;
		sleepAmmount = (long) o[i++];
		gameover = false;
		gridColor = (Color) o[i++];
		backGroundColor = gridColor.brighter();// RandomShape.getColor();
		frameColor = backGroundColor.brighter();// RandomShape.getColor();}
	}

	void exmplofInit() {
		Cnst c = new Cnst(new Object[] { 25, 10, 20, 1000,
				RandomShape.getColor() });
	}

}

// class C {
// /**
// *
// */
// public static Cnst cn;
//
// /**
// *
// */
// public C(Cnst c) {
// C.cn = c;
// }
//
// }

class FourSquaresBox extends AShape {
	public FourSquaresBox() {
		super();
	}

	public FourSquaresBox(Color c) {
		super(c);
	}

	public FourSquaresBox(int x, int y) {
		super(x, y);

	}

	@Override
	protected void buildBoxes() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(0, l);
		sttm.put(1, l);
		sttm.put(2, l);
		sttm.put(3, l);

	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));

		l.add(new Box(0 + b, 0, c));
		l.add(new Box(0, 0 + b, c));
		l.add(new Box(0 + b, 0 + b, c));
		return l;
	}
}

class FourSquaresRod extends AShape {
	public FourSquaresRod() {
		super();
	}

	public FourSquaresRod(Color c) {
		super(c);
	}

	public FourSquaresRod(int x, int y) {
		super(x, y);

	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		l = new LinkedList<>();
		l.add(new Box(b, 0, c));
		l.add(new Box(b - b, 0, c));
		l.add(new Box(b - b - b, 0, c));
		l.add(new Box(b + b, 0, c));
		return l;
	}

	@Override
	protected void buildBoxes() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x - b - b, y, c));
		l.add(new Box(x + b, y, c));
		sttm.put(0, l);
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x - b - b, y, c));
		l.add(new Box(x + b, y, c));
		sttm.put(2, l);

		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y - b - b, c));
		l.add(new Box(x, y + b, c));
		sttm.put(1, l);
		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y - b - b, c));
		l.add(new Box(x, y + b, c));
		sttm.put(3, l);
	}

}

class LeftDuck extends AShape {
	public LeftDuck() {
		super();
	}

	public LeftDuck(Color c) {
		super(c);
	}

	public LeftDuck(int x, int y) {
		super(x, y);

		// TODO Auto-generatedcn.uctor stub
	}

	@Override
	protected void buildBoxes() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(0, l);
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(2, l);
		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y - b, c));
		sttm.put(1, l);
		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y - b, c));
		sttm.put(3, l);
	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		// state 1
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));
		l.add(new Box(0 - b, 0, c));
		l.add(new Box(0, 0 + b, c));
		l.add(new Box(0 + b, 0 + b, c));
		return l;
	}
}

/**
 * @author HASHIM
 * @see <a href="cse.hashim.hossam@gmail.com">cse.hashim.hossam@gmail.com</a>
 *
 */
@SuppressWarnings("javadoc")
interface IShape extends Cloneable {
  IShape clone();


	/**
	 * @param that
	 *            any box from the pool
	 * @return true if collision happened between <code>"that"</code> and the
	 *         current shape
	 */
	@Deprecated
    boolean Collision(Box that);

	/**
	 * @param g
	 *            the graphics object that we paint into
	 */
    void draw(Graphics g);

	/**
	 * @return a list of boxes that represents the shape
	 */
    List<Box> extract();

	/**
	 * use when you want the shape to fall down by one step
	 */
    void fall();

	/**
	 * @return get a default static copy of the shape for being displayed into
	 *         next window (pannel)
	 */
    List<Box> getStaticCopy();

	/**
	 * @return return true if the shape has the capability to move
	 */
    boolean isMoving();

	/**
	 * @param that
	 *            any foreign box
	 * @return true if the shape is landed over <code>"that"</code> box
	 */
    boolean landOn(Box that);

	/**
	 * offer shape to move to the left by one step
	 */
    boolean left();

	/**
	 * offer shape to move to the right by one step
	 */
    boolean right();

	/**
	 * FIXME: rotate gets the shape out of the screen<br>
	 * rotate the shape in <code>direction</code>
	 *
	 * @param direction
	 *            if true direction will be annticlockwise else inverse
	 */
    void rotate(boolean direction);

	/**
	 * FIXME: BUGGY
	 *
	 * @param that
	 *            any box
	 * @return true if the shape slam any box
	 */
    boolean slamLeft(Box that);

	/**
	 * @param that
	 *            any box
	 * @return true if the shape slam any box
	 */
    boolean slamRight(Box that);

	// public Polygon getBounds();

	/**
	 * @return true if shape is stick to bottom of the screen
	 */
    boolean stickToBottom();

	/**
	 * @return true if shape is stick to left edge of the screen
	 */
    boolean stickToLeft();

	/**
	 * @return true if shape is stick to right edge of the screen
	 */
    boolean stickToRight();

}

class L extends AShape {
	public L() {
		super();
	}

	public L(Color c) {
		super(c);
	}

	public L(int x, int y) {
		super(x, y);

	}

	@Override
	protected void buildBoxes() {

		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x - b, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x - b, y - b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y - b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(i, l);

	}

	void backup() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y - b, c));
		l.add(new Box(x + b, y - b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y + b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x, y - b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(i, l);
	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		//
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));
		l.add(new Box(0 - b, 0, c));
		l.add(new Box(0 + b, 0, c));
		l.add(new Box(0 - b, 0 + b, c));
		return l;
	}
}

class RandomShape {
	public static Color getColor() {
		// return new Color(new Random().nextInt());
		// return new Color(new Random().nextFloat(), new
		// Random().nextFloat(),
		// new Random().nextFloat(), new Random().nextFloat());
		return new Color(new Random().nextFloat(), new Random().nextFloat(),
				new Random().nextFloat());

	}

	public static IShape getShape() {
		Color c;
		do {
			c = RandomShape.getColor();
		} while (c.equals(Tetris.cn.backGroundColor)
				|| c.equals(Tetris.cn.gridColor));
		return RandomShape.getShape(c);
	}

	private static IShape getShape(Color c) {
		Random rn = new Random();
		int i = rn.nextInt(Tetris.cn.N_SHAPES);
		// System.out.println(i);
		switch (i) {
		case 0:
			return new FourSquaresBox(c);
		case 1:
			return new FourSquaresRod(c);
		case 2:
			return new L(c);
		case 3:
			return new LeftDuck(c);
		case 4:
			return new ReversedL(c);
		case 5:
			return new RightDuck(c);
		case 6:
			return new ShortT(c);
		default:
			return null;
		}

	}

	@Deprecated
	public static IShape getShape(int x, int y) {
		Random rn = new Random();
		int i = (int) Math.floor(rn.nextDouble() * Tetris.cn.N_SHAPES);
		switch (i) {
		case 0:
			return new FourSquaresBox(x, y);
		case 1:
			return new FourSquaresRod(x, y);
		case 2:
			return new L(x, y);
		case 3:
			return new LeftDuck(x, y);
		case 4:
			return new ReversedL(x, y);
		case 5:
			return new RightDuck(x, y);
		case 6:
			return new ShortT(x, y);
		default:
			return null;
		}
	}

	// private static IShape getShape1() {
	// return RandomShape.getShape(
	// Math.floorDiv(Tetris.cn.SCREEN_WIDTH, 2), 0);
	// }
}

class ReversedL extends AShape {
	public ReversedL() {
		super();
	}

	public ReversedL(Color c) {
		super(c);
	}

	public ReversedL(int x, int y) {
		super(x, y);

	}

	@Override
	protected void buildBoxes() {

		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y + b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x - b, y + b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x - b, y - b, c));
		l.add(new Box(x + b, y, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y - b, c));
		sttm.put(i, l);

	}

	void backup() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x + b + b, y + b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y - b, c));
		l.add(new Box(x + b, y + b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x, y - b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y - b, c));
		sttm.put(i, l);

	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));
		l.add(new Box(0 - b, 0, c));
		l.add(new Box(0 + b, 0, c));
		l.add(new Box(0 + b, 0 + b, c));
		return l;
	}
}

class RightDuck extends AShape {
	public RightDuck() {
		super();
	}

	public RightDuck(Color c) {
		super(c);
	}

	public RightDuck(int x, int y) {
		super(x, y);

	}

	void bbb() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x + b, y - b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x + b + b, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y + b, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b + b, y, c));
		l.add(new Box(x + b, y + b, c));
		sttm.put(i, l);

	}

	@Override
	protected void buildBoxes() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x - b, y + b, c));
		sttm.put(0, l);
		// state 1
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x - b, y + b, c));
		sttm.put(2, l);
		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y + b, c));
		l.add(new Box(x, y - b, c));
		sttm.put(1, l);
		// state 2
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x + b, y + b, c));
		l.add(new Box(x, y - b, c));
		sttm.put(3, l);
	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		LinkedList<Box> l;
		// state 1
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));
		l.add(new Box(0 + b, 0, c));
		l.add(new Box(0, 0 + b, c));
		l.add(new Box(0 - b, 0 + b, c));
		return l;
	}

	// @Override
	// protected void buildStts() {
	// for (int i = 0; i < 4; i++) {
	// LinkedList<Box> b = new LinkedList<>();
	// sttm.put(i, b);
	// }
	// }

}

class ShortT extends AShape {
	public ShortT() {
		super();
	}

	public ShortT(Color c) {
		super(c);
	}

	public ShortT(int x, int y) {
		super(x, y);

	}

	@Override
	protected void buildBoxes() {

		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x, y + b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x, y + b, c));
		l.add(new Box(x, y - b, c));

		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x + b, y, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x, y - b, c));
		sttm.put(i, l);
		//
		i++;
		l = new LinkedList<>();
		l.add(new Box(x, y, c));
		l.add(new Box(x, y - b, c));
		l.add(new Box(x - b, y, c));
		l.add(new Box(x, y + b, c));
		sttm.put(i, l);

	}

	@Override
	public List<Box> getStaticCopy() {
		int b = Tetris.cn.BOX_SIZE;
		int i;
		LinkedList<Box> l;
		//
		i = 0;
		l = new LinkedList<>();
		l.add(new Box(0, 0, c));
		l.add(new Box(0 + b, 0, c));
		l.add(new Box(0 - b, 0, c));
		l.add(new Box(0, 0 + b, c));
		return l;
	}

}

class Stt {
	static final int A = 0;
	static final int B = 1;
	static final int C = 2;
	static final int D = 3;

	public static int getNextStt(int stt) {
		if (stt >= Stt.D) {
			return Stt.A;
		} else {
			return stt + 1;
		}
	}

	public static int getPrevStt(int stt) {
		if (stt <= Stt.A) {
			return Stt.D;
		} else {
			return stt - 1;
		}
	}

	public static int getRandStt() {
		Random r = new Random();
		return r.nextInt(4);
	}
}

/**
 * @author HASHIM
 * @see <a href="cse.hashim.hossam@gmail.com">cse.hashim.hossam@gmail.com</a>
 *
 */
@SuppressWarnings("javadoc")
abstract class AShape implements IShape {
    @Override
    public IShape clone() {
        return this;
    }

    /** x coordination of the shape */
	protected int x;
	/** y coordination of the shape */
	protected int y;
	/** a collection contains the shape structured as boxes */
	protected List<Box> boxes;
	/** represents last state boxes */
	protected List<Box> old;
	/** declares if shape can be moved or not */
	public boolean moving = true;
	/** collor of the shape is applied to all boxes */
	protected Color c = RandomShape.getColor();// cn.COLOR;
	/** represents the state of the shape every shape has 4 states */
	int stt;
	/**
	 * a hash map for the shape states where each state is inserted into it as a
	 * list of boxes
	 */
	HashMap<Integer, List<Box>> sttm = new HashMap<>();

	/**
	 * the defaultcn.uctor
	 */
	public AShape() {
		this(Math.floorDiv(Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE, 2)
				- Tetris.cn.BOX_SIZE, /*-3*/-1 * Tetris.cn.BOX_SIZE);

	}

	/**
	 * @param c
	 *            color of the shape if you want to specify
	 */
	public AShape(Color c) {
		this();
		this.c = c;

	}

	/**
	 * @param x
	 *            the x coordinates by pxls
	 * @param y
	 *            the y coordinates by pxls
	 */
	@SuppressWarnings("Depricated")
	@Deprecated
	public AShape(int x, int y) {
		boxes = new LinkedList<Box>();
		old = new LinkedList<>(boxes);
		moving = true;
		this.x = x;
		this.y = y;
		stt = Stt.getRandStt();
		buildBoxes();
		update();
	}

	protected abstract void buildBoxes();

	@Override
	@Deprecated
	public boolean Collision(Box that) {
		// // return that.getBounds().intersects(getBounds());
		boolean colides = false;
		for (Box b : boxes) {
			if (b.collision(that)) {
				colides = true;
				break;
			}
		}

		return colides;
	}

	boolean collisionLoop() {
		for (Box that : Tetris.cn.pool) {
			if (Collision(that)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		for (Box b : boxes) {
			b.draw(g);
		}
	}

	@Override
	public List<Box> extract() {
		return boxes;
	}

	@Override
	public void fall() {
		if (!stickToBottom()) {
			move(0, Tetris.cn.BOX_SIZE);// 1
		}

	}

	@Override
	public abstract List<Box> getStaticCopy();

	@Override
	public boolean isMoving() {
		return moving;
	}

	@Override
	public boolean landOn(Box that) {
		for (Box b : boxes) {
			if (b.landOn(that)) {
				moving = false;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean left() {
		if (!stickToLeft()) {
			move(-Tetris.cn.BOX_SIZE, 0);
			return true;
		}return false;

	}

	// protected abstract void buildStts();

	boolean loopSlamLeft() {
		for (Box that : Tetris.cn.pool) {
			if (slamLeft(that)) {
				return true;
			}
		}
		return false;
	}

	boolean loopSlamRight() {
		for (Box that : Tetris.cn.pool) {
			if (slamRight(that)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a general implementation to move a shape in any direction
	 *
	 * @param dx
	 *            amount if x displacement
	 * @param dy
	 *            amount of y displacement
	 */
	private void move(int dx, int dy) {
		// dx *=cn.BOX_SIZE;
		// dy *=cn.BOX_SIZE;
		if (moving) {
			for (Box b : boxes) {
				b.move(dx, dy);
			}
			for (int i = 0; i < 4; i++) {
				List<Box> l = sttm.get(i);
				if (!l.equals(boxes)) {
					for (Box b : l) {
						b.move(dx, dy);
					}
				}
			}

		}
	}

	@Override
	public boolean right() {
		if (!stickToRight()) {
			move(Tetris.cn.BOX_SIZE, 0);
			return true;
		}return false;

	}

	@Override
	public void rotate(boolean direction) {
		if (direction) {// unticlockwize
			stt = Stt.getPrevStt(stt);
			// boxes = sttm.get(stt);
			// if (loopSlamLeft() || loopSlamRight() || stickToLeft()
			// || stickToBottom() || stickToRight()) {
			// stt = Stt.getNextStt(stt);
			// }
		} else {
			stt = Stt.getNextStt(stt);
			// boxes = sttm.get(stt);
			// if (loopSlamLeft() || loopSlamRight() || stickToLeft()
			// || stickToBottom() || stickToRight()) {
			// stt = Stt.getPrevStt(stt);
			// }
		}

		update();
	}

	@Override
	public boolean slamLeft(Box that) {

		for (Box box : boxes) {
			if (box.slamLeft(that)) {
				return true;

			}
		}
		return false;
	}

	@Override
	public boolean slamRight(Box that) {
		for (Box box : boxes) {
			if (box.slamRight(that)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean stickToBottom() {
		for (int i = 0; i < boxes.size(); i++) {
			if (boxes.get(i).stickToBottom()) {
				moving = false;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean stickToLeft() {
		for (Box box : boxes) {
			if (box.stickToLeft()) {
				return true;
			}
		}
		if (loopSlamLeft()) {
			System.out.println("slamL");
			return true;
		}
		// if (collisionLoop()) {
		// return true;
		// }
		return false;
	}

	@Override
	public boolean stickToRight() {
		for (Box box : boxes) {
			if (box.stickToRight()) {
				return true;
			}
		}
		if (loopSlamRight()) {
			System.out.println("slamR");
			return true;
		}
		// if (collisionLoop()) {
		// System.out.println("coll");
		// return true;
		// }

		return false;

	}
public static int outerL=0;
    public static int outerR=0;
	protected void update() {
		boxes = sttm.get(stt);
//        if (outerL == 2) {
//            left();left();
//            outerL=0;
//        }
//        if (outerL == 1) {
//            if(left())
//            outerL=0;
//        }
        if (outerR == 1) {
            if(right())
            outerR=0;
        }
		while (outerL>0){ if(left())outerL--;else break;}
//		while(outerR>0){if(right())outerR--;else break;}

		//new
        boolean lft;
        boolean rgt;
        do {
            lft = false;
            rgt = false;
            for (Box box : boxes) {
                if (box.outLeft()) {
                    lft = true;
                    outerL++;
                }
                if (box.outRight()) {
                    rgt = true;
                    outerR++;
                }
            }
            if (lft) {
                right();
            }
            if (rgt) {
                left();
            }
        }while (lft||rgt);//multiple boxes are out of the bounds
	}

}

/**
 * @author HASHIM
 * @see <a href="cse.hashim.hossam@gmail.com">cse.hashim.hossam@gmail.com</a>
 *
 */
class TetrisGameCreator {

	public static void createGame(Object[] current) {
		Tetris.cn = new Cnst(current);
		Frm2.runme(null);
	}
}

class TetrisFrmSelector extends JFrame {

	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(() -> {
	// try {
	// TetrisFrmSelector frame = new TetrisFrmSelector();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// });
	// }

    private JTextField a;
	private JTextField b;
	private JTextField c;
	private JTextField d;
    Object[] dflt = { 25, 10, 20, 1000, RandomShape.getColor() };
	private Object[] current = dflt;
	private Color color = RandomShape.getColor();

	/**
	 * Create the frame.
	 */
	public TetrisFrmSelector() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 189, 230);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
//		contentPane
//				.setLayout(new FormLayout(new ColumnSpec[] {
//						FormFactory.RELATED_GAP_COLSPEC,
//						FormFactory.DEFAULT_COLSPEC,
//						FormFactory.RELATED_GAP_COLSPEC,
//						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC,
//						FormFactory.RELATED_GAP_ROWSPEC,
//						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel("Box Size");
		contentPane.add(lblNewLabel, "2, 2, right, default");

		a = new JTextField();
		a.setText("25");
		contentPane.add(a, "4, 2, fill, default");
		a.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Width");
		contentPane.add(lblNewLabel_1, "2, 4, right, default");

		b = new JTextField();
		b.setText("10");
		contentPane.add(b, "4, 4, fill, default");
		b.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Height");
		contentPane.add(lblNewLabel_2, "2, 6, right, default");

		c = new JTextField();
		c.setText("20");
		c.setToolTipText("20");
		contentPane.add(c, "4, 6, fill, default");
		c.setColumns(10);

		JLabel lblDelay = new JLabel("Delay");
		contentPane.add(lblDelay, "2, 8, right, default");

		d = new JTextField();
		d.setText("1000");
		contentPane.add(d, "4, 8, fill, default");
		d.setColumns(10);

		JLabel label_3 = new JLabel("");
		contentPane.add(label_3, "2, 10, right, default");

        JButton e = new JButton("Color");
		e.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// JColorChooser tcc ;
				// tcc = new JColorChooser(banner.getForeground());
			}
		});
		contentPane.add(e, "4, 10");

        JButton f = new JButton("Reset");
		f.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				current = dflt;
			}
		});
		contentPane.add(f, "2, 12");

        JButton g = new JButton("OK");
		g.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				current = new Object[] { Integer.parseInt(a.getText()),
						Integer.parseInt(b.getText()),
						Integer.parseInt(c.getText()),
						Long.parseLong(d.getText()), color };
				TetrisGameCreator.createGame(current);
				TetrisFrmSelector.this.dispose();
			}
		});
		contentPane.add(g, "4, 12");
	}
}

class StaticPan extends JPanel {
	int w = 4;
	int b = Tetris.cn.BOX_SIZE;
	private static Pan p;
	private static long oldTrig;

	public StaticPan(Pan p) {
		StaticPan.p = p;
		this.setBounds(0, 0, w * b, w * b);
		setBackground(Color.WHITE);
		StaticPan.oldTrig = p.triggerFall;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(1 * b, 1 * b);
		for (Box elm : StaticPan.p.next.getStaticCopy()) {
			elm.draw(g);
		}
		g.translate(-1 * b, -1 * b);

	}

	public void trig() {
		if (StaticPan.p.triggerFall != StaticPan.oldTrig) {
			this.repaint();
		}
	}

}

// class TettrisLauncher {
// public static void main(String[] args) {
// EventQueue.invokeLater(() -> {
// try {
// TetrisFrmSelector frame = new TetrisFrmSelector();
// frame.setVisible(true);
// } catch (Exception e) {
// e.printStackTrace();
// }
// });
// }
// }

// class thrdtst implements Runnable {
// public static void main(String args[]) {
// (new Thread(new thrdtst())).start();
// }
//
// @Override
// public void run() {
// // TODO Auto-generated method stub
// while (true) {
// System.out.println("helooo");
// }
//
// }
//
// }

class Pan extends JPanel implements KeyListener {

	private static final long serialVersionUID = 8615712401205211648L;
	FallThread ft;
	ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Box>> h;
	IShape next;

	public long triggerFall = 0;

	Frm2 f2;
	String msg = "";

	int cnt = 0;

	public boolean ismsg = false;

	int count = 0;

	/**
	 * Create the panel.
	 *
	 * @param _f2
	 */
	public Pan(Frm2 _f2) {
		f2 = _f2;
		h = new ConcurrentHashMap<>();
		this.setSize(Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE,
				Tetris.cn.SCREEN_HEIGHT * Tetris.cn.BOX_SIZE);
		Tetris.cn.current = RandomShape.getShape();
		next = RandomShape.getShape();
		setBackground(Tetris.cn.backGroundColor);
		addKeyListener(this);
		// movieLoop();
		ft = new FallThread(this);
		ft.start();

		// this.test2();
	}

	private void _gameOver() {
		if (!Tetris.cn.gameover) {
			Tetris.cn.gameover = true;
			ft.stop();
			ft = null;
			msg = "Game Over";
			Tetris.cn.running = false;
			ismsg = true;
			this.repaint();
		}
	}

	void _update_() {
		if (!Tetris.cn.gameover) {
			IShape is = Tetris.cn.current;
			for (Box elem : is.extract()) {
				if (elem.getY() <= 0) {
					_gameOver();
				}
			}
			if (!Tetris.cn.gameover) {
				Tetris.cn.current = next.clone();
				next = RandomShape.getShape();
				triggerFall++;
				f2.sp.trig();
				// ft.interrupted();
				ft.stop();
				ft = new FallThread(this);
				ft.start();
			}
			Tetris.cn.pool.addAll(is.extract());
			this.addH(is.extract());

			checkH();
			checkH();
			checkH();
			checkH();
		}
	}

	void addH(Box b) {
		if (h.containsKey(b.y())) {
		} else {
			h.put(b.y(), new ConcurrentLinkedQueue<>());
		}
		if (h.get(b.y()).contains(b)) {
		} else {
			h.get(b.y()).add(b);
		}
	}

	void addH(List<Box> l) {
		for (Box b : l) {
			this.addH(b);
		}
	}

	void check() {
		if (Tetris.cn.current.isMoving()) {
			for (int i = 0; i < Tetris.cn.pool.size(); i++) {
				if (Tetris.cn.current.landOn(Tetris.cn.pool.get(i))) {
					_update_();
					break;
				}
			}
		}
		if (!Tetris.cn.current.isMoving()) {
			_update_();
		}
	}

	/**
	 * check h if there are rows to be deleted or not and delete them if
	 * necessary
	 */
	private void checkH() {
		// System.out.println(h);
		// int or box? TODO
		// HashMap<Integer, ConcurrentLinkedQueue<Box>> lst = new
		// HashMap<>();
		// Set<Integer> ks = h.keySet();

		// for (Integer integer : ks) {
		for (int i = Tetris.cn.SCREEN_HEIGHT - 1; h.containsKey(i) && i >= 0; i--) {
			if (h.get(i).size() == Tetris.cn.SCREEN_WIDTH) {
				// lst.put(integer, h.get(integer));
				deleteRow(i);
			}
		}
		// repaint();
	}

	/**
	 * check h if there are rows to be deleted or not and delete them if
	 * necessary
	 */
	private void checkHOld() {
		// System.out.println(h);
		// int or box? TODO
		HashMap<Integer, ConcurrentLinkedQueue<Box>> lst = new HashMap<>();
		Set<Integer> ks = h.keySet();
		for (Integer integer : ks) {
			if (h.get(integer).size() == Tetris.cn.SCREEN_WIDTH) {
				lst.put(integer, h.get(integer));
			}
		}
		// deleteRows(integer, h.get(integer));
		if (!lst.isEmpty() && lst != null) {
			deleteRows(lst);// TODO: "lst" must me not empty}
			// System.out.println("kset=" + lst.keySet());
		}
	}

	private void deleteRow(int i) {
		LinkedList<Box> l = new LinkedList<>();
		l.addAll(h.get(i));
		Tetris.cn.pool.removeAll(l);
		h.remove(i);
		shiftDown(i);
	}

	void deleteRows(HashMap<Integer, ConcurrentLinkedQueue<Box>> lst) {
		LinkedList<Integer> ks = new LinkedList<>();
		ks.addAll(lst.keySet());
		Collections.sort(ks);
		Collections.reverse(ks);
		for (Integer itr : ks) {
			Tetris.cn.pool.removeAll(lst.get(itr));
			h.remove(itr);
			shiftDown(itr);
			// repaint();
		}
		// System.out.println(h);
		// shiftDown(ks);
	}

	private void drawGrid(Graphics g) {
		g.setColor(Tetris.cn.gridColor);
		int b = Tetris.cn.BOX_SIZE;
		int w = Tetris.cn.SCREEN_WIDTH * b, h = Tetris.cn.SCREEN_HEIGHT * b;
		for (int i = 0; i <= h; i += b) {
			g.drawLine(0, i, w, i);
		}
		for (int i = 0; i <= w; i += b) {
			g.drawLine(i, 0, i, h);
		}

	}

	// TODO: make a configuration applet for custom play buttons
	@Override
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
			Tetris.cn.current.left();
			this.repaint();
		}
		if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
			Tetris.cn.current.right();
			this.repaint();
		}
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("Exitting");
			System.exit(0);
		}
        if (evt.getKeyCode() == KeyEvent.VK_R) {

            if(Tetris.cn.gameover){
                Tetris.cn.pool.clear();
                h.clear();
                next = RandomShape.getShape();
                Tetris.cn.current = next;
                next = RandomShape.getShape();
                triggerFall=0;
                f2.sp.trig();
//                // ft.interrupted();
//                ft.stop();
                ft = new FallThread(this);
                ft.start();
                Tetris.cn.gameover=false;
                Tetris.cn.running=true;
//                _update_();
//                checkH();checkH();checkH();checkH();checkH();
                this.repaint();
            }
        }
		if (evt.getKeyCode() == KeyEvent.VK_P&&!Tetris.cn.gameover) {

			if (Tetris.cn.running) {
				// System.out.println("Paused");
				msg = "Paused";
				Tetris.cn.running = false;
				ft.suspend();
				ismsg = true;
				this.repaint();
			} else {
				// System.out.println("Play");
				msg = "Play";
				Tetris.cn.running = true;
				ismsg = true;
				ft.resume();
				this.repaint();
			}
		}
		if (evt.getKeyCode() == KeyEvent.VK_DOWN&&!Tetris.cn.gameover&&Tetris.cn.running) {
			Tetris.cn.current.fall();
			this.repaint();

		}
		if (evt.getKeyCode() == KeyEvent.VK_A&&!Tetris.cn.gameover&&Tetris.cn.running) {
			Tetris.cn.current.rotate(true);
			this.repaint();

		}

		if (evt.getKeyCode() == KeyEvent.VK_S&&!Tetris.cn.gameover&&Tetris.cn.running) {
			Tetris.cn.current.rotate(false);
			this.repaint();

		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// do nothing
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// do nothing
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
		check();
		// checkH();
		Tetris.cn.current.draw(g);
		for (Box s : Tetris.cn.pool) {
			s.draw(g);
		}
		if (ismsg) {
			ismsg = false;

			g.setColor(Color.gray);
			g.setFont(new Font("Times New Roman", 40, 40));
			g.drawString(msg, Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE / 2
					- 40, Tetris.cn.SCREEN_HEIGHT * Tetris.cn.BOX_SIZE / 2);
		}
		if (Tetris.cn.gameover) {
			g.setColor(Color.red);
			g.setFont(new Font("Arial", 40, 40));
			g.drawString(msg, Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE / 2
					- 100, Tetris.cn.SCREEN_HEIGHT * Tetris.cn.BOX_SIZE / 2);
		}

	}

	private void shiftDown(Integer itr) {
		int i = itr - 1;
		while (i >= 0) {
			if (h.containsKey(i)) {
				ConcurrentLinkedQueue<Box> clq = h.remove(i);
				for (Box box : clq) {
					box.fall();
				}

				h.put(clq.peek().y(), clq);

			} else {
				break;// FIXME
			}
			i--;
		}
		// System.out.println(h);
	}

	public void test() {
		int bs = Tetris.cn.BOX_SIZE;
		int y = 18;
		for (; y < Tetris.cn.SCREEN_HEIGHT; y++) {
			for (int i = 0; i < Tetris.cn.SCREEN_WIDTH; i++) {

				Tetris.cn.pool.add(new Box(i * bs, y * bs, RandomShape
						.getColor()));
			}
		}
		this.addH(Tetris.cn.pool);
		checkH();
	}

	private void test2() {
		int bs = Tetris.cn.BOX_SIZE;
		int y = 15;
		for (; y < 20; y++) {

			for (int i = 1; i < Tetris.cn.SCREEN_WIDTH; i++) {
				if (y == 18 && i == 4) {

				} else {
					Tetris.cn.pool.add(new Box(i * bs, y * bs, RandomShape
							.getColor()));
				}

			}
		}
		// updatePlate();
		this.addH(Tetris.cn.pool);
		checkH();
		Tetris.cn.current = new FourSquaresRod(RandomShape.getColor());
	}

}

class TetrisFrmConfig extends JFrame {

    /**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(() -> {
	// try {
	// TetrisFrmConfig frame = new TetrisFrmConfig();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// });
	// }

	/**
	 * Create the frame.
	 */
	public TetrisFrmConfig() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}

class FallThread extends Thread {
	JPanel p;

	public FallThread(JPanel pan) {
		p = pan;
	}

	@Override
	public void run() {
		// while (cn.running) {
		while (true) {

			Tetris.cn.current.fall();

			p.repaint();
			try {
				Thread.sleep(Tetris.cn.sleepAmmount);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

//

class Frm extends JFrame {
	//
	// private JPanel contentPane;
	//
	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// Frm frame = new Frm();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	//
	// /**
	// * Create the frame.
	// */
	// public Frm() {
	// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// setBounds(100, 100, Const.SCREEN_WIDTH * Const.BOX_SIZE + 40,
	// Const.SCREEN_HEIGHT * Const.BOX_SIZE + 60);
	// contentPane = new JPanel();
	// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	// setContentPane(contentPane);
	// contentPane.setLayout(null);
	//
	// Pan panel = new Pan();
	// // panel.setBounds(10, 11, 414, 239);
	// panel.setBounds(10, 10, Const.SCREEN_WIDTH * Const.BOX_SIZE + 1,
	// Const.SCREEN_HEIGHT * Const.BOX_SIZE + 1);
	//
	// contentPane.add(panel);
	// panel.setFocusable(true);
	// // NxtFrame.runme(panel);
	//
	// }
}

class Frm2 extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void runme(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Frm2 frame = new Frm2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    Pan panel;
	StaticPan sp;

	/**
	 * Create the frame.
	 */
	public Frm2() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, (int) (Tetris.cn.SCREEN_WIDTH
				* Tetris.cn.BOX_SIZE * 1.5 + 40), Tetris.cn.SCREEN_HEIGHT
				* Tetris.cn.BOX_SIZE + 60);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Tetris.cn.frameColor);

		panel = new Pan(this);
		// StaticPan np = new StaticPan(panel);
		// panel.setBounds(10, 11, 414, 239);
		panel.setBounds(10, 10,
				Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE + 1,
				Tetris.cn.SCREEN_HEIGHT * Tetris.cn.BOX_SIZE + 1);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null,
				null));

		contentPane.add(panel);
		panel.setFocusable(true);

		sp = new StaticPan(panel);
		sp.setBackground(Tetris.cn.backGroundColor);
		sp.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		sp.setBounds(Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE + 1
				+ Tetris.cn.BOX_SIZE, 10, 4 * Tetris.cn.BOX_SIZE,
				4 * Tetris.cn.BOX_SIZE);
		contentPane.add(sp);
		sp.trig();
		// NxtFrame.runme(panel);

	}
}

class NxtFrame extends JFrame {
	//
	// private NxtPan np;
	//
	// public static void main(String[] args) {
	// runme(new Pan());
	// }
	//
	// /**
	// * Launch the application.
	// */
	// public static void runme(Pan p) {
	// EventQueue.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// NxtFrame frame = new NxtFrame(p);
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	//
	// /**
	// * Create the frame.
	// */
	// public NxtFrame(Pan pan) {
	// np = new NxtPan(pan);
	// this.setBounds(np.getBounds());
	// }
	//
	// class NxtPan extends JPanel {
	// int w = 4;
	// int b = Const.BOX_SIZE;
	// Pan p;
	//
	// public NxtPan(Pan p) {
	// this.p = p;
	// setBounds(0, 0, w * b, w * b);
	// setBackground(Color.WHITE);
	// }
	//
	// @Override
	// protected void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// g.translate(-2, -2);
	// for (Box elm : p.next.getStaticCopy()) {
	// elm.draw(g);
	// }
	// }
	//
	// Box IShape(Box next) {
	// return new Box(0, 0, next.getC());
	// }
	// }
}

class Level {
	// public static levelUp(Pan p) {
	// p.setBackground(RandomShape.getColor());
	// }
}

@Deprecated
class MsgThread extends Thread {
	Pan p;
	Graphics g;
	String s;

	public MsgThread(String str, Pan pan, Graphics gr) {
		p = pan;
		g = gr;
		s = str;
	}

	@Override
	public void run() {
		// while (cn.running) {
		// while (true) {
		long startingTime = System.currentTimeMillis();
		long cumTime = startingTime;
		while (cumTime - startingTime < 999) {
			long timePassed = System.currentTimeMillis() - cumTime;
			cumTime += timePassed;
			g.setColor(Color.gray);
			g.setFont(new Font("Times New Roman", 40, 40));
			g.drawString(s, Tetris.cn.SCREEN_WIDTH * Tetris.cn.BOX_SIZE / 2
					- 40, Tetris.cn.SCREEN_HEIGHT * Tetris.cn.BOX_SIZE / 2);
			p.repaint();
			// try {
			// sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

		}
	}
}
