


public class Missile extends Sprite {

// depending on the velocity increment the missiles can move at different speeds
private int velocity;



public Missile(int x,int y,int velocity) {

this.x = x ;
this.y = y ;
this.velocity = velocity ;
}



// this method is called by the ship's missiles from the GamePanel class
public void moveRight() {

        this.x = x + velocity ;
}


// this method is called by the ennemy missiles from the GamePanel class
public void moveLeft() {

    this.x = x - velocity ;
}


}
