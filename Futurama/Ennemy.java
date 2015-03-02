

import java.util.* ;

public class Ennemy extends Sprite {

// in this case x,y are of type double for reasons of precision
public double x ;
public double y ;


// these methods are static because all instances of this class have the same values
// for these variables per level
public static int width ; 
public static int height ; 
public static boolean shoots ; 
 
public ArrayList missiles = new ArrayList() ;




private static Random random = new Random();
public int index ;
private static int inc = 0 ;



private int targetY ;
private int path = 3 ;
private int k;
private int j;

private GamePanel gp ;


public Ennemy(double x,GamePanel gp) {

this.gp = gp ;
this.x = x ;


this.index = 1 + inc ;
inc = (inc + 1)%4 ;
    
       
    switch (index) {

    case 1:
    this.y = gp.height/2 ;
    break ;
    
    case 2:
    this.y = 130 + random.nextInt(gp.height-270)  ;    
    break ;
    
    default:
    this.y = 30 + random.nextInt(gp.height-120) ;
    break ;
           
    }

this.targetY = (int) y ;

}





public void move(List ennemies, int level, PlanetExpressShip pxs) {


    x =(int)( x - gp.speed/5 ) ;


switch (index) 
    {
        
    case 1:
    {                 
           move_1() ;        
           break ;
    }
    
         
    case 2:
    {               
        move_2() ;    
        break ;
    }
    
    case 3:
    {
        move_3() ;
        break ;
      }
      
      case 4:
      {
          // just moves straight
          break ;
      }
      
      }

if (x<0)
ennemies.remove(this) ;
}


private void move_1() {

    if ((targetY-5 <= y)&&(y <= targetY+5))
    {
   targetY = 25+random.nextInt(gp.height-60) ; 
                
                k = (int) (x-y)/10 ;
                j = (int) (gp.height-x-y)/10 ;
                }
 
    if ((targetY-y)>0)
    path = 2 ;
    else
    path = 1 ;
 
    if ( y <= 30 )
    {
        path = 2 ;
        j = (int) (gp.height-x-y)/10 ;
        }
 
    if ( y >= (gp.height-50) )
    {
        path = 1 ; 
        k = (int) (x-y)/10 ; 
        }




    switch (path)
        {
            case 1:            
            y = x - 10*k ;
            break ;
            
            case 2:
            y = -x + gp.height -10*j ;
            break ;
                                    
        }

}



private void move_2() {

y = (int) (targetY + 100*Math.sin(x/(20+5*gp.speed))) ;

}


private void move_3() {


}


public void fire() {

Missile missile = new Missile((int)x,(int)y+height/2,15) ;
missiles.add(missile) ;

}

}
