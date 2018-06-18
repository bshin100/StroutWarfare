package bshin100.swgame;

import bshin100.swgame.gfx.AnimatedSprite;
import bshin100.swgame.gfx.Sprite;
import bshin100.swgame.input.Keyboard;
import bshin100.swgame.input.Mouse;
import bshin100.swgame.util.Direction;
import bshin100.swgame.util.SoundManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

//TODO: collision w "solid" objects
/**
 * APCS Project - "Strout Warfare"
 * Main running class with game rendering and logic.
 * Created on 5/25/2018.
 * @author Brian Shin
 */
public class Game implements Runnable {

    public static int WIDTH = 1000;
    public static int HEIGHT = 700;
    private final int MOUSE_DELAY = 5; // Play around with this, probably between 4-6. Dirty code for mouse debounce.
    private boolean startScreen = true;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private Keyboard keyboard;
    private Mouse mouse;

    private SoundManager soundManager;
    private HashMap<String, Integer> sounds;

    /**
     * Initialize application.
     */
    public Game() {
        frame = new JFrame("Strout Warfare by Brian Shin");

        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        keyboard = new Keyboard();
        mouse = new Mouse();
        canvas.addKeyListener(keyboard);
        canvas.addMouseListener(mouse);

        canvas.setFocusable(true);
        /*canvas.addKeyListener(new KeyAdapter() { // TESTING
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                //System.out.println("test");
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_ENTER) { startScreen = false; }
                if(key == KeyEvent.VK_F) { player1.shoot(); }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                System.out.println("something");
            }
        });*/

        soundManager = new SoundManager();
        sounds = new HashMap<String, Integer>();

        addSound("music");
        addSound("victory");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        canvas.requestFocus();
    }

    private boolean hasSelectionBeenMade = false;
    private Clip music;
    private Clip victory;

    private Tank player1;
    private Tank player2;

    private Sprite background, fgObj1, fgObj2, fgObj3;
    private Sprite deathScreen;
    private static Sprite[] collidableObjects;

    private BufferedImage[] player1SelectedTank;
    private BufferedImage[] player2SelectedTank;
    private BufferedImage[] maps, map1Obj, tank1Low, tank2Low, tank3Low, tank4Low, tank1Mid, tank2Mid, tank3Mid, tank4Mid;
    static BufferedImage[] tankExplode;
    private BufferedImage projectileImg;
    private BufferedImage p1WinScreenImg, p2WinScreenImg;

