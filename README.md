# Train Invaders

Controls:
```
  A/Left Arrow Key - Move Left
  D/Right Arrow Key - Move Right
  Space - Action/Shoot 
```

Idea Summary: 
Train Invaders is a Space Invader like game with a twist of using a 2.5D perspective. Rather than 
the enemies slowly flying down from the top of the screen to the bottom now they do that but 
with the illusion that they're actually in a 3D space, achieved by them walking towards the bottom 
of the screen and becoming bigger the closer to the player they are. With the enemies
being of different size and becoming larger as the come closer to the bottom of the screen I got 
the 2.5D perspective that offers something new to the game style. 

The theme used western with a carnival arcade game gimmick. I wanted to emulate the siding of the 
enemies and the player by making it like shooting range games at the carnival arcade. The western
theme came from the idea of using the train as the player character, giving a reason why they would 
only move left and right, sadly that didn't pan out due to the size of the train. So the train 
was replaced by a sheriff that's pretty much just a bandit facing away from the player. The 
train code was already done so it just became part of the menu screen as fun toy for the player to 
mess around with. 

Code Breakdown: 
The folder contains four folders called android, desktop, ios and html they're just used to interface
with their respective platforms. The main code is inside the core folder. Which breaks into four 
important folders. 
    1) Main this holds the file from which the program starts 
    2) Screen holds all the screens that the player interacts with, it breaks down into four screens
        1) Loading - First screen player sees and the one that loads the tiled data and music 
        2) Menu - Where the player first interact with the game - only leads to the game screen 
        3) Main - Where the space invader gameplay occurs 
        4) Credits - A scroll of everyone's work, me and crediting music and SFX I used 
        5) A folder full of textures, these files hold initialization for each of the screens textures 
           Mostly used to clean up the main files 
    3) Objects - Object break down it three categories that go further down
        1) Generic Object - holds a hit box and info to modify it, base of all objects 
            1)Static Object - is a child of generic but with a texture such object are the bullet 
                and a train who have textures but aren't very animated 
            2)Animated Object - objects that use a sprite sheet to create an animation such as the 
                bandit and the player.
    4) Tools - tools are extra classes that hold useful code that can be used anywhere, it breaks down
        into four different tools.
        1) Debug Rendering - sets up the drawing objects that will be used for debugging hit boxes 
        2) Music Controls - lets you play sound effects and music 
        3) Text Alignment - center the text  
        4) Tiled - Used to import data from tiled, a software that allows me to place objects 
                and give them variables. Really useful for creating different formations 
                and filling the enemies with data without having to create them by hand. 
