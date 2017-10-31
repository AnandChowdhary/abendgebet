import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class FinalProject extends PApplet {

/*
  Main Tab
  --------
*/

// Import sound library for background music

SoundFile bgMusic;

// Initial speed and score of game
float speed = 5;
int scoreCount = 0;
int highscore = 0;
int gameRunning = 0, gameStarted = 0;

// Objects of classes
Player deer;
Obstacle painting;
Background bg;
Fireworks[] fireworks = new Fireworks[3];

public void setup() {

  

  // Initialize background music
  bgMusic = new SoundFile(this, "backgroundmusic.mp3");

  // Initialize all objects
  deer = new Player();
  painting = new Obstacle();
  bg = new Background();
  for (int i = 0; i < 3; i++) {
    fireworks[i] = new Fireworks();
  }

  // Check if highscore file exists
  File f = new File(sketchPath("highscore.txt"));
  if (f.exists()) {
    // If it does, load the highscore
    String[] lines = loadStrings("highscore.txt");
    highscore = PApplet.parseInt(lines[0]);
  }

}
public void draw() {

  // If game is not running, stop looping
  if (gameRunning == 0) noLoop();

  // Display the objects
  bg.display();
  deer.display();
  painting.display();

  // Move the obstacles
  painting.move();

  // Score
  fill(255);
  textSize(24);
  text(Integer.toString(highscore) + "   " + Integer.toString(scoreCount), 35, 60);
  scoreCount++;

  // Explode the fireworks
  fireworks[0].explode(width / 2 - 250, -200);
  fireworks[1].explode(width / 2 - 450, -150);
  fireworks[2].explode(width / 2 + 50, -100);

  // Initial screen when starting the game
  if (gameStarted == 0) {
      fill(0, 0, 0, 95);
      rect(0, 0, width, height);
      fill(255);
      textSize(48);
      text("Welcome", 250, 200);
      textSize(24);
      text("Press spacebar to start/jump", 250, 250);
      gameStarted = 1;
  }

}

public void keyPressed() {

  // If pressing the spacebar
  if (key == ' ') {

    if (gameRunning == 1) {

      // Make the deer jump if the game has started
      deer.jump();

    } else {

      // Otherwise start/restart the game
      bgMusic.stop();
      bgMusic.play();

      // Reset all objects
      deer.reset();
      painting.reset();
      fireworks[0].reset();
      fireworks[1].reset();
      fireworks[2].reset();

      // Start looping and reset variables
      loop();
      speed = 5;
      gameRunning = 1;
      scoreCount = 0;

      // Load highscore if exists
      File f = new File(sketchPath("highscore.txt"));
      if (f.exists()) {
        String[] lines = loadStrings("highscore.txt");
        highscore = PApplet.parseInt(lines[0]);
      } else {
        highscore = 0;
      }

    }

  }

}
/*
  Background Class
  ----------------
*/

class Background {

  PImage bg1, bg2, leftBg, grass1, grass2;
  int xPos = 533, grassPos = 0;

  // Load all background images
  Background() {
    leftBg = loadImage("left1.png");
    bg1 = loadImage("bg.png");
    bg2 = loadImage("bg.png");
    grass1 = loadImage("grass.png");
    grass2 = loadImage("grass.png");
  }
  
  public void display() {

    // Only show the left image the first time
    if (xPos > 0) {
      image(leftBg, xPos - 533, 0);
    }

    // Display all background images
    image(bg1, xPos, 0);
    image(bg2, xPos + 1600, 0);
    image(grass1, grassPos, height - 227);
    image(grass2, grassPos + 1600, height - 227);

    // Translate the background left
    xPos -= speed / 2;
    grassPos -= speed;

    // Loop the backgrounds if they're out of the frame
    if (xPos < -1600) {
      xPos = 0;
    }
    if (grassPos < -1600) {
      grassPos = 0;
    }

  }

}
/*
  Fireworks Class
  ---------------
*/

class Fireworks {

  PImage[] fireworks = new PImage[45];
  float xPos, yPos;
  int currFrame = 0;
  int done = 0;

  // Load sprites and initialize positions
  Fireworks() {
    for (int i = 0; i < 45; i++) {
      String n = i < 10 ? "0" + i : Integer.toString(i);
      fireworks[i] = loadImage("frame_" + n + "_delay-0.06s.png");
    }
    xPos = 0;
    yPos = 0;
  }

  // Get positions from parameters
  public void explode(float xPos, float yPos) {

    // Start showing animation when highscore is beat
    if (currFrame == 0 && scoreCount > highscore && done == 0) {
      currFrame = frameCount;
    }

    // Display images for 80 frames
    if (frameCount < currFrame + 80 && currFrame > 0) {
      image(fireworks[frameCount % 44], xPos, yPos);
      done = 1;
    } else {
      // Then stop displaying images
      currFrame = 0;
    }

  }

  // Reset variables when restarting game
  public void reset() {
    done = 0;
    currFrame = 0;
  }

}
/*
  Obstacle Class
  --------------
*/

class Obstacle {

  float xPos, yPos, obstacleWidth, obstacleHeight, padding;
  int count;
  PImage[] paintings = new PImage[15];

  // Initialize positions, sizes, and load images 
  Obstacle() {
    padding = 125;
    obstacleWidth = 125;
    obstacleHeight = 150;
    xPos = width - padding;
    yPos = height - padding - obstacleHeight;
    count = 0;
    for (int i = 0; i < 15; i++) {
      paintings[i] = loadImage("painting" + (i + 1) + ".png");
    }
  }

  // Display the painting from array[count]
  public void display() {
    image(paintings[count], xPos, yPos);
  }
  
  
  public void move() {

    // When obstacle goes out of the frame
    if (xPos < -1 * obstacleWidth) {
      if (count < 14) {
        // Increment the counter if more paintings
        count++;
      } else {
        // Otherwise go back to the first painting
        count = 0;
      }

      // Randomize the position of the obstacle 
      xPos = width * random(1.0f, 2);
      if (speed < 25) {
        // And increase the speed of the game
        speed *= 1.05f;
      }
    }

    // Collision logic, check for coordinates (10% allowed)
    // We use deer.getDimensions() function to get data from `player` to `obstacle`
    if (yPos < deer.getDimensions().y * 0.9f && xPos < deer.getDimensions().x * 0.9f && xPos > deer.getDimensions().x - 200) {

      // Check if highscore was beat
      if (scoreCount > highscore) {
        String words = Integer.toString(scoreCount);
        String[] list = split(words, ' ');
        // Save new highscore
        saveStrings("highscore.txt", list);
      }

      // Show game ended screen
      fill(0, 0, 0, 95);
      rect(0, 0, width, height);
      fill(255);
      textSize(48);
      text("Your Score: " + scoreCount, 250, 200);
      textSize(24);
      text("Press spacebar to play again", 250, 250);

      // Stop looping the game
      gameRunning = 0;

    } else {
      // If no collision, continue going left
      xPos -= speed;
    }

  }

  // Get the size of the obstacle for `player`
  public float getHeight() {
    return obstacleHeight;
  }
  public float getWidth() {
    return obstacleWidth;
  }

  // Reset variables when restarting game
  public void reset() {
    xPos = width;
    yPos = height - padding - obstacleHeight;
  }

}
/*
  Player Class
  ------------
*/

class Player {

  float xPos, yPos, playerWidth, playerHeight, bottomPadding, initPos;
  String moving = "none";
  PImage up, down;
  int frame = 0;

  // Array of image URLs for animation sprite
  String[] imageUrls = { "1.png", "2.png", "3.png", "4.png", "3.png", "2.png", "1.png" };
  PImage[] images = new PImage[imageUrls.length];

  // Initialize positions and sizes, load and resize images
  Player() {
    bottomPadding = 125;
    playerWidth = 250;
    playerHeight = 200;
    initPos = height - bottomPadding - playerHeight;
    xPos = 150;
    yPos = initPos;
    for (int i = 0; i < images.length; i++) {
      images[i] = loadImage(imageUrls[i]);
      images[i].resize(PApplet.parseInt(playerWidth), PApplet.parseInt(playerHeight));
    }
    up = loadImage("jump.png");
    up.resize(PApplet.parseInt(playerWidth), PApplet.parseInt(playerHeight));
    down = loadImage("down.png");
    down.resize(PApplet.parseInt(playerWidth), PApplet.parseInt(playerHeight));
  }
   
  public void display() {

    // Change animation sprite every `N` frames
    int everyNFrames = 3;
    if (speed < 10) everyNFrames = 5;

    // Show the next sprite
    if (frameCount % everyNFrames == 0) {
      frame = (frame + 1) % images.length;
    }

    // If you've touched the top, start going down
    if (yPos < initPos - (painting.getHeight() + playerHeight) * 0.9f) {
      moving = "down";
    }

    if (moving == "up") {

      // Change image sprite to `up` when jumping
      image(up, xPos, yPos);

      // And start moving up
      yPos -= speed * 1.1f;

    } else if (moving == "down") {

      // Change image sprite to `down` when going down
      image(down, xPos, yPos);

      // And start descending
      yPos += speed * 1.1f;

      // Go back to original position
      if (yPos > initPos) {
        yPos = initPos;
        moving = "none";
      }

    } else {

      // Show the animation
      image(images[frame], xPos, yPos);

    }

  }

  // Return dimensions of `player` for `obstacle`
  public PVector getDimensions() {
    return new PVector(xPos + playerWidth, yPos + playerHeight);
  }

  // Start jumping up
  public void jump() {
     if (moving == "none") {
       moving = "up";
     }
  }

  // Reset variables when restarting game
  public void reset() {
    xPos = 150;
    yPos = initPos;
  }

}
  public void settings() {  size(960, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "FinalProject" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
