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
        gl.glClearColor(90/255f, 0/255f, 180/255f, 1.0f);
        gl.glViewport(0, 0, 600, 300);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-600.0, 600.0, -300.0, 300.0, -1.0, 1.0);
    }

    /**
     * Take care of drawing here.
     */
    int x = -610 ;
    double leftEyeV = -290 , rightEyeV = 240 , eyeHeight = 200 , theta = 0 , radius = 100 , velocity = 0.005;
    boolean moveDirection = true ;

    public void display(GLAutoDrawable drawable) {
        //  push --> translate or scaled or rotated --> pop
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        triangle(250,231,12,-200,150,200,150,0,-150,drawable);
        // left eye
        circle(255, 255, 255, 280, -270, 180, drawable);
        eyeMove(leftEyeV,eyeHeight,velocity,drawable);
        // right eye
        circle(255, 255, 255, 280, 270, 180, drawable);
        eyeMove(rightEyeV,eyeHeight,velocity,drawable);
        // ball move
        gl.glPushMatrix();
        gl.glTranslated(x+=3 , 0 , 0);
        circle(226,0,4,35,x,-300,drawable);
        if (x >= 610) x= -610 ;
        gl.glPopMatrix();
    }
    public void eyeMove(double posX , double posY ,double velocity , GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        gl.glPushMatrix();
        double eyeX = posX - radius * Math.cos(theta);
        double eyeY = posY - radius * Math.sin(theta) ;
        gl.glTranslated(eyeX,eyeY, 0);
        gl.glScaled(0.2, 0.2, 1);
        circle(0, 0, 0, 400, 0, 0, drawable);
        if (moveDirection) {
            theta+=velocity;
            if (theta >= Math.PI) {
                moveDirection = !moveDirection;
            }
        } else {
            theta-=velocity;
            if (theta <= 0) {
                moveDirection = !moveDirection;
            }
        }
        gl.glPopMatrix();
    }
    public void circle (float R , float G , float B , double radius , int i , int j , GLAutoDrawable drawable){
        GL gl =drawable.getGL();
        gl.glColor3f(R/255,G/255,B/255);
        final double ONE_DEGREE = (Math.PI/180);
        final double THREE_SIXTY = 2 * Math.PI;
        double x , y ;
        gl.glBegin(GL.GL_POLYGON);
        for (double a = 0 ;  a < THREE_SIXTY ; a+=ONE_DEGREE){
            x = radius * (Math.cos(a)) + i ; // to change the x-position.
            y = radius * (Math.sin(a)) + j ; // to change the y-position.
            gl.glVertex2d(x , y);
        }
        gl.glEnd();
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