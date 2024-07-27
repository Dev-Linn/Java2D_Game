package defaults;


import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private boolean running = false;
    private Thread thread;
    private Handler handler;

    public Game() {
        new Window(1000, 563, "Wizzard", this);
        start();
    }

    handler = new Handler();

    private void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        /////////////////////////////////////

        g.setColor(Color.red);
        g.fillRect(0, 0, 10000, 563);

        handler.render(g);

        /////////////////////////////////////
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game();
    }
}
