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
import org.mcisb.ui.tracking.*;
import org.mcisb.ui.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyPanel extends ObjectParameterPanel implements SampleParameterPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private final JList<?> sampleList = new JList<>( new DefaultListModel<>() );
	
	/**
	 * 
	 * @param title
	 */
	public OntologyPanel( final String title )
	{
		super( title, new JList<>( new DefaultListModel<>() ), true );
		
		setValid( true );
		
		sampleList.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		sampleList.addListSelectionListener( this );
		
		// Add components:
		add( new JLabel( resourceBundle.getString( "OntologyPanel.ontology" ) ), 0, 0, false, false, true, false, GridBagConstraints.HORIZONTAL ); //$NON-NLS-1$
		add( new JScrollPane( objectList ), 1, 0, true, true, true, false, GridBagConstraints.BOTH );
		add( new JLabel( resourceBundle.getString( "OntologyPanel.sample" ) ), 0, 1, false, true, true, false, GridBagConstraints.HORIZONTAL ); //$NON-NLS-1$
		add( new JScrollPane( sampleList ), 1, 1, true, true, true, true, GridBagConstraints.BOTH );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.ObjectParameterPanel#dispose()
	 */
	@Override
	public void dispose() throws Exception
	{
		super.dispose();
		sampleList.removeListSelectionListener( this );
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.tracking.SampleParameterPanel#setSamples(java.util.Collection)
	 */
	@Override
	public void setSamples( Collection<?> samples )
	{
		DefaultListModel<Object> model = ( (DefaultListModel<Object>)sampleList.getModel() );
		model.clear();
		
		for( Iterator<?> iterator = samples.iterator(); iterator.hasNext(); )
		{
			model.addElement( iterator.next() );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.ObjectParameterPanel#getObject()
	 */
	@Override
	public Object getObject()
	{
		Collection<Object> samples = new ArrayList<>();
		ListModel<?> model = sampleList.getModel();
		
		for( int i = 0; i < model.getSize(); i++ )
		{
			samples.add( model.getElementAt( i ) );
		}
		
		return samples;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.util.ObjectParameterPanel#save(java.lang.Object)
	 */
	@Override
	protected void save( Object object )
	{
		if( object != null )
		{
			final OntologyTerm ontologyTerm = (OntologyTerm)object;
			final java.util.List<?> samples = sampleList.getSelectedValuesList();
			
			if( samples != null )
			{
				for( int i = 0; i < samples.size(); i++ )
				{
					UniqueObject sample = (UniqueObject)samples.get( i );
					sample.addOntologyTerm( ontologyTerm );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged( ListSelectionEvent e )
	{
		super.valueChanged( e );
		
		final OntologyTerm ontologyTerm = (OntologyTerm)objectList.getSelectedValue();
		final ListModel<?> sampleListModel = sampleList.getModel();
		
		if( e.getSource().equals( objectList ) )
		{
			final Collection<Integer> indices = new ArrayList<>();
			
			for( int i = 0; i < sampleListModel.getSize(); i++ )
			{
				final UniqueObject sample = (UniqueObject)sampleListModel.getElementAt( i );
				
				if( sample.getOntologyTerms().contains( ontologyTerm ) )
				{
					indices.add( Integer.valueOf( i ) );
				}
			}
			
			sampleList.setSelectedIndices( CollectionUtils.toIntArray( indices ) );
		}
		else
		{
			for( int i = e.getFirstIndex(); i <= e.getLastIndex(); i++ )
			{
				// Deselection?
				if( !sampleList.isSelectedIndex( i ) )
				{
					if( i > 0 && i < sampleListModel.getSize() )
					{
						UniqueObject sample = (UniqueObject)sampleListModel.getElementAt( i );
						sample.removeOntologyTerm( ontologyTerm );
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.tracking.Manager#deleteObject()
	 */
	@Override
	public void deleteObject()
	{
		previousIndex = -1;
		
		final ListModel<?> ontologyTermListModel = objectList.getModel();
		final OntologyTerm ontologyTerm = (OntologyTerm)objectList.getSelectedValue();
		
		if( ontologyTermListModel instanceof DefaultListModel )
		{
			( (DefaultListModel<Object>)ontologyTermListModel ).removeElement( ontologyTerm );
		}
		
		final ListModel<?> sampleListModel = sampleList.getModel();
		
		for( int i = 0; i < sampleListModel.getSize(); i++ )
		{
			final UniqueObject sample = (UniqueObject)sampleListModel.getElementAt( i );
			sample.removeOntologyTerm( ontologyTerm );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ui.tracking.Manager#newObject()
	 */
	@Override
	public void newObject()
	{
		final Container parent = getTopLevelAncestor();
		Frame frame = null;
		
		if( parent instanceof Frame )
		{
			frame = (Frame)parent;
		}
		
		final JDialog dialog = new JDialog( frame, true );
		
		final GenericBean bean = new GenericBean();
		new OntologyChooserApp( dialog, bean ).show();
		
		final ListModel<?> listModel = objectList.getModel();
		
		if( listModel instanceof DefaultListModel )
		{
			final Object[] ontologyTerms = (Object[])bean.getProperty( org.mcisb.util.PropertyNames.CHOOSER_OBJECTS );
			
			if( ontologyTerms != null )
			{
				for( int i = 0; i < ontologyTerms.length; i++ )
				{
					final DefaultListModel<Object> defaultListModel = (DefaultListModel<Object>)listModel;
					
					if( !defaultListModel.contains( ontologyTerms[ i ] ) )
					{
						defaultListModel.addElement( ontologyTerms[ i ] );
					}
				}
			}
		}
	}
}
