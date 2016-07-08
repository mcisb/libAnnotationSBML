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
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import org.mcisb.ontology.*;
import org.mcisb.ui.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologySearchAction extends AbstractAction
{
	/**
	 * 
	 */
	public final static String OPTIONS = "OPTIONS"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private final PropertyChangeSupport support = new PropertyChangeSupport( this );

	/**
	 * 
	 */
	final OntologyLookupServiceClient client;

	/**
	 * 
	 */
	String ontologyName;

	/**
	 * 
	 */
	String searchTerm;

	/**
	 * 
	 * @param client
	 */
	public OntologySearchAction( final OntologyLookupServiceClient client )
	{
		super( ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ).getString( "OntologySearchAction.searchActionLabel" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		this.client = client;
	}

	/**
	 * 
	 * @param ontologyName
	 */
	public void setOntologyName( final String ontologyName )
	{
		this.ontologyName = ontologyName;
	}

	/**
	 * 
	 * @param searchTerm
	 */
	public void setSearchTerm( final String searchTerm )
	{
		this.searchTerm = searchTerm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed( final ActionEvent e )
	{
		Container owner = null;

		if( e.getSource() instanceof JComponent )
		{
			owner = ( (JComponent)e.getSource() ).getTopLevelAncestor();
		}

		performSearch( owner );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractAction#addPropertyChangeListener(java.beans.
	 * PropertyChangeListener)
	 */
	@Override
	public synchronized void addPropertyChangeListener( PropertyChangeListener l )
	{
		support.addPropertyChangeListener( l );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractAction#removePropertyChangeListener(java.beans.
	 * PropertyChangeListener)
	 */
	@Override
	public synchronized void removePropertyChangeListener( PropertyChangeListener l )
	{
		support.removePropertyChangeListener( l );
	}

	/**
	 * 
	 * @param options
	 */
	void setOptions( Collection<OntologyTerm> options )
	{
		support.firePropertyChange( OPTIONS, null, options );
	}

	/**
	 * 
	 * @param owner
	 */
	private void performSearch( final Container owner )
	{
		Runnable runnable = new Runnable()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run()
			{
				try
				{
					if( owner != null )
					{
						SwingUtilities.invokeAndWait( new Runnable()
						{
							@Override
							public void run()
							{
								owner.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
							}
						} );
					}

					setOptions( new ArrayList<OntologyTerm>() );

					Collection<OntologyTerm> options = new ArrayList<>();

					if( searchTerm.length() > 0 )
					{
						Map<String,String> terms = OntologyLookupServiceClient.getTermsByName( searchTerm.trim(), ontologyName );

						for( Iterator<Map.Entry<String,String>> iterator = terms.entrySet().iterator(); iterator.hasNext(); )
						{
							final Map.Entry<String,String> entry = iterator.next();
							final String key = entry.getKey();
							final String value = entry.getValue();

							if( key.indexOf( Ontology.ID_SEPARATOR ) != -1 )
							{
								final StringTokenizer tokenizer = new StringTokenizer( key, Ontology.ID_SEPARATOR );

								// TODO: OntologyTerms can only currently be
								// generated from MIRIAM-supported Ontologies.
								// This needs fixing...
								final OntologyTerm ontologyTerm = OntologyUtils.getInstance().getOntologyTerm( tokenizer.nextToken(), tokenizer.nextToken() );

								if( ontologyTerm != null )
								{
									ontologyTerm.setName( value );
									options.add( ontologyTerm );
								}
							}
						}
					}

					setOptions( options );
				}
				catch( Exception ex )
				{
					final JDialog errorDialog = new ExceptionComponentFactory( true ).getExceptionDialog( owner, ExceptionUtils.toString( ex ), ex );
					ComponentUtils.setLocationCentral( errorDialog );
					errorDialog.setVisible( true );
				}
				finally
				{
					if( owner != null )
					{
						try
						{
							SwingUtilities.invokeAndWait( new Runnable()
							{
								@Override
								public void run()
								{
									owner.setCursor( Cursor.getDefaultCursor() );
								}
							} );
						}
						catch( Exception ex )
						{
							final JDialog errorDialog = new ExceptionComponentFactory( true ).getExceptionDialog( owner, ExceptionUtils.toString( ex ), ex );
							ComponentUtils.setLocationCentral( errorDialog );
							errorDialog.setVisible( true );
						}
					}
				}
			}
		};

		new Thread( runnable ).start();
	}
}