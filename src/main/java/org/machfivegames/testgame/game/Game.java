package main.java.org.machfivegames.testgame.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Game {

    // class variables
    private float x = 400, y = 300;
    private float rotation = 0;
    private long lastFrame;
    private int fps;
    private long lastFps;

    private void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch(LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        initOpenGl();
        getDelta();

        while(!Display.isCloseRequested()) {
            int delta = getDelta();

            update(delta);
            drawQuad();

            Display.update();
            Display.sync(60); // Cap FPS at 60
        }

        Display.destroy();
    }

    private void update(int delta) {
        // rotate quad
        rotation += 0.15f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) x -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) x += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) y += 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) y -= 0.35f * delta;

        // keep quad on the screen
        if (x < 0) x = 0;
        if (x > 800) x = 800;
        if (y < 0) y = 0;
        if (y > 600) y = 600;

        updateFps();
    }

    private int getDelta() {
        long time = getTime();
        int delta = (int)(time-lastFrame);
        lastFrame = time;

        return delta;
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void updateFps() {
        if (getTime() - lastFps > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFps += 1000;
        }
        fps++;
    }

    private void initOpenGl() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private void drawQuad() {
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Set the color of the quad (RGBA)
        GL11.glColor3f(0.5f, 0.5f, 1.0f);

        // draw quad
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(rotation, 0f, 0f, 1f);
        GL11.glTranslatef(-x, -y, 0);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 50, y - 50);
        GL11.glVertex2f(x + 50, y - 50);
        GL11.glVertex2f(x + 50, y + 50);
        GL11.glVertex2f(x - 50, y + 50);
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
