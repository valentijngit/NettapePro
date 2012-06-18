package net.nettapepro.client.gui.ui.awindow;

import net.nettapepro.client.gui.ui.util.WidgetUtils;
import net.nettapepro.client.gui.util.StringUtil;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public abstract class AbstractL1Window implements IWindow, Listener {

	private static final Logger logger = Logger.getLogger(AbstractL1Window.class);

	protected final Shell shell;

	protected int width;
	protected int height;

	public AbstractL1Window() {
		this(SWT.NONE);
	}

	public AbstractL1Window(Shell parent) {
		this(parent, SWT.NONE, "", 0, 0);
	}

	public AbstractL1Window(Shell parent, int style) {
		this(parent, style, "", 0, 0);
	}

	public AbstractL1Window(int style) {
		this(null, style, "", 0, 0);
	}

	public AbstractL1Window(String title) {
		this(null, SWT.NONE, title, 0, 0);
	}

	public AbstractL1Window(Shell parent, int style, String title, int width, int height) {
		Rectangle rect = Display.getDefault().getPrimaryMonitor().getBounds();
		this.width = width;
		this.height = height;
		if (this.width == 0) {
			if (rect.width > 1000) {
				this.width = 800;
			} else {
				this.width = 640;
			}
		}
		if (this.height == 0) {
			if (rect.height > 700) {
				this.height = 600;
			} else {
				this.height = 480;
			}
		}

		if (parent != null) {
			this.shell = new Shell(parent, SWT.TITLE | SWT.CLOSE | style);
		} else {
			this.shell = new Shell(Display.getDefault().getActiveShell(), SWT.TITLE | SWT.CLOSE
					| style);
		}
		this.shell.setSize(this.width, this.height);
		this.shell.setLayout(new GridLayout(2, false));
		if ((title != null) && (title.length() > 0)) {
			this.shell.setText(title);
		}
	}

	public void open() {
		this.ensureMinimalSize();
		this.open(true);
	}

	public void open(boolean pack) {
		Display d = null;
		try {
			if ((this.shell == null) || this.shell.isDisposed()) {
				return;
			}
			d = this.shell.getDisplay();
			if (pack) {
				this.shell.pack();
			}
			if (this.shell.getParent() != null) {
				this.shell.setLocation(WidgetUtils.centerInWindow(this.shell.getParent().getBounds(),
						this.shell.getBounds()));
			} else {
				this.shell.setLocation(WidgetUtils.centerInWindow(Display.getDefault().getBounds(),
						this.shell.getBounds()));
				// this.shell.setLocation(Display.getDefault().getBounds().width
				// / 2 - this.shell.getBounds().width / 2,
				// Display.getDefault().getBounds().height / 2
				// - this.shell.getBounds().height / 2);
			}
			this.shell.open();
			while ((this.shell != null) && !this.shell.isDisposed()) {
				if ((d != null) && !d.readAndDispatch()) {
					d.sleep();
				}
			}
		} catch (Exception ex) {
			AbstractL1Window.logger.fatal(ex, ex);
		}
	}

	public void setSize(int width, int height) {
		this.shell.setSize(width, height);
	}

	public void setImage(Image image) {
		this.shell.setImage(image);
	}

	public void setText(String text) {
		if (StringUtil.isNotEmpty(text)) {
			this.shell.setText(text);
		}
	}

	private void ensureMinimalSize() {
		boolean correctionNeeded = Boolean.FALSE;
		try {
			Rectangle rect = this.shell.getBounds();
			if (rect.width < 350) {
				rect.width = 350;
				correctionNeeded = Boolean.TRUE;
			}
			if (rect.height < 120) {
				rect.height = 120;
				correctionNeeded = Boolean.TRUE;
			}
			if (correctionNeeded) {
				this.shell.setBounds(rect);
			}
		} catch (Exception e) {
			AbstractL1Window.logger.fatal(e, e);
		}
	}

}
