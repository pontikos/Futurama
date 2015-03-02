

import javax.swing.* ;
import java.awt.event.* ;

/* This class provides an animation for objects of type JPanel by calling the repaint method
 *  of the JPanel object passed as a parameter to the Animation constructor.
 *  The advantage of using javax.swing.Timer rather then java.lang.Thread is that although
 *  Timer instances use their own thread to perforym waiting, their action-event handlers
 *  execute directly on the event-dispatching thread (responsible for repainting the Swing
 *  components). This is critical because Swing components are not thread-safe (ie only the
 *  event-dispatching thread should access the state of the GUI). 
 *  Timer takes care of that for us.
 *  */


/*NOTE: 
 * I could have made Animation extend Timer but i prefer doing it this way because
 * it is clearer to the person who reads the code what methods an instance of the animation 
 * class has. The A
 * */

public class Animation implements ActionListener {

private Timer timer ;
private JPanel panel ;

public Animation(int fps,JPanel panel) {

// the delay is set to 100 milliseconds if the frame-rate is less than 0 or more 
// than 1000 per second
int delay = ((fps>0)||(fps>1000)) ? (1000/fps) : 100 ;

this.panel = panel ;

timer = new Timer(delay,this) ;

}


public void stop() {

if (timer.isRunning())
timer.stop() ;

}


public void start() {

timer.start() ;

}

public void actionPerformed(ActionEvent e) {

panel.repaint() ;

}

public boolean isRunning() {

return timer.isRunning() ;
}

}
