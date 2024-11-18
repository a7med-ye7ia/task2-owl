import com.sun.opengl.util.FPSAnimator;

import java.awt.*;
import javax.swing.*;
import javax.media.opengl.*;


/**
 * This is a basic JOGL app. Feel free to
 * reuse this code or modify it.
 */
class SimpleJoglApp extends JFrame {
    private GLCanvas glCanvas = new GLCanvas();
    private SimpleGLEventListener Listner = new SimpleGLEventListener();
    static FPSAnimator animator = null;

    /**
     *
     */


    public static void main(String[] args) {
        final SimpleJoglApp app = new SimpleJoglApp();
        animator.start();
        // show what we've done
//    SwingUtilities.invokeLater (
//   new Runnable() {
//     public void run() {
//       app.setVisible(true);
//     }
//   }
// );
    }


    public SimpleJoglApp() {
        //set the JFrame title
        super("Simple JOGL Application");
        //kill the process when the JFrame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //only three JOGL lines of code ... and here they are
        GLCanvas glcanvas = new GLCanvas();
        //glcanvas.addGLEventListener(new SimpleGLEventListener());
        glcanvas.addGLEventListener(Listner);
        animator = new FPSAnimator(glcanvas, 60);
        // add the GLCanvas just like we would  any Component
        add(glcanvas, BorderLayout.CENTER);
        setSize(500, 300);
        //center the JFrame on the screen
        centerWindow();
        setVisible(true);
    }

    public void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();


        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        this.setLocation(
                (screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1
        );
    }
}


/**
 * For our purposes only two of the
 * GLEventListeners matter. Those would
 * be init() and display().
 */
class SimpleGLEventListener implements GLEventListener {

    /**
     * Take care of initialization here.
     */
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0/255f, 0/255f, 0/255f, 1.0f);
        gl.glViewport(0, 0, 600, 300);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-600.0, 600.0, -300.0, 300.0, -1.0, 1.0);
    }

    double x = 10, y = -10 ;
    float  rotationAngle = 0 ;
    boolean moveUp = true, moveLeft = true;

    /**
     * Take care of drawing here.
     */
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Draw grid lines
        for (int i = -590, j = -290; i <= 590; i += 20, j +=20) {
            gl.glColor3f(255 / 255f, 255 / 255f, 255 / 255f);
            // H lines
            line(i, i, 300, -300, 1, drawable);
            // V lines
            line(600, -600, j, j, 1, drawable);
        }

        gl.glColor3f(0 / 255f, 0 / 255f, 232 / 255f);
        // Draw vertical lines
        triangle(0, 0, 232, -580, -25, -580, 5, -595, -10, drawable);
        triangle(0, 0, 232, 580, -25, 580, 5, 595, -10, drawable);
        line(580, -580, -10, -10, 3, drawable);

        // Draw horizontal lines
        triangle(0, 0, 232, -5, 280, 25, 280, 10, 295, drawable);
        triangle(0, 0, 232, -5, -280, 25, -280, 10, -295, drawable);
        line(10, 10, -280, 280, 3, drawable);

        // Move the ball
        gl.glPushMatrix();

        if (moveUp) {
            y += 2;
            if (y >= 210) {
                moveUp = false;
            }
        } else {
            y -= 2;
            if (y <= -210) {
                moveUp = true;
            }
        }

        if (moveLeft) {
            x += 2;
            if (x >= 520) {
                moveLeft = false;
            }
        } else {
            x -= 2;
            if (x <= -520) {
                moveLeft = true;
            }
        }
        gl.glTranslated(x, y, 0);
        rotationAngle +=5;
        gl.glRotatef(rotationAngle, 0, 0, 1);
        if (rotationAngle == 360) rotationAngle = 0 ;
        ball(drawable);
        gl.glPopMatrix();
    }

    public void  ball (GLAutoDrawable drawable){
        int radius = 80 ;
        circle(255,215,0,radius,0,0,drawable);
        int x1 = 0;
        int y1 = radius;
        int x2 = (int) (-Math.sqrt(3) * radius / 2);
        int y2 = -radius / 2;
        int x3 = (int) (Math.sqrt(3) * radius / 2);
        int y3 = -radius / 2;
        triangle(255, 215, 0, x1, y1, x2, y2, x3, y3, drawable);
    }
    public void line (int start1,int end1, int start2 , int end2 ,float width,GLAutoDrawable drawable){
        GL gl =drawable.getGL();
        gl.glLineWidth(width);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2i(start1,start2);
        gl.glVertex2i(end1,end2);
        gl.glEnd();
        gl.glLineWidth(1.0f);
    }
    public void circle (float R , float G , float B , double radius , int i , int j , GLAutoDrawable drawable){
        GL gl =drawable.getGL();
        gl.glColor3f(R/255,G/255,B/255);
        final double ONE_DEGREE = (Math.PI/180);
        final double THREE_SIXTY = 2 * Math.PI;
        double x , y ;
        gl.glLineWidth(3.0f);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (double a = 0 ;  a < THREE_SIXTY ; a+=ONE_DEGREE){
            x = radius * (Math.cos(a)) + i ; // to change the x-position.
            y = radius * (Math.sin(a)) + j ; // to change the y-position.
            gl.glVertex2d(x , y);
        }
        gl.glEnd();
        gl.glLineWidth(1.0f);
    }
    public void triangle(float R , float G , float B ,int a1 , int a2
            ,int b1 ,int b2 ,int c1 ,int c2 , GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        gl.glColor3f(R/255,G/255,B/255);
        gl.glBegin(GL.GL_POLYGON);
        gl.glVertex2i(a1 , a2);
        gl.glVertex2i(b1 , b2);
        gl.glVertex2i(c1 , c2);
        gl.glEnd();
    }

    /**
     * Called when the GLDrawable (GLCanvas
     * or GLJPanel) has changed in size. We
     * won't need this, but you may eventually
     * need it -- just not yet.
     */
    public void reshape(
            GLAutoDrawable drawable,
            int x,
            int y,
            int width,
            int height
    ) {
    }

    /**
     * If the display depth is changed while the
     * program is running this method is called.
     * Nowadays this doesn't happen much, unless
     * a programmer has his program do it.
     */
    public void displayChanged(
            GLAutoDrawable drawable,
            boolean modeChanged,
            boolean deviceChanged
    ) {
    }


    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }
}