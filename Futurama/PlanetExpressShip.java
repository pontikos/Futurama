


import java.util.* ;

/* This class contains all instance variables of the spaceShip you
 * control. Alot of them have been made public so as to make access simpler
 * in the GamePanel class.
 * */


public class PlanetExpressShip extends Sprite {

public int width;
public int height;

public int life ;
public boolean dead = false ;

// the ship's missile arrayList
public ArrayList missiles = new ArrayList();

// when the ship's laser overheats it takes time to cool down
// so you have to wait for it cool before you can fire again
public int heat = 0 ;
public int heatCost = 15 ;

public int specialMissiles = 10 ;

private boolean invincible = false ;

// these variables determine the players accuracy
public int numberOfMissilesFired = 1;
public int numberOfHits = 0 ;

public PlanetExpressShip() {

    this.life = 100 ;

    this.width =  90;
    this.height =  38;


}

// when this method is called a Missile instance is added to the ship's missiles arrayList
public void fire() {

    numberOfMissilesFired++ ;
    heat = heatCost ;
    Missile m = new Missile(this.x+this.width-5,this.y+this.height/2,20) ;  
    missiles.add(m) ;
}


// this fires the ship's special weapon
public void specialFire() {

    // counts only as if only one missile was fired
    numberOfMissilesFired++ ;
    heat = heatCost ;
    specialMissiles-- ;

// fires a barrage of missiles
    for (int i=0 ; i<10; i++)
    {
        Missile m = new Missile(this.x+this.width-5,this.y-i*10,20) ;   
        missiles.add(m) ;
        Missile m2 = new Missile(this.x+this.width-5,this.y+i*10,20) ;
        missiles.add(m2) ;
        }

}


public void invincible() {

    invincible = true ;
}


// if invincible is set to true and the timer in the GamePanel class
// is less then 500 then the ship is invincible (see GamePanel class)
public boolean isInvincible(int timer3) {

    if ((timer3<500)&&invincible)
    return true ;
    else
    {   
        invincible = false ;
        return false ;
        }

}


}
