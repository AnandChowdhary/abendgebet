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
  void explode(float xPos, float yPos) {

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
  void reset() {
    done = 0;
    currFrame = 0;
  }

}