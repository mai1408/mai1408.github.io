package binarySearch;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Animation extends Applet implements Runnable, ActionListener, AdjustmentListener {
	
	Thread thread;	
	
	// Animation panel
	int key = 1;
	private int[] B = {9, 24, 35, 37, 37, 39, 40, 41, 55, 94};
	private int[] C = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};	
	private int[] A = {9, 24, 35, 37, 37, 39, 40, 41, 55, 94};
	
	Bar[] bars;
	private int low = 0;
	private int high = A.length - 1;
	private int mid;
	
	private int[] values;
	private int currentIndex = -1;
	
	private int counter = 0;
	private int tempcounter;
	
	// Pseudocode panel
	private String[] code1 = {"BinarySearch(A[0..N-1], value)", "low ← 0", "high ← N – 1", "while (low ≤ high)", "mid ← (low + high) / 2",
			"if (A[mid] > value)", "high ← (mid – 1)", "else if (A[mid] < value)", "low ← (mid + 1)", "else", "return mid", "end if",
			"end while", "return -1", "end"	};
	private Pseudocode binaryPseudocode;
	private int activeRow = 0;
	
	// Control panel
	private Button restart, pause, previous, next, exit;
	private Scrollbar speed;
	private int milisec = 1000;
	
	
	// Text panel
	private String info = "Click Start";
	FontRenderContext frc;
	Font font;
	private TextField keyTextfield;
	
	
	// Double buffering
	private Image i;
	private Graphics doubleG;

	
	// Menu
	private Menu fileMenu, examplesMenu;
	private MenuItem restartMenu, pauseMenu, exitMenu, example1, example2, random;
	
	
	// Booleans
	private boolean isPaused = true, started = false, found = false, stepByStep = false, nextpressed = false;
	

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setSize(800, 600);
		
		Object f = getParent ();
		while (! (f instanceof Frame))
			f =  ((Component) f).getParent();
		Frame frame = (Frame) f;
		
		MenuBar mb = new MenuBar();	    
		
		mb.add(fileMenu = new Menu("File"));
		
		mb.add(examplesMenu = new Menu("Examples"));
	    
	    fileMenu.add(restartMenu = new MenuItem("Start"));
	    restartMenu.addActionListener(this);
	    
		fileMenu.add(pauseMenu = new MenuItem("Pause"));
		pauseMenu.setEnabled(false);
		pauseMenu.addActionListener(this);
		
		fileMenu.add(exitMenu = new MenuItem("Exit"));
		exitMenu.addActionListener(this);
		
		examplesMenu.add(example1 = new MenuItem("Example 1"));
		example1.addActionListener(this);
		examplesMenu.add(example2 = new MenuItem("Example 2"));
		example2.addActionListener(this);
		examplesMenu.add(random = new MenuItem("Random"));
		random.addActionListener(this);
	    frame.setMenuBar(mb);
		
		/*AppletMenuBar menubar = new AppletMenuBar();
	    menubar.setForeground(Color.black);
	    menubar.setHighlightColor(Color.red);
	    menubar.setFont(new Font("helvetica", Font.BOLD, 12));
	    this.setLayout(new BorderLayout());
	    this.add(menubar, BorderLayout.NORTH);
	    
	    PopupMenu file = new PopupMenu();
	    file.add("Restart");
	    file.add("Pause");
	    file.add("Exit");
	    PopupMenu examples = new PopupMenu();
	    examples.add("Random");
	    examples.add("Example 1");
	    examples.add("Example 2");

	    menubar.addMenu("File", file);
	    menubar.addMenu("Examples", examples);*/
		
		keyTextfield = new TextField("", 1);
	    this.add(keyTextfield);
	    keyTextfield.addActionListener(this);
	    
		restart = new Button("Start");		
		this.add(restart);		
		restart.addActionListener(this);
		
		pause = new Button("Pause");		
		this.add(pause);
		pause.setVisible(false);
		pause.addActionListener(this);
		
		previous = new Button("Previous");		
		this.add(previous);
		previous.setVisible(false);
		previous.addActionListener(this);
		
		next = new Button("Next");		
		this.add(next);
		next.setVisible(false);
		next.addActionListener(this);
		
		exit = new Button("Exit");		
		this.add(exit);		
		exit.addActionListener(this);
		
		speed = new Scrollbar(Scrollbar.HORIZONTAL, 10, 10, 1, 100);
		this.add(speed);
		speed.addAdjustmentListener(this);		
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub		
		bars = new Bar[A.length+2];		
		for(int i=0;i<bars.length;i++) {
			if(i == 0)
				bars[i] = new Bar(10, 50, key);
			else if(i == 1)
				bars[i] = new Bar(40, 50, 0);
			else
				bars[i] = new Bar(40 + 30*i, 50, A[i-2]);
		}
		
		binaryPseudocode = new Pseudocode(code1, getWidth()/2 + 20, 20);

		AffineTransform affinetransform = new AffineTransform();     
		frc = new FontRenderContext(affinetransform,true,true);     
		font = new Font("Tahoma", Font.PLAIN, 12);
		
		thread = new Thread(this);
		thread.start();					
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (started  == true) {
			if(!stepByStep && !nextpressed) {
				waiting();
			}
		while (activeRow < 3) {			
			activeRow++;
			counter++;
			System.out.println(counter);
			
			if (activeRow == 1)
				info = "low=" + String.valueOf(low);
			else if (activeRow == 2)
				info = "high=" + String.valueOf(high);
			else if (activeRow == 3)
				info = String.valueOf(low) + "<=" + String.valueOf(high);
			checktopaint();
		}
		while (low <= high && !found) {
			//currentIndex ++;
			mid = (low + high) / 2;			
			activeRow = 4;
			counter++;
			System.out.println(counter);
			info = "mid=" + String.valueOf(mid);
			checktopaint();
			
			// animation start
			bars[1].setHeight(A[mid]);
			bars[1].setX(bars[mid+2].getX());
			bars[1].setY(bars[mid+2].getY());			
			while(bars[1].getX() > 40) {
				while(bars[1].getX() > 40 && bars[1].getY() < 150) {
					bars[1].setX(bars[1].getX() - 10);
					bars[1].setY(bars[1].getY() + 10);
					//waiting();
				}
				while(bars[1].getX() > 40 && bars[1].getY() > 50) {
					bars[1].setX(bars[1].getX() - 10);
					bars[1].setY(bars[1].getY() - 10);
					//waiting();
				}
				if(bars[1].getY() != 50)
					bars[1].setY(50);				
				//waiting();
			}
			//waiting();
			
			// animation end
			
			if (A[mid] > key) {
				activeRow = 5;
				counter++;
				System.out.println(counter);
				info = String.valueOf(A[mid]) + ">" + String.valueOf(key);
				checktopaint();
				high = mid - 1;
				activeRow = 6;
				counter++;
				System.out.println(counter);
				info = "high=" + String.valueOf(high);
				checktopaint();
			}			
			else if (A[mid] < key) {
				activeRow = 7;
				counter++;
				System.out.println(counter);
				info = String.valueOf(A[mid]) + "<" + String.valueOf(key);
				checktopaint();
				low = mid + 1;
				activeRow = 8;
				counter++;
				System.out.println(counter);
				info = "low=" + String.valueOf(low);
				checktopaint();
			}
			else {
				activeRow = 9;
				counter++;
				System.out.println(counter);
				info = String.valueOf(key) + " found";
				checktopaint();
				activeRow = 10;
				counter++;
				System.out.println(counter);
				info = "element found, " + "index = " + String.valueOf(mid) + ", " + String.valueOf(mid+1) + " position";
				checktopaint();
				found = true;
				System.out.println("index="+mid);
			}
			if (!found) {
				activeRow=3;
				counter++;
				System.out.println(counter);
				info = String.valueOf(low)+"<="+String.valueOf(high);
				checktopaint();
			}			
		}		
		if (!found) {
			activeRow = 13;
			counter++;
			System.out.println(counter);
			info = "element not found";
			checktopaint();
			started = false;
			System.out.println("Index="+-1);
		}
		activeRow = 14;
		counter++;
		System.out.println(counter);
		pause.setVisible(false);
		pauseMenu.setEnabled(false);
		previous.setVisible(false);
		next.setVisible(false);
		checktopaint();
		started = false;
		}
	}
	
	public void checktopaint() {
		if(!stepByStep && !nextpressed) {
			waiting();
		}
		else {
			if(stepByStep) {
				if(counter==tempcounter-1) {
					waiting();
					stepByStep=false;
					pause();					
				}
			}
			if(nextpressed) {
				if(counter==tempcounter+1) {
					waiting();
					nextpressed=false;
					pause();
				}
			}			
		}
	}
	
	public void waiting() {
		repaint();
		try {
			thread.sleep(milisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Graphics g) {
		if (i == null) {
			i = createImage(this.getSize().width, this.getSize().height);
			doubleG = i.getGraphics();
		}

		doubleG.setColor(getBackground());
		doubleG.fillRect(0, 0, this.getSize().width, this.getSize().height);

		doubleG.setColor(getForeground());
		paint(doubleG);

		g.drawImage(i, 0, 0, this);

	}
	
	@Override
	public void paint(Graphics g) {
		
		// Pseudocode panel		
		// Active row indicator
		g.setColor(Color.CYAN);
		g.fillRect(getWidth()/2, activeRow*30 , getWidth()/2, 30);
		// Pseudocode text
		g.setColor(Color.BLACK);
		binaryPseudocode.paint(g);
		
		// Control Panel
		// Previous Button
		previous.setLocation(getWidth()/2 + getWidth()/2 *1/4 - previous.getWidth()/2, getHeight()*5/6 + getHeight()/6 *1/3 - previous.getHeight()/2);
		previous.setSize(80, 30);
		// Pause Button
		pause.setLocation(getWidth()/2 + getWidth()/2 *2/4 - pause.getWidth()/2, getHeight()*5/6 + getHeight()/6 *1/3 - pause.getHeight()/2);
		pause.setSize(80, 30);
		// Next Button
		next.setLocation(getWidth()/2 + getWidth()/2 *3/4 - next.getWidth()/2, getHeight()*5/6 + getHeight()/6 *1/3 - next.getHeight()/2);
		next.setSize(80, 30);
		// Restart Button
		restart.setLocation(getWidth()/2 + getWidth()/2 *1/4 - restart.getWidth()/2, getHeight()*5/6 + getHeight()/6 *2/3 - restart.getHeight()/2);
		restart.setSize(80, 30);
		// Exit Button
		exit.setLocation(getWidth()/2 + getWidth()/2 *2/4 - exit.getWidth()/2, getHeight()*5/6 + getHeight()/6 *2/3 - exit.getHeight()/2);
		exit.setSize(80, 30);
		// Speed slider
		speed.setLocation(getWidth()/2 + getWidth()/2 *3/4 - speed.getWidth()/2, getHeight()*5/6 + getHeight()/6 *2/3 - speed.getHeight()/2);
		speed.setSize(100, 30);
		
		// Key Input Textfield		
		g.drawString("Key: ", keyTextfield.getX() - 35, getHeight()*5/6 + getHeight()/6 *2/3 + 4);
		keyTextfield.setLocation(getWidth()/4 - keyTextfield.getWidth()/2, getHeight()*5/6 + getHeight()/6 *2/3 - keyTextfield.getHeight()/2);
		
		// Borders
		// Horizontal left
		g.fillRect(0, getHeight()*2/3, getWidth()/2, 5);
		// Horizontal right
		g.fillRect(getWidth()/2, getHeight()*5/6, getWidth()/2, 5);
		// Vertical
		g.fillRect(getWidth()/2, 0, 5, getHeight());		
		
		// Bars
		for(int i=0;i<bars.length;i++) {
			if(i == 0) {
				bars[i].setPosition(i);
				bars[i].paint(g);
			}				
			else if(i == 1) {
				bars[i].setPosition(i);
				bars[i].paint(g);
			}				
			else {
				bars[i].setPosition(i);
				bars[i].paint(g);
			}
		}
		
		// Information Panel		
		int textwidth = (int)(font.getStringBounds(info, frc).getWidth());
		int textheight = (int)(font.getStringBounds(info, frc).getHeight());		
		g.drawString(info, getWidth()/4 - textwidth/2 , getHeight()*2/3 + getHeight()/6);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == restart) {						
			restart();			
		}
		if (e.getSource() == restartMenu) {						
			restart();			
		}
		if (e.getSource() == pause) {
			pause();						
		}
		if (e.getSource() == pauseMenu) {
			pause();						
		}
		if (e.getSource() == previous) {
			previous();
		}
		if (e.getSource() == next) {
			next();
		}
		if (e.getSource() == exit) {
			System.exit(0);
		}
		if (e.getSource() == exitMenu) {
			System.exit(0);
		}
		if (e.getSource() == example1) {			
			A=B;
			restart();
		}
		if (e.getSource() == example2) {
			A=C;
			restart();
		}
		if (e.getSource() == random) {
			random();
		}
		if (e.getSource() == keyTextfield) {
			int keyInput = Integer.parseInt(keyTextfield.getText());							
			key = keyInput;
			restart();				
		}
	}

	private void next() {
		// TODO Auto-generated method stub		
		nextpressed = true;
		tempcounter = counter;
		restart();
	}

	private void previous() {
		// TODO Auto-generated method stub		
		stepByStep = true;
		tempcounter = counter;
		restart();
	}

	private void random() {
		// TODO Auto-generated method stub
		int[] D = new int[A.length];
		for(int i=0;i<A.length;i++) {
			Random random = new Random();
			int rand = random.nextInt(10);
			if(i==0)
				D[i]=rand;
			else {
				while(rand < D[i-1]) {						
					rand = random.nextInt(300);
				}
				D[i] = rand;
			}										
		}
		A=D;
		restart();
	}

	private void pause() {
		// TODO Auto-generated method stub
		if (isPaused  == false) {
			isPaused = true;
			pause.setLabel("Resume");
			pauseMenu.setLabel("Resume");
			thread.suspend();
		}
		else {
			isPaused = false;
			pause.setLabel("Pause");
			pauseMenu.setLabel("Pause");
			thread.resume();
		}
	}

	private void restart() {
		// TODO Auto-generated method stub
		if (started == false) {
			started = true;
			restart.setLabel("Restart");
			restartMenu.setLabel("Restart");
		}
		
		if (isPaused  == true) {
			isPaused = false;
			pause.setLabel("Pause");
			pauseMenu.setLabel("Pause");
		}
		bars = new Bar[A.length+2];			
		for(int i=0;i<bars.length;i++) {
			if(i == 0)
				bars[i] = new Bar(10, 50, key);
			else if(i == 1)
				bars[i] = new Bar(40, 50, 0);
			else
				bars[i] = new Bar(40 + 30*i, 50, A[i-2]);
		}			
		binaryPseudocode = new Pseudocode(code1, getWidth()/2 + 20, 20);
		JavaCode jcode = new JavaCode(A);
		jcode.findElement(key, A);
/*		values = new int[jcode.midValues.size()];		
		for(int i=0;i<values.length;i++)
			values[i]  = jcode.midValues.get(i);		
*/		low = 0;
		high = A.length - 1;
		found = false;
		activeRow=0;
		counter=0;
		thread.suspend();
		thread = new Thread(this);
		thread.start();
		pause.setVisible(true);
		pauseMenu.setEnabled(true);
		previous.setVisible(true);
		next.setVisible(true);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		Scrollbar source = (Scrollbar)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        milisec = 100*(int)source.getValue();
	        repaint();
	    }
	}	

}
