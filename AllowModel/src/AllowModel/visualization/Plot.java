package AllowModel.visualization;



import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Plot {
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Canvas Example");
    shell.setLayout(new FillLayout());

    Canvas canvas = new Canvas(shell, SWT.NONE);

    canvas.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        Canvas canvas = (Canvas) e.widget;
        int maxX = canvas.getSize().x;
        int maxY = canvas.getSize().y;

        // Calculate the middle
        int halfX = (int) maxX / 2;
        int halfY = (int) maxY / 2;

        // Set the line color and draw a horizontal axis
        e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
      //  e.gc.drawLine(0, halfY, maxX, halfY);

        
        List<Integer> number = new ArrayList<Integer>()   ;
    	List<Double> quality = new ArrayList<Double>()   ;
    	File inputfile = new File("D:\\Allowlog.txt");
    	
    	try {
			getNormalizedSine(inputfile,number,quality);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
      }
    });

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

  static void getNormalizedSine(File inputfile,List<Integer> number,List<Double> quality) throws IOException {
	  
	  
	  BufferedReader br = new BufferedReader(new FileReader(inputfile));
	    try {
	        StringBuilder sb = new StringBuilder();
	     
            
	        String line = br.readLine(); // line by line reading
           int count=0;
           line = br.readLine(); // remove the first line(contains tags) 
	        while (line != null) {
	        	
	        	String[] words = line.split(" "); // split words
	        	// order of strings in line  // NodeId || Instances || mean || margin
	        	
	        	
	        	double mean = 0,margin = 0;
	        	for (String word : words){
	        		word.trim();
	        		if(word.contentEquals(""))
	        			continue;
	        		else count++;
	        		if(count%4 ==1)
	        			number.add(Integer.parseInt(word)) ;
	        		
	        		if(count%4 ==2)
	        			quality.add(Double.parseDouble(word)) ;
	        		
	        	}
	       
	           line = br.readLine();
	        }
	        
	        
	    } finally {
	        br.close();
	    }

  }
}