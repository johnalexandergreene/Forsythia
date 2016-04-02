package org.fleen.forsythia.app.grammarEditor.util;

import javax.swing.JPanel;

/*
 * interface implemented by editor classes that use a viewer panel, so we can access it
 */
public interface HasViewer{
  
  JPanel getViewer();

}
