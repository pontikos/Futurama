

import java.applet.* ;
import java.net.* ;
import java.io.* ;
import java.awt.* ;
import java.util.* ;

/* This class handles all operations which involve reading from a file. It provides instance 
 * methods for getting resources such as images and sounds and returns them in the form 
 * of an array. For images it actually loads them using MediaTracker before returning the
 * array so that they can be drawn directly to the JPanel without any delay.
 * The levelSettings method is called by GamePanel at the beginning of each level to read
 * the values to which the GamePanel variables are to be initialized 
 * The class also offers a simple instance method for reading all strings from a text 
 * file and returning an arrayList containing all strings.
 * 
 * These are the public methods that this class contains:
 * 
 * levelSettings
 * getPictures
 * getSound
 * getStrings
 * 
 * */

public class Load {


 
// sets the variables for the current level
public void levelSettings(int level,GamePanel gamePanel) {
     
    BufferedReader bfr ;

    try 
    {  
    File file = new File("Data/" + "level" + new Integer(level).toString() + ".text") ;
    bfr = openFile(file)  ;
    }
      catch (IOException ioe)
    {
        System.err.println(ioe); 
        return ;
        }    
   
   try 
   {
   readLevelFile(bfr,gamePanel) ;
   }
        catch (IOException ioe)
        {
            System.err.println(ioe);
            return ;
        }
   }
    
// reads all picture paths, finds the pictures and puts them in the pictures array but 
// only loads and returns those specified by the index 
public Image[] getPictures(javax.swing.JPanel panel,int startIndex,int endIndex) {

    BufferedReader bfr ;
// 5 levels + 5 ennemies + 5 bosses + 3 space ships + 1 explosion + 3 power Ups + 4 menus = 26    
    Image[] allPictures = new Image[26] ;
    
    try 
    {  
    File file = new File("Data/pictures.text") ;
    bfr = openFile(file)  ;
    }
      catch (IOException ioe)
    {
        System.err.println(ioe); 
        return allPictures;
        }    
 
 
      try 
      {
          allPictures = readPictureFile(bfr,allPictures) ;
      }
      catch (IOException ioe) 
      {
          System.err.println(ioe) ;
          return allPictures;
      }
      
      Image[] pictures = loadPictures(panel,allPictures,startIndex,endIndex) ;
      return pictures ;
  }
           

// returns an array containing all audio clips read from the file
public AudioClip[] getSounds() {

    BufferedReader bfr ;
    AudioClip[] sounds = new AudioClip[5] ;

    try 
    {  
    File file = new File("Data/sounds.text") ;
    bfr = openFile(file)  ;
    }
      catch (IOException ioe)
    {
        System.err.println(ioe); 
        return sounds;
        }    
 
 
      try 
      {
          sounds = readSoundFile(bfr,sounds) ;
      }
      catch (IOException ioe) 
      {
          System.err.println(ioe) ;
          return sounds;
      }

    return sounds ;
}
    

// return all strings read from the file
public ArrayList getStrings(String fileName) {

    BufferedReader bfr ;
    ArrayList strings = new ArrayList() ;

    try 
    {  
    File file = new File(fileName) ;
    bfr = openFile(file)  ;
    }
      catch (IOException ioe)
    {
        System.err.println(ioe); 
        return strings;
        }    
 
 
      try 
      {
          strings = readStrings(bfr,strings) ;
      }
      catch (IOException ioe) 
      {
          System.err.println(ioe) ;
          return strings;
      }
      
      return strings ;
      
  }
  

public ArrayList getScores(String fileName) {

    ArrayList scores = new ArrayList() ;
  
    ArrayList strings = getStrings(fileName) ;
    
    
    for (ListIterator li = strings.listIterator(); li.hasNext();)
    {
        String nameOfPlayer = (String)li.next() ;
        
        int n ;
        try 
        {
        n = Integer.parseInt((String)li.next()) ;
        }
        catch (NumberFormatException nfe)
        {
         n = -1 ;
         }
         
        scores.add(new Score(nameOfPlayer,n)) ;
    }
      
      
      return scores ;

}


private void readLevelFile(BufferedReader bfr,GamePanel gamePanel) throws IOException {
                              
        bfr.readLine() ; // read header
        try 
        {
        gamePanel.numberOfEnnemies = Integer.parseInt(bfr.readLine()) ; 
        
        bfr.readLine() ; // read header
        Ennemy.width = Integer.parseInt(bfr.readLine()) ;   
        
        bfr.readLine() ; // read header
        Ennemy.height = Integer.parseInt(bfr.readLine()) ;   
        }
        catch (NumberFormatException nfe) 
        {     
            System.err.println(nfe) ;
            }
        
        bfr.readLine() ; // read header
        Ennemy.shoots = (boolean) new Boolean(bfr.readLine()).booleanValue() ;
        
        bfr.readLine() ; // read header
        
        gamePanel.message = bfr.readLine() ;
        gamePanel.message2 = bfr.readLine() ;    
        gamePanel.message3 = bfr.readLine() ;                   
        gamePanel.message4 = bfr.readLine() ;
        
        
        int bossX ;
        int bossY ;
        int bossWidth ;
        int bossHeight ;
        int bossLife ;
        
        
        bfr.readLine() ; // read header
        try {
        bossX = Integer.parseInt(bfr.readLine()) ;   
        
        bfr.readLine() ; // read header
        bossY = Integer.parseInt(bfr.readLine()) ;   
        
        bfr.readLine() ; // read header
        bossWidth = Integer.parseInt(bfr.readLine()) ;  
        
        bfr.readLine() ; // read header
        bossHeight = Integer.parseInt(bfr.readLine()) ;  
        
        bfr.readLine() ; // read header
        bossLife = Integer.parseInt(bfr.readLine()) ;   
        
        }
        catch (NumberFormatException nfe) 
        {   
            System.err.println("Error in file, default values have been set!") ;
            
            bossX = gamePanel.width - 50 ;
            bossY = gamePanel.height/2 ;
            bossWidth = 100 ;
            bossHeight = 50 ;
            bossLife = 20 ;
            }
        
        bfr.readLine() ; // read header
        boolean shoots = (boolean) new Boolean(bfr.readLine()).booleanValue() ; 
       
                
        gamePanel.boss = new Boss(bossX, bossY, bossWidth, bossHeight, bossLife, shoots ) ;
       
       bfr.readLine() ; // read header
       try 
       {       
       gamePanel.scoreObjective = Integer.parseInt(bfr.readLine()) ;
       bfr.readLine() ; // read header
       gamePanel.countDown = Integer.parseInt(bfr.readLine()) ;
       }
       catch (NumberFormatException nfe)
       {
         System.err.println(nfe) ;  
       }
   
       
       bfr.close() ; 
       
   }


// reads the path of the pictures that are to be used, finds them
// and stores them in an array
private Image[] readPictureFile(BufferedReader bfr,Image[] pictures) throws IOException {

    Toolkit toolkit = Toolkit.getDefaultToolkit() ;
        
        bfr.readLine() ; // read header of background gifs
        
        for (int i=0; i<5; i++)
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ; // line 1 to 5
        }

        bfr.readLine() ;  // read header of ennemies gifs    
        
        for (int i=5; i<10; i++) 
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ;
        }
        
        bfr.readLine() ; // read header of bosses gifs
        
        for (int i=10; i<15; i++)
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ;
        }

        bfr.readLine() ; // read header of space ship gifs
        
        for (int i=15; i<18; i++)
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ;
        }
        
        
        bfr.readLine(); // read header of explosion gif
        
            pictures[18] = toolkit.getImage(bfr.readLine()) ;
        
        bfr.readLine(); // read header for power ups
        
        for (int i=19; i<22; i++)
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ;
        }
        
        bfr.readLine(); // read header for menu pictures
        
        for (int i=22; i<26; i++)
        {
            pictures[i] = toolkit.getImage(bfr.readLine()) ;
        }
        
        bfr.close() ; // close input stream
        
        return pictures ;
}




// reads the path of the audio clips from the relevant file, finds them 
// and stores them in an array
private AudioClip[] readSoundFile(BufferedReader bfr,AudioClip[] sounds) throws IOException {
    
    for (int i=0; i<5; i++)
    {
        
    try
    {
    sounds[i] = Applet.newAudioClip(new URL("file:" + System.getProperty("user.dir") +
      System.getProperty("file.separator") + "Sounds" + System.getProperty("file.separator") +  bfr.readLine())) ; 

    }
    catch (MalformedURLException e) 
    {
      System.err.println(e);
        }
    
    }

    return sounds ;

}

private ArrayList readStrings(BufferedReader bfr,ArrayList strings) throws IOException {

    String s;
    
    while ((s = bfr.readLine()) != null)
    {
        strings.add(s) ;
    }

    return strings ;
}


// This method tries to open the specified file and returns the character stream
// for that file. To allow quicker and more efficient reading from the file, the
// stream is "wrapped" by a buffered reader, allowing a whole line to be read at a time.
private BufferedReader openFile(File file) throws IOException {
               
     FileReader fr = new FileReader(file) ;
     BufferedReader bfr = new BufferedReader(fr) ; 
       
       return bfr;
       }



// the MediaTracker is very useful for loading pictures before drawing them
private Image[] loadPictures(javax.swing.JPanel panel,Image[] allPictures,int startIndex,int endIndex) {

// this is the array that is to be returned
Image[] pictures = new Image[endIndex-startIndex] ;

MediaTracker pictureTracker = new MediaTracker(panel) ;

/* The MediaTracker keeps track of the pictures and gives them a priority index.
 * The lower the index the higher the priority. In this case all pictures have the
 * same priority.
 * At the same time we add the wanted pictures to the MediaTracker we also fill the new
 * array which contains only the needed pictures. After the pictures have been added,
 * the following method waits for all pictures to be loaded.
 * Finally the array with the wanted pictures (which are now loaded and ready to 
 * be drawn without any delay) is returned.
 * */
int j=0 ;    
    for (int i=startIndex; i<endIndex; i++) 
    {                
        pictures[j] = allPictures[i] ;
        pictureTracker.addImage(allPictures[i],0) ;
        j++ ;
    }

waitForPictures(pictureTracker) ;

return pictures ;
}




// this is a very useful method which allows all pictures to be loaded simultaneously
// so that they can be all be drawn at the same time and without any delay
private void waitForPictures(MediaTracker pictureTracker) {

    try 
    {
        pictureTracker.waitForAll() ;
        }
        catch (InterruptedException ie)
        {
            System.err.println(pictureTracker.isErrorAny()) ;
            return ;
            }

}

}
