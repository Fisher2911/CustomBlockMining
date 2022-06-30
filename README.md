# CustomBlockMining
Big thanks to [PacketEvents](https://github.com/retrooper/packetevents) for their amazing packet library.

## How to use

There are three main methods that are used to manage the custom block mining system. They are:
- [BlockManager#startMining](https://github.com/Fisher2911/CustomBlockMining/blob/1a30efa580b4ce9e58d2fb3256e4ad6162202af9/src/main/java/io/github/fisher2911/customblockmining/BlockBreakManager.java#L24)
- [BlockManager#reset](https://github.com/Fisher2911/CustomBlockMining/blob/1a30efa580b4ce9e58d2fb3256e4ad6162202af9/src/main/java/io/github/fisher2911/customblockmining/BlockBreakManager.java#L30)
- [BlockManager#cancel](https://github.com/Fisher2911/CustomBlockMining/blob/1a30efa580b4ce9e58d2fb3256e4ad6162202af9/src/main/java/io/github/fisher2911/customblockmining/BlockBreakManager.java#L36)

BlockManager#startMining should be called when a player begins to mine a block
<br>

BlockManager#reset should be called when a player breaks a block, and you want the block to appear without any cracks
<br>

BlockManager#cancel should be called when you want a block to no longer have a custom break time and to remove any cracks from it

To begin, you should create an instance of the class [BlockMineListener](https://github.com/Fisher2911/CustomBlockMining/blob/master/src/main/java/io/github/fisher2911/customblockmining/BlockMineListener.java).
<br>
You can see an example at [CustomBlockMining#registerTests](https://github.com/Fisher2911/CustomBlockMining/blob/1a30efa580b4ce9e58d2fb3256e4ad6162202af9/src/main/java/io/github/fisher2911/customblockmining/CustomBlockMining.java#L61).

If you have any questions, please [create an issue](https://github.com/Fisher2911/CustomBlockMining/issues).