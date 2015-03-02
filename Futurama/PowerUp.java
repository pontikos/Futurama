


/* The power-up class provides power-ups such as:
 * life + 10 
 * increased firing ratio and a special weapon
 * brief invincibility
 * Depending on the randomly generated "type" variable you'll get one
 * of the 3.
 * */


public class PowerUp extends Sprite {


private int width ;
private int height ;
private int type ;

public PowerUp(int type,int width,int height) {

this.width = width ;
this.height = height-10 ;
this.type = type ;
    
    setStartingPosition() ;
}


// the different power-ups start at different positions
public void setStartingPosition() {

switch (type)
    {
        case 1:
        x = width ;
        y = height/2 ;
        break ;
        
        case 2:
        x = width ;
        y = 0 ;
        break ;
        
        case 3:
        x = width ;
        y = height ;
    }
}


// the different power-ups move differently
public void move() {

x = x - GamePanel.speed/5 ;

    switch (type)
    {
        case 1:
        // just moves straight right to left
        break ;
        
        case 2:
        y++ ; // moves diagonally up-right to bottom-left
        break ;
        
        case 3:
        y-- ; // moves diagonally up-left to bottom-right
        break ;
        }

}


// If the ship picture and the power-up picture collide then this method is called from
// the GamePanel class. This method changes the ship's variables depending on the type of
// power-up.
public void poweredUp(PlanetExpressShip pxs) {
    
    switch (type)
    {
        case 1:
        if (pxs.life < 100)
        // life can't be more then 100
        pxs.life = ((pxs.life + 10)>100) ? 100 : (pxs.life + 10) ;  
        break ;
        
        case 2:
        if (pxs.heatCost > 5)  
	  {
	    	    pxs.heatCost -= 5  ;
	  }
	pxs.specialMissiles++ ;
        break ;
        
        case 3:
        pxs.invincible() ;
        break ;
    }
}

// return the type of the power-up (see GamePanel class)
public int getType() {

    return type ;
}


}
