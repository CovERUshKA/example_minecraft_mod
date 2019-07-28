package ru.cover.test.stats;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList
{
    public static final StatBase CONNECTOR_TABLE_INTERACTION = (new StatBasic("stat.connectorTableInteraction", new TextComponentTranslation("stat.connectorInteraction", new Object[0]))).registerStat();
}