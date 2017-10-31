/*
  Main Tab
  --------
*/

// Import sound library for background music
import processing.sound.*;
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

void setup() {

  size(960, 600);

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
    highscore = int(lines[0]);
  }

}
void draw() {

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

void keyPressed() {

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
        highscore = int(lines[0]);
      } else {
        highscore = 0;
      }

    }

  }

}