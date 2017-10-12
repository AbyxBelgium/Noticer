package com.abyx.noticer;

/**
 * Interface that can be used with Dialogs. This interface makes it possible to execute certain
 * tasks in the host activity when a user clicks some button in a dialog. This can be very handy
 * to adapt data contained in the main activity that's not accessible from the outside.
 */
public interface DialogListener {
    public void onPositiveClick();
    public void onNegativeClick();
}
