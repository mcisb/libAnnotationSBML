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
package org.mcisb.sbml;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.xml.stream.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.chebi.*;
import org.mcisb.ontology.kegg.*;
import org.mcisb.ontology.pubchem.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.util.*;
import org.mcisb.util.xml.*;
import org.sbml.jsbml.*;

/**
 * @author Neil Swainston
 */
@SuppressWarnings("deprecation")
public class SbmlUtils
{
	/**
	 * 
	 */
	public static final int DEFAULT_LEVEL = 2;

	/**
	 * 
	 */
	public static final int DEFAULT_VERSION = 4;

	/**
	 * 
	 */
	public final static String FORMULA = "FORMULA"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String NEUTRAL_FORMULA = "NEUTRAL_FORMULA"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String NON_SPECIFIC_FORMULA = "NON_SPECIFIC_FORMULA"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String SMILES = "SMILES"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String INCHI = "INCHI"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String ORIGIN = "ORIGIN"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String CHARGE = "CHARGE"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String GENE_ASSOCIATION = "GENE_ASSOCIATION"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String SUBSYSTEM = "SUBSYSTEM"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String UNANNOTATED = "UNANNOTATED"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String ADDITION = " + "; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String REVERSIBLE = " <=> "; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String IRREVERSIBLE = " --> "; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String COMPARTMENT_START = " ("; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String COMPARTMENT_END = ")"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String MODIFIERS_START = " ["; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static String MODIFIERS_END = "]"; //$NON-NLS-1$

	/**
	 * 
	 */
	public final static double DEFAULT_INITIAL_CONCENTRATION = 1;

	/**
	 * 
	 */
	private final static String SPACE = " "; //$NON-NLS-1$

	/**
	 * 
	 */
	private final static String NEGATIVE_CHARGE_REGEXP = "(?<=\\()\\d+(?=-\\)$)"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final static String POSITIVE_CHARGE_REGEXP = "(?<=\\()\\d+(?=\\+\\)$)"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final static String defaultSbmlNamespaces = SBMLDocument.URI_NAMESPACE_L2V4;

	/**
	 * 
	 */
	private Creator creator = null;

	/**
	 * @return String
	 */
	public static String getDefaultSBMLNamespace()
	{
		return defaultSbmlNamespaces;
	}

	/**
	 * 
	 * @param creator
	 */
	public void setCreator( final Creator creator )
	{
		this.creator = creator;
	}

	/**
	 * @param model
	 */
	public void setModelHistory( final Model model )
	{
		setMetaId( model );

		final Calendar calendar = Calendar.getInstance();
		final Date date = calendar.getTime();

		History modelHistory = model.getHistory();

		if( modelHistory == null )
		{
			modelHistory = new History();
			model.setHistory( modelHistory );
		}

		if( modelHistory.getCreatedDate() == null )
		{
			modelHistory.setCreatedDate( date );
		}
		else
		{
			modelHistory.setModifiedDate( date );
		}

		if( creator != null && !modelHistory.isSetListOfCreators() )
		{
			modelHistory.addCreator( creator );
		}
	}

	/**
	 * @param model
	 * @return long
	 */
	public static long getCreatedDate( final Model model )
	{
		final Date date = model.getHistory().getCreatedDate();
		return date.getTime();
	}

	/**
	 * @param sbase
	 * @param ontologyTerms
	 */
	public static void addOntologyTerms( final SBase sbase, final Map<OntologyTerm,Object[]> ontologyTerms )
	{
		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = new TreeMap<>( ontologyTerms ).entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<OntologyTerm,Object[]> entry = iterator.next();
			addOntologyTerm( sbase, entry.getKey(), (CVTerm.Type)entry.getValue()[ 0 ], (CVTerm.Qualifier)entry.getValue()[ 1 ] );
		}
	}

	/**
	 * @param sbase
	 * @param ontologyTerms
	 * @param qualifierType
	 * @param specificQualifierType
	 */
	public static void addOntologyTerms( final SBase sbase, final Collection<OntologyTerm> ontologyTerms, final CVTerm.Type qualifierType, final CVTerm.Qualifier specificQualifierType )
	{
		for( Iterator<OntologyTerm> iterator = new TreeSet<>( ontologyTerms ).iterator(); iterator.hasNext(); )
		{
			addOntologyTerm( sbase, iterator.next(), qualifierType, specificQualifierType );
		}
	}

	/**
	 * @param sbase
	 * @param ontologyTerm
	 * @param qualifierType
	 * @param specificQualifierType
	 */
	public static void addOntologyTerm( final SBase sbase, final OntologyTerm ontologyTerm, final CVTerm.Type qualifierType, final CVTerm.Qualifier specificQualifierType )
	{
		setMetaId( sbase );

		final CVTerm cvTerm = new CVTerm();
		cvTerm.setQualifierType( qualifierType );

		if( qualifierType == CVTerm.Type.MODEL_QUALIFIER )
		{
			cvTerm.setModelQualifierType( specificQualifierType );
		}
		else if( qualifierType == CVTerm.Type.BIOLOGICAL_QUALIFIER )
		{
			cvTerm.setBiologicalQualifierType( specificQualifierType );
		}

		final String resource = ontologyTerm.toUri();
		cvTerm.addResource( resource );
		sbase.addCVTerm( cvTerm );
	}

	/**
	 * @param sbase
	 * @return Map
	 * @throws Exception
	 */
	public static Map<OntologyTerm,Object[]> getOntologyTerms( final SBase sbase ) throws Exception
	{
		return getOntologyTerms( getAnnotationTerms( sbase ) );
	}

	/**
	 * @param sbase
	 * @param ontologyName
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public static OntologyTerm getOntologyTerm( final SBase sbase, final String ontologyName ) throws Exception
	{
		final Map<OntologyTerm,Object[]> ontologyTerms = getOntologyTerms( getAnnotationTerms( sbase ) );

		for( Map.Entry<OntologyTerm,Object[]> entry : ontologyTerms.entrySet() )
		{
			if( entry.getKey().getOntologyName().equals( ontologyName ) )
			{
				return entry.getKey();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param sbase
	 * @param qualifierType
	 * @param specificQualifierType
	 * @throws Exception
	 */
	public static Collection<OntologyTerm> getOntologyTerms( final SBase sbase, final CVTerm.Type qualifierType, final CVTerm.Qualifier specificQualifierType ) throws Exception
	{
		final Map<OntologyTerm,Object[]> ontologyTerms = getOntologyTerms( sbase );

		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = ontologyTerms.entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<OntologyTerm,Object[]> entry = iterator.next();

			if( entry.getValue()[ 0 ] == qualifierType && entry.getValue()[ 1 ] == specificQualifierType )
			{
				continue;
			}

			iterator.remove();
		}

		return ontologyTerms.keySet();
	}

	/**
	 * @param model
	 * @param reactionId
	 * @param reactantNumber
	 * @return Species
	 */
	public static Species getReactantSpecies( final Model model, final String reactionId, final int reactantNumber )
	{
		return model.getSpecies( model.getReaction( reactionId ).getReactant( reactantNumber ).getSpecies() );
	}

	/**
	 * @param model
	 * @param reactionId
	 * @param productNumber
	 * @return Species
	 */
	public static Species getProductSpecies( final Model model, final String reactionId, final int productNumber )
	{
		final Reaction reaction = model.getReaction( reactionId );
		return ( reaction.getNumProducts() > productNumber ) ? model.getSpecies( reaction.getProduct( productNumber ).getSpecies() ) : null;
	}

	/**
	 * @param model
	 * @param reactionId
	 * @param modifierNumber
	 * @return Species
	 */
	public static Species getModifierSpecies( final Model model, final String reactionId, final int modifierNumber )
	{
		return model.getSpecies( model.getReaction( reactionId ).getModifier( modifierNumber ).getSpecies() );
	}

	/**
	 * @param model
	 * @return List<Species>
	 */
	public static List<Species> getListOfSpecies( final Model model )
	{
		final List<Species> list = new ArrayList<>();
		final ListOf<Species> listOfSpecies = model.getListOfSpecies();

		for( int l = 0; l < listOfSpecies.size(); l++ )
		{
			list.add( listOfSpecies.get( l ) );
		}

		return list;
	}

	/**
	 * @param sbase
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isCompound( final SBase sbase ) throws Exception
	{
		if( sbase.getSBOTerm() == SboUtils.SIMPLE_CHEMICAL )
		{
			return true;
		}

		final Collection<OntologyTerm> ontologyTerms = getOntologyTerms( sbase ).keySet();

		for( Iterator<OntologyTerm> iterator = ontologyTerms.iterator(); iterator.hasNext(); )
		{
			final String ontologyName = iterator.next().getOntologyName();

			if( ontologyName.equals( Ontology.SGD ) || ontologyName.equals( Ontology.TAIR_LOCUS ) || ontologyName.equals( Ontology.KEGG_GENES ) || ontologyName.equals( Ontology.UNIPROT ) )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param model
	 * @param reaction
	 * @return boolean
	 */
	public static boolean isTransport( final Model model, final Reaction reaction )
	{
		final Set<String> compartmentIds = new HashSet<>();

		for( int l = 0; l < reaction.getNumReactants(); l++ )
		{
			final SpeciesReference speciesReference = reaction.getReactant( l );
			final String speciesId = speciesReference.getSpecies();
			final Species species = model.getSpecies( speciesId );
			compartmentIds.add( species.getCompartment() );
		}

		for( int l = 0; l < reaction.getNumProducts(); l++ )
		{
			final SpeciesReference speciesReference = reaction.getProduct( l );
			final String speciesId = speciesReference.getSpecies();
			final Species species = model.getSpecies( speciesId );
			compartmentIds.add( species.getCompartment() );
		}

		return compartmentIds.size() > 1;
	}

	/**
	 * @param model
	 * @param reactionId
	 * @return String
	 */
	public static String toString( final Model model, final String reactionId )
	{
		return toString( model, reactionId, true );
	}

	/**
	 * @param model
	 * @param reactionId
	 * @param includeModifiers
	 * @return String
	 */
	public static String toString( final Model model, final String reactionId, final boolean includeModifiers )
	{
		final StringBuffer buffer = new StringBuffer();
		final Reaction reaction = model.getReaction( reactionId );
		boolean isReactants = false;
		boolean isProducts = false;

		for( int i = 0; i < reaction.getNumReactants(); i++ )
		{
			final Species species = getReactantSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getReactant( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			buffer.append( getName( model, species ) );

			final String compartmentName = model.getCompartment( species.getCompartment() ).getName();

			if( compartmentName != null && compartmentName.length() > 0 )
			{
				buffer.append( COMPARTMENT_START );
				buffer.append( compartmentName );
				buffer.append( COMPARTMENT_END );
			}

			buffer.append( ADDITION );
			isReactants = true;
		}

		if( isReactants )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		buffer.append( reaction.isSetReversible() && !reaction.getReversible() ? IRREVERSIBLE : REVERSIBLE );

		for( int i = 0; i < reaction.getNumProducts(); i++ )
		{
			final Species species = getProductSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getProduct( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			buffer.append( getName( model, species ) );

			final String compartmentName = model.getCompartment( species.getCompartment() ).getName();

			if( compartmentName != null && compartmentName.length() > 0 )
			{
				buffer.append( COMPARTMENT_START );
				buffer.append( compartmentName );
				buffer.append( COMPARTMENT_END );
			}

			buffer.append( ADDITION );
			isProducts = true;
		}

		if( isProducts )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		if( includeModifiers && reaction.getNumModifiers() > 0 )
		{
			buffer.append( MODIFIERS_START );

			for( int i = 0; i < reaction.getNumModifiers(); i++ )
			{
				final Species species = getModifierSpecies( model, reactionId, i );
				buffer.append( getName( model, species ) );
				buffer.append( COMPARTMENT_START );
				buffer.append( model.getCompartment( species.getCompartment() ).getName() );
				buffer.append( COMPARTMENT_END );
				buffer.append( ADDITION );
			}

			buffer.setLength( buffer.length() - ADDITION.length() );
			buffer.append( MODIFIERS_END );
		}

		return buffer.toString();
	}

	/**
	 * @param model
	 * @param reactionId
	 * @return String
	 * @throws Exception
	 */
	public static String toFormulaString( final Model model, final String reactionId ) throws Exception
	{
		final StringBuffer buffer = new StringBuffer();
		final Reaction reaction = model.getReaction( reactionId );
		boolean isReactants = false;
		boolean isProducts = false;

		for( int i = 0; i < reaction.getNumReactants(); i++ )
		{
			final Species species = getReactantSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getReactant( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			final String formula = getFormula( model, species );
			final int charge = getCharge( model, species );

			buffer.append( formula );
			buffer.append( " (" ); //$NON-NLS-1$
			buffer.append( charge == ChebiUtils.UNDEFINED_CHARGE ? "unknown" : Integer.valueOf( charge ) ); //$NON-NLS-1$
			buffer.append( ") " ); //$NON-NLS-1$
			buffer.append( ADDITION );
			isReactants = true;
		}

		if( isReactants )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		buffer.append( reaction.isSetReversible() && !reaction.getReversible() ? IRREVERSIBLE : REVERSIBLE );

		for( int i = 0; i < reaction.getNumProducts(); i++ )
		{
			final Species species = getProductSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getProduct( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			final String formula = getFormula( model, species );
			final int charge = getCharge( model, species );

			buffer.append( formula );
			buffer.append( " (" ); //$NON-NLS-1$
			buffer.append( charge == ChebiUtils.UNDEFINED_CHARGE ? "unknown" : Integer.valueOf( charge ) ); //$NON-NLS-1$
			buffer.append( ") " ); //$NON-NLS-1$
			buffer.append( ADDITION );
			isProducts = true;
		}

		if( isProducts )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		return buffer.toString();
	}

	/**
	 * @param model
	 * @param reactionId
	 * @return String
	 */
	public static String toIdString( final Model model, final String reactionId )
	{
		final StringBuffer buffer = new StringBuffer();
		final Reaction reaction = model.getReaction( reactionId );
		boolean isReactants = false;
		boolean isProducts = false;

		for( int i = 0; i < reaction.getNumReactants(); i++ )
		{
			final Species species = getReactantSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getReactant( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			buffer.append( species.getId() );
			buffer.append( ADDITION );
			isReactants = true;
		}

		if( isReactants )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		buffer.append( reaction.isSetReversible() && !reaction.getReversible() ? IRREVERSIBLE : REVERSIBLE );

		for( int i = 0; i < reaction.getNumProducts(); i++ )
		{
			final Species species = getProductSpecies( model, reactionId, i );
			final double stoichiometry = reaction.getProduct( i ).getStoichiometry();

			if( stoichiometry != 1.0 )
			{
				buffer.append( stoichiometry );
				buffer.append( SPACE );
			}

			buffer.append( species.getId() );
			buffer.append( ADDITION );
			isProducts = true;
		}

		if( isProducts )
		{
			buffer.setLength( buffer.length() - ADDITION.length() );
		}

		return buffer.toString();
	}

	/**
	 * @param model
	 * @param species
	 * @return String
	 */
	public static String getName( final Model model, final Species species )
	{
		if( !species.isSetName() && species.isSetSpeciesType() )
		{
			return model.getSpeciesType( species.getSpeciesType() ).getName();
		}

		return species.getName();
	}

	/**
	 * @param document
	 * @param reactionId
	 * @return Model
	 * @throws Exception
	 */
	public static SBMLDocument getSubDocument( final SBMLDocument document, final String reactionId ) throws Exception
	{
		return getSubDocument( document, Arrays.asList( reactionId ), true );
	}

	/**
	 * @param document
	 * @param reactionIds
	 * @param includeModifiers
	 * @return Model
	 * @throws Exception
	 */
	public static SBMLDocument getSubDocument( final SBMLDocument document, final Collection<String> reactionIds, final boolean includeModifiers ) throws Exception
	{
		final SBMLDocument subDocument = new SBMLDocument( document.getLevel(), document.getVersion() );
		final Model subModel = subDocument.createModel( StringUtils.getUniqueId() );

		final Model model = document.getModel();

		for( int l = 0; l < model.getNumCompartments(); l++ )
		{
			final Compartment compartment = model.getCompartment( l );
			final Compartment subModelCompartment = subModel.createCompartment();
			subModelCompartment.setId( compartment.getId() );
			subModelCompartment.setName( compartment.getName() );
			subModelCompartment.setSize( compartment.getSize() );

			if( compartment.isSetSBOTerm() )
			{
				subModelCompartment.setSBOTerm( compartment.getSBOTerm() );
			}

			subModelCompartment.setNotes( compartment.getNotesString() );
			addOntologyTerms( subModelCompartment, getOntologyTerms( compartment ) );
		}

		for( int l = 0; l < model.getNumUnitDefinitions(); l++ )
		{
			final UnitDefinition unitDefinition = model.getUnitDefinition( l );
			final UnitDefinition subModelUnitDefinition = subModel.createUnitDefinition();
			subModelUnitDefinition.setId( unitDefinition.getId() );
			subModelUnitDefinition.setName( unitDefinition.getName() );

			for( int m = 0; m < unitDefinition.getNumUnits(); m++ )
			{
				final Unit unit = unitDefinition.getUnit( m );
				final Unit subModelUnit = subModelUnitDefinition.createUnit();
				subModelUnit.setKind( unit.getKind() );
				subModelUnit.setScale( unit.getScale() );
				subModelUnit.setExponent( unit.getExponent() );
				subModelUnit.setMultiplier( unit.getMultiplier() );
			}
		}

		for( String reactionId : reactionIds )
		{
			// reactionId = reactionId.replaceAll( "\\(", "\\_" ).replaceAll(
			// "\\)", "\\_" );

			final Reaction reaction = model.getReaction( reactionId );

			if( reaction != null )
			{
				// System.out.println( this.toString( model, reactionId, false )
				// + "\t" + this.getNotes( reaction ).get( SUBSYSTEM ) );
				final Reaction subModelReaction = subModel.createReaction();
				subModelReaction.setId( reactionId );
				subModelReaction.setName( reaction.getName() );

				if( reaction.isSetSBOTerm() )
				{
					subModelReaction.setSBOTerm( reaction.getSBOTerm() );
				}

				subModelReaction.setNotes( reaction.getNotesString() );
				addOntologyTerms( subModelReaction, getOntologyTerms( reaction ) );

				if( reaction.getKineticLaw() != null )
				{
					final KineticLaw kineticLaw = model.getReaction( reactionId ).getKineticLaw();
					final KineticLaw subModelKL = subModelReaction.createKineticLaw();
					subModelKL.setMath( kineticLaw.getMath() );
					subModelKL.setMetaId( kineticLaw.isSetMetaId() ? kineticLaw.getMetaId() : StringUtils.getUniqueId() );
					subModelKL.setNotes( kineticLaw.getNotesString() );

					for( LocalParameter klLp : kineticLaw.getListOfLocalParameters() )
					{
						final LocalParameter lp = subModelKL.createLocalParameter();
						lp.setId( klLp.getId() );
						lp.setValue( klLp.getValue() );
						lp.setUnits( klLp.getUnits() );

						if( klLp.getNotesString() != null )
						{
							lp.setNotes( klLp.getNotesString() );
						}
					}

					/*
					 * for( int l = 0; l < kineticLaw.getNumParameters(); l++ )
					 * { final Parameter klP = kineticLaw.getParameter( l );
					 * final Parameter p = subModelKL.createParameter();
					 * p.setId( klP.getId() ); p.setValue( klP.getValue() );
					 * p.setUnits( klP.getUnits() );
					 * 
					 * if( klP.getNotesString() != null ) { p.setNotes(
					 * klP.getNotesString() ); } }
					 */
				}

				for( int l = 0; l < reaction.getNumReactants(); l++ )
				{
					final Species species = getReactantSpecies( model, reactionId, l );

					if( subModel.getSpecies( species.getId() ) == null )
					{
						cloneSpecies( species, subModel.createSpecies() );
					}

					final SpeciesReference speciesReference = subModelReaction.createReactant();
					speciesReference.setSpecies( species.getId() );
					speciesReference.setStoichiometry( reaction.getReactant( l ).getStoichiometry() );
				}
				for( int l = 0; l < reaction.getNumProducts(); l++ )
				{
					final Species species = getProductSpecies( model, reactionId, l );

					if( subModel.getSpecies( species.getId() ) == null )
					{
						cloneSpecies( species, subModel.createSpecies() );
					}

					final SpeciesReference speciesReference = subModelReaction.createProduct();
					speciesReference.setSpecies( species.getId() );
					speciesReference.setStoichiometry( reaction.getProduct( l ).getStoichiometry() );
				}

				if( includeModifiers )
				{
					for( int l = 0; l < reaction.getNumModifiers(); l++ )
					{
						final Species species = getModifierSpecies( model, reactionId, l );

						if( subModel.getSpecies( species.getId() ) == null )
						{
							cloneSpecies( species, subModel.createSpecies() );
						}

						final ModifierSpeciesReference speciesReference = subModelReaction.createModifier();
						speciesReference.setSpecies( species.getId() );
					}
				}
			}
		}

		return subDocument;
	}

	/**
	 * @param model
	 * @throws Exception
	 */
	public static void updateAnnotations( final Model model ) throws Exception
	{
		updateSBaseAnnotations( model );

		for( int i = 0; i < model.getNumCompartments(); i++ )
		{
			updateSBaseAnnotations( model.getCompartment( i ) );
		}

		for( int i = 0; i < model.getNumSpecies(); i++ )
		{
			updateSBaseAnnotations( model.getSpecies( i ) );
		}

		for( int i = 0; i < model.getNumReactions(); i++ )
		{
			updateSBaseAnnotations( model.getReaction( i ) );
		}
	}

	/**
	 * @param model
	 * @param reaction
	 * @return String
	 */
	public static String getCompartmentId( final Model model, final Reaction reaction )
	{
		final Collection<String> compartmentIds = new TreeSet<>();

		for( int l = 0; l < reaction.getNumReactants(); l++ )
		{
			compartmentIds.add( getReactantSpecies( model, reaction.getId(), l ).getCompartment() );
		}
		for( int l = 0; l < reaction.getNumProducts(); l++ )
		{
			compartmentIds.add( getProductSpecies( model, reaction.getId(), l ).getCompartment() );
		}

		if( compartmentIds.size() == 1 )
		{
			return CollectionUtils.getFirst( compartmentIds );
		}
		else if( compartmentIds.size() == 2 )
		{
			final String[] compartmentIdsArray = compartmentIds.toArray( new String[ compartmentIds.size() ] );
			String membraneCompartmentId = getMembraneCompartmentId( model, compartmentIdsArray[ 0 ], compartmentIdsArray[ 1 ] );

			if( membraneCompartmentId != null )
			{
				return membraneCompartmentId;
			}

			membraneCompartmentId = getMembraneCompartmentId( model, compartmentIdsArray[ 1 ], compartmentIdsArray[ 0 ] );

			if( membraneCompartmentId != null )
			{
				return membraneCompartmentId;
			}
		}

		return null;
	}

	/**
	 * @param reaction
	 */
	public static void cleanReaction( final Reaction reaction )
	{
		final Map<String,Double> idToStoichiometry = new LinkedHashMap<>();

		for( int l = 0; l < reaction.getNumReactants(); l++ )
		{
			final SpeciesReference reference = reaction.getReactant( l );

			if( idToStoichiometry.get( reference.getSpecies() ) == null )
			{
				idToStoichiometry.put( reference.getSpecies(), Double.valueOf( reference.getStoichiometry() ) );
			}
			else
			{
				idToStoichiometry.put( reference.getSpecies(), Double.valueOf( idToStoichiometry.get( reference.getSpecies() ).doubleValue() + reference.getStoichiometry() ) );
			}
		}

		for( int l = 0; l < reaction.getNumProducts(); l++ )
		{
			final SpeciesReference reference = reaction.getProduct( l );

			if( idToStoichiometry.get( reference.getSpecies() ) == null )
			{
				idToStoichiometry.put( reference.getSpecies(), Double.valueOf( -reference.getStoichiometry() ) );
			}
			else
			{
				idToStoichiometry.put( reference.getSpecies(), Double.valueOf( idToStoichiometry.get( reference.getSpecies() ).doubleValue() - reference.getStoichiometry() ) );
			}
		}

		while( reaction.getNumReactants() > 0 )
		{
			reaction.removeReactant( 0 );
		}
		while( reaction.getNumProducts() > 0 )
		{
			reaction.removeProduct( 0 );
		}

		for( Map.Entry<String,Double> entry : idToStoichiometry.entrySet() )
		{
			final double stoichiometry = entry.getValue().doubleValue();
			SpeciesReference speciesReference = null;

			if( stoichiometry > 0 )
			{
				speciesReference = reaction.createReactant();
			}
			else if( stoichiometry < 0 )
			{
				speciesReference = reaction.createProduct();
			}

			if( speciesReference != null )
			{
				speciesReference.setSpecies( entry.getKey() );
				speciesReference.setStoichiometry( Math.abs( stoichiometry ) );
			}
		}
	}

	/**
	 * @param model
	 * @param species
	 * @return SBase
	 */
	public static NamedSBase getSpecies( final Model model, final Species species )
	{
		if( species.isSetSpeciesType() )
		{
			return model.getSpeciesType( species.getSpeciesType() );
		}

		return species;
	}

	/**
	 * @param model
	 * @param species
	 * @return String
	 * @throws Exception
	 */
	public static String getFormula( final Model model, final Species species ) throws Exception
	{
		final String R_GROUP_REG_EXP = "FULLR(\\d*)"; //$NON-NLS-1$

		final SBase sbase = getSpecies( model, species );
		Object formula = getNotes( sbase ).get( FORMULA );

		if( formula != null && formula.toString().length() > 0 )
		{
			return formula.toString().replaceAll( R_GROUP_REG_EXP, "R" ); //$NON-NLS-1$
		}

		formula = getNotes( sbase ).get( NEUTRAL_FORMULA );

		if( formula != null && formula.toString().length() > 0 )
		{
			return formula.toString().replaceAll( R_GROUP_REG_EXP, "R" ); //$NON-NLS-1$
		}

		final ChebiTerm chebiTerm = (ChebiTerm)getOntologyTerm( sbase, Ontology.CHEBI );

		if( chebiTerm != null )
		{
			formula = chebiTerm.getFormula();

			if( formula != null )
			{
				return formula.toString();
			}
		}

		final PubChemTerm pubChemTerm = (PubChemTerm)getOntologyTerm( sbase, Ontology.PUBCHEM_COMPOUND );

		if( pubChemTerm != null )
		{
			formula = pubChemTerm.getFormula();

			if( formula != null )
			{
				return formula.toString();
			}
		}

		final KeggCompoundTerm keggCompoundTerm = (KeggCompoundTerm)getOntologyTerm( sbase, Ontology.KEGG_COMPOUND );

		if( keggCompoundTerm != null )
		{
			formula = keggCompoundTerm.getFormula();

			if( formula != null )
			{
				return formula.toString();
			}
			else if( keggCompoundTerm.getId().equals( "C05693" ) ) // R //$NON-NLS-1$
			{
				return "R"; //$NON-NLS-1$
			}
			else if( keggCompoundTerm.getId().equals( "C05694" ) ) // R //$NON-NLS-1$
			{
				return "CH3R"; //$NON-NLS-1$
			}
		}

		final OntologyTerm inchiTerm = getOntologyTerm( sbase, Ontology.INCHI );

		if( inchiTerm != null )
		{
			return CollectionUtils.getFirst( RegularExpressionUtils.getMatches( inchiTerm.getId(), "(?<=/)[\\w\\.]*(?=/)" ) ); //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * @param species
	 * @return String
	 * @throws Exception
	 */
	public static String getSmiles( @SuppressWarnings("unused") final Model model, final Species species ) throws Exception
	{
		final SBase sbase = species;
		Object smiles = getNotes( sbase ).get( SMILES );

		if( smiles != null )
		{
			return smiles.toString();
		}

		final ChebiTerm chebiTerm = (ChebiTerm)CollectionUtils.getFirst( OntologyUtils.getInstance().getXrefs( getOntologyTerms( sbase ).keySet(), Ontology.CHEBI ) );

		if( chebiTerm != null )
		{
			smiles = chebiTerm.getSmiles();

			if( smiles != null )
			{
				return smiles.toString();
			}
		}

		final PubChemTerm pubChemTerm = (PubChemTerm)CollectionUtils.getFirst( OntologyUtils.getInstance().getXrefs( getOntologyTerms( sbase ).keySet(), Ontology.PUBCHEM_COMPOUND ) );

		if( pubChemTerm != null )
		{
			smiles = pubChemTerm.getSmiles();

			if( smiles != null )
			{
				return smiles.toString();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param species
	 * @return String
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static String getNonSpecificFormula( final Species species ) throws UnsupportedEncodingException, XMLStreamException
	{
		return (String)( getNotes( species ).get( NON_SPECIFIC_FORMULA ) );
	}

	/**
	 * @param species
	 * @return String
	 * @throws Exception
	 */
	public static String getInchi( final Species species ) throws Exception
	{
		final SBase sbase = species;
		Object smiles = getNotes( sbase ).get( INCHI );

		if( smiles != null )
		{
			return smiles.toString();
		}

		final ChebiTerm chebiTerm = (ChebiTerm)CollectionUtils.getFirst( OntologyUtils.getInstance().getXrefs( getOntologyTerms( sbase ).keySet(), Ontology.CHEBI ) );

		if( chebiTerm != null )
		{
			smiles = chebiTerm.getInchi();

			if( smiles != null )
			{
				return smiles.toString();
			}
		}

		return null;
	}

	/**
	 * @param species
	 * @param formula
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static void setFormula( final Species species, final String formula ) throws XMLStreamException, UnsupportedEncodingException
	{
		set( species, FORMULA, formula );
	}

	/**
	 * @param species
	 * @param charge
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static void setCharge( final Species species, final int charge ) throws XMLStreamException, UnsupportedEncodingException
	{
		if( charge == NumberUtils.UNDEFINED )
		{
			set( species, CHARGE, null );
			species.unsetCharge();
		}
		else
		{
			set( species, CHARGE, Integer.valueOf( charge ) );
			species.setCharge( charge );
		}
	}

	/**
	 * 
	 * @param species
	 * @param smiles
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static void setSmiles( final Species species, final String smiles ) throws UnsupportedEncodingException, XMLStreamException
	{
		set( species, SMILES, smiles );
	}

	/**
	 * @param sbase
	 * @param term
	 * @param value
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static void set( final SBase sbase, final String term, final Object value ) throws XMLStreamException, UnsupportedEncodingException
	{
		final Map<String,Object> notes = getNotes( sbase );

		if( value == null )
		{
			notes.remove( term );
		}
		else
		{
			notes.put( term, value );
		}

		setNotes( sbase, notes );
	}

	/**
	 * @param model
	 * @param species
	 * @return int
	 * @throws Exception
	 */
	public static int getCharge( final Model model, final Species species ) throws Exception
	{
		if( species.isSetCharge() )
		{
			return species.getCharge();
		}

		final SBase sbase = getSpecies( model, species );

		Object charge = getNotes( sbase ).get( CHARGE );

		if( charge != null && charge.toString().length() > 0 )
		{
			return Integer.parseInt( charge.toString() );
		}

		final ChebiTerm chebiTerm = (ChebiTerm)getOntologyTerm( sbase, Ontology.CHEBI );

		if( chebiTerm != null && chebiTerm.getCharge() != NumberUtils.UNDEFINED )
		{
			return chebiTerm.getCharge();
		}

		final PubChemTerm pubChemTerm = (PubChemTerm)getOntologyTerm( sbase, Ontology.PUBCHEM_COMPOUND );

		if( pubChemTerm != null && pubChemTerm.getCharge() != NumberUtils.UNDEFINED )
		{
			return pubChemTerm.getCharge();
		}

		final String negativeChargeString = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( species.getName(), NEGATIVE_CHARGE_REGEXP ) );

		if( negativeChargeString != null )
		{
			return -Integer.parseInt( negativeChargeString );
		}

		final String UTF8 = "UTF8"; //$NON-NLS-1$
		final String positiveChargeString = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( new String( species.getName().getBytes( UTF8 ), UTF8 ), POSITIVE_CHARGE_REGEXP ) );

		if( positiveChargeString != null )
		{
			return Integer.parseInt( positiveChargeString );
		}

		return NumberUtils.UNDEFINED;
	}

	/**
	 * @param sbase
	 * @return Map<String,String>
	 * @throws XMLStreamException
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String,Object> getNotes( final SBase sbase ) throws XMLStreamException, UnsupportedEncodingException
	{
		final String PARAGRAPH = "p"; //$NON-NLS-1$
		final String SEPARATOR = ":"; //$NON-NLS-1$

		final Map<String,Object> notes = new TreeMap<>();

		if( sbase.isSetNotes() )
		{
			final String sbaseNotes = sbase.getNotesString();

			for( final String element : XmlUtils.getElements( PARAGRAPH, new ByteArrayInputStream( sbaseNotes.getBytes( Charset.defaultCharset() ) ), true ) )
			{
				final int index = element.indexOf( SEPARATOR );

				if( index == -1 )
				{
					notes.put( element, null );
				}
				else
				{
					notes.put( element.substring( 0, index ), element.substring( index + SEPARATOR.length() ).trim() );
				}
			}
		}

		return notes;
	}

	/**
	 * @param sbase
	 * @param notes
	 * @throws XMLStreamException
	 */
	public static void setNotes( final SBase sbase, final Map<String,Object> notes ) throws XMLStreamException
	{
		final String PARAGRAPH = "<p>"; //$NON-NLS-1$
		final String SEPARATOR = ": "; //$NON-NLS-1$
		final String PARAGRAPH_END = "</p>"; //$NON-NLS-1$
		sbase.unsetNotes();

		if( notes.size() > 0 )
		{
			final Map<String,Object> orderedNotes = new TreeMap<>();
			orderedNotes.putAll( notes );

			final StringBuffer buffer = new StringBuffer( "<body xmlns=\"http://www.w3.org/1999/xhtml\">" ); //$NON-NLS-1$

			for( Map.Entry<String,Object> entry : orderedNotes.entrySet() )
			{
				buffer.append( PARAGRAPH );
				buffer.append( entry.getKey() );
				buffer.append( SEPARATOR );

				final Object value = entry.getValue();

				if( value != null && value.toString().length() > 0 )
				{
					buffer.append( XmlUtils.encode( value.toString() ) );
				}

				buffer.append( PARAGRAPH_END );
			}

			buffer.append( "</body>" ); //$NON-NLS-1$
			sbase.setNotes( buffer.toString() );
		}
	}

	/**
	 * 
	 * @param document
	 * @throws Exception
	 */
	public static void checkFormulae( final SBMLDocument document ) throws Exception
	{
		final Model model = document.getModel();

		for( int i = 0; i < model.getNumSpecies(); i++ )
		{
			final Species species = model.getSpecies( i );
			final Object notesFormula = getNotes( species ).get( FORMULA );
			final Object notesCharge = getNotes( species ).get( CHARGE );
			final OntologyTerm ontologyTerm = getOntologyTerm( species, Ontology.CHEBI );
			String chebiFormula = null;
			float chebiCharge = NumberUtils.UNDEFINED;

			if( ontologyTerm != null )
			{
				final ChebiTerm chebiTerm = (ChebiTerm)ontologyTerm;
				chebiFormula = chebiTerm.getFormula();
				chebiCharge = chebiTerm.getCharge();

				if( notesFormula != null || notesCharge != null || chebiFormula != null || chebiCharge != NumberUtils.UNDEFINED )
				{
					final boolean formulaeMatch = ( notesFormula != null && notesFormula.equals( chebiFormula ) ) || ( chebiFormula != null && chebiFormula.equals( notesFormula ) );
					final boolean chargesMatch = notesCharge != null && Float.parseFloat( notesCharge.toString() ) == chebiCharge;

					if( !( formulaeMatch && chargesMatch ) )
					{
						System.out.println( species.getId() + "\t" + notesFormula + "\t" + notesCharge + "\t" + chebiFormula + "\t" + chebiCharge + "\t" + formulaeMatch + "\t" + chargesMatch ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param model
	 * @param sbase
	 * @return boolean
	 */
	public static boolean add( final Model model, final SBase sbase )
	{
		if( sbase instanceof Compartment )
		{
			return add( model, (Compartment)sbase );
		}
		if( sbase instanceof UnitDefinition )
		{
			return add( model, (UnitDefinition)sbase );
		}
		if( sbase instanceof Species )
		{
			return add( model, (Species)sbase );
		}
		if( sbase instanceof Parameter )
		{
			return add( model, (Parameter)sbase );
		}
		if( sbase instanceof Reaction )
		{
			return add( model, (Reaction)sbase );
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * @param model
	 * @param compartment
	 * @return boolean
	 */
	public static boolean add( final Model model, final Compartment compartment )
	{
		return model.addCompartment( compartment.clone() );
	}

	/**
	 * @param model
	 * @param unitDefinition
	 * @return boolean
	 */
	public static boolean add( final Model model, final UnitDefinition unitDefinition )
	{
		return model.addUnitDefinition( unitDefinition.clone() );
	}

	/**
	 * @param model
	 * @param species
	 * @return boolean
	 */
	public static boolean add( final Model model, final Species species )
	{
		if( !( species.isSetInitialAmount() || species.isSetInitialConcentration() ) )
		{
			species.setInitialConcentration( DEFAULT_INITIAL_CONCENTRATION );
		}

		return model.addSpecies( species.clone() );
	}

	/**
	 * @param model
	 * @param parameter
	 * @return boolean
	 */
	public static boolean add( final Model model, final Parameter parameter )
	{
		return model.addParameter( parameter.clone() );
	}

	/**
	 * @param model
	 * @param speciesType
	 * @return boolean
	 */
	public static boolean add( final Model model, final SpeciesType speciesType )
	{
		return model.addSpeciesType( speciesType.clone() );
	}

	/**
	 * @param model
	 * @param reaction
	 * @return boolean
	 */
	public static boolean add( final Model model, final Reaction reaction )
	{
		return model.addReaction( reaction.clone() );
	}

	/**
	 * 
	 * @param model
	 * @return Map<String,Integer>
	 */
	public static Map<String,Integer> getMetaboliteDegree( final Model model, final boolean considerTransport )
	{
		final Map<String,Integer> metaboliteDegree = new LinkedHashMap<>();

		for( Reaction reaction : model.getListOfReactions() )
		{
			final boolean isTransport = SbmlUtils.isTransport( model, reaction );

			if( considerTransport || !isTransport )
			{
				for( SpeciesReference reference : reaction.getListOfReactants() )
				{
					addMetaboliteDegree( reference.getSpecies(), metaboliteDegree );
				}

				for( SpeciesReference reference : reaction.getListOfProducts() )
				{
					addMetaboliteDegree( reference.getSpecies(), metaboliteDegree );
				}
			}
		}

		return metaboliteDegree;
	}

	/**
	 * 
	 * @param speciesId
	 * @param metaboliteDegree
	 */
	private static void addMetaboliteDegree( final String speciesId, final Map<String,Integer> metaboliteDegree )
	{
		Integer count = metaboliteDegree.get( speciesId );

		if( count == null )
		{
			metaboliteDegree.put( speciesId, Integer.valueOf( 1 ) );
		}
		else
		{
			metaboliteDegree.put( speciesId, Integer.valueOf( count.intValue() + 1 ) );
		}
	}

	/**
	 * @param model
	 * @param compartmentId1
	 * @param compartmentId2
	 * @return String
	 */
	private static String getMembraneCompartmentId( final Model model, final String compartmentId1, final String compartmentId2 )
	{
		final Compartment compartment1 = model.getCompartment( compartmentId1 );

		if( compartment1.isSetOutside() )
		{
			final Compartment outside = model.getCompartment( compartment1.getOutside() );

			if( outside.isSetOutside() && outside.getOutside().equals( compartmentId2 ) )
			{
				return outside.getId();
			}
		}

		final Compartment compartment2 = model.getCompartment( compartmentId1 );

		if( compartment2.isSetOutside() )
		{
			final Compartment outside = model.getCompartment( compartment2.getOutside() );

			if( outside.isSetOutside() && outside.getOutside().equals( compartmentId1 ) )
			{
				return outside.getId();
			}
		}

		return null;
	}

	/**
	 * @param sbase
	 * @throws Exception
	 */
	private static void updateSBaseAnnotations( final NamedSBase sbase ) throws Exception
	{
		// TODO: investigate whether this is fixed in JSBML:
		// sbase.unsetMetaId();
		// sbase.setMetaId( METAID_PREFIX + sbase.getId() );
		final History history = sbase.getAnnotation().getHistory();

		final Map<OntologyTerm,Object[]> ontologyTerms = getOntologyTerms( sbase );
		sbase.unsetAnnotation();

		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = ontologyTerms.entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<OntologyTerm,Object[]> entry = iterator.next();
			addOntologyTerm( sbase, entry.getKey(), (CVTerm.Type)entry.getValue()[ 0 ], (CVTerm.Qualifier)entry.getValue()[ 1 ] );
		}

		sbase.setHistory( history );
	}

	/**
	 * 
	 * @param document
	 * @param checkConsistency
	 * @param hasProcessingInstructions
	 * @throws IOException
	 */
	public static void validate( final SBMLDocument document, final boolean checkConsistency, final boolean hasProcessingInstructions ) throws IOException
	{
		if( checkConsistency )
		{
			document.checkConsistency();
			// document.checkInternalConsistency();
		}

		final String LINE = "Line "; //$NON-NLS-1$
		final String COMMA = ","; //$NON-NLS-1$
		final String SEPARATOR = ": "; //$NON-NLS-1$
		final String LINE_SEPARATOR = System.getProperty( "line.separator" ); //$NON-NLS-1$
		final StringBuffer errors = new StringBuffer();

		for( int l = 0; l < document.getNumErrors(); l++ )
		{
			final SBMLError error = document.getError( l );

			if( hasProcessingInstructions && !error.getMessage().equals( "Missing encoding attribute in XML declaration." ) ) //$NON-NLS-1$
			{
				errors.append( LINE + error.getLine() + COMMA + error.getColumn() + SEPARATOR + error.getMessage() );
				errors.append( LINE_SEPARATOR );
			}
		}

		if( errors.length() > 0 )
		{
			throw new IOException( errors.toString() );
		}
	}

	/**
	 * @param sbase
	 * @return Collection
	 */
	private static Collection<CVTerm> getAnnotationTerms( final SBase sbase )
	{
		final Collection<CVTerm> annotationTerms = new LinkedHashSet<>();

		for( int l = 0; l < sbase.getNumCVTerms(); l++ )
		{
			annotationTerms.add( sbase.getCVTerm( l ) );
		}

		return annotationTerms;
	}

	/**
	 * @param annotationTerms
	 * @return Map
	 * @throws Exception
	 */
	private static Map<OntologyTerm,Object[]> getOntologyTerms( final Collection<CVTerm> annotationTerms ) throws Exception
	{
		final OntologyUtils ontologyUtils = OntologyUtils.getInstance();
		final Map<OntologyTerm,Object[]> ontologyTerms = new TreeMap<>();

		for( Iterator<CVTerm> iterator = annotationTerms.iterator(); iterator.hasNext(); )
		{
			final CVTerm cvTerm = iterator.next();
			final List<String> resources = cvTerm.getResources();

			for( int i = 0; i < resources.size(); i++ )
			{
				final OntologyTerm ontologyTerm = ontologyUtils.getOntologyTerm( resources.get( i ).trim() );

				if( ontologyTerm != null )
				{
					ontologyTerms.put( ontologyTerm, new Object[] { cvTerm.getQualifierType(), cvTerm.getQualifierType() == CVTerm.Type.BIOLOGICAL_QUALIFIER ? cvTerm.getBiologicalQualifierType() : cvTerm.getModelQualifierType() } );
				}
			}
		}

		return ontologyTerms;
	}

	/**
	 * @param sbase
	 */
	private static void setMetaId( final SBase sbase )
	{
		if( !sbase.isSetMetaId() )
		{
			sbase.setMetaId( StringUtils.getUniqueId() );
		}
	}

	/**
	 * @param original
	 * @param clone
	 * @throws Exception
	 */
	private static void cloneSpecies( final Species original, final Species clone ) throws Exception
	{
		clone.setId( original.getId() );
		clone.setName( original.getName() );
		clone.setCompartment( original.getCompartment() );

		if( original.isSetSBOTerm() )
		{
			clone.setSBOTerm( original.getSBOTerm() );
		}

		clone.setNotes( original.getNotesString() );
		clone.setInitialConcentration( original.getInitialConcentration() );
		clone.setUnits( original.getUnits() );

		final Map<OntologyTerm,Object[]> ontologyTerms = getOntologyTerms( original );

		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = ontologyTerms.entrySet().iterator(); iterator.hasNext(); )
		{
			if( iterator.next().getKey().getOntologyName().equals( Ontology.INCHI ) )
			{
				iterator.remove();
			}
		}

		addOntologyTerms( clone, ontologyTerms );
	}
}