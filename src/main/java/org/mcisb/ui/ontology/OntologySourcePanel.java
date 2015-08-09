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
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.mcisb.ontology.*;
import org.mcisb.ui.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologySourcePanel extends GridBagPanel implements ListSelectionListener, Disposable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	final static ResourceBundle resourceBundle = ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private final transient Map<Object,OntologySource> nameToOntologySource;

	/**
	 * 
	 */
	private transient OntologySource ontologySource;

	/**
	 * 
	 */
	private JList<?> list;

	/**
	 * 
	 * @param nameToOntologySource
	 */
	public OntologySourcePanel( final Map<Object,OntologySource> nameToOntologySource )
	{
		this.nameToOntologySource = nameToOntologySource;

		list = new JList<>( nameToOntologySource.keySet().toArray() );
		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		list.addListSelectionListener( this );
		add( new JLabel( resourceBundle.getString( "OntologySourcePanel.ontologyPrompt" ) ), 0, 0, false, false, true, true, GridBagConstraints.NONE, 1 ); //$NON-NLS-1$
		add( new JScrollPane( list ), 1, 0, true, true, true, true, GridBagConstraints.HORIZONTAL, 1 );
	}

	/**
	 * 
	 */
	public void init()
	{
		final int FIRST = 0;
		list.setSelectedIndex( FIRST );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged( ListSelectionEvent e )
	{
		if( !e.getValueIsAdjusting() && e.getSource() instanceof JList )
		{
			final Object selection = ( (JList<?>)e.getSource() ).getSelectedValue();
			setOntologySource( nameToOntologySource.get( selection ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.util.Disposable#dispose()
	 */
	@Override
	public void dispose()
	{
		list.removeListSelectionListener( this );
	}

	/**
	 * @param ontologySource
	 */
	public void setOntologySource( final OntologySource ontologySource )
	{
		final OntologySource oldOntologySource = this.ontologySource;
		this.ontologySource = ontologySource;
		firePropertyChange( org.mcisb.ontology.PropertyNames.ONTOLOGY_SOURCE, oldOntologySource, ontologySource );
	}
}