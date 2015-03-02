

import java.applet.* ;
import java.awt.image.* ;
import java.awt.* ;
import javax.swing.* ;
import java.awt.geom.* ;
import java.awt.event.* ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.Random ;

/* This is the core class of the mini-project ("the game engine"). Here is were all the 
 * objects of type Sprite are instantiated and displayed to the screen.
 * This class uses the Timer class to repaint the panel. On each repaint all sprites call
 * their move method. The frame rate can be changed in the options class...
 * */

public class GamePanel extends JPanel implements KeyListener{


/////////// INSTANCE VARIABLE DECLARATIONS /////////////////////////////////
public int width;
public int height;
private TheSpacePilot3000 tsp ;

private Load load = new Load() ;
private int level = 1 ;


private Image[] pictures ;

// picture variables 
private Image backPic, ennemyPic, bossPic, shipPic, shipPic2, shipPic3, explosionPic, lifeUpPic, weaponUpPic, 
invincibilityPic ;


// sound variables
private AudioClip laserSound, explosionSound, soundTrack, specialSoundTrack ;

private Animation animation ; 

private PowerUp pw ;

private String playerName ;
private int score = 0 ;

// the following variables are public because they are being initialized from the Load class
public int scoreObjective ;
public int numberOfEnnemies ;

// messages which are displayed (ie "Level 1: Defeat ... ")
public String message, message2, message3, message4 ;

// boss variables
public Boss boss ;
private boolean showBoss = false ;
private boolean playing = false ;

// THE SPACE SHIP YOU CONTROL 
private PlanetExpressShip pxs = new PlanetExpressShip() ;
private Rectangle spaceShip ;

// BAD GUYS
// The ennemies arrayList is made thread-safe so must therefore be accessed from
// synchronized context (see methods...)
private List ennemies = Collections.synchronizedList(new ArrayList()) ;


private boolean moving = false;
private int speedMeter = 1 ;
public static int speed = 10 ;

// these are the variables which make the background picture scroll backwards
private int backx = 0;
private int backx2 = 650;

// TIMERS 
// all set to 0 by default
private int timer, timer2, timer3 ;
// this timer represents the time limit per level (when it reaches zero you die)
public int countDown ; 

// FRAMES PER SECOND
// fps is static because it can be changed when GamePanel is not instantiated
// (see OptionsPanel for details) has a default value of 40
public static int fps = 40 ; 

// TIME LIMIT
// time limit is static because it can be changed when GamePanel is not instantiated
// (see OptionsPanel for details) is true by default
public static boolean timeLimit = true ; 

public static Insets insets ; 

private Random random = new Random() ;

private boolean special = false ;
/////////////////////////////////////////////////////////////////////



public GamePanel(String name,int width,int height,TheSpacePilot3000 tsp) {

// just in case double-buffered isn't set by default (normally it is)
super(true) ;

this.addKeyListener(this) ; 

this.playerName = name ;

this.width = width ;
this.height = height ;

this.tsp = tsp ;
this.pictures = pictures ;

load.levelSettings(level,this);
pictures = load.getPictures(this,0,22) ;

// assign variables to the pictures
assignPics() ;

// gets the sound from the TheSpacePilot3000 class
assignSounds() ;

setInsets() ;

// create an instance of class animation by passing the chosen frame-rate and
// a reference of the current GamePanel as parameters
animation = new Animation(fps,this) ; 
animation.start() ;

// tune for the game
soundTrack.loop() ;

}





// This is to try and improve performance so that the picture variables can be found
// as quickly as possible.
// Also it improves readability of the code.
private void assignPics() {

    backPic = pictures[level-1] ;
    ennemyPic = pictures[5+level-1] ;
    bossPic = pictures[10+level-1] ;
    shipPic = pictures[15] ;
    shipPic2 = pictures[16] ;
    shipPic3 = pictures[17] ;
    explosionPic = pictures[18] ;
    lifeUpPic = pictures[19] ;
    weaponUpPic = pictures[20] ;
    invincibilityPic = pictures[21] ;
}


// assign sounds to variables
private void assignSounds() {

    laserSound = tsp.getSound(0) ;
    explosionSound = tsp.getSound(1) ;
    specialSoundTrack = tsp.getSound(3) ;
    soundTrack = tsp.getSound(2) ;    
}



public void setInsets() {

    insets = new Insets(30,30,30,30) ;
    
}



// this is the paint method which is used by Swing components 
// (equivalent to AWT's paint method but for Swing)
public void paintComponent(Graphics g) {

// tells the JPanel to paint itself thus setting the background
// this avoids visual artefacts
super.paintComponent(g) ;

// we use the Graphics2D class which extends the Graphics class
Graphics2D g2d = (Graphics2D) g ;

// makes animation smoother by only repainting the necessary area (ie the visible area)
g2d.setClip(0,0,640,467) ;

// makes the background scroll backwards to give a sense of motion
setBackground(g2d) ;

// increment/decrement timers
countDown-- ;
timer++ ;
timer3++ ;
   
// this determines the time your laser takes to cool down
if (pxs.heat > 0)   
pxs.heat-- ;   


// every time the background has scrolled back twice, ennemies are added
if ((backx <= -width) && (score < scoreObjective))
addEnnemies();


//display all moving objects on the GamePanel
g2d.setColor(Color.red) ;
displaySprites(g2d) ;

// displays information
displayInfo(g2d) ;


// display reactors when moving
displayReactors(g2d) ;

}



private void setBackground(Graphics2D g2d) {
 
 // the background is in fact 2 pictures scrolling backwards
     
    g2d.drawImage(backPic,backx,0,width+30,height-30,this) ;
    g2d.drawImage(backPic,backx2,0,width+30,height-30,this) ;
    
    
    if(backx <= -width)
    backx = width ;

// the speed at which the background scrolls is proportional to the speed of the ship
    backx = backx - speed ;
    
    if(backx2 <= -width)
    backx2 = width ;


    backx2 = backx2 - speed ;

    if (special == true)  // when the ship fires its special weapon the screen becomes red
    {
    g2d.setColor(Color.red) ;
    g2d.fillRect(0,0,width,height) ;
    special = false ;
    }
}




// adds ennemies to the GamePanel
private synchronized void addEnnemies() {
          
    for (int i=0; i < numberOfEnnemies; i++)
    {
    
    Ennemy ennemy = new Ennemy(width+i*40,this);
    ennemies.add(ennemy) ;
    }

}


// this displays all the sprites (ie ennemies, ship, powerUps, boss and missiles)
private synchronized void  displaySprites(Graphics2D g2d) {

spaceShip = new Rectangle(pxs.x,pxs.y,pxs.width,pxs.height) ;

    if (pxs.isInvincible(timer3))
    {
        // when invincible the ship flashes blue/yellow
        if (timer3 % 2 == 0)
        g2d.setColor(Color.blue) ;
        else
        g2d.setColor(Color.yellow) ;

    g2d.fillOval(pxs.x,pxs.y,pxs.width,pxs.height) ; 
    }

// set color back to red to display missiles
g2d.setColor(Color.red) ;

// display powerup
displayPowerUp(g2d) ;

// displays ship missiles
for (int i=0; i<pxs.missiles.size(); i++)
{
    Missile m = (Missile) pxs.missiles.get(i) ;      
      
     m.moveRight() ;
      
      Rectangle missileShape = new Rectangle(m.x,m.y,20,2) ;
      
      g2d.fill(missileShape) ;      
      
      if (m.x >= width)
      pxs.missiles.remove(i) ;   
  }

displayEnnemies(g2d) ;

if (showBoss == true)
displayBoss(g2d) ;     


// if invincible play a special music
if (pxs.isInvincible(timer3))
{
if (playing == false)
{
    specialSoundTrack.loop() ;
    playing = true ;
}
}

// a special music is played when you are either invincible or fighting the boss
if ((!pxs.isInvincible(timer3))&&(showBoss==false))
{
    specialSoundTrack.stop();
    playing = false ;
}
  

// changes the ship's picture when it gets damaged 
  if (pxs.life > 50)
  {
  g2d.drawImage(shipPic,pxs.x,pxs.y,pxs.width,pxs.height,this) ;
  }
  else 
  {
      if (pxs.life <= 50 && pxs.life > 20)
      g2d.drawImage(shipPic2,pxs.x,pxs.y,pxs.width,pxs.height,this) ;
      else      
      g2d.drawImage(shipPic3,pxs.x,pxs.y,pxs.width,pxs.height,this) ;
  }
  
}
    
private synchronized void displayEnnemies(Graphics2D g2d) {

// display ship and ennemies normally if no collision occurs
for (int i=0; i<ennemies.size(); i++)
{

Ennemy en = (Ennemy) ennemies.get(i) ;
en.move(ennemies,level,pxs) ;

Rectangle ennemy = new Rectangle((int)en.x,(int)en.y,en.width,en.height) ;

// collision detection  
if (g2d.hit(spaceShip,ennemy,false)||isShot(g2d,ennemy))
{     
   explosionSound.play() ;
   score = score + 10 ;
   try 
   {
   ennemies.remove(i) ;
   }
   catch (IndexOutOfBoundsException ie)
   {
   }
   g2d.fillOval((int)en.x,(int)en.y,en.width,en.height) ;
   
   if (g2d.hit(spaceShip,ennemy,false))
   {   
   
   if (!pxs.isInvincible(timer3))
   {
      pxs.life = pxs.life - 5 ;
      g2d.setColor(Color.red) ;
      g2d.fillOval(pxs.x,pxs.y,pxs.width,pxs.height) ;
      }

   }
   
     }
     else 
     { 
         g2d.drawImage(ennemyPic,(int)en.x,(int)en.y,en.width,en.height,this) ;
     }
     
     if (Ennemy.shoots == true)
     {
     if (timer%200 == 0)
     {
     en.fire() ;
     }
     displayEnnemyMissiles(g2d,en) ;
     }
     
     if (en.x <= 0)
     {
         try 
         {
             ennemies.remove(i) ;
             }
             catch (IndexOutOfBoundsException e)
             {
                 }
     }
 }
 
}



 
private void displayBoss(Graphics2D g2d) {
    
    
    Rectangle bossShape = new Rectangle(boss.x,boss.y,boss.width,boss.height) ;
    boss.move(level,pxs,this) ;
    
    if (g2d.hit(bossShape,spaceShip,false))
    {
        explosionSound.play() ;
        // you only get damaged if you're not invincible
        if (!pxs.isInvincible(timer3))
        {
           pxs.life = pxs.life - 5 ;        
           g2d.setColor(Color.red) ;
           g2d.fillOval(pxs.x,pxs.y,pxs.width,pxs.height) ;
        }
        else
        {        
        
        }
        
        boss.life--;
    }
    
    if (isShot(g2d,bossShape))
        {
        boss.life-- ;
        g2d.setColor(Color.red) ;
        g2d.fillOval(boss.x,boss.y,boss.width,boss.height) ; 
        }
    // every twenty frames the boss fires
    if ((boss.shoots == true) && (timer%20 == 0))
    {
    boss.fire() ;
    }
    
    // display the missiles that the boss shoots (if he does shoot)
    displayBossMissiles(g2d) ;
    
    // display the boss' picture
    g2d.drawImage(bossPic,boss.x,boss.y,boss.width,boss.height,this) ;

} 
 



// return true if any of the ship's missiles hits the specified shape "baddy"
private boolean isShot(Graphics2D g2d, Shape baddy) {
    
    for (int i=0; i<(pxs.missiles).size(); i++)
    {
      
      Missile m = (Missile) pxs.missiles.get(i) ;      
      
      Rectangle missileShape = new Rectangle(m.x,m.y,20,2) ;
    
    if (g2d.hit(missileShape,baddy,true))
    {
        
        pxs.numberOfHits++ ;
        explosionSound.play() ;
        pxs.missiles.remove(i) ;
        return true ;                
    }
    
    }
    
    return false ;
    
}

private void displayPowerUp(Graphics2D g2d) {

// powerUps appear every 1000 frames
    if (countDown % 600 == 0)
    {
        // can be of one of 3 types (see PowerUp class)
        int type = 1 + random.nextInt(3) ; 
        pw = new PowerUp(type,width,height) ;
      }

    if (pw != null)
    {
        Image powerUpPic ;

    switch (pw.getType()) 
    {
        case 1:
        powerUpPic = lifeUpPic ;
        break ;
        
        case 2:
        powerUpPic = weaponUpPic ;
        break ;
        
        default:
        powerUpPic = invincibilityPic ;
        break ;
    }
        
        g2d.drawImage(powerUpPic,pw.x,pw.y,this) ;

    // increment the power-up's position
    pw.move() ;

    // if you ship hits the powerUp then you're poweredUP
    if (g2d.hit(new Rectangle(pw.x,pw.y,40,40),spaceShip,true)||(pw.x<0)||(pw.y<0)||(pw.y>height))
    {
    timer3 = 0 ;
    
    if (g2d.hit(new Rectangle(pw.x,pw.y,40,40),spaceShip,true))
    pw.poweredUp(pxs) ;
    
    
    // if the powerUp is not visible anymore (ie out of the clipping area)
    // then it disapears
    pw = null ;
    }

    
    }


}


private void displayEnnemyMissiles(Graphics2D g2d,Ennemy ennemy) {

    for (int j=0; j<ennemy.missiles.size(); j++)
    {
     
     Missile m = (Missile) ennemy.missiles.get(j) ;
     m.moveLeft() ;
     
     Rectangle missile = new Rectangle(m.x,m.y,20,2) ;
     
   if (g2d.hit(missile,spaceShip,false))
    {
    explosionSound.play() ;

    if (!pxs.isInvincible(timer3))
    {
        pxs.life = pxs.life - 10 ;
        g2d.fillOval(pxs.x,pxs.y,pxs.width,pxs.height) ;
    }
    
    // if collision occurs remove missile
    ennemy.missiles.remove(j) ;    
    }
    else
    {
     if (m.x<=0)
     // if the missile is of the screen then remove missile
     ennemy.missiles.remove(j) ;
     else
     {
     g2d.drawImage(shipPic,pxs.x,pxs.y,pxs.width,pxs.height,this) ;          
     
     g2d.fill(missile) ;
     }
     }
 
 }

}

private void displayBossMissiles(Graphics2D g2d) {

    for (int i=0; i<boss.missiles.size(); i++)
    {
      Missile m = (Missile)boss.missiles.get(i) ;
      
      Line2D missilepic = new Line2D.Double(m.x,m.y,m.x-20,m.y) ;
      
      Rectangle missile = new Rectangle((int)m.x,(int)m.y,20,2) ;  
       
       if (g2d.hit(missile,spaceShip,false))
       {           
           explosionSound.play() ;
           
           if (!pxs.isInvincible(timer3))
           {
           pxs.life = pxs.life - 10 ; 
           g2d.fillOval(pxs.x,pxs.y,pxs.width,pxs.height) ; 
           }          
           
           boss.missiles.remove(i) ;
                     
                     }
           else
           {        
           if (m.x <= 0)
           boss.missiles.remove(i) ;
           else
           {
           g2d.drawImage(shipPic,pxs.x,pxs.y,pxs.width,pxs.height,this) ; 
           
           g2d.setStroke(new BasicStroke(2)) ;
           g2d.draw(missilepic) ;
           }
 
        }
        m.moveLeft() ; // ennemy missiles position is incremented (see Missile class)
        }
}



private void displayInfo(Graphics2D g2d) {

    Font font = new Font("Helvetica Bold",1,20) ;
    g2d.setFont(font) ;
    g2d.setColor(Color.yellow) ;
    
    if (!animation.isRunning())
    g2d.drawString("PAUSE",width/2-30,height/2) ;
    
    
    g2d.drawString("Speed: " + speedMeter,20,30) ;

    // display ship's life:
    g2d.setColor(Color.red) ;
    g2d.fillRect(width/2-80,10,100,20) ;
    g2d.setColor(Color.green) ;
    g2d.fillRect(width/2-80,10,pxs.life,20) ;
    g2d.drawString("Life: "+pxs.life+"%",width/2+30,30) ;

    // display invincibility countdown
    if (pxs.isInvincible(timer3))
    g2d.drawString((500-timer3)+"",width-50,30) ;


    g2d.drawString("Score: " + score + " / " + scoreObjective,20,height-60) ;

    // indicates speed at which ship's laser can fire:
    g2d.setColor(Color.red) ;
    g2d.fillRect(250,height-80,100,20) ;
    g2d.setColor(Color.blue) ;
    g2d.fillRect(250,height-80,100-pxs.heat*(100/pxs.heatCost),20) ;
    g2d.drawString(" "+pxs.specialMissiles,380,height-60) ;
    
    if (timeLimit == true)
    g2d.drawString("Time: " + countDown,500,height-60) ; 

    // display boss life    
    if (score >= scoreObjective)
    {
       g2d.setColor(Color.red) ;
       g2d.drawString(boss.life+"%",width-50,height/2-100) ;
   }
    
    
    if (timer <= 200)
    {
        g2d.setColor(Color.red) ;
        g2d.drawString("Level: " + level,50,100);
        g2d.drawString(message,20,150);
    }
    
    if ((pxs.life<=0)||((countDown <= 0)&&(timeLimit==true)))
     {
     
     g2d.setColor(Color.yellow) ;
     g2d.drawImage(explosionPic,pxs.x-10,pxs.y-10,pxs.width+30,pxs.height+30,this) ;
     
     g2d.drawString(message4,5,height/2) ; 
     
      
      specialSoundTrack.stop() ;
      soundTrack.stop() ;
      animation.stop() ;
     
     
        new Score(playerName,score + 10*100*pxs.numberOfHits/pxs.numberOfMissilesFired).save() ;
        pxs.dead = true ;
      }
      
      if (score >= scoreObjective)
      {
    timer2++ ;
    if (timer2 < 100)
    {
    g2d.setColor(Color.yellow) ;
    g2d.drawString(message2,100,height/2) ;  
    }
    
    // defeat boss to proceed to next level
    if (boss.life >= 0)
        {
            showBoss = true ;
            if (playing == false)
            {
            soundTrack.stop();
            specialSoundTrack.loop();
            playing = true ;
            }
        }
                else if (pxs.life > 0)
                {                   
                    showBoss = false ;
                    playing = false ;
                    specialSoundTrack.stop();
                    soundTrack.loop();
                    g2d.drawImage(explosionPic,boss.x-10,boss.y-10,boss.width+30,boss.height+30,this) ;
                    g2d.drawString(message3,5,height/2) ; 
                    
                    
                    if (level<5)
                    {
                    level++; 
                    ennemies.clear() ;
                    timer = 0 ;
                    timer2 = 0 ;
                    assignPics() ;
                    load.levelSettings(level,this) ;
                    }
                    else
                    {
                        tsp.addCreditsPanel() ;
                    }
                
                    animation.stop() ;
                  
                
                }
}

    
    }




private void displayReactors(Graphics2D g2d) {

    g2d.setColor(Color.red) ;
    if (moving == true)
    {
    g2d.drawLine(pxs.x,pxs.y,pxs.x-10,pxs.y-10) ;
    g2d.drawLine(pxs.x,pxs.y+40,pxs.x-10,pxs.y+50) ;
    }
    moving = false ;
}



        
    

    

//////////////////////KEY LISTENERS////////////////////////////////////////

// remark: keys aren't responsive enough so I had to make the ship
// movement a bit jerky (ie too big increment at each key press)

public void keyPressed (KeyEvent e) {

keyActions(e) ;

}



public void keyReleased (KeyEvent e) {
// do nothing
}


public void keyTyped (KeyEvent e) {
// goes to score panel if ship is dead
if (!pxs.dead)
keyActions(e) ;
else
tsp.addScorePanel() ;
}


public void keyActions(KeyEvent e) {
   
   
     
 switch (e.getKeyCode()) 
    {
        
        case 80:
        if (animation.isRunning())
        {
        animation.stop() ;
        repaint() ;
        }
        else
        {
        animation.start() ;
        }
        break;
        
        case 67:
        animation.start() ;
        break;
        
        case 27:
        new Score(playerName,score).save() ;
        soundTrack.stop() ;
        specialSoundTrack.stop() ;
        tsp.addMenuPanel() ;
        break;
        
        case 32:                   
        // fire ship's missile 
        if (pxs.heat == 0)
        {
         laserSound.play() ;    
         pxs.fire();
         }       
        break ;
   
        case 37:
        if (pxs.x>insets.left)
        {
        pxs.x = pxs.x - 50 ;
        moving = true ;
        }
        break;
   
        case 38:
        if (pxs.y>insets.top)
        {
        pxs.y = pxs.y - 20 ;
        moving = true ;
        }
        break;
   
        case 39:
        if (pxs.x< width - insets.right-pxs.width)
        {
        pxs.x = pxs.x + 50 ;
        moving = true ;
        }
        break;
   
        case 40:
        if (pxs.y < height - insets.bottom - 2*pxs.height)
        {
        pxs.y = pxs.y + 20 ;
        moving = true ;
        }
        break;
        
        case 88:
        if ((pxs.heat == 0)&&(pxs.specialMissiles>0))
            {
            special = true ;
            laserSound.play() ;    
            pxs.specialFire();
            }            
        break;
   
        case 90:
        if (speedMeter == 1)
        {
        speedMeter ++ ;
        speed = speed + 10 ;       
        }
        else
        {
        speedMeter -- ;
        speed = speed - 10 ;
        }        
        break;
                
    }
    
}

}
