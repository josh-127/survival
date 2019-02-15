package net.survival.gen;

import net.survival.gen.layer.GenLayer;

abstract class ColumnDecorator
{
    public abstract void decorate(long columnPos, ColumnPrimer primer, GenLayer biomeMap);
}