
# MC-Texture-Tool

**Dependecies:**

- [Java](https://www.oracle.com/in/java/technologies/downloads/)

**Build from source:**

1. Clone the repo:

    ```bat
    git clone https://github.com/crt-dot-soluble/mc-texture-tool.git    
    ```

2. Navigate to the directory the repo was cloned to:

    ```bat
    cd mc-texture-tool
    ```

3. Compile the source:

    ```bat
    javac src/*.java
    ```

## Purpose

MC-Texture-Tool assists in mod development and custom texture creation.

Recommended for use within [MCreator](https://mcreator.net/), a mod creation suite with solid support for texture templating and colorization.

## Features

- Texture extraction for
  - Items
  - Blocks
  - Armor
- Grayscaling of extracted textures
- User options via runtime flags

## Usage

**Note:**

Due to the use of preview features the java flag ```--enable-preview``` is required by java to run the application.
This will not be required in the future when the features are no longer in preview.

### From Compiled Source

```txt
java --enable-preview App 
```

### From Executable Jar

```txt
java --enable-preview -jar mc-texture-tool.jar  
```

### Supported Flags

```txt
java --enable-preview -jar -g           // grayscale output
                           -i <Path>    // Specify input folder (the location of the .minecraft folder)
                           -o <Path>    // Specify output folder (i.e. C:/textures/)
                           -v <A.B.C>   // Specify game version  (i.e. 1.20.6)

// A valid example (assuming the .minecraft folder is located in  C:/)
// java --enable preview App -g -i "C:/" -o "C:/textures/"
```

### Expected Output

An execution with the ```-g``` flag present would provide the following output...

```text
***************************************************
*             Minecraft Texture Tool
*         Author: github.com/crt.soluble
***************************************************
*
* [>] Setting user flags
*        [+] Grayscale output flag set: true
*
* [>] Executing with settings:
*        [+] Input Path: C:\Users\User\AppData\Roaming\.minecraft
*        [+] Output Path: C:\Users\User\mc-texture-tool
*        [+] Grayscale Output: true
*        [+] Minecraft Version: 1.20.6
*
* [>] Initializing jar extractor
*        [+] Jar file found for version 1.20.6: C:\Users\User\AppData\Roaming\.minecraft\versions\1.20.6\1.20.6.jar
*        [+] Jar file loaded
*        [+] Extraction started
*        [+] Extraction output sent to: C:\Users\User\mc-texture-tool\original
*
* [>] Grayscaling the extracted textures
*        [+] Directory created: C:\Users\User\mc-texture-tool\grayscale\item
*        [+] Grayscaler completed processing 602 item textures
*        [+] Directory created: C:\Users\User\mc-texture-tool\grayscale\block
*        [+] Grayscaler completed processing 1061 block textures
*        [+] Directory created: C:\Users\User\mc-texture-tool\grayscale\models\armor
*        [+] Grayscaler completed processing 15 armor textures
*
* [>] Press any key to exit...
```

## Using With MCreator

[MCreator](https://mcreator.net/) allows you to import and colorizing texture templates to create custom item and block textures. To easily create custom vanilla style textures using the original textures as a base copy the contents of the directories to the appropriate MCreator template folder:

```text
grayscale/item          -> ...\.mcreator\templates\textures\texturemaker
grayscale/block         -> ...\.mcreator\templates\textures\texturemaker
grayscale/models/armor  -> ...\.mcreator\templates\textures\armormaker
```

and (optionally, if you'd like to use the vanilla tool and weapon handles etc...)

```text
original/item           -> ...\.mcreator\templates\textures\texturemaker
original/block          -> ...\.mcreator\templates\textures\texturemaker
original/models/armor   -> ...\.mcreator\templates\textures\armormaker
```

Open, or reopen MCreator to get access to the new texture templates.

## MCreator Custom Texture Examples

Below are some vanilla style item textures created by colorizing the grayscaled vanilla textures.

![alt text](images/custom_textures.png)

## Planned Features

- Automatic install of textures to MCreator templates folder
- Graphical UI package
- Support for a wider range of textures (and maybe models)
- Code cleanup & refactor (ick)
