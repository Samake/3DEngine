package com.txgui.events;

import java.util.ArrayList;
import java.util.List;

import com.txgui.core.TXElement;

public class TXGUIEventHandler implements TXGUIEvents {

    private List<TXGUIEvents> listeners = new ArrayList<TXGUIEvents>();

    public TXGUIEventHandler addListener(TXGUIEvents toAdd) {
        listeners.add(toAdd);
        return this;
    }

    public void onButtonClicked(TXElement element) {
        for (TXGUIEvents hl : listeners) {
        	hl.onButtonClicked(element);
        }
    }
}
