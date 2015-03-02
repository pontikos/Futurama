

import javax.imageio.* ;
import javax.swing.* ;
import java.awt.* ;
import java.awt.image.* ;
import java.io.* ;
import java.util.* ;
import java.awt.geom.* ;
import javax.swing.event.* ;
import java.awt.event.* ;

public class ScorePanel extends JPanel {


private int height ;
private int width ;
private TheSpacePilot3000 tsp ;

private int y = 100 ;

private Load load = new Load() ;

private BufferedImage buffImage; 
private Graphics2D big ;
private Area backMenu ;

private ArrayList scores = new ArrayList() ;

/* The ScorePanel just gets the scores from the file, writes them to the buffered image by
 * calling the scores' toString method and then display the buffered image to the screen.
 * */
 

public ScorePanel(int width,int height,TheSpacePilot3000 tsp) {

    this.width = width ;
    this.height = height ;
    this.tsp = tsp ;

    MouseActions mouseActions = new MouseActions() ;
    this.addMouseListener(mouseActions) ;
    
    this.backMenu = new Area(new Rectangle(10,height-70,100,50)) ;




buffImage = new BufferedImage(width,height,1) ;

big = (Graphics2D) buffImage.createGraphics() ;

    try 
    {
        BufferedImage pic = ImageIO.read(new File("Images/scores.jpg")) ;
        big.drawImage(pic,0,0,width-5,height-30,this) ;
        }
        catch (IOException ioe) 
        {
            }
      
   this.scores = load.getScores("Data/scores.text") ;

   writeScoresToImage(big) ;

repaint() ;
}

private void writeScoresToImage(Graphics2D big) {

    big.setColor(Color.red) ;
    big.setFont(new Font("Helvetica Bold",10,25)) ;                                
     
     for (int i=0; i<scores.size(); i++)
     {
         big.drawString(((Score)scores.get(i)).toString(),200,y) ;  
         
         if (y>height-80)
         break ; // just display enough scores to fill the panel
         
         y = y + 25 ; 
     }
    
}

         

public void paintComponent(Graphics g) {
   
   super.paintComponent(g) ;
   
   Graphics2D g2d = (Graphics2D) g; 
     
   g2d.setClip(0,0,width,height) ;  
   g2d.drawImage(buffImage,0,0,this) ;
   

    
}


class MouseActions extends MouseInputAdapter {

public void mouseClicked(MouseEvent e) {

   if (backMenu.contains(e.getPoint()))
   {
   tsp.addMenuPanel() ;
   }
}

}


}
