# Minecraft Resourcepack Merger

Ever wanted to add multiple resource packs to your server, just to discover that you can't?  
I did, which is why I made this! :D

### Installation

#### Prerequisites
- Java 17

#### Downloading

Download the latest jar file from [here](https://github.com/OffsetMonkey538/Minecraft-Resourcepack-Merger/releases).  
Then put it in its own folder, for example `C:/Users/ME/Downloads/pack-merger`.  

### Using

You can run it through the terminal/command prompt like any other java program (`java -jar Minecraft-Resourcepack-Merger-VERSION-all.jar`).  

Now that you have the `packs` folder, it's finally time to add some packs!  
Just drag them into the `packs` folder. and you're don- oh wait...

<br>

You'll have to assign the packs "*priorities*". Just like in Minecraft, you can set some packs to overwrite others.  
This is because multiple packs could change the same texture, you'll have to choose which pack is more important.  
**Important**: A higher priority means the pack will be applied *later*, which means it can *overwrite* the ones below it.

To assign the priority, just rename the pack and include the priority at the start, with a dash between it and the source pack.  
A packs folder could look something like this:
- `0-least-important-pack.zip`
- `1-more-less-important.zip`
- `2-less-important.zip`
- `10-most-important-pack.zip`

Then, once you run the program, it should generate a `pack.zip` file.  
It should also output the SHA1 hash of the pack to the console, which is  
useful for adding the pack to a server.