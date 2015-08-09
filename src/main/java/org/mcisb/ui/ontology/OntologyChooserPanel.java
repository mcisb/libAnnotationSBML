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
import java.net.*;
import java.rmi.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.xml.rpc.*;
import org.mcisb.ontology.*;
import org.mcisb.ui.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyChooserPanel extends ParameterPanel implements Chooser, ItemListener, FocusListener, ListSelectionListener, PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	final DefaultListModel<Object> model = new DefaultListModel<>();
	
	/**
	 * 
	 */
	private final OntologyLookupServiceClient client;
	
	/**
	 * 
	 */
	private final OntologySearchAction searchAction;
	
	/**
	 * 
	 */
	private final JComboBox<String> ontologyComboBox;
	
	/**
	 * 
	 */
	private final JList<Object> list;
	
	/**
	 * 
	 */
	private final JTextField searchTextField;
	
	/**
	 * 
	 */
	private final HyperlinkLabel olsLabel;
	
	/**
	 * 
	 */
	private transient MouseListener mouseListener;
	
	
	/**
	 * 
	 * @param title
	 * @throws RemoteException
	 * @throws ServiceException
	 * @throws MalformedURLException
	 */
	public OntologyChooserPanel( final String title ) throws RemoteException, ServiceException, MalformedURLException
	{
		super( title );

		client = new OntologyLookupServiceClient();
		
		list = new JList<>( model );
		list.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		list.getSelectionModel().addListSelectionListener( this );

		ontologyComboBox = new JComboBox<>( new Vector<>( new TreeSet<>( OntologyLookupServiceClient.getOntologyNames().keySet() ) ) );
		ontologyComboBox.addItemListener( this );
		
		final int COLUMNS = 10;
		searchTextField = new JTextField( COLUMNS );
		searchTextField.addMouseListener( getMouseListener() );
		searchTextField.addFocusListener( this );
		
		searchAction = new OntologySearchAction( client );
		searchAction.addPropertyChangeListener( this );
		searchAction.setOntologyName( (String)ontologyComboBox.getSelectedItem() );

		final ResourceBundle resourceBundle = ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ); //$NON-NLS-1$

		olsLabel = new HyperlinkLabel( resourceBundle.getString( "OntologyChooserPanel.olsLabel" ), new ResourceFactory().getImageIcon( resourceBundle.getString( "OntologyChooserPanel.olsIcon" ) ), SwingConstants.LEFT, new URL( resourceBundle.getString( "OntologyChooserPanel.olsUrl" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add( new JLabel( resourceBundle.getString( "OntologyChooserPanel.ontologyLabel" ) ), 0, 0, false, false ); //$NON-NLS-1$
		add( ontologyComboBox, 1, 0, true, false, GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER );
		add( new JLabel( resourceBundle.getString( "OntologyChooserPanel.searchLabel" ) ), 0, 1, false, false ); //$NON-NLS-1$
		add( searchTextField, 1, 1, false, false );
		add( new JButton( searchAction ), 2, 1, true, false, GridBagConstraints.HORIZONTAL );
		add( new JScrollPane( list ), 0, 2, true, false, GridBagConstraints.BOTH, GridBagConstraints.REMAINDER );
		add( olsLabel, 0, 3, true, true, GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.Chooser#clearSelection()
	 */
	@Override
	public void clearSelection() 
	{
		list.clearSelection();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.Chooser#getSelection()
	 */
	@Override
	public Object getSelection()
	{
		return list.getSelectedValuesList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.Chooser#setSelection(java.lang.Object)
	 */
	@Override
	public void setSelection( Object object )
	{
		list.setSelectedValue( object , true );
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.util.Disposable#dispose()
	 */
	@Override
	public void dispose()
	{
		list.getSelectionModel().removeListSelectionListener( this );
		ontologyComboBox.removeItemListener( this );
		
		searchAction.removePropertyChangeListener( this );
		olsLabel.dispose();
		
		searchTextField.removeMouseListener( getMouseListener() );
		searchTextField.addFocusListener( this );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged( ItemEvent e )
	{
		searchAction.setOntologyName( (String)e.getItem() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged( @SuppressWarnings("unused") ListSelectionEvent e )
	{
		setValid( list.getSelectedValuesList().size() > 0 );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained( @SuppressWarnings("unused") final FocusEvent e ) 
	{
		// No implementation.	
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost( @SuppressWarnings("unused") final FocusEvent e )
	{
		searchAction.setSearchTerm( searchTextField.getText() );	
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange( final PropertyChangeEvent e )
	{
		if( e.getPropertyName().equals( OntologySearchAction.OPTIONS ) )
		{
			Runnable runnable = new Runnable()
			{
				/*
				 * (non-Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run()
				{
					synchronized( model )
					{
						model.clear();
						
						if( e.getNewValue() instanceof Collection )
						{
							final Collection<Object> options = new TreeSet<>( (Collection<?>)e.getNewValue() );
							
							for( Iterator<Object> iterator = options.iterator(); iterator.hasNext(); )
							{
								model.addElement( iterator.next() );
							}
						}
					}
				}
			};
			
			SwingUtilities.invokeLater( runnable );
		}
	}
	
	/**
	 * 
	 * @return MouseListener
	 */
	private MouseListener getMouseListener()
	{
		if( mouseListener == null )
		{
			mouseListener = new JMenuMouseListener( new JTextComponentMenu() );
		}
		
		return mouseListener;
	}
}