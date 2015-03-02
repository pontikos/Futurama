
 NAME OF PROJECT: THE SPACE PILOT 3000
 TYPE: GAME

 AUTHOR: NIKOLAS PONTIKOS

 
One of the key issues of this project was the choice between good
object-oriented design and performance
When it came to displaying the sprites at first each sprite had its own
getImage method which would return the sprites picture. Although this was
good in terms of object-oriented design it had the effect of slowing down
the performance (especially on slower machines).
Also instead of having getX and getY methods for each sprite I chose to
make the x and y variables public and to access them directly. I am aware
that it is not considered good practice but the end result is ultimately
the same and it greatly simplifies the code.
I have taken precautions to try and limit the number of public and static
variables
The main concern of this mini-project was 
to marry good performance with good object
oriented design.