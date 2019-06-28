package me.pwns.logistica.world.regen;

import javax.annotation.Nullable;

public class BlockGroupContainer {
    private BlockGroup childGroup = null;


    public BlockGroupContainer(BlockGroup group) {
        this.childGroup = group;
        childGroup.setParent(this);
    }

    public BlockGroupContainer() {
    }

    public void addBlock(SavedBlockState block) {
        childGroup.addBlock(block);
    }


    public BlockGroup getChildGroup(){
        return this.childGroup;
    }


    public void setChildGroup(BlockGroup blockGroup){
        this.childGroup = blockGroup;
        blockGroup.setParent(this);
    }

    public void removeChildGroup(){
        this.childGroup = null;
    }

    public void joinGroup(BlockGroup newGroup) {
        childGroup = childGroup.joinGroup(newGroup);

    }

    public void restore() {
        // TODO this is where it's broke, fix it
        this.childGroup.restore();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof BlockGroupContainer)) {
            return false;
        }

        BlockGroupContainer blockGroupContainer = (BlockGroupContainer) o;

        return blockGroupContainer.childGroup == childGroup;
    }
}
