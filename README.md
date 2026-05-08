# Terminal Chess Game - Java Implementation

A complete, terminal-based two-player chess game written in Java, designed as an intermediate-level educational project demonstrating clean architecture, proper OOP principles, and well-structured code.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Class Diagram](#class-diagram)
4. [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
5. [Data Structures Used](#data-structures-used)
6. [Threading Implementation](#threading-implementation)
7. [Exception Handling](#exception-handling)
8. [Enums Used](#enums-used)
9. [How to Compile and Run](#how-to-compile-and-run)
10. [Example Gameplay](#example-gameplay)
11. [Class Documentation](#class-documentation)

---

## Project Overview

This project implements a fully playable chess game that runs entirely in the terminal (no GUI). Two human players take turns entering moves using standard algebraic notation (e.g., `e2 e4`). The game includes:

- ✅ Complete chess rules (movement, capture, check, checkmate, stalemate)
- ✅ Turn-based gameplay
- ✅ Move validation and illegal move rejection
- ✅ Check and checkmate detection
- ✅ Stalemate detection
- ✅ Undo functionality
- ✅ Chess clock with threading
- ✅ ASCII board display
- ✅ Pawn promotion (auto-promotes to queen)

**Note**: Castling and en passant are intentionally omitted for educational clarity.

---

## Architecture

### High-Level Design

The project follows a **modular, layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│           Main (Entry Point)            │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│         Game (Orchestrator)             │
│  - Controls game flow                   │
│  - Manages players and turn order       │
│  - Handles user input                   │
│  - Coordinates board and clock           │
└──────┬──────────────┬───────────────────┘
       │              │
       ▼              ▼
┌─────────────┐  ┌──────────────┐
│   Board     │  │ ChessClock   │
│  - 8x8 grid │  │  - Threading │
│  - Rules    │  │  - Timer     │
└──────┬──────┘  └──────────────┘
       │
       ▼
┌─────────────────────────────────────────┐
│         Piece Hierarchy                 │
│  (Abstract: Piece)                     │
│  (Concrete: King, Queen, Rook, etc.)   │
└─────────────────────────────────────────┘
```

### Package Structure

```
chess/
├── Main.java                    # Entry point
├── game/
│   └── Game.java               # Main game controller
├── board/
│   ├── Board.java              # Chess board logic
│   └── Tile.java               # Single square representation
├── pieces/
│   ├── Piece.java              # Abstract base class
│   ├── King.java
│   ├── Queen.java
│   ├── Rook.java
│   ├── Bishop.java
│   ├── Knight.java
│   └── Pawn.java
├── model/
│   ├── Position.java           # Coordinate value object
│   ├── PieceColor.java         # Enum: WHITE, BLACK
│   ├── PieceType.java          # Enum: KING, QUEEN, etc.
│   ├── GameStatus.java         # Enum: ACTIVE, CHECK, etc.
│   └── MoveStatus.java         # Enum: VALID, INVALID, etc.
├── move/
│   └── Move.java               # Move representation
├── player/
│   └── Player.java             # Player information
├── exception/
│   ├── GameException.java      # Base exception
│   ├── InvalidMoveException.java
│   └── InvalidPositionException.java
└── util/
    └── ChessClock.java         # Thread-based timer
```

---

## Class Diagram

### Text-Based UML Representation

```
┌─────────────┐
│    Main     │
└──────┬──────┘
       │ uses
       ▼
┌─────────────────────────────────────────────────────────┐
│                        Game                             │
│  - board: Board                                         │
│  - whitePlayer: Player                                  │
│  - blackPlayer: Player                                  │
│  - moveHistory: List<Move>                              │
│  - undoStack: Stack<Move>                               │
│  - clock: ChessClock                                    │
│  - currentTurn: PieceColor                              │
│  - gameStatus: GameStatus                               │
└──────┬──────────────┬──────────────┬───────────────────┘
       │              │              │
       │ has-a       │ has-a        │ has-a
       ▼             ▼              ▼
┌──────────┐  ┌──────────┐  ┌──────────────┐
│  Board   │  │  Player  │  │ ChessClock   │
└────┬─────┘  └──────────┘  └──────┬───────┘
     │                             │
     │ composes                    │ implements
     ▼                             ▼
┌──────────┐              ┌─────────────────┐
│   Tile   │              │    Runnable     │
│  - row   │              └─────────────────┘
│  - col   │
│  - piece │
└────┬─────┘
     │
     │ references
     ▼
┌──────────────────────────────────────────────┐
│              Piece (abstract)                │
│  - color: PieceColor                         │
│  + getType(): PieceType                      │
│  + getSymbol(): char                         │
│  + getPossibleMoves(Board, Position): List   │
└──────┬───────────────────────────────────────┘
       │ extends
       ├──────────┬──────────┬──────────┬──────────┬──────────┐
       ▼          ▼          ▼          ▼          ▼          ▼
   ┌──────┐  ┌───────┐  ┌──────┐  ┌───────┐  ┌───────┐  ┌──────┐
   │ King │  │ Queen │  │ Rook │  │Bishop │  │Knight │  │ Pawn │
   └──────┘  └───────┘  └──────┘  └───────┘  └───────┘  └──────┘

┌──────────────────────────────────────────────┐
│                    Move                       │
│  - from: Position                             │
│  - to: Position                               │
│  - movedPiece: Piece                          │
│  - capturedPiece: Piece                       │
│  - status: MoveStatus                         │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│                  Position                     │
│  - row: int (final)                           │
│  - col: int (final)                           │
│  + fromAlgebraic(String): Position            │
│  + toAlgebraic(): String                      │
└──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│              GameException                    │
│  (extends Exception)                         │
└──────┬───────────────────────────────────────┘
       │ extends
       ├──────────────────┬────────────────────┐
       ▼                  ▼                    ▼
┌─────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│InvalidMove      │  │InvalidPosition   │  │ (future)         │
│Exception        │  │Exception         │  │                  │
└─────────────────┘  └──────────────────┘  └──────────────────┘
```

---

## OOP Concepts Demonstrated

### 1. **Encapsulation**
- All classes use **private fields** with **public getters** where appropriate
- `Board` encapsulates the 8x8 tile array
- `Game` encapsulates game state (turn, status, history)
- `ChessClock` encapsulates timer state with synchronized access
- `Position` is **immutable** (final fields, no setters)

### 2. **Inheritance**
- **Abstract class**: `Piece` serves as the base class
- **Concrete subclasses**: `King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn` all extend `Piece`
- **Exception hierarchy**: `InvalidMoveException` and `InvalidPositionException` extend `GameException`, which extends `Exception`

### 3. **Polymorphism**
- **Runtime polymorphism**: `Board.isInCheck()` calls `piece.getPossibleMoves()` without knowing the concrete piece type
- All pieces are stored as `Piece` references, but execute their own movement logic
- Example: `List<Position> moves = piece.getPossibleMoves(board, from);` works for any piece type

### 4. **Abstraction**
- `Piece` abstract class defines the **interface** for all pieces without implementing movement logic
- `Game` uses `ChessClock` through a simple interface (`start()`, `stop()`, `switchPlayer()`) without knowing threading details
- `Board` provides high-level operations (`isInCheck()`, `hasAnyLegalMove()`) that abstract away piece-specific logic

### 5. **Interfaces**
- `ChessClock` implements `Runnable` interface to enable threading
- Demonstrates interface-based design for threading

### 6. **Composition**
- `Board` **composes** 64 `Tile` objects (Board owns Tiles; lifetime is bound)
- `Game` **composes** `Board`, `ChessClock`, and `Player` objects
- `Tile` **has-a** `Piece` reference (nullable)

### 7. **Aggregation**
- `Game` **aggregates** `Move` objects in `List<Move>` (moves exist independently)
- `Game` **aggregates** `Move` objects in `Stack<Move>` for undo functionality
- `Board.getPieces()` returns a list of pieces (aggregation relationship)

### 8. **Method Overriding**
- Each concrete piece class **overrides**:
  - `getType()` - returns specific `PieceType`
  - `getSymbol()` - returns piece-specific ASCII character
  - `getPossibleMoves()` - implements piece-specific movement rules

### 9. **Method Overloading**
- `Board.getTile(int row, int col)` and `Board.getTile(Position position)` - same method name, different parameters
- `Move` constructors: one with default `MoveStatus.VALID`, one with explicit status
- `GameException` constructors: message-only and message+cause variants

---

## Data Structures Used

### 1. **2D Array**
- `Board.tiles`: `Tile[][]` - 8x8 grid representing the chess board
- Provides O(1) access to any square by row/column

### 2. **ArrayList**
- `Game.moveHistory`: `List<Move>` - stores complete move history
- Allows sequential access and easy iteration
- Used for recording all moves made during the game

### 3. **Stack**
- `Game.undoStack`: `Stack<Move>` - LIFO structure for undo functionality
- `push()` when a move is made, `pop()` when undoing
- Perfect for implementing undo/redo functionality

### 4. **Collections Framework**
- `List<Position>` - returned by `Piece.getPossibleMoves()` for all piece types
- `List<Piece>` - returned by `Board.getPieces()` for filtering pieces by color
- Proper use of Java Collections API throughout

### 5. **HashMap (Potential Use)**
- While not explicitly used in current implementation, `Position` implements `equals()` and `hashCode()`, making it suitable as a HashMap key if needed for future optimizations

---

## Threading Implementation

### ChessClock Class

The `ChessClock` class demonstrates **meaningful threading** by implementing a background timer:

#### Implementation Details

1. **Implements Runnable Interface**
   ```java
   public class ChessClock implements Runnable {
       // ...
   }
   ```

2. **Thread Creation and Management**
   - Creates a daemon thread in `start()` method
   - Thread runs continuously, decrementing time every second
   - Gracefully stops via `stop()` method

3. **Synchronization**
   - Uses `synchronized` blocks with a `lock` object
   - Protects shared state (`whiteRemainingSeconds`, `blackRemainingSeconds`, `activeColor`)
   - Ensures thread-safe access from both game thread and clock thread

4. **How It Works**
   - Clock thread sleeps for 1 second
   - Wakes up and decrements the active player's remaining time
   - Game thread calls `switchPlayer()` to toggle which player's time is decrementing
   - Game thread checks `isFlagFallen()` to detect time expiration

#### Thread Safety

- **volatile** keyword on `running` flag ensures visibility across threads
- **synchronized** blocks prevent race conditions when reading/writing time values
- Proper thread lifecycle management (start, stop, interrupt)

#### Usage in Game

- Clock starts when game begins (`clock.start()`)
- Clock switches active player after each move (`clock.switchPlayer()`)
- Game checks for time expiration each turn (`clock.isFlagFallen()`)
- Clock stops when game ends (`clock.stop()`)

---

## Exception Handling

### Custom Exception Hierarchy

```
Exception
  └── GameException (base custom exception)
       ├── InvalidMoveException
       └── InvalidPositionException
```

### Exception Usage

1. **InvalidMoveException**
   - Thrown when:
     - No piece exists at source position
     - Wrong player's turn
     - Piece cannot move to destination
     - Move would leave king in check

2. **InvalidPositionException**
   - Thrown when:
     - Algebraic notation is malformed (e.g., "z9", "a")
     - Position is outside board bounds

3. **GameException**
   - Base class for all game-related exceptions
   - Allows catching all custom exceptions with `catch (GameException ex)`

### Exception Handling Pattern

```java
try {
    // Parse and execute move
    Position from = parsePosition(tokens[0]);
    Position to = parsePosition(tokens[1]);
    executeMove(from, to);
} catch (GameException ex) {
    System.out.println("Error: " + ex.getMessage());
} catch (IllegalArgumentException ex) {
    System.out.println("Input error: " + ex.getMessage());
}
```

- User-friendly error messages
- Game continues after invalid input
- Proper exception propagation and handling

---

## Enums Used

### 1. **PieceColor**
```java
public enum PieceColor {
    WHITE,
    BLACK;
    
    public PieceColor opposite() { ... }
}
```
- Represents piece/player color
- Includes utility method `opposite()` for switching sides

### 2. **PieceType**
```java
public enum PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}
```
- Identifies piece types
- Used in `Piece.getType()` method

### 3. **GameStatus**
```java
public enum GameStatus {
    ACTIVE, CHECK, CHECKMATE, STALEMATE, DRAW
}
```
- Tracks overall game state
- Updated after each move
- Used to determine game end conditions

### 4. **MoveStatus**
```java
public enum MoveStatus {
    VALID, INVALID, ILLEGAL
}
```
- Represents move validation result
- Stored in `Move` objects
- Currently all moves are `VALID` (invalid moves throw exceptions)

### Benefits of Using Enums

- **Type safety**: Prevents invalid values (e.g., can't use "RED" as a color)
- **Readability**: `PieceColor.WHITE` is clearer than `1` or `"white"`
- **IDE support**: Autocomplete and refactoring support
- **No magic numbers/strings**: All constants are named and documented

---

## How to Compile and Run

### Prerequisites

- **Java 17 or later** installed and on your PATH
- Command line access (Terminal/PowerShell/CMD)

### Compilation

From the project root directory (`e:\Downloads\chess2player`):

```bash
# Windows (PowerShell/CMD)
javac -d out src\chess\Main.java

# Linux/Mac
javac -d out src/chess/Main.java
```

This will:
- Compile all Java files in the `src` directory
- Place compiled `.class` files in the `out` directory
- Preserve package structure

### Running

```bash
# Windows (PowerShell/CMD)
java -cp out chess.Main

# Linux/Mac
java -cp out chess.Main
```

### Expected Output

You should see:
```
=== Terminal Chess ===

8  r n b q k b n r
7  p p p p p p p p
6  . . . . . . . .
5  . . . . . . . .
4  . . . . . . . .
3  . . . . . . . .
2  P P P P P P P P
1  R N B Q K B N R

   a b c d e f g h

Status: ACTIVE
White (WHITE) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 15:00 | Black: 15:00
Enter your move:
```

---

## Example Gameplay

### Sample Game Session

```
=== Terminal Chess ===

8  r n b q k b n r
7  p p p p p p p p
6  . . . . . . . .
5  . . . . . . . .
4  . . . . . . . .
3  . . . . . . . .
2  P P P P P P P P
1  R N B Q K B N R

   a b c d e f g h

Status: ACTIVE
White (WHITE) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 15:00 | Black: 15:00
Enter your move: e2 e4

8  r n b q k b n r
7  p p p p p p p p
6  . . . . . . . .
5  . . . . . . . .
4  . . . . P . . .
3  . . . . . . . .
2  P P P P . P P P
1  R N B Q K B N R

   a b c d e f g h

Status: ACTIVE
Black (BLACK) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 14:59 | Black: 15:00
Enter your move: e7 e5

8  r n b q k b n r
7  p p p p . p p p
6  . . . . . . . .
5  . . . . p . . .
4  . . . . P . . .
3  . . . . . . . .
2  P P P P . P P P
1  R N B Q K B N R

   a b c d e f g h

Status: ACTIVE
White (WHITE) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 14:59 | Black: 14:59
Enter your move: g1 f3

8  r n b q k b n r
7  p p p p . p p p
6  . . . . . . . .
5  . . . . p . . .
4  . . . . P . . .
3  . . . . . N . .
2  P P P P . P P P
1  R N B Q K B . R

   a b c d e f g h

Status: ACTIVE
Black (BLACK) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 14:58 | Black: 14:59
Enter your move: undo
Undid move: g1-f3

[Board reverts to previous state]

Status: ACTIVE
White (WHITE) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 14:59 | Black: 15:00
Enter your move: d2 d4

[Game continues...]

[Later in the game...]

Status: CHECK
Black (BLACK) to move. (e.g., e2 e4, 'undo', 'restart', 'quit')
Clock - White: 12:30 | Black: 10:15
Enter your move: f8 c5

Status: CHECKMATE! WHITE wins.
```

### Available Commands

- **Move**: Enter two positions separated by space (e.g., `e2 e4`)
- **undo**: Reverts the last move
- **restart**: Starts a new game
- **quit**: Exits the program

### Move Notation

- Uses **algebraic notation**: file (a-h) + rank (1-8)
- Examples:
  - `e2 e4` - pawn from e2 to e4
  - `g1 f3` - knight from g1 to f3
  - `e1 g1` - king from e1 to g1

---

## Class Documentation

### Main Classes

#### `chess.Main`
- **Purpose**: Entry point of the application
- **Responsibilities**: Creates `Game` instance and starts it
- **Design Pattern**: Delegation (delegates all logic to `Game`)

#### `chess.game.Game`
- **Purpose**: Central game controller
- **Key Responsibilities**:
  - Manages game loop
  - Handles user input parsing
  - Coordinates board, players, and clock
  - Validates moves
  - Detects game end conditions
- **State Management**: Tracks current turn, game status, move history

#### `chess.board.Board`
- **Purpose**: Represents the chess board and game rules
- **Key Responsibilities**:
  - Initializes starting position
  - Provides board manipulation methods
  - Detects check, checkmate, stalemate
  - Validates move legality (king safety)
- **Data Structure**: 8x8 array of `Tile` objects

#### `chess.board.Tile`
- **Purpose**: Represents a single square on the board
- **Fields**: Row, column, and optional `Piece` reference
- **Relationship**: Composed by `Board`

#### `chess.pieces.Piece` (Abstract)
- **Purpose**: Base class for all chess pieces
- **Abstract Methods**:
  - `getType()`: Returns piece type
  - `getSymbol()`: Returns ASCII character
  - `getPossibleMoves()`: Calculates possible moves
- **Polymorphism**: Enables runtime dispatch to concrete piece types

#### Concrete Piece Classes (`King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn`)
- **Purpose**: Implement specific movement rules
- **Key Feature**: Each overrides `getPossibleMoves()` with piece-specific logic
- **Movement Rules**:
  - **King**: One square in any direction
  - **Queen**: Any number of squares in 8 directions
  - **Rook**: Horizontal/vertical lines
  - **Bishop**: Diagonal lines
  - **Knight**: L-shaped jumps
  - **Pawn**: Forward movement + diagonal capture

#### `chess.model.Position`
- **Purpose**: Immutable coordinate value object
- **Features**:
  - Converts between internal indices and algebraic notation
  - Validates bounds (0-7)
  - Implements `equals()` and `hashCode()`

#### `chess.move.Move`
- **Purpose**: Represents a single move
- **Fields**: From/to positions, moved/captured pieces, status
- **Usage**: Stored in history list and undo stack

#### `chess.player.Player`
- **Purpose**: Represents a human player
- **Fields**: Name and color
- **Usage**: Used for display purposes and turn identification

#### `chess.util.ChessClock`
- **Purpose**: Thread-based chess timer
- **Threading**: Implements `Runnable`, runs in background thread
- **Synchronization**: Uses synchronized blocks for thread-safe access
- **Features**: Tracks time per player, switches active player, detects time expiration

#### Exception Classes
- **`GameException`**: Base exception for all game errors
- **`InvalidMoveException`**: Thrown for illegal moves
- **`InvalidPositionException`**: Thrown for invalid coordinates

---

## Design Decisions

### Why No Castling?
- Castling adds complexity (king/rook movement, check detection during castling)
- Omitted for educational clarity while maintaining core chess functionality

### Why No En Passant?
- En passant requires tracking previous pawn moves
- Adds state complexity without being essential for learning OOP principles

### Why Auto-Promote to Queen?
- Simplifies user input (no need to specify promotion piece)
- Queen is the most common promotion choice
- Can be extended later to allow piece selection

### Why 15 Minutes Default?
- Reasonable time control for learning/playing
- Can be easily modified in `Game` constructor
- Demonstrates meaningful threading usage

---

## Future Enhancements

Potential improvements for advanced learners:

1. **Castling**: Implement king-side and queen-side castling
2. **En Passant**: Track and allow en passant captures
3. **Promotion Choice**: Allow selecting promotion piece (queen, rook, bishop, knight)
4. **Move History Display**: Show full move history in algebraic notation
5. **Save/Load**: Serialize game state to file
6. **Computer Opponent**: Add simple AI using minimax algorithm
7. **Move Validation Hints**: Suggest legal moves when invalid move entered
8. **Threefold Repetition**: Detect draw by repetition
9. **Fifty-Move Rule**: Implement draw by 50-move rule

---

## License

This project is provided as an educational example. Feel free to use, modify, and learn from it.

---

## Author Notes

This project was designed to demonstrate:
- **Clean Architecture**: Separation of concerns, modular design
- **OOP Principles**: All major OOP concepts in practice
- **Java Best Practices**: Proper exception handling, threading, collections
- **Educational Value**: Clear, well-documented code suitable for learning

Perfect for:
- Intermediate Java students
- Developers learning OOP design patterns
- Anyone wanting to understand chess game implementation
- Projects requiring demonstration of Java threading and exception handling

---

**Enjoy playing chess!** 🎮♟️
