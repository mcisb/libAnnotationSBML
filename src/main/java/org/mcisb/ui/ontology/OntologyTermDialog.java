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
import java.util.*;
import javax.swing.*;
import org.mcisb.ontology.*;
import org.mcisb.ui.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTermDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	protected final JPanel mainPanel = new JPanel( new BorderLayout() );

	/**
	 * 
	 */
	protected final transient DefaultDocumentListener documentListener = new DefaultDocumentListener();

	/**
	 * 
	 */
	protected final OntologyTermPanel ontologyTermPanel;

	/**
	 * 
	 */
	private final static int COLUMNS = 20;

	/**
	 * 
	 */
	private final JTextField textField = new JTextField( COLUMNS );

	/**
	 * 
	 */
	private boolean ok = true;

	/**
	 * 
	 * @param owner
	 * @param title
	 * @param modal
	 * @param message
	 * @param searchTerm
	 */
	public OntologyTermDialog( final Dialog owner, final String title, final boolean modal, final String message, final String searchTerm )
	{
		super( owner, title, modal );

		final ResourceBundle resourceBundle = ResourceBundle.getBundle( "org.mcisb.ui.ontology.messages" ); //$NON-NLS-1$

		final GridBagPanel textPanel = new GridBagPanel();

		setSearchTerm( searchTerm );

		textField.getDocument().addDocumentListener( documentListener );

		textPanel.add( new JLabel( resourceBundle.getString( "OntologyTermDialog.searchPrompt" ) ), 0, 0, false, false, true, true, GridBagConstraints.NONE, 1 ); //$NON-NLS-1$
		textPanel.add( textField, 1, 0, true, true, true, true, GridBagConstraints.HORIZONTAL, 1 );

		ontologyTermPanel = new OntologyTermPanel( message );

		mainPanel.add( textPanel, BorderLayout.CENTER );
		mainPanel.add( ontologyTermPanel, BorderLayout.SOUTH );

		final JPanel buttonPanel = new JPanel();
		buttonPanel.add( new JButton( new CloseAction( resourceBundle.getString( "OntologyTermDialog.ok" ), true ) ) ); //$NON-NLS-1$
		buttonPanel.add( new JButton( new CloseAction( resourceBundle.getString( "OntologyTermDialog.cancel" ), false ) ) ); //$NON-NLS-1$

		final Container contentPane = getContentPane();
		contentPane.setLayout( new BorderLayout() );

		contentPane.add( mainPanel, BorderLayout.CENTER );
		contentPane.add( buttonPanel, BorderLayout.SOUTH );

		pack();
	}

	/**
	 * 
	 * @param searchTerm
	 */
	public void setSearchTerm( final String searchTerm )
	{
		if( searchTerm != null )
		{
			textField.setText( searchTerm );
		}

		textField.setEnabled( searchTerm == null );
	}

	/**
	 * 
	 * @param ontologyTerms
	 */
	public void setOntologyTerms( final Collection<OntologyTerm> ontologyTerms )
	{
		ontologyTermPanel.setOntologyTerms( ontologyTerms );
	}

	/**
	 * 
	 * @return OntologyTerm
	 */
	public OntologyTerm getOntologyTerm()
	{
		final Object selection = ontologyTermPanel.getOntologyTerm();
		return ( ok && selection != null ) ? (OntologyTerm)selection : null;
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

		textField.getDocument().removeDocumentListener( documentListener );

		try
		{
			ontologyTermPanel.dispose();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param okStatus
	 */
	void close( final boolean okStatus )
	{
		this.ok = okStatus;
		setVisible( false );
		setOntologyTerms( new ArrayList<OntologyTerm>() );
	}

	/**
	 * 
	 * @author Neil Swainston
	 */
	private class CloseAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		private final boolean okStatus;

		/**
		 * 
		 * @param name
		 * @param okStatus
		 */
		public CloseAction( final String name, final boolean okStatus )
		{
			super( name );
			this.okStatus = okStatus;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		@Override
		public void actionPerformed( @SuppressWarnings("unused") ActionEvent e )
		{
			close( okStatus );
		}
	}
}