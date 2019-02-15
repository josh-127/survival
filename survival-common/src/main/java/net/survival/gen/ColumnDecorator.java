package net.survival.gen;

import net.survival.block.Column;
import net.survival.gen.layer.GenLayer;

abstract class ColumnDecorator
{
    public abstract void decorate(long columnPos, Column column, GenLayer biomeMap);
}