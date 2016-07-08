/*******************************************************************************
 * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 * 
 * Copyright (C) 2007 University of Manchester
 * 
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ui.ontology;

import java.io.*;
import java.util.*;

import org.json.simple.parser.*;
import org.mcisb.ui.wizard.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyChooserWizard extends Wizard
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param bean
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public OntologyChooserWizard( final GenericBean bean ) throws IOException, ParseException
	{
		super( bean, null );

		final ResourceBundle resourceBundle = ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ); //$NON-NLS-1$
		addWizardComponent( new ChooserWizardComponent( bean, new OntologyChooserPanel( resourceBundle.getString( "OntologyChooserWizard.title" ) ) ) ); //$NON-NLS-1$
		init();
	}
}
