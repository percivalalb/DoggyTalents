package modcompiler;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * @author Alex Barter (10AS)
 */
public interface Output {

	public void print(String text, Object... format);
	
	public void println(String text, Object... format);
	
	public boolean clear();
	
	public static class Default implements Output {

		@Override
		public void print(String text, Object... format) {
			System.out.print(String.format(text, format));
		}

		@Override
		public void println(String text, Object... format) {
			System.out.println(String.format(text, format));
			
		}

		@Override
		public boolean clear() {
			return false;
		}
	}
	
	public static class Silent implements Output {

		@Override
		public void print(String text, Object... format) {
			
		}

		@Override
		public void println(String text, Object... format) {
			
		}

		@Override
		public boolean clear() {
			return true;
		}
	}
	
	public static class TextComponent implements Output {

		public JTextArea textComponent;
		public JScrollPane pane;
		
		public TextComponent(JTextArea textComponent, final JScrollPane pane) {
			this.textComponent = textComponent;
			this.pane = pane;
			// Disable the auto scroll :
		    ((DefaultCaret)textComponent.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		    
		    // Add a listener to the vertical scroll bar :
		    pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

		        private int _val = 0;
		        private int _ext = 0;
		        private int _max = 0;

		        private final BoundedRangeModel _model = pane.getVerticalScrollBar().getModel();

		        @Override
		        public void adjustmentValueChanged(AdjustmentEvent e) {

		            int newMax = _model.getMaximum();

		            // If the new max has changed and if we were scrolled to bottom :
		            if (newMax != _max && (_val + _ext == _max) ) {

		                // Scroll to bottom :
		                _model.setValue(_model.getMaximum() - _model.getExtent());
		            }

		            // Save the new values :
		            _val = _model.getValue();
		            _ext = _model.getExtent();
		            _max = _model.getMaximum();
		        }
		    });
		}
		
		@Override
		public void print(String text, Object... format) {
			this.textComponent.append(String.format(text, format));
		}
	

		@Override
		public void println(String text, Object... format) {
			this.print(text + "\n", format);
		}
		
		@Override
		public boolean clear() {
			this.textComponent.setText("");
			return true;
		}

	}

}
