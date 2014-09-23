/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.umich.asbarber.fm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Aaron
 */
public class ToggleButton extends JButton {

    private boolean isOn;
    private String onText, offText;
    private ToggleListener toggleListener;

    public ToggleButton(String onText, String offText) {
        this(onText, offText, false);
    }

    public ToggleButton(String onText, String offText, boolean isOn) {
        this.isOn = isOn;
        this.onText = onText;
        this.offText = offText;
        this.toggleListener = new ToggleListener();

        if (isOn) {
            super.setText(onText);
        } else {
            super.setText(offText);
        }

        this.addActionListener(toggleListener);
    }

    public boolean isOn() {
        return isOn;
    }

    public void toggle() {
        if (isOn) {
            super.setText(offText);
        } else {
            super.setText(onText);
        }

        //Reverses
        isOn = !isOn;
    }

    private class ToggleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            toggle();
        }

    }
}
