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
  
  void display() {

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