package chess.game;

import chess.board.Board;
import chess.exception.GameException;
import chess.exception.InvalidMoveException;
import chess.exception.InvalidPositionException;
import chess.model.GameStatus;
import chess.model.MoveStatus;
import chess.model.PieceColor;
import chess.model.Position;
import chess.move.Move;
import chess.pieces.Piece;
import chess.player.Player;
import chess.util.ChessClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Coordinates the overall chess game flow.
 *
 * <p>The {@code Game} class composes the board, players, clock, and move
 * history. It is responsible for the game loop, validating user input, and
 * detecting check, checkmate, and stalemate conditions.</p>
 */
public class Game {

    private final Board board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final List<Move> moveHistory;
    private final Stack<Move> undoStack;
    private final ChessClock clock;

    private PieceColor currentTurn;
    private GameStatus gameStatus;

    /**
     * Constructs a new game instance with default players and configuration.
     */
    public Game() {
        this.board = new Board();
        this.whitePlayer = new Player("White", PieceColor.WHITE);
        this.blackPlayer = new Player("Black", PieceColor.BLACK);
        this.moveHistory = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.clock = new ChessClock(15 * 60); // 15 minutes per player
        this.currentTurn = PieceColor.WHITE;
        this.gameStatus = GameStatus.ACTIVE;
    }

    /**
     * Starts a new game loop that runs until the players choose to quit.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Terminal Chess ===");
        boolean keepPlaying = true;

        while (keepPlaying) {
            resetGameState();
            playSingleGame(scanner);

            System.out.print("Play again? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            keepPlaying = answer.startsWith("y");
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    /**
     * Resets all game state to start a new game.
     */
    private void resetGameState() {
        board.initialize();
        moveHistory.clear();
        undoStack.clear();
        currentTurn = PieceColor.WHITE;
        gameStatus = GameStatus.ACTIVE;
    }

    /**
     * Runs a single full game from initial position until an end condition.
     *
     * @param scanner input scanner shared with the main loop
     */
    private void playSingleGame(Scanner scanner) {
        clock.start();

        while (true) {
            board.print();
            printStatus();

            if (gameStatus == GameStatus.CHECKMATE
                    || gameStatus == GameStatus.STALEMATE
                    || gameStatus == GameStatus.DRAW) {
                break;
            }

            if (clock.isFlagFallen(currentTurn)) {
                PieceColor winner = currentTurn.opposite();
                System.out.println("Time has expired for " + currentTurn + ". " + winner + " wins on time!");
                break;
            }

            try {
                System.out.printf("%s to move. (e.g., e2 e4, 'undo', 'restart', 'quit')%n",
                        currentTurn == PieceColor.WHITE ? whitePlayer : blackPlayer);
                printClock();
                System.out.print("Enter your move: ");
                String line = scanner.nextLine().trim();

                if (line.equalsIgnoreCase("quit")) {
                    clock.stop();
                    return;
                } else if (line.equalsIgnoreCase("restart")) {
                    System.out.println("Restarting game...");
                    return;
                } else if (line.equalsIgnoreCase("undo")) {
                    undoLastMove();
                    continue;
                }

                String[] tokens = line.split("\\s+");
                if (tokens.length != 2) {
                    throw new InvalidMoveException("Please enter moves in the form 'e2 e4'.");
                }

                Position from = parsePosition(tokens[0]);
                Position to = parsePosition(tokens[1]);

                executeMove(from, to);

                // Switch active clock side and current turn after a successful move
                clock.switchPlayer();
                currentTurn = currentTurn.opposite();
                updateGameStatus();

            } catch (GameException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println("Input error: " + ex.getMessage());
            }
        }

        clock.stop();
    }

    /**
     * Parses a position from algebraic notation into a {@link Position} object.
     *
     * @param algebraic algebraic notation such as "e2"
     * @return position instance
     * @throws InvalidPositionException if the notation is invalid
     */
    private Position parsePosition(String algebraic) throws InvalidPositionException {
        try {
            return Position.fromAlgebraic(algebraic);
        } catch (IllegalArgumentException ex) {
            throw new InvalidPositionException("Invalid position: " + algebraic);
        }
    }

    /**
     * Executes a full move (including validation and recording history).
     *
     * @param from origin
     * @param to   destination
     * @throws InvalidMoveException if the move is not allowed
     */
    private void executeMove(Position from, Position to) throws InvalidMoveException {
        Piece piece = board.getPiece(from);
        if (piece == null) {
            throw new InvalidMoveException("There is no piece at " + from.toAlgebraic());
        }
        if (piece.getColor() != currentTurn) {
            throw new InvalidMoveException("It is not " + piece.getColor() + "'s turn.");
        }

        List<Position> possibleMoves = piece.getPossibleMoves(board, from);
        if (!possibleMoves.contains(to)) {
            throw new InvalidMoveException("That piece cannot move to " + to.toAlgebraic());
        }

        if (!board.isLegalMove(currentTurn, from, to)) {
            throw new InvalidMoveException("Move would leave your king in check.");
        }

        Piece captured = board.movePiece(from, to);
        Move move = new Move(from, to, piece, captured, MoveStatus.VALID);
        moveHistory.add(move);
        undoStack.push(move);

        if (captured != null) {
            System.out.println("Captured: " + captured.getType() + " at " + to.toAlgebraic());
        }
    }

    /**
     * Undoes the last move if possible.
     */
    private void undoLastMove() {
        if (undoStack.isEmpty()) {
            System.out.println("No moves to undo.");
            return;
        }
        Move last = undoStack.pop();

        // Reverse the move
        Piece piece = last.getMovedPiece();
        Piece captured = last.getCapturedPiece();

        board.getTile(last.getFrom()).setPiece(piece);
        board.getTile(last.getTo()).setPiece(captured);

        moveHistory.remove(moveHistory.size() - 1);
        currentTurn = currentTurn.opposite();
        clock.switchPlayer();

        System.out.println("Undid move: " + last);
    }

    /**
     * Updates the {@link GameStatus} according to the current board state.
     */
    private void updateGameStatus() {
        PieceColor sideToMove = currentTurn;
        boolean inCheck = board.isInCheck(sideToMove);
        boolean hasMove = board.hasAnyLegalMove(sideToMove);

        if (inCheck && !hasMove) {
            gameStatus = GameStatus.CHECKMATE;
        } else if (!inCheck && !hasMove) {
            gameStatus = GameStatus.STALEMATE;
        } else if (inCheck) {
            gameStatus = GameStatus.CHECK;
        } else {
            gameStatus = GameStatus.ACTIVE;
        }
    }

    /**
     * Prints the current game and clock status.
     */
    private void printStatus() {
        switch (gameStatus) {
            case CHECK:
                System.out.println("Status: CHECK against " + currentTurn);
                break;
            case CHECKMATE:
                System.out.println("Status: CHECKMATE! " + currentTurn.opposite() + " wins.");
                break;
            case STALEMATE:
                System.out.println("Status: STALEMATE. The game is a draw.");
                break;
            case DRAW:
                System.out.println("Status: DRAW.");
                break;
            default:
                System.out.println("Status: ACTIVE");
        }
    }

    /**
     * Prints remaining clock time for both players.
     */
    private void printClock() {
        int white = clock.getRemainingSeconds(PieceColor.WHITE);
        int black = clock.getRemainingSeconds(PieceColor.BLACK);
        System.out.printf("Clock - White: %s | Black: %s%n",
                formatSeconds(white), formatSeconds(black));
    }

    /**
     * Utility method to format seconds as mm:ss.
     *
     * @param totalSeconds total seconds
     * @return formatted string
     */
    private String formatSeconds(int totalSeconds) {
        int minutes = Math.max(0, totalSeconds) / 60;
        int seconds = Math.max(0, totalSeconds) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}

