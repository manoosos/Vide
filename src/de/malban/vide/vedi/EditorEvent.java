/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.malban.vide.vedi;

/**
 *
 * @author malban
 */
public class EditorEvent {
    public static final int EV_CARET_CHANGED = 0;
    public static final int EV_TEXT_CHANGED = 1;
    public static final int EV_TEXT_UNDO = 2;
    public static final int EV_TEXT_REDO = 3;
    
    EditorPanelFoundation source;
    int type;
}