package tray.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.osgi.annotation.versioning.ConsumerType;
import org.osgi.service.component.annotations.Reference;
import eim.api.EclipseService;
import eim.api.ListLocationService;
import java.util.Map;

import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Workspace;

@ConsumerType
public class TrayApplication {
	
	private static EclipseService eclService;
	private static ListLocationService listLocSvc;
	private static Map<Integer, Pair<Installation, Workspace>> locationEntries;
	@Reference
	public void bindEclipseService(EclipseService eclsvc) {
		TrayApplication.eclService = eclsvc;
	}

	@Reference
	public void bindLocationService(ListLocationService listLocSvc) {
		TrayApplication.listLocSvc = listLocSvc;
	}
	
	
	public static boolean dispose = false;

	public static void main(String[] args) {
		locationEntries = listLocSvc.getLocationEntries();
		Display display = new Display();
		Shell shell = new Shell(display);
		Image image = new Image(display, 16, 16);
		Image image2 = new Image(display, 16, 16);
		GC gc = new GC(image2);
		gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));
		gc.fillRectangle(image2.getBounds());
		gc.dispose();
		final Tray tray = display.getSystemTray();
		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("Eclipe Installation Manager");
			item.addListener(SWT.Show, event -> System.out.println("show"));
			item.addListener(SWT.Hide, event -> System.out.println("hide"));
			item.addListener(SWT.Selection, event -> System.out.println("selection"));
			item.addListener(SWT.DefaultSelection, event -> System.out.println("default selection"));
			final Menu menu = new Menu(shell, SWT.POP_UP);

			// TODO: Replace this with functional implementation
//			for (int i = 0; i < 8; i++) {
//				MenuItem mi = new MenuItem(menu, SWT.PUSH);
//				mi.setText("Item" + i);
//				mi.addListener(SWT.Selection, event -> System.out.println("selection " + event.widget));
//				if (i == 0)
//					menu.setDefaultItem(mi);
//			}
			for (Map.Entry<Integer, Pair<Installation, Workspace>> entry : locationEntries.entrySet()) {
				Integer key = entry.getKey();
				Pair<Installation, Workspace> val = entry.getValue();
				
				String installationName = val.getElement1().getName();
				String workspaceName = val.getElement2().getName();
				
				MenuItem mi = new MenuItem(menu, SWT.PUSH);
				mi.setText("Entry " + key + "\n"
						+ installationName + "\n"
						+ workspaceName
						);
				mi.addListener(SWT.Selection, event -> eclService.startEntry(key, locationEntries));
				if (key == 1) {
					menu.setDefaultItem(mi);
				}
				
			}
			item.addListener(SWT.MenuDetect, event -> menu.setVisible(true));
			item.setImage(image2);
			item.setHighlightImage(image);
		}
		while (!dispose) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		image2.dispose();
		display.dispose();
	}

}