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
      images[i].resize(int(playerWidth), int(playerHeight));
    }
    up = loadImage("jump.png");
    up.resize(int(playerWidth), int(playerHeight));
    down = loadImage("down.png");
    down.resize(int(playerWidth), int(playerHeight));
  }
   
  void display() {

    // Change animation sprite every `N` frames
    int everyNFrames = 3;
    if (speed < 10) everyNFrames = 5;

    // Show the next sprite
    if (frameCount % everyNFrames == 0) {
      frame = (frame + 1) % images.length;
    }

    // If you've touched the top, start going down
    if (yPos < initPos - (painting.getHeight() + playerHeight) * 0.9) {
      moving = "down";
    }

    if (moving == "up") {

      // Change image sprite to `up` when jumping
      image(up, xPos, yPos);

      // And start moving up
      yPos -= speed * 1.1;

    } else if (moving == "down") {

      // Change image sprite to `down` when going down
      image(down, xPos, yPos);

      // And start descending
      yPos += speed * 1.1;

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
  PVector getDimensions() {
    return new PVector(xPos + playerWidth, yPos + playerHeight);
  }

  // Start jumping up
  void jump() {
     if (moving == "none") {
       moving = "up";
     }
  }

  // Reset variables when restarting game
  void reset() {
    xPos = 150;
    yPos = initPos;
  }

}