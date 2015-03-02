


import java.io.* ;
import java.util.* ;

/** A score object is created by GamePanel then the object's save method is called which
 *  reads all scores from the file, sorts them in reverse order, then writes them back to
 *  the file. Then all the ScorePanel class has to do is read the scores from the file and
 *  call their toString method which I have over-ridden (see method).
 * */



public class Score {

private String playerName ;
private int number ;

private Load load = new Load() ;

private ArrayList scores ;

public Score(String playerName,int number) {

    this.playerName = playerName ;
    this.number = number ;
}   




private void sortScores() {
    
    this.scores = load.getScores("Data/scores.text") ;
    scores.add(this) ;
     
     
     // sort scores by score number ( reverse bubble sort) before writing them
     // to the file
     
     boolean sorted = false ;
    
    while ((sorted == false)&&(scores.size()>1))
    {
        sorted = true ;
        
        for (int i=0; i<scores.size()-1; i++)
        {        
        if (((Score)scores.get(i)).number < ((Score)scores.get(i+1)).number)
        {
            Score temp = (Score)scores.get(i) ;
            
            scores.set(i,(Score)scores.get(i+1)) ;
            scores.set(i+1,temp) ;
            
            sorted = false ;
        }
        
        }
        
    }
    
   
    
}



public void save() {
    
    sortScores() ;
    
    BufferedWriter bfw ;
    
    File file = new File("Data/scores.text") ;
    
    
    try 
    {
     file.createNewFile() ;
     bfw = openFile(file) ;     
     }
     catch (IOException ioe)
     {
     System.err.println(ioe) ;
     return ;
     }
     
    try
    {
    writeFile(bfw) ; 
    }
    catch(IOException ioe)
    {
     System.err.println(ioe) ;
     return ;
    }
        
}

private BufferedWriter openFile(File file) throws IOException {

return new BufferedWriter(new FileWriter(file,false)) ;
}


private void writeFile(BufferedWriter bfw) throws IOException {
    
    for (ListIterator li = scores.listIterator(); li.hasNext();)
    {
       Score score = (Score)li.next() ;
       
       bfw.write(score.playerName) ;
       bfw.newLine() ;
       
       bfw.write(score.number+"") ;
       bfw.newLine() ;      
    }
    
    bfw.close() ;
}


public String toString() {

return (this.number + " " + this.playerName) ;
}



}