    /**
     * Initialize game variables and load assets.
     */
    private void initGame() {

        try {
            maps = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/grassmap.png")),
                    ImageIO.read(getClass().getResource("/assets/images/forestmap.png")),
                    ImageIO.read(getClass().getResource("/assets/images/snowmap.png")),
            };
            map1Obj = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/grassmap_fountain.png")),
                    ImageIO.read(getClass().getResource("/assets/images/grassmap_wall.png")),
                    ImageIO.read(getClass().getResource("/assets/images/grassmap_wall2.png"))
            };
            tank1Low = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Low/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Low/sprite_1.png"))
            };
            tank2Low = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank2_Low/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank2_Low/sprite_1.png"))
            };
            tank3Low = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank3_Low/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank3_Low/sprite_1.png"))
            };
            tank4Low = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank4_Low/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank4_Low/sprite_1.png"))
            };
            tank1Mid = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Mid/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Mid/sprite_1.png"))
            };
            tank2Mid = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank2_Mid/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank2_Mid/sprite_1.png"))
            };
            tank3Mid = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank3_Mid/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank3_Mid/sprite_1.png"))
            };
            tank4Mid = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank4_Mid/sprite_0.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank4_Mid/sprite_1.png"))
            };
            tankExplode = new BufferedImage[]{
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_00.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_01.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_02.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_03.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_04.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_05.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_06.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_07.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_08.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_09.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_10.png")),
                    ImageIO.read(getClass().getResource("/assets/images/Tank_Explode/sprite_11.png")),
            };

            projectileImg = ImageIO.read(getClass().getResource("/assets/images/shell.png"));
            p1WinScreenImg = ImageIO.read(getClass().getResource("/assets/images/p1win.png"));
            p2WinScreenImg = ImageIO.read(getClass().getResource("/assets/images/p2win.png"));

            deathScreen = new Sprite(p1WinScreenImg, 0, 0);

            player1 = new Tank(tank1Low, 10, 255, 600, keyboard, 1);
            player2 = new Tank(tank2Low, 10, 850, 150, keyboard, 2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int randMap = (int)(Math.random() * 3 + 1);
        loadMap(randMap);

        collidableObjects = new Sprite[]{fgObj1, fgObj2, fgObj3};

        music = playSound("music");
        if (music != null) music.loop(Clip.LOOP_CONTINUOUSLY);
        victory = playSound("victory");
        if (victory != null) victory.stop();

        player1.setCannonDirection(Direction.RIGHT);
        player2.setCannonDirection(Direction.LEFT);
        player2.flipImageY();
    }

    private void loadMap(int mapNum) {
        if(mapNum == 1) {
            background = new Sprite(maps[0], 0, 0);
            fgObj1 = new Sprite(map1Obj[0], 433, 359); // Objects for collision
            fgObj2 = new Sprite(map1Obj[1], 0, 584);
            fgObj3 = new Sprite(map1Obj[2], 660, 102);
        } else if(mapNum == 2) {
            background = new Sprite(maps[1], 0, 0);
            fgObj1 = new Sprite(map1Obj[0], 0, 0); fgObj1.setVisible(false); // No significant objects
            fgObj2 = new Sprite(map1Obj[1], 0, 0); fgObj2.setVisible(false);
            fgObj3 = new Sprite(map1Obj[2], 0, 0); fgObj3.setVisible(false);
        } else if(mapNum == 3) {
            background = new Sprite(maps[2], 0, 0);
            fgObj1 = new Sprite(map1Obj[0], 0, 0); fgObj1.setVisible(false); // No significant objects
            fgObj2 = new Sprite(map1Obj[1], 0, 0); fgObj2.setVisible(false);
            fgObj3 = new Sprite(map1Obj[2], 0, 0); fgObj3.setVisible(false);
        }
    }

    private long desiredFPS = 60;
    private long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    private boolean running = true;

    public void run() {

        long beginLoopTime;
        long endLoopTime;
        long deltaLoop;

        initGame();

        while(running) {
            beginLoopTime = System.nanoTime();

            render();
            update();

            endLoopTime = System.nanoTime();
            deltaLoop = endLoopTime - beginLoopTime;

            if(deltaLoop <= desiredDeltaLoop) {
                try {
                    Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
                } catch(InterruptedException e) {
                    System.err.println("oof");
                }
            }
            //System.out.println(deltaLoop/1000000000.0);
        }
    }

    /**
     * Render game.
     */
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        if(startScreen) {
            renderStartScreen(g);
            if (keyboard.select) {
                startScreen = false;
            }
        } else {
            render(g);
        }
        g.dispose();
        bufferStrategy.show();
    }

    /**
     * Update method; game logic.
     */
    private void update() {
        Direction p1DirNaught = player1.getCannonDirection(); // Dirty throttle system.
        Direction p2DirNaught = player2.getCannonDirection();
        keyboard.update();
        mouse.update();

        if(hasSelectionBeenMade && getTankSelection(1) != null && getTankSelection(2) != null && startScreen) {
            player1.setFrames(getTankSelection(1));
            player2.setFrames(getTankSelection(2));
            player2.flipImageY();
        }

        player1.update();
        player2.update();

        Direction p1Dir = player1.getCannonDirection();
        Direction p2Dir = player2.getCannonDirection();
        if(player1.getCannonDirection() == Direction.RIGHT && !(p1DirNaught == p1Dir)) {
            if(p1SelCount == 1) {
                player1.setFrames(tank1Low);
            } else if(p1SelCount == 2) {
                player1.setFrames(tank2Low);
            } else if(p1SelCount == 3) {
                player1.setFrames(tank3Low);
            } else if(p1SelCount == 4) {
                player1.setFrames(tank4Low);
            }
        } else if(player1.getCannonDirection() == Direction.RIGHT23 && !(p1DirNaught == p1Dir)) {
            if(p1SelCount == 1) {
                player1.setFrames(tank1Mid);
            } else if(p1SelCount == 2) {
                player1.setFrames(tank2Mid);
            } else if(p1SelCount == 3) {
                player1.setFrames(tank3Mid);
            } else if(p1SelCount == 4) {
                player1.setFrames(tank4Mid);
            }
        }

        if(player2.getCannonDirection() == Direction.LEFT && !(p2DirNaught == p2Dir)) {
            if(p2SelCount == 1) {
                player2.setFrames(tank1Low);
                player2.flipImageY();
            } else if(p2SelCount == 2) {
                player2.setFrames(tank2Low);
                player2.flipImageY();
            } else if(p2SelCount == 3) {
                player2.setFrames(tank3Low);
                player2.flipImageY();
            } else if(p2SelCount == 4) {
                player2.setFrames(tank4Low);
                player2.flipImageY();
            }
        } else if(player2.getCannonDirection() == Direction.LEFT23 && !(p2DirNaught == p2Dir)) {
            if(p2SelCount == 1) {
                player2.setFrames(tank1Mid);
                player2.flipImageY();
            } else if(p2SelCount == 2) {
                player2.setFrames(tank2Mid);
                player2.flipImageY();
            } else if(p2SelCount == 3) {
                player2.setFrames(tank3Mid);
                player2.flipImageY();
            } else if(p2SelCount == 4) {
                player2.setFrames(tank4Mid);
                player2.flipImageY();
            }
        }

        for(Projectile projectile : player1.projectiles) {
            projectile.update();
            if(getProjCollision(player2, projectile)) {
                player2.die();
                music.stop();
                if(!victory.isActive()) victory.start();
            }
        }

        for(Projectile projectile : player2.projectiles) {
            projectile.update();
            if(getProjCollision(player1, projectile)) {
                player1.die();
                music.stop();
                if(!victory.isActive()) victory.start();
            }
        }
    }

    /**
     * Main rendering method.
     */
    private void render(Graphics2D g) {
        g.drawImage(background.getImage(), background.getX(), background.getY(), null);
        if(fgObj1.isVisible()) g.drawImage(fgObj1.getImage(), fgObj1.getX(), fgObj1.getY(), null);
        if(fgObj2.isVisible()) g.drawImage(fgObj2.getImage(), fgObj2.getX(), fgObj2.getY(), null);
        if(fgObj3.isVisible()) g.drawImage(fgObj3.getImage(), fgObj3.getX(), fgObj3.getY(), null);
        if(player1.isVisible()) {
            g.drawImage(player1.getSprite(), player1.getX(), player1.getY(), null);
            for(Projectile projectile : player1.projectiles) {
                g.drawImage(projectileImg, projectile.getX(), projectile.getY(), null);
            }
        } else {
            deathScreen.setImage(p2WinScreenImg);
            g.drawImage(deathScreen.getImage(), deathScreen.getX(), deathScreen.getY(), null);
        }
        if(player2.isVisible()) {
            g.drawImage(player2.getSprite(), player2.getX(), player2.getY(), null);
            for(Projectile projectile : player2.projectiles) {
                g.drawImage(flipImageY(projectileImg), projectile.getX(), projectile.getY(), null);
            }
        } else {
            deathScreen.setImage(p1WinScreenImg);
            g.drawImage(deathScreen.getImage(), deathScreen.getX(), deathScreen.getY(), null);
        }
        //Rectangle rect = fgObj1.getObjectBounds();
        //g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());

    }

    private BufferedImage bf1;
    private BufferedImage bf2;
    private int counter = 1;
    private int p1SelCount = 1;
    private int p2SelCount = 2;

    /**
     * Render starting screen.
     * @param g Graphics2D object.
     */
    private void renderStartScreen(Graphics2D g) {
        Font title = new Font(Font.SANS_SERIF, Font.BOLD, 70);
        Font subtitle = new Font(Font.SANS_SERIF, Font.PLAIN, 32);
        Font startmsg = new Font(Font.SANS_SERIF, Font.ITALIC, 24);
        Font credits = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        Font tip = new Font(Font.SANS_SERIF, Font.ITALIC, 18);
        FontRenderContext fontRenderContext = g.getFontRenderContext();

        String titleStr = "Strout Warfare";
        String subTStr = "1v1 Fight to the Death With Tanks!";
        String startStr = "Press ENTER to start game.";
        String creditStr = "Created by Brian Shin";
        String tipStr = "Click to switch colors.";

        g.setColor(Color.black);
        g.setFont(title);
        g.drawString(titleStr, calcCenter(titleStr, title, fontRenderContext), 100);
        g.setFont(subtitle);
        g.drawString(subTStr, calcCenter(subTStr, subtitle, fontRenderContext), 200);
        g.setFont(startmsg);
        g.drawString(startStr, calcCenter(startStr, startmsg, fontRenderContext), 500);
        g.setFont(credits);
        g.drawString(creditStr, calcCenter(creditStr, credits, fontRenderContext), 675);
        g.setFont(tip);
        g.drawString(tipStr, 50, 375);
        g.drawString(tipStr, 775, 375);

        AnimatedSprite tempP1 = new AnimatedSprite(tank1Low, 10, 0, 0);
        AnimatedSprite tempP2 = new AnimatedSprite(tank2Low, 10, 0, 0);

        Rectangle p1Sel = tempP1.getObjectBounds();
        p1Sel.setLocation(100, 300);
        Rectangle p2Sel = tempP2.getObjectBounds();
        p2Sel.setLocation(800, 300);

        // I cried while writing the following code. I'm sorry, Java.
        if(mouse.clicked && p1Sel.contains(mouse.mousePosX, mouse.mousePosY) ) {
            counter++;
            if(counter > MOUSE_DELAY*4) counter = 1;
            if(counter <= MOUSE_DELAY) p1SelCount = 1;
            if(counter > MOUSE_DELAY && counter <= MOUSE_DELAY*2) p1SelCount = 2;
            if(counter > MOUSE_DELAY*2 && counter <= MOUSE_DELAY*3) p1SelCount = 3;
            if(counter > MOUSE_DELAY*3 && counter <= MOUSE_DELAY*4) p1SelCount = 4;
            hasSelectionBeenMade = true;
        }
        if(p1SelCount == 1) { bf1 = tank1Low[0]; player1SelectedTank = tank1Low;
        } else if(p1SelCount == 2) { bf1 = tank2Low[0]; player1SelectedTank = tank2Low;
        } else if(p1SelCount == 3) { bf1 = tank3Low[0]; player1SelectedTank = tank3Low;
        } else { bf1 = tank4Low[0]; player1SelectedTank = tank4Low;}

        if(mouse.clicked && p2Sel.contains(mouse.mousePosX, mouse.mousePosY) ) {
            counter++;
            if(counter > MOUSE_DELAY*4) counter = 1;
            if(counter <= MOUSE_DELAY) p2SelCount = 1;
            if(counter > MOUSE_DELAY && counter <= MOUSE_DELAY*2) p2SelCount = 2;
            if(counter > MOUSE_DELAY*2 && counter <= MOUSE_DELAY*3) p2SelCount = 3;
            if(counter > MOUSE_DELAY*3 && counter <= MOUSE_DELAY*4) p2SelCount = 4;
            hasSelectionBeenMade = true;
        }
        if(p2SelCount == 1) { bf2 = tank1Low[0]; player2SelectedTank = tank1Low;
        } else if(p2SelCount == 2) { bf2 = tank2Low[0]; player2SelectedTank = tank2Low;
        } else if(p2SelCount == 3) { bf2 = tank3Low[0]; player2SelectedTank = tank3Low;
        } else { bf2 = tank4Low[0]; player2SelectedTank = tank4Low;}

        g.drawImage(bf1, 100, 280, null);
        g.drawImage(flipImageY(bf2), 813, 280, null);

        Rectangle bounds = getTextBounds(creditStr, credits, fontRenderContext);
        bounds.setLocation(calcCenter(creditStr, credits, fontRenderContext), (int)(675 - bounds.getHeight()));
        //g.drawRect((int) p1Sel.getX(), (int) p1Sel.getY(), (int) p1Sel.getWidth(), (int) p1Sel.getHeight());
    }

    /**
     * Check item collision between two sprites.
     * @param s1 Sprite 1
     * @param s2 Sprite 2
     * @return Returns boolean true or false.
     */
    public static boolean getItemCollision(Sprite s1, Sprite s2) {
        Rectangle s1Bounds = new Rectangle(s1.getObjectBounds());
        Rectangle s2Bounds = new Rectangle(s2.getObjectBounds());
        return s1.isVisible() && s2.isVisible() && s1Bounds.intersects(s2Bounds);
    }

    private boolean getProjCollision(Tank t1, Projectile s2) {
        Rectangle t1Bounds = new Rectangle(t1.getObjectBounds());
        Rectangle s2Bounds = new Rectangle(s2.getObjectBounds());
        return s2Bounds.intersects(t1Bounds);
    }

    private static Sprite getObject(Rectangle rectangle) {
        for(Sprite obj : collidableObjects) {
            if(obj.getObjectBounds().intersects(rectangle)) {
                return obj;
            }
        }
        return null;
    }

    public static boolean collision(Sprite t1) {
        if(getObject(t1.getObjectBounds()) != null)
            return getItemCollision(t1, getObject(t1.getObjectBounds()));
        return false;
    }

    public static Direction getCollisionDir(Sprite t1) { //FIXME
        if(getObject(t1.getObjectBounds()) != null) {
            if (t1.getX() < getObject(t1.getObjectBounds()).getX() + getObject(t1.getObjectBounds()).getObjectBounds().width ) {
                return Direction.LEFT;
            } else if (t1.getY() < getObject(t1.getObjectBounds()).getY() + getObject(t1.getObjectBounds()).getObjectBounds().height) {
                return Direction.UP;
            } else if (t1.getX() + t1.getObjectBounds().width > getObject(t1.getObjectBounds()).getX()) {
                return Direction.RIGHT;
            } else if (t1.getY() + t1.getObjectBounds().height > getObject(t1.getObjectBounds()).getY()) {
                return Direction.DOWN;
            } else {
                return Direction.NONE;
            }
        }
        return Direction.NONE;
    }

    /**
     * Calculate center position of a string of text on screen.
     * @param text String of text.
     * @param font Font Style.
     * @param fontRenderContext FontRenderContext object.
     * @return Calculated center position.
     */
    private int calcCenter(String text, Font font, FontRenderContext fontRenderContext) {
        final int width = (int) font.createGlyphVector(fontRenderContext, text).getVisualBounds().getWidth();
        final int centerPos = 1000;
        return (centerPos - width) / 2;
    }

    /**
     * Get the bounding rectangle of a string of text.
     * Note: Returned Rectangle isn't positioned, must define manually.
     * @param text String of text.
     * @param font Font style.
     * @param fontRenderContext FontRenderContext object.
     * @return Rectangle object bounding the text.
     */
    private Rectangle getTextBounds(String text, Font font, FontRenderContext fontRenderContext) {
        int x = (int) font.createGlyphVector(fontRenderContext, text).getVisualBounds().getX();
        int y = (int) font.createGlyphVector(fontRenderContext, text).getVisualBounds().getY();
        int width = (int) font.createGlyphVector(fontRenderContext, text).getVisualBounds().getWidth();
        int height = (int) font.createGlyphVector(fontRenderContext, text).getVisualBounds().getHeight();
        return new Rectangle(x, y, width, height);
    }

    private void addSound(String name) {
        try {
            sounds.put(name, soundManager.addClip("/assets/audio/" + name + ".wav"));
        }catch (IOException e) {
            e.printStackTrace();
        }catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private Clip playSound(String sound) {
        try {
            return soundManager.playSound(sounds.get(sound));
        }catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage[] getTankSelection(int player) {
        if(player == 1) {
            return player1SelectedTank;
        } else if(player == 2) {
            return player2SelectedTank;
        }
        return tank1Low;
    }

    private BufferedImage flipImageY(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    private static Thread thread;
    public static void main(String[] args) {
        Game ex = new Game();
        thread = new Thread(ex);
        thread.start();
    }

}