package com.fhannenheim.spaghetti.capabilities;

import com.fhannenheim.spaghetti.SpaghettiType;

public interface ISpaghettiCapability {
    SpaghettiType getType();
    void setType(SpaghettiType type);
}
