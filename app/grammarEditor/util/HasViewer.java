package org.fleen.forsythia.app.grammarEditor.util;

import javax.swing.JPanel;

/*
 * interface implemented by editor classes that use a viewer panel, so we can access it
 * TODO are we still using this?
 */
public interface HasViewer{
  
  JPanel getViewer();

}
