

import java.applet.* ;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.* ;
import java.awt.geom.Area ;

/* An instance of the MenuPanel, class when added to the frame allows the user to choose
 * which panel he wishes to view. Once the panel is chosen using the mouse then the relevant
 * method is called by the TheSpacePilot3000 instance which removes the current panel
 * from the frame and replaces it by the wanted panel.
 * */

public class MenuPanel extends JPanel  {  
   
   private int width ;
   private int height ;
   private TheSpacePilot3000 tsp;
   
    
    private Load load = new Load() ;
   
    private Area playMenu, creditsMenu, scoreMenu, optionsMenu, exitMenu;
    private AudioClip clip ;
    
    
    
    private Random random = new Random() ;
    private Font font = new Font("Helvetica Bold",1,25) ;
    
    private int line = 0 ;
    private ArrayList messages ; 
    private Image[] pictures ;
    
    
    int x = 0 ;
    
    
    
    public MenuPanel(int width,int height,TheSpacePilot3000 tsp)
    {
      
      this.width = width ;
      this.height = height ;
      this.tsp = tsp;
     
      MouseActions mouseActions = new MouseActions() ;
      this.addMouseListener(mouseActions) ;
      
      
      
      this.messages = load.getStrings("Data/randomMessages.text") ;
      this.pictures = load.getPictures(this,22,23) ;
      
    
       this.clip = tsp.getSound(4) ;

   
          
    clip.loop();
      
      
      
      this.playMenu = new Area(new Rectangle(210, 210, 44, 30));
      this.creditsMenu = new Area(new Rectangle(265,213, 70, 30));
      this.scoreMenu = new Area(new Rectangle(350,213, 56, 30));
      this.optionsMenu = new Area(new Rectangle(417,217,75,30));
      this.exitMenu = new Area(new Rectangle(505,218,56,30));
      
              
      }
 
 


public void paintComponent(Graphics g) { 
     
     super.paintComponent(g) ;
                 
     g.drawImage(pictures[0],0,0,width,height,this);      
         
     g.setColor(new Color(232,110,211)) ;      
     g.setFont(font) ;
     g.drawString((String)messages.get(random.nextInt(messages.size())),5,height-100) ;
       
}


class MouseActions extends javax.swing.event.MouseInputAdapter {

    public void mouseClicked(MouseEvent e) {
            
       
       chooseMenu(e.getPoint());
    }
    
    
    public void chooseMenu(Point point)
    {
    
    if(playMenu.contains(point))
    {                 
      clip.stop();
      
      tsp.addGamePanel();      
    }
    
    if(scoreMenu.contains(point))
    {     
       clip.stop();      
       removeMouseListener(this) ;
       tsp.addScorePanel() ;
    }
    
    if(creditsMenu.contains(point))
    {
       clip.stop() ;      
       removeMouseListener(this) ;
       tsp.addCreditsPanel() ;

    }
      
      
    if(optionsMenu.contains(point))
    {
            clip.stop() ;      
            removeMouseListener(this) ;
            tsp.addOptionsPanel() ;
    }
      
      
    if(exitMenu.contains(point))
    {
      clip.stop() ;            
      System.exit(0) ;
    }
    
    }
}
    
    
  }
  


