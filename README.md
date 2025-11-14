# Wordle Clone (JavaFX)

This project is a custom Wordle-style game built in **JavaFX**.  
The player has 5 attempts to guess a random 5-letter word. After each guess,  
the game provides visual feedback on the correctness of each letter similar to the original Wordle:

- 🟩 **Green** — correct letter in the correct position  
- 🟨 **Yellow** — correct letter in the wrong position  
- ⬜ **Grey** — letter not in the word  

The game includes:
- A dynamic on-screen keyboard  
- A textfield-based game grid  
- A dictionary validation system that checks whether the input word exists  
- Visual coloring of the board and keyboard  
- Random word selection from a list  

---

## 🎮 Features

### ✔️ Word Input Grid
- 5 rows (attempts) × 5 columns (letters)  
- Boxes automatically move as you type  
- ENTER submits the row  
- DELETE support  
- Only letters allowed  

### ✔️ On-Screen Keyboard
- Displays QWERTY keyboard  
- Keys change color (green/yellow/grey) based on guesses  
- Built automatically using loops  

### ✔️ Word Validation
- Reads from a dictionary file (`words.txt`)  
- Guess is only accepted if:
  - All 5 letters are filled  
  - The word exists in the dictionary  

### ✔️ Random Word Generation
- Game selects from a preset list of words  
- Uses `Random` to choose one at launch

Technologies used:
- Java (OOP, event handling)
- JavaFX (GUI, layouts, styles)
---

Built 2022/README updated 2025


