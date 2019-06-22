package me.pwns.logistica.world.regen;

public class BlockGroupContainer {
    private BlockGroup childGroup;

    public BlockGroupContainer(SavedBlockState block) {
        this.childGroup = new BlockGroup(block, this);
    }

    public void addBlock(SavedBlockState block) {
        childGroup.addBlock(block);
    }

    public BlockGroup getChildGroup(){
        return this.childGroup;
    }

    public void joinGroup(BlockGroup newGroup) {
        childGroup = childGroup.joinGroup(newGroup); // TODO is this dumb? Should we be making new?
    }

}
