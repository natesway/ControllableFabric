package com.mrcrayfish.controllable.client.util;

import net.minecraft.client.gui.components.AbstractSelectionList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: MrCrayfish
 */
public class ReflectUtil
{
    //private static final Method ABSTRACT_SELECTION_LIST_GET_ROW_TOP = ObfuscationReflectionHelper.findMethod(AbstractSelectionList.class, "m_7610_", int.class);
    //private static final Method ABSTRACT_SELECTION_LIST_GET_ROW_BOTTOM = ObfuscationReflectionHelper.findMethod(AbstractSelectionList.class, "m_93485_", int.class);

    public static int getAbstractListRowTop(AbstractSelectionList<?> list, int index)
    {
        return (int) list.getRowTop(index);
    }

    public static int getAbstractListRowBottom(AbstractSelectionList<?> list, int index)
    {
        return (int) list.getRowBottom(index);
    }
}
