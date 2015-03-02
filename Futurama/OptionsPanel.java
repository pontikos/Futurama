

import java.awt.* ;
import javax.swing.* ;
import java.awt.geom.* ;
import javax.swing.event.* ;
import java.awt.event.* ;

/* This class allows the user to change two static variables in the GamePanel class:
 * the animation rate (ie number of frames per second)
 * time limit (ie if there is one or not)
 * */

public class OptionsPanel extends JPanel {

private int width ;
private int height ;
private TheSpacePilot3000 tsp ;
private JFrame gameFrame ;

private Area backMenu, frameRateMenu, timeLimitMenu ;
private Image[] pictures ;

public OptionsPanel(int width,int height, TheSpacePilot3000 tsp,JFrame gameFrame) {

    this.width = width ;
    this.height = height ;
    this.tsp = tsp ;    
    this.gameFrame = gameFrame ;


    MouseActions mouseActions = new MouseActions() ;
    this.addMouseListener(mouseActions) ;
    
    this.backMenu = new Area(new Rectangle(10,height-70,100,100)) ;
    this.frameRateMenu = new Area(new Rectangle(405,100,190,25)) ;
    this.timeLimitMenu = new Area(new Rectangle(405,155,165,25)) ;
    
   
    
    this.pictures = new Load().getPictures(this,25,26) ;

        
}


public void paintComponent(Graphics g) {

   super.paintComponent(g) ;
           
   g.drawImage(pictures[0],0,0,this) ;
      
}


public void displayFrameRateOption() {

String frameRate = JOptionPane.showInputDialog(gameFrame,"Frames per second (must be between 30 and 1000): ","Modify frame rate option",JOptionPane.PLAIN_MESSAGE ) ;

try 
    {
        GamePanel.fps = Integer.parseInt(frameRate) ;
    }
    catch (NumberFormatException nfe)
    {
        GamePanel.fps = 40 ;
    }
}
    
public void displayTimeLimitOption() {

int timeLimitChosen = JOptionPane.showConfirmDialog(gameFrame,"Do you want a time limit? (there is one by default) ","Time limit option",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) ;

// if NO has been chosen then:
if (timeLimitChosen == 1)
GamePanel.timeLimit = false ;

if (timeLimitChosen == 0)
GamePanel.timeLimit = true ;
}



class MouseActions extends MouseInputAdapter {

public void mouseClicked(MouseEvent e) {

   if (backMenu.contains(e.getPoint()))
   {
   tsp.addMenuPanel() ;
   }
   
   if (frameRateMenu.contains(e.getPoint()))
   {
       displayFrameRateOption() ;
   }
   
   if (timeLimitMenu.contains(e.getPoint()))
   {
       displayTimeLimitOption() ;
   }
}

}



}

