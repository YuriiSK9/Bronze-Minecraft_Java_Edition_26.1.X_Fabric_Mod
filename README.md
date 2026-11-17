# Bronze

> **Note:** Due to technical issues with building the final `.jar` file, only the source code is provided here so you can compile and play the mod directly using Gradle.

## Requirements

* **Java Development Kit (JDK):** **Java 25 (JDK 25)** is required to compile and run this project (Minecraft 26.1). Older versions like Java 21 or Java 17 will not work.
* **Git** (optional, to clone the repository).

## How to Run the Game

1. **Download the source code:**
   Clone this repository or click **Code > Download ZIP** and extract it to a folder.

2. **Open a terminal in the project folder:**
   * **Windows:** Open the folder, click on the address bar at the top, type `cmd` (or `powershell`), and press **Enter**.
   * **Linux / macOS:** Right-click inside the folder and select **Open in Terminal**.

3. **Run the Minecraft client:**
   Execute the command corresponding to your operating system:

   * **On Windows (CMD / PowerShell):**
     ```bat
     .\gradlew.bat runClient
     ```
     *(Note: In standard Command Prompt, `gradlew runClient` also works).*

   * **On Linux / macOS:**
     First, grant execution permissions to the Gradle wrapper:
     ```bash
     chmod +x gradlew
     ```
     Then, launch the client:
     ```bash
     ./gradlew runClient
     ```

4. **Wait for setup:** The first time you run this command, Gradle will automatically download Minecraft 26.1 and its dependencies. This may take a few minutes, after which the game will launch automatically.

---

## License & Copyright

**All Rights Reserved.**

The source code in this repository is made available strictly for personal use, allowing players to compile and play the mod locally. You are **not** permitted to copy, modify, redistribute, or use any part of this code in other projects or commercial products without explicit permission from the author.
