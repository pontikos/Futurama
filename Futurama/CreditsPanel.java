

import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

import javax.swing.JPanel;

/* This is the panel which displays the credits it uses the Load class
 * to read the credits from the credits file and display them.
 * It also creates an instance of the Animation class to get the credits 
 * scrolling upwards.
 * */

public class CreditsPanel extends JPanel {
        
    private int width ;
    private int height ;    
    private TheSpacePilot3000 tsp ; 
    
    private Load load = new Load() ;
    private Image[] pictures ;
    private AudioClip creditsMusic ;
        
    private ArrayList credits = new ArrayList() ;
    
    private Font font = new Font("Helvetica Bold",3,30) ;   
    private int y ;
    private int y2 = 0;
    
            
    private Animation animation ;
    
    private Area backMenu ;

public CreditsPanel(int width,int height, TheSpacePilot3000 tsp) {

    this.width = width ;
    this.height = height ;
    this.tsp = tsp ;
   
    this.y = height ;
    
    MouseActions mouseActions = new MouseActions() ;
    this.addMouseListener(mouseActions) ;
    
     this.backMenu = new Area(new Rectangle(25,height-70,100,35)) ;
    
     this.creditsMusic = tsp.getSound(3) ;
     creditsMusic.loop() ;
     
     this.credits = load.getStrings("Data/credits.text") ;
     this.pictures = load.getPictures(this,23,24) ;
     
     animation = new Animation(30,this) ;
     animation.start() ;
  }


public void paintComponent(Graphics g) {
      
      super.paintComponent(g) ;
      
     g.setFont(font) ;          
    
    g.drawImage(pictures[0],0,0,this) ;
    
       for (int i=0; i<credits.size(); i++)
       {
           g.drawString((String)credits.get(i),10,y) ;
           y += 32 ;
       }
    
    // makes the credits scroll from the bottom up and start again
    // from the bottom once all credits have been displayed
    y2 = (y2 + 1) % (width+font.getSize()*credits.size()) ; 
    y = height-y2 ;
  }




class MouseActions extends javax.swing.event.MouseInputAdapter {

public void mouseClicked(MouseEvent e) {

   if (backMenu.contains(e.getPoint()))
   {
   creditsMusic.stop() ;
   animation.stop() ;
   tsp.addMenuPanel() ;
   }
}

}



}

