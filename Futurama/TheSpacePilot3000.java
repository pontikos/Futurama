

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.* ;
import java.util.ArrayList ;
import java.applet.* ;

/* This is the program's main class (the class containing the main method which runs the 
 * the program). This class is in charge of creating among other things the JFrame where 
 * all the panels will be placed and also contains an array of all the sound clips the 
 * program uses (I explain later why I made such a choice).
 * This class is also the class where all objects of type JPanel are instantiated
 * and then added to the frame.
 * It contains instance methods which allow different panels to be added and removed.
 * The idea is that each time an object of type JPanel is instantiated, the current instance
 * of the TheSpacePilot3000 class is passed as a parameter to the object's constructor so
 * that all panels can call the instance methods which make the frame change panel 
 * (see the relevant methods for more detail).
 * 
 * */

public class TheSpacePilot3000  {

private int width;
private int height;

private JFrame gameFrame ; 
private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
private Container gameFrameContainer ;
private Component[] components ;

private final AudioClip[] sounds = new Load().getSounds() ; 
    

public TheSpacePilot3000(int width,int height) {

    this.width = width;
    this.height = height;                
    
    createFrame(width,height) ;  

    }


public void createFrame(int width,int height) {
   
   
   gameFrame = new JFrame("The Space Pilot 3000") ;
    
    gameFrame.pack() ;
    
    
    gameFrame.setSize(width,height) ;
    gameFrame.setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
     
     
     Image icon = Toolkit.getDefaultToolkit().getImage("Images/planetExpressShipT.gif") ;
     gameFrame.setIconImage(icon) ;
     
    gameFrame.setResizable(false) ;
    gameFrameContainer = gameFrame.getContentPane();
    
    
 /* I had a problem with the sound threads because if the user was exiting the program by 
  * closing the frame and not using the game's provided exit method, then the sound thread 
  * wasn't terminating (the music was still playing).
  * Therefore I decided that the class containing the frame should load the sound arraylist 
  * so that it may stop all sound threads when the window closing event takes place. 
  * All the panels who use sound must refer to this class for sound through the 
  * "getSound(index)" method.
  * */   
          
    
    gameFrame.addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent e) {            
            
  // stop all sounds on exit
            for (int i=0; i<sounds.length; i++)
            {
                sounds[i].stop() ;
            }
            
            System.gc() ;
            System.exit(0);
            
        }
        });
    
    
    addMenuPanel() ;
    gameFrame.setVisible(true) ;
}


// this a utility method which returns the sound clip at the given index,
// the index depends on the line number of the clip's name in the Sounds file
public AudioClip getSound(int i) {
        
        return sounds[i] ;
    }
    

// this method removes the previous Component from the frame and adds an instance of
// the GamePanel class
public void addGamePanel() {
   
    String name = JOptionPane.showInputDialog(gameFrame,"Please enter name",
    "Pilot's name",JOptionPane.PLAIN_MESSAGE ) ;
    
    if (name != null)
    {
    gameFrame.setTitle("The Space Pilot 3000 \b player: "+name) ;
    
    this.components = gameFrameContainer.getComponents() ;

    try 
    {
    gameFrameContainer.remove(components[components.length-1]) ;
    }
    catch (IndexOutOfBoundsException e)
    {
    }
    
    
    
    GamePanel gamePanel = new GamePanel(name,width,height,this) ;
    gamePanel.setFocusable(true) ;
    
    gameFrameContainer.add(gamePanel,BorderLayout.CENTER);
    
    gameFrame.setVisible(true);
    
    gamePanel.requestFocus(true) ;
    }
    else
    {
        addMenuPanel() ;
    }
    
}


  public void addCreditsPanel() {

    this.components = gameFrameContainer.getComponents() ;

    try {
    gameFrameContainer.remove(components[components.length-1]) ;
    }
    catch (IndexOutOfBoundsException e)
    {
    }
    
    
    gameFrameContainer.add(new CreditsPanel(width,height,this),BorderLayout.CENTER);
    gameFrame.setVisible(true);
}

public void addScorePanel() {

    this.components = gameFrameContainer.getComponents() ;

    try {
    gameFrameContainer.remove(components[components.length-1]) ;
    }
    catch (IndexOutOfBoundsException e)
    {
    }
    
    
    gameFrameContainer.add(new ScorePanel(width,height,this),BorderLayout.CENTER);

    //ScorePanel panel = new ScorePanel(width,height,this) ;
    //JScrollPane scrollPane = new JScrollPane(panel) ;
    //scrollPane.setPreferredSize(new Dimension(width,height)) ;
    //scrollPane.setVerticalScrollBar(scrollPane.createVerticalScrollBar()) ;
    //gameFrameContainer.add(scrollPane,BorderLayout.CENTER); 
    
    gameFrame.setVisible(true);
    
}


public void addOptionsPanel() {


    this.components = gameFrameContainer.getComponents() ;

    try {
    gameFrameContainer.remove(components[components.length-1]) ;
    }
    catch (IndexOutOfBoundsException e)
    {
    }
    
    
    gameFrameContainer.add(new OptionsPanel(width,height,this,gameFrame),BorderLayout.CENTER);
    gameFrame.setVisible(true);
}








public void addMenuPanel() {


    this.components = gameFrameContainer.getComponents() ;

    try {
    gameFrameContainer.remove(components[components.length-1]) ;
    }
    catch (IndexOutOfBoundsException e)
    {
    }
    
    gameFrameContainer.add(new MenuPanel(width,height,this),BorderLayout.CENTER);
    gameFrame.setVisible(true);
}




public static void main(String[] args) {

JFrame.setDefaultLookAndFeelDecorated(true) ; 
TheSpacePilot3000 sp3000 = new TheSpacePilot3000(650,500) ;    
}



}

    
