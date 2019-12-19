package mypack;

import javax.swing.JOptionPane;

public class Message 
{
	// Show messageBox
		public static void messageBox(String message, String titleBar) {
			JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.INFORMATION_MESSAGE);
		}
}
