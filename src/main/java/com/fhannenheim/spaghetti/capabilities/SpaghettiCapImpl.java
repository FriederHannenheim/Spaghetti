package com.fhannenheim.spaghetti.capabilities;

import com.fhannenheim.spaghetti.SpaghettiType;

public class SpaghettiCapImpl implements ISpaghettiCapability{
    SpaghettiType type = SpaghettiType.NoSpaghetti;
    @Override
    public SpaghettiType getType() {
        return type;
    }

    @Override
    public void setType(SpaghettiType type) {
        this.type = type;
    }
}
