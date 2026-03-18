package chess.util;

import chess.model.PieceColor;

/**
 * Simple chess clock implementation using a background thread.
 *
 * <p>The clock tracks remaining time for both players and decrements the
 * currently active player's time every second. It demonstrates use of the
 * {@link Runnable} interface and thread synchronization.</p>
 */
public class ChessClock implements Runnable {

    private final Object lock = new Object();

    private final int initialSecondsPerPlayer;
    private int whiteRemainingSeconds;
    private int blackRemainingSeconds;

    private volatile boolean running;
    private PieceColor activeColor;
    private Thread thread;

    /**
     * Creates a clock with the same initial time for both players.
     *
     * @param initialSecondsPerPlayer number of seconds per player
     */
    public ChessClock(int initialSecondsPerPlayer) {
        this.initialSecondsPerPlayer = initialSecondsPerPlayer;
        this.whiteRemainingSeconds = initialSecondsPerPlayer;
        this.blackRemainingSeconds = initialSecondsPerPlayer;
        this.activeColor = PieceColor.WHITE;
    }

    /**
     * Starts the underlying timer thread.
     */
    public void start() {
        if (thread == null) {
            running = true;
            thread = new Thread(this, "ChessClock");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Stops the timer thread gracefully.
     */
    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * Switches the active player whose time is being decremented.
     */
    public void switchPlayer() {
        synchronized (lock) {
            activeColor = activeColor.opposite();
        }
    }

    /**
     * Returns remaining seconds for a given color.
     *
     * @param color color to query
     * @return remaining time in seconds
     */
    public int getRemainingSeconds(PieceColor color) {
        synchronized (lock) {
            return (color == PieceColor.WHITE) ? whiteRemainingSeconds : blackRemainingSeconds;
        }
    }

    /**
     * Indicates whether the given color has run out of time.
     *
     * @param color color to query
     * @return true if that player's clock has expired
     */
    public boolean isFlagFallen(PieceColor color) {
        synchronized (lock) {
            return getRemainingSeconds(color) <= 0;
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(1000);
                synchronized (lock) {
                    if (activeColor == PieceColor.WHITE) {
                        whiteRemainingSeconds--;
                    } else {
                        blackRemainingSeconds--;
                    }
                }
            }
        } catch (InterruptedException ignored) {
            // Thread stopped
        }
    }
}

