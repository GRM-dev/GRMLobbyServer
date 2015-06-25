package pl.grm.lobby.server.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JTabbedPane;

public class ClosableTabbedPane extends JTabbedPane {

	private class TabCloseUI implements MouseListener, MouseMotionListener {

		private ClosableTabbedPane tabbedPane;
		private int closeX = 0, closeY = 0, meX = 0, meY = 0;
		private int selectedTab;
		private final int width = 8, height = 8;
		private Rectangle rectangle = new Rectangle(0, 0, this.width, this.height);

		public TabCloseUI(ClosableTabbedPane pane) {

			this.tabbedPane = pane;
			this.tabbedPane.addMouseMotionListener(this);
			this.tabbedPane.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent me) {}

		@Override
		public void mouseDragged(MouseEvent me) {}

		@Override
		public void mouseEntered(MouseEvent me) {}

		@Override
		public void mouseExited(MouseEvent me) {}

		@Override
		public void mouseMoved(MouseEvent me) {
			this.meX = me.getX();
			this.meY = me.getY();
			if (this.mouseOverTab(this.meX, this.meY)) {
				this.controlCursor();
				this.tabbedPane.repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent me) {}

		@Override
		public void mouseReleased(MouseEvent me) {
			if (this.closeUnderMouse(me.getX(), me.getY())) {
				boolean isToCloseTab = ClosableTabbedPane.this.tabAboutToClose(this.selectedTab);
				if (isToCloseTab && (this.selectedTab > -1)) {
					this.tabbedPane.removeTabAt(this.selectedTab);
				}
				this.selectedTab = this.tabbedPane.getSelectedIndex();
			}
		}

		public void paint(Graphics g) {

			int tabCount = this.tabbedPane.getTabCount();
			for (int j = 0; j < tabCount; j++) {
				if (this.tabbedPane.getComponent(j).isShowing()) {
					int x = (this.tabbedPane.getBoundsAt(j).x + this.tabbedPane.getBoundsAt(j).width) - this.width - 5;
					int y = this.tabbedPane.getBoundsAt(j).y + 5;
					this.drawClose(g, x, y);
					break;
				}
			}
			if (this.mouseOverTab(this.meX, this.meY)) {
				this.drawClose(g, this.closeX, this.closeY);
			}
		}

		private boolean closeUnderMouse(int x, int y) {
			this.rectangle.x = this.closeX;
			this.rectangle.y = this.closeY;
			return this.rectangle.contains(x, y);
		}

		private void controlCursor() {
			if (this.tabbedPane.getTabCount() > 0) {
				if (this.closeUnderMouse(this.meX, this.meY)) {
					this.tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
					if (this.selectedTab > -1) {
						this.tabbedPane.setToolTipTextAt(this.selectedTab,
								"Close " + this.tabbedPane.getTitleAt(this.selectedTab));
					}
				} else {
					this.tabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					if (this.selectedTab > -1) {
						this.tabbedPane.setToolTipTextAt(this.selectedTab, "");
					}
				}
			}
		}

		private void drawClose(Graphics g, int x, int y) {
			if ((this.tabbedPane != null) && (this.tabbedPane.getTabCount() > 0)) {
				Graphics2D g2 = (Graphics2D) g;
				this.drawColored(g2, this.isUnderMouse(x, y) ? Color.RED : Color.WHITE, x, y);
			}
		}

		private void drawColored(Graphics2D g2, Color color, int x, int y) {
			g2.setStroke(new BasicStroke(5, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
			g2.setColor(Color.BLACK);
			g2.drawLine(x, y, x + this.width, y + this.height);
			g2.drawLine(x + this.width, y, x, y + this.height);
			g2.setColor(color);
			g2.setStroke(new BasicStroke(3, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
			g2.drawLine(x, y, x + this.width, y + this.height);
			g2.drawLine(x + this.width, y, x, y + this.height);

		}

		private boolean isUnderMouse(int x, int y) {
			if ((Math.abs(x - this.meX) < this.width) && (Math.abs(y - this.meY) < this.height)) { return true; }
			return false;
		}

		private boolean mouseOverTab(int x, int y) {
			int tabCount = this.tabbedPane.getTabCount();
			for (int j = 0; j < tabCount; j++) {
				if (this.tabbedPane.getBoundsAt(j).contains(this.meX, this.meY)) {
					this.selectedTab = j;
					this.closeX = (this.tabbedPane.getBoundsAt(j).x + this.tabbedPane.getBoundsAt(j).width) - this.width
							- 5;
					this.closeY = this.tabbedPane.getBoundsAt(j).y + 5;
					return true;
				}
			}
			return false;
		}
	}

	private static final long serialVersionUID = 1L;
	private TabCloseUI closeUI = new TabCloseUI(this);

	@Override
	public void addTab(String title, Component component) {
		super.addTab(title + "  ", component);
	}

	public String getTabTitleAt(int index) {
		return super.getTitleAt(index).trim();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		this.closeUI.paint(g);
	}

	public boolean tabAboutToClose(int tabIndex) {
		if ((this.getComponentCount() != 0) && (this.getComponentAt(tabIndex) != null)) { return true; }
		return false;
	}

}
