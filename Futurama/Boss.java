

import java.util.* ;

/* When the score objective has been obtained for the current level 
 * then the boss appears
 * */

public class Boss extends Sprite {

public int width ;
public int height ;
public int life ;
public boolean shoots;

private double theta = 0 ;
private int path = 1 ;

private int k;
private int j;

private Random random = new Random() ;
private int targetY = 200 ;

// some bosses can shoot therefore have a missiles arrayList
public ArrayList missiles = new ArrayList() ;

public Boss(int x, int y, int width, int height, int life, boolean shoots) {

this.x = x ;
this.y = y ;
this.width = width ;
this.height = height ;        
this.life = life ;
this.shoots = shoots ;

}


public void move(int level,PlanetExpressShip pxs,GamePanel gp) {

    switch (level)
    {
                                
        case 2:
        {            
         circularMovement(gp) ;
         break ;
         } 
          
         default:
         {         
          randomMovement(gp) ;      
          break ;        
          } 
    
    }

}


public void fire() {
// this creates a missile which will start moving from the specified location
// then adds to the boss' missile arrayList
Missile missile = new Missile(x,y+height/2,10) ;
missiles.add(missile) ;
}




private void circularMovement(GamePanel gp) {

     x = (int) (200*Math.cos(theta) + gp.width-300) ;
     y = (int) (200*Math.sin(theta) + gp.height/2-this.height) ;
           
     theta = (theta + 0.01)%(2*Math.PI) ;
       }


private void randomMovement(GamePanel gp) {

   if ((targetY-10 <= y)&&(y <= targetY+10))
      {
        targetY = 30+random.nextInt(gp.height-90) ; 

        j = (-y-x+gp.height-50)/10 ;
        k = (-y+x)/10 ;

        if (path == 1)
        path = 2 ;
        else
        path = 1 ;
    }

if (x <= 30 || x >= gp.width-60)
    {
       
 j = (-y-x+gp.height-50)/10 ;
 k = (-y+x)/10 ;      
       
       if (path == 1)
        path = 2 ;
        else
        path = 1 ;
        
       }
       
       
    if ((targetY-y)>0)
    {
        if (path == 1)
        {
        x = x + 5 ; 
        y = x - k*10 ;
        }
        else
        {
        x = x - 5 ; 
        y = -(x + 10*j) + gp.height-50 ;
        }
    }
    else
    {
        if (path == 1)
        {
        x = x - 5 ;
        y = x - k*10 ;
        }
        else
        {
        x = x + 5 ;  
        y = -(x + 10*j) + gp.height-50 ;
        }
        
        }


    }



}



