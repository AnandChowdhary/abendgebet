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
  void display() {
    image(paintings[count], xPos, yPos);
  }
  
  
  void move() {

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
      xPos = width * random(1.0, 2);
      if (speed < 25) {
        // And increase the speed of the game
        speed *= 1.05;
      }
    }

    // Collision logic, check for coordinates (10% allowed)
    // We use deer.getDimensions() function to get data from `player` to `obstacle`
    if (yPos < deer.getDimensions().y * 0.9 && xPos < deer.getDimensions().x * 0.9 && xPos > deer.getDimensions().x - 200) {

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
  float getHeight() {
    return obstacleHeight;
  }
  float getWidth() {
    return obstacleWidth;
  }

  // Reset variables when restarting game
  void reset() {
    xPos = width;
    yPos = height - padding - obstacleHeight;
  }

}