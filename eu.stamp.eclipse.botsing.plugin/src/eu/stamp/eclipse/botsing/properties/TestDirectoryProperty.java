/*******************************************************************************
 * Copyright (c) 2018 Atos
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Ricardo José Tejada García (Atos) - main developer
 * Jesús Gorroñogoitia (Atos) - architect
 * Initially developed in the context of STAMP EU project https://www.stamp-project.eu
 *******************************************************************************/
package eu.stamp.eclipse.botsing.properties;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.PlatformUI;

/**
 * This is the property associated with the directory to store the 
 * generated tests
 */
public class TestDirectoryProperty extends BotsingExplorerField {

	public TestDirectoryProperty(String defaultValue, String key, String name) {
		super(defaultValue, key, name);
	}

	@Override
	protected String openExplorer() {
		
		DirectoryDialog dialog = 
				new DirectoryDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());
		
		dialog.setMessage(" Select test directory ");
		dialog.setText(" Test directory selection ");
		
		String result = dialog.open();
		
		callListeners();
		return result;
	}

}
