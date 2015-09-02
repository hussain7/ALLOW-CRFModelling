package AllowModel.visualization;



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
        e.gc.drawLine(0, halfY, maxX, halfY);

        // Draw the sine wave
        for (int i = 0; i < maxX; i++) {
          e.gc.drawPoint(i, getNormalizedSine(i, halfY, maxX));
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

  static int getNormalizedSine(int x, int halfY, int maxX) {
    double piDouble = 2 * Math.PI;
    double factor = piDouble / maxX;
    return (int) (Math.sin(x * factor) * halfY + halfY);
  }
}