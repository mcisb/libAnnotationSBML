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

import java.awt.*;
import java.beans.*;
import java.util.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTermSearchDialog extends OntologyTermDialog implements PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final transient OntologyTermSearcher ontologyTermSearcher = new OntologyTermSearcher();

	/**
	 * 
	 */
	private final OntologySourcePanel ontologySourcePanel;

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param message
	 * @param ontologySourceMap
	 */
	public OntologyTermSearchDialog( final Dialog owner, final String title, final boolean modal, final String message, final Map<Object,OntologySource> ontologySourceMap )
	{
		super( owner, title, modal, message, null );

		ontologySourcePanel = new OntologySourcePanel( ontologySourceMap );

		ontologyTermSearcher.addPropertyChangeListener( this );
		ontologyTermSearcher.addPropertyChangeListener( ontologyTermPanel );
		documentListener.addPropertyChangeListener( ontologyTermSearcher );
		ontologySourcePanel.addPropertyChangeListener( ontologyTermSearcher );
		ontologySourcePanel.init();

		mainPanel.add( ontologySourcePanel, BorderLayout.NORTH );
		pack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose()
	{
		super.dispose();
		ontologyTermSearcher.removePropertyChangeListener( this );
		ontologyTermSearcher.removePropertyChangeListener( ontologyTermPanel );
		documentListener.removePropertyChangeListener( ontologyTermSearcher );
		ontologySourcePanel.removePropertyChangeListener( ontologyTermSearcher );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		if( evt.getPropertyName().equals( OntologyTermSearcher.SEARCHING ) && evt.getNewValue() instanceof Boolean )
		{
			setCursor( ( (Boolean)evt.getNewValue() ).booleanValue() ? Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) : Cursor.getDefaultCursor() );
		}
	}
}