package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.logs.Arrest.ArrestReport;
import com.drozal.dataterminal.logs.Arrest.ArrestReports;
import com.drozal.dataterminal.logs.Callout.CalloutReport;
import com.drozal.dataterminal.logs.Death.DeathReport;
import com.drozal.dataterminal.logs.Impound.ImpoundReport;
import com.drozal.dataterminal.logs.Incident.IncidentReport;
import com.drozal.dataterminal.logs.Patrol.PatrolReport;
import com.drozal.dataterminal.logs.Search.SearchReport;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReport;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.drozal.dataterminal.util.Misc.controllerUtils.setSmallColumnWidth;

public class InitTableColumns {
	static double minColumnWidth = 185.0;
	
	//TODO change initcolumns
	
	public static void initializeDeathReportColumns(TableView tableview) {
		TableColumn<DeathReport, String> notesColumn = new TableColumn<>("Notes");
		notesColumn.setCellValueFactory(new PropertyValueFactory<>("notesTextArea"));
		
		TableColumn<DeathReport, String> divisionColumn = new TableColumn<>("Division");
		divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
		
		TableColumn<DeathReport, String> agencyColumn = new TableColumn<>("Agency");
		agencyColumn.setCellValueFactory(new PropertyValueFactory<>("agency"));
		
		TableColumn<DeathReport, String> numberColumn = new TableColumn<>("Number");
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		
		TableColumn<DeathReport, String> rankColumn = new TableColumn<>("Rank");
		rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
		
		TableColumn<DeathReport, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<DeathReport, String> streetColumn = new TableColumn<>("Street");
		streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
		
		TableColumn<DeathReport, String> countyColumn = new TableColumn<>("County");
		countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
		
		TableColumn<DeathReport, String> areaColumn = new TableColumn<>("Area");
		areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
		
		TableColumn<DeathReport, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<DeathReport, String> timeColumn = new TableColumn<>("Time");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		
		TableColumn<DeathReport, String> deathReportNumberColumn = new TableColumn<>("Death Report Number");
		deathReportNumberColumn.setCellValueFactory(new PropertyValueFactory<>("deathReportNumber"));
		
		TableColumn<DeathReport, String> decedentColumn = new TableColumn<>("Decedent");
		decedentColumn.setCellValueFactory(new PropertyValueFactory<>("decedent"));
		
		TableColumn<DeathReport, String> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
		
		TableColumn<DeathReport, String> genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		
		TableColumn<DeathReport, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		TableColumn<DeathReport, String> addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		
		TableColumn<DeathReport, String> witnessesColumn = new TableColumn<>("Witnesses");
		witnessesColumn.setCellValueFactory(new PropertyValueFactory<>("witnesses"));
		
		TableColumn<DeathReport, String> causeOfDeathColumn = new TableColumn<>("Cause of Death");
		causeOfDeathColumn.setCellValueFactory(new PropertyValueFactory<>("causeOfDeath"));
		
		TableColumn<DeathReport, String> modeOfDeathColumn = new TableColumn<>("Mode of Death");
		modeOfDeathColumn.setCellValueFactory(new PropertyValueFactory<>("modeOfDeath"));
		
		TableColumn<DeathReport, String> timeofDeathColumn = new TableColumn<>("Time of Death");
		timeofDeathColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfDeath"));
		
		TableColumn<DeathReport, String> dateOfDeathColumn = new TableColumn<>("Date of Death");
		dateOfDeathColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfDeath"));
		
		ObservableList<TableColumn<DeathReport, ?>> deathReportColumns = FXCollections.observableArrayList(notesColumn,
		                                                                                                   divisionColumn,
		                                                                                                   agencyColumn,
		                                                                                                   numberColumn,
		                                                                                                   rankColumn,
		                                                                                                   nameColumn,
		                                                                                                   streetColumn,
		                                                                                                   countyColumn,
		                                                                                                   areaColumn,
		                                                                                                   dateColumn,
		                                                                                                   timeColumn,
		                                                                                                   deathReportNumberColumn,
		                                                                                                   decedentColumn,
		                                                                                                   ageColumn,
		                                                                                                   genderColumn,
		                                                                                                   descriptionColumn,
		                                                                                                   addressColumn,
		                                                                                                   witnessesColumn,
		                                                                                                   causeOfDeathColumn,
		                                                                                                   modeOfDeathColumn,
		                                                                                                   timeofDeathColumn,
		                                                                                                   dateOfDeathColumn);
		
		tableview.getColumns().addAll(deathReportColumns);
		
		for (TableColumn<DeathReport, ?> column : deathReportColumns) {
			column.setMinWidth(minColumnWidth);
		}
		
		setSmallColumnWidth(deathReportNumberColumn);
		setSmallColumnWidth(dateColumn);
		setSmallColumnWidth(timeColumn);
		setSmallColumnWidth(timeofDeathColumn);
		setSmallColumnWidth(dateOfDeathColumn);
		setSmallColumnWidth(ageColumn);
		setSmallColumnWidth(genderColumn);
		setSmallColumnWidth(numberColumn);
	}
	
	public static void initializeImpoundColumns(TableView tableview) {
		
		TableColumn<ImpoundReport, String> impoundNumberColumn = new TableColumn<>("Impound #");
		impoundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundNumber"));
		
		TableColumn<ImpoundReport, String> impoundDateColumn = new TableColumn<>("Impound Date");
		impoundDateColumn.setCellValueFactory(new PropertyValueFactory<>("impoundDate"));
		
		TableColumn<ImpoundReport, String> impoundTimeColumn = new TableColumn<>("Impound Time");
		impoundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundTime"));
		
		TableColumn<ImpoundReport, String> ownerNameColumn = new TableColumn<>("Owner Name");
		ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		
		TableColumn<ImpoundReport, String> ownerAgeColumn = new TableColumn<>("Owner Age");
		ownerAgeColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAge"));
		
		TableColumn<ImpoundReport, String> ownerGenderColumn = new TableColumn<>("Owner Gender");
		ownerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("ownerGender"));
		
		TableColumn<ImpoundReport, String> ownerAddressColumn = new TableColumn<>("Owner Address");
		ownerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));
		
		TableColumn<ImpoundReport, String> impoundPlateNumberColumn = new TableColumn<>("Veh. Plate #");
		impoundPlateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundPlateNumber"));
		
		TableColumn<ImpoundReport, String> impoundModelColumn = new TableColumn<>("Veh. Model");
		impoundModelColumn.setCellValueFactory(new PropertyValueFactory<>("impoundModel"));
		
		TableColumn<ImpoundReport, String> impoundTypeColumn = new TableColumn<>("Veh. Type");
		impoundTypeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundType"));
		
		TableColumn<ImpoundReport, String> impoundColorColumn = new TableColumn<>("Veh. Color");
		impoundColorColumn.setCellValueFactory(new PropertyValueFactory<>("impoundColor"));
		
		TableColumn<ImpoundReport, String> impoundCommentsColumn = new TableColumn<>("Comments");
		impoundCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("impoundComments"));
		
		TableColumn<ImpoundReport, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<ImpoundReport, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<ImpoundReport, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<ImpoundReport, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<ImpoundReport, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		ObservableList<TableColumn<ImpoundReport, ?>> impoundColumns = FXCollections.observableArrayList(
				impoundNumberColumn, impoundDateColumn, impoundTimeColumn, ownerNameColumn, ownerAgeColumn,
				ownerGenderColumn, ownerAddressColumn, impoundPlateNumberColumn, impoundModelColumn, impoundTypeColumn,
				impoundColorColumn, impoundCommentsColumn, officerRankColumn, officerNameColumn, officerNumberColumn,
				officerDivisionColumn, officerAgencyColumn);
		
		tableview.getColumns().addAll(impoundColumns);
		
		for (TableColumn<ImpoundReport, ?> column : impoundColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(impoundNumberColumn);
		setSmallColumnWidth(impoundDateColumn);
		setSmallColumnWidth(impoundTimeColumn);
		setSmallColumnWidth(ownerAgeColumn);
		setSmallColumnWidth(ownerGenderColumn);
		setSmallColumnWidth(impoundPlateNumberColumn);
		setSmallColumnWidth(impoundModelColumn);
		setSmallColumnWidth(impoundColorColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializePatrolColumns(TableView tableview) {
		
		TableColumn<PatrolReport, String> patrolNumberColumn = new TableColumn<>("Patrol #");
		patrolNumberColumn.setCellValueFactory(new PropertyValueFactory<>("patrolNumber"));
		
		TableColumn<PatrolReport, String> patrolDateColumn = new TableColumn<>("Date");
		patrolDateColumn.setCellValueFactory(new PropertyValueFactory<>("patrolDate"));
		
		TableColumn<PatrolReport, String> patrolLengthColumn = new TableColumn<>("Length");
		patrolLengthColumn.setCellValueFactory(new PropertyValueFactory<>("patrolLength"));
		
		TableColumn<PatrolReport, String> patrolStartTimeColumn = new TableColumn<>("Start Time");
		patrolStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStartTime"));
		
		TableColumn<PatrolReport, String> patrolStopTimeColumn = new TableColumn<>("Stop Time");
		patrolStopTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStopTime"));
		
		TableColumn<PatrolReport, String> officerRankColumn = new TableColumn<>("Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<PatrolReport, String> officerNameColumn = new TableColumn<>("Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<PatrolReport, String> officerNumberColumn = new TableColumn<>("Number");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<PatrolReport, String> officerDivisionColumn = new TableColumn<>("Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<PatrolReport, String> officerAgencyColumn = new TableColumn<>("Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<PatrolReport, String> officerVehicleColumn = new TableColumn<>("Vehicle");
		officerVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("officerVehicle"));
		
		TableColumn<PatrolReport, String> patrolCommentsColumn = new TableColumn<>("Comments");
		patrolCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("patrolComments"));
		
		ObservableList<TableColumn<PatrolReport, ?>> patrolColumns = FXCollections.observableArrayList(
				patrolNumberColumn, patrolDateColumn, patrolLengthColumn, patrolStartTimeColumn, patrolStopTimeColumn,
				officerRankColumn, officerNameColumn, officerNumberColumn, officerDivisionColumn, officerAgencyColumn,
				officerVehicleColumn, patrolCommentsColumn);
		
		tableview.getColumns().addAll(patrolColumns);
		
		for (TableColumn<PatrolReport, ?> column : patrolColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(patrolNumberColumn);
		setSmallColumnWidth(patrolDateColumn);
		setSmallColumnWidth(patrolLengthColumn);
		setSmallColumnWidth(patrolStartTimeColumn);
		setSmallColumnWidth(patrolStopTimeColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializeCitationColumns(TableView tableview) {
		
		TableColumn<TrafficCitationReport, String> citationNumberColumn = new TableColumn<>("Citation #");
		citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));
		
		TableColumn<TrafficCitationReport, String> citationDateColumn = new TableColumn<>("Citation Date");
		citationDateColumn.setCellValueFactory(new PropertyValueFactory<>("citationDate"));
		
		TableColumn<TrafficCitationReport, String> citationTimeColumn = new TableColumn<>("Citation Time");
		citationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("citationTime"));
		
		TableColumn<TrafficCitationReport, String> citationChargesColumn = new TableColumn<>("Charges");
		citationChargesColumn.setCellValueFactory(new PropertyValueFactory<>("citationCharges"));
		
		TableColumn<TrafficCitationReport, String> citationCountyColumn = new TableColumn<>("County");
		citationCountyColumn.setCellValueFactory(new PropertyValueFactory<>("citationCounty"));
		
		TableColumn<TrafficCitationReport, String> citationAreaColumn = new TableColumn<>("Area");
		citationAreaColumn.setCellValueFactory(new PropertyValueFactory<>("citationArea"));
		
		TableColumn<TrafficCitationReport, String> citationStreetColumn = new TableColumn<>("Street");
		citationStreetColumn.setCellValueFactory(new PropertyValueFactory<>("citationStreet"));
		
		TableColumn<TrafficCitationReport, String> offenderNameColumn = new TableColumn<>("Sus. Name");
		offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));
		
		TableColumn<TrafficCitationReport, String> offenderGenderColumn = new TableColumn<>("Sus. Gender");
		offenderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("offenderGender"));
		
		TableColumn<TrafficCitationReport, String> offenderAgeColumn = new TableColumn<>("Sus. Age");
		offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));
		
		TableColumn<TrafficCitationReport, String> offenderHomeAddressColumn = new TableColumn<>("Sus. Address");
		offenderHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("offenderHomeAddress"));
		
		TableColumn<TrafficCitationReport, String> offenderDescriptionColumn = new TableColumn<>("Sus. Description");
		offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));
		
		TableColumn<TrafficCitationReport, String> offenderVehicleModelColumn = new TableColumn<>("Sus. Veh. Model");
		offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));
		
		TableColumn<TrafficCitationReport, String> offenderVehicleColorColumn = new TableColumn<>("Sus. Veh. Color");
		offenderVehicleColorColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleColor"));
		
		TableColumn<TrafficCitationReport, String> offenderVehicleTypeColumn = new TableColumn<>("Sus. Veh. Type");
		offenderVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleType"));
		
		TableColumn<TrafficCitationReport, String> offenderVehiclePlateColumn = new TableColumn<>("Sus. Veh. Plate");
		offenderVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehiclePlate"));
		
		TableColumn<TrafficCitationReport, String> offenderVehicleOtherColumn = new TableColumn<>("Sus. Veh. Other");
		offenderVehicleOtherColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleOther"));
		
		TableColumn<TrafficCitationReport, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<TrafficCitationReport, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<TrafficCitationReport, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<TrafficCitationReport, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<TrafficCitationReport, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<TrafficCitationReport, String> citationCommentsColumn = new TableColumn<>("Comments");
		citationCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("citationComments"));
		
		ObservableList<TableColumn<TrafficCitationReport, ?>> citationColumns = FXCollections.observableArrayList(
				citationNumberColumn, citationDateColumn, citationTimeColumn, citationChargesColumn,
				citationCountyColumn, citationAreaColumn, citationStreetColumn, offenderNameColumn,
				offenderGenderColumn, offenderAgeColumn, offenderHomeAddressColumn, offenderDescriptionColumn,
				offenderVehicleModelColumn, offenderVehicleColorColumn, offenderVehicleTypeColumn,
				offenderVehiclePlateColumn, offenderVehicleOtherColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn, citationCommentsColumn);
		
		tableview.getColumns().addAll(citationColumns);
		
		for (TableColumn<TrafficCitationReport, ?> column : citationColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(citationNumberColumn);
		setSmallColumnWidth(citationDateColumn);
		setSmallColumnWidth(citationTimeColumn);
		setSmallColumnWidth(offenderGenderColumn);
		setSmallColumnWidth(offenderAgeColumn);
		setSmallColumnWidth(offenderVehicleColorColumn);
		setSmallColumnWidth(offenderVehiclePlateColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializeArrestColumns(TableView tableview) {
		
		TableColumn<ArrestReport, String> arrestNumberColumn = new TableColumn<>("Arrest #");
		arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));
		
		TableColumn<ArrestReport, String> arrestDateColumn = new TableColumn<>("Arrest Date");
		arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));
		
		TableColumn<ArrestReport, String> arrestTimeColumn = new TableColumn<>("Arrest Time");
		arrestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrestTime"));
		
		TableColumn<ArrestReport, String> arrestChargesColumn = new TableColumn<>("Charges");
		arrestChargesColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCharges"));
		
		TableColumn<ArrestReport, String> arrestCountyColumn = new TableColumn<>("County");
		arrestCountyColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCounty"));
		
		TableColumn<ArrestReport, String> arrestAreaColumn = new TableColumn<>("Area");
		arrestAreaColumn.setCellValueFactory(new PropertyValueFactory<>("arrestArea"));
		
		TableColumn<ArrestReport, String> arrestStreetColumn = new TableColumn<>("Street");
		arrestStreetColumn.setCellValueFactory(new PropertyValueFactory<>("arrestStreet"));
		
		TableColumn<ArrestReport, String> arresteeNameColumn = new TableColumn<>("Sus. Name");
		arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));
		
		TableColumn<ArrestReport, String> arresteeAgeColumn = new TableColumn<>("Sus. Age/DOB");
		arresteeAgeColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeAge"));
		
		TableColumn<ArrestReport, String> arresteeGenderColumn = new TableColumn<>("Sus. Gender");
		arresteeGenderColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeGender"));
		
		TableColumn<ArrestReport, String> arresteeDescriptionColumn = new TableColumn<>("Sus. Description");
		arresteeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeDescription"));
		
		TableColumn<ArrestReport, String> ambulanceYesNoColumn = new TableColumn<>("Ambulance (Y/N)");
		ambulanceYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanceYesNo"));
		
		TableColumn<ArrestReport, String> taserYesNoColumn = new TableColumn<>("Taser (Y/N)");
		taserYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("TaserYesNo"));
		
		TableColumn<ArrestReport, String> arresteeMedicalInformationColumn = new TableColumn<>("Med. Info.");
		arresteeMedicalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeMedicalInformation"));
		
		TableColumn<ArrestReport, String> arresteeHomeAddressColumn = new TableColumn<>("Sus. Address");
		arresteeHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeHomeAddress"));
		
		TableColumn<ArrestReport, String> arrestDetailsColumn = new TableColumn<>("Details");
		arrestDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDetails"));
		
		TableColumn<ArrestReport, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<ArrestReport, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<ArrestReport, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<ArrestReport, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<ArrestReport, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		ObservableList<TableColumn<ArrestReport, ?>> arrestColumns = FXCollections.observableArrayList(
				arrestNumberColumn, arrestDateColumn, arrestTimeColumn, arrestChargesColumn, arrestCountyColumn,
				arrestAreaColumn, arrestStreetColumn, arresteeNameColumn, arresteeAgeColumn, arresteeGenderColumn,
				arresteeDescriptionColumn, ambulanceYesNoColumn, taserYesNoColumn, arresteeMedicalInformationColumn,
				arresteeHomeAddressColumn, arrestDetailsColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn);
		
		tableview.getColumns().addAll(arrestColumns);
		
		for (TableColumn<ArrestReport, ?> column : arrestColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(arrestNumberColumn);
		setSmallColumnWidth(arrestDateColumn);
		setSmallColumnWidth(arrestTimeColumn);
		setSmallColumnWidth(arresteeAgeColumn);
		setSmallColumnWidth(arresteeGenderColumn);
		setSmallColumnWidth(ambulanceYesNoColumn);
		setSmallColumnWidth(taserYesNoColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializeIncidentColumns(TableView tableview) {
		
		TableColumn<IncidentReport, String> incidentNumberColumn = new TableColumn<>("Incident #");
		incidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));
		
		TableColumn<IncidentReport, String> incidentDateColumn = new TableColumn<>("Date");
		incidentDateColumn.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));
		
		TableColumn<IncidentReport, String> incidentTimeColumn = new TableColumn<>("Time");
		incidentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("incidentTime"));
		
		TableColumn<IncidentReport, String> incidentStatementColumn = new TableColumn<>("Statement");
		incidentStatementColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStatement"));
		
		TableColumn<IncidentReport, String> incidentWitnessesColumn = new TableColumn<>("Suspects");
		incidentWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("incidentWitnesses"));
		
		TableColumn<IncidentReport, String> incidentVictimsColumn = new TableColumn<>("Victims/Witnesses");
		incidentVictimsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentVictims"));
		
		TableColumn<IncidentReport, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<IncidentReport, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<IncidentReport, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<IncidentReport, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<IncidentReport, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<IncidentReport, String> incidentStreetColumn = new TableColumn<>("Street");
		incidentStreetColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStreet"));
		
		TableColumn<IncidentReport, String> incidentAreaColumn = new TableColumn<>("Area");
		incidentAreaColumn.setCellValueFactory(new PropertyValueFactory<>("incidentArea"));
		
		TableColumn<IncidentReport, String> incidentCountyColumn = new TableColumn<>("County");
		incidentCountyColumn.setCellValueFactory(new PropertyValueFactory<>("incidentCounty"));
		
		TableColumn<IncidentReport, String> incidentActionsTakenColumn = new TableColumn<>("Details");
		incidentActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("incidentActionsTaken"));
		
		TableColumn<IncidentReport, String> incidentCommentsColumn = new TableColumn<>("Comments");
		incidentCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentComments"));
		
		ObservableList<TableColumn<IncidentReport, ?>> incidentColumns = FXCollections.observableArrayList(
				incidentNumberColumn, incidentDateColumn, incidentTimeColumn, incidentStatementColumn,
				incidentWitnessesColumn, incidentVictimsColumn, officerNameColumn, officerRankColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, incidentStreetColumn,
				incidentAreaColumn, incidentCountyColumn, incidentActionsTakenColumn, incidentCommentsColumn);
		
		tableview.getColumns().addAll(incidentColumns);
		for (TableColumn<IncidentReport, ?> column : incidentColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(incidentNumberColumn);
		setSmallColumnWidth(incidentDateColumn);
		setSmallColumnWidth(incidentTimeColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializeSearchColumns(TableView tableview) {
		
		TableColumn<SearchReport, String> searchNumberColumn = new TableColumn<>("Search #");
		searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));
		
		TableColumn<SearchReport, String> searchDateColumn = new TableColumn<>("Date");
		searchDateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));
		
		TableColumn<SearchReport, String> searchTimeColumn = new TableColumn<>("Time");
		searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("searchTime"));
		
		TableColumn<SearchReport, String> searchSeizedItemsColumn = new TableColumn<>("Details/Field Sob.");
		searchSeizedItemsColumn.setCellValueFactory(new PropertyValueFactory<>("searchSeizedItems"));
		
		TableColumn<SearchReport, String> searchGroundsColumn = new TableColumn<>("Grounds");
		searchGroundsColumn.setCellValueFactory(new PropertyValueFactory<>("searchGrounds"));
		
		TableColumn<SearchReport, String> searchTypeColumn = new TableColumn<>("Type");
		searchTypeColumn.setCellValueFactory(new PropertyValueFactory<>("searchType"));
		
		TableColumn<SearchReport, String> searchMethodColumn = new TableColumn<>("Method");
		searchMethodColumn.setCellValueFactory(new PropertyValueFactory<>("searchMethod"));
		
		TableColumn<SearchReport, String> searchWitnessesColumn = new TableColumn<>("Witnesses");
		searchWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("searchWitnesses"));
		
		TableColumn<SearchReport, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<SearchReport, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<SearchReport, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<SearchReport, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<SearchReport, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<SearchReport, String> searchStreetColumn = new TableColumn<>("Street");
		searchStreetColumn.setCellValueFactory(new PropertyValueFactory<>("searchStreet"));
		
		TableColumn<SearchReport, String> searchAreaColumn = new TableColumn<>("Area");
		searchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("searchArea"));
		
		TableColumn<SearchReport, String> searchCountyColumn = new TableColumn<>("County");
		searchCountyColumn.setCellValueFactory(new PropertyValueFactory<>("searchCounty"));
		
		TableColumn<SearchReport, String> searchCommentsColumn = new TableColumn<>("Comments");
		searchCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("searchComments"));
		
		TableColumn<SearchReport, String> searchedPersonsColumn = new TableColumn<>("Sus. Searched");
		searchedPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("searchedPersons"));
		
		TableColumn<SearchReport, String> testsConductedColumn = new TableColumn<>("Test(s) Cond.");
		testsConductedColumn.setCellValueFactory(new PropertyValueFactory<>("testsConducted"));
		
		TableColumn<SearchReport, String> resultsColumn = new TableColumn<>("Result(s)");
		resultsColumn.setCellValueFactory(new PropertyValueFactory<>("testResults"));
		
		TableColumn<SearchReport, String> BACMeasurementColumn = new TableColumn<>("BAC");
		BACMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerBACMeasure"));
		
		ObservableList<TableColumn<SearchReport, ?>> searchColumns = FXCollections.observableArrayList(
				searchNumberColumn, searchDateColumn, searchTimeColumn, searchSeizedItemsColumn, searchGroundsColumn,
				searchTypeColumn, searchMethodColumn, searchWitnessesColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, searchStreetColumn, searchAreaColumn,
				searchCountyColumn, searchCommentsColumn, searchedPersonsColumn, testsConductedColumn, resultsColumn,
				BACMeasurementColumn);
		
		tableview.getColumns().addAll(searchColumns);
		
		for (TableColumn<SearchReport, ?> column : searchColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(searchNumberColumn);
		setSmallColumnWidth(searchDateColumn);
		setSmallColumnWidth(searchTimeColumn);
		setSmallColumnWidth(officerNumberColumn);
		setSmallColumnWidth(testsConductedColumn);
		setSmallColumnWidth(resultsColumn);
		setSmallColumnWidth(BACMeasurementColumn);
	}
	
	public static void initializeTrafficStopColumns(TableView tableview) {
		TableColumn<TrafficStopLogEntry, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
		
		TableColumn<TrafficStopLogEntry, String> timeColumn = new TableColumn<>("Time");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
		
		TableColumn<TrafficStopLogEntry, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		TableColumn<TrafficStopLogEntry, String> rankColumn = new TableColumn<>("Rank");
		rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));
		
		TableColumn<TrafficStopLogEntry, String> numberColumn = new TableColumn<>("Number");
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
		
		TableColumn<TrafficStopLogEntry, String> divisionColumn = new TableColumn<>("Division");
		divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));
		
		TableColumn<TrafficStopLogEntry, String> agencyColumn = new TableColumn<>("Agency");
		agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));
		
		TableColumn<TrafficStopLogEntry, String> stopNumberColumn = new TableColumn<>("Stop #");
		stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("StopNumber"));
		
		TableColumn<TrafficStopLogEntry, String> commentsTextAreaColumn = new TableColumn<>("Comments");
		commentsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("CommentsTextArea"));
		
		TableColumn<TrafficStopLogEntry, String> streetColumn = new TableColumn<>("Street");
		streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));
		
		TableColumn<TrafficStopLogEntry, String> countyColumn = new TableColumn<>("County");
		countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));
		
		TableColumn<TrafficStopLogEntry, String> areaColumn = new TableColumn<>("Area");
		areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));
		
		TableColumn<TrafficStopLogEntry, String> plateNumberColumn = new TableColumn<>("Plate #");
		plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
		
		TableColumn<TrafficStopLogEntry, String> colorColumn = new TableColumn<>("Color");
		colorColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));
		
		TableColumn<TrafficStopLogEntry, String> typeColumn = new TableColumn<>("Type");
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
		
		TableColumn<TrafficStopLogEntry, String> modelColumn = new TableColumn<>("Model");
		modelColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseModel"));
		
		TableColumn<TrafficStopLogEntry, String> otherInfoColumn = new TableColumn<>("Other Info.");
		otherInfoColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseOtherInfo"));
		
		TableColumn<TrafficStopLogEntry, String> operatorNameColumn = new TableColumn<>("Operator Name");
		operatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("operatorName"));
		
		TableColumn<TrafficStopLogEntry, String> operatorAgeColumn = new TableColumn<>("Operator Age");
		operatorAgeColumn.setCellValueFactory(new PropertyValueFactory<>("operatorAge"));
		
		TableColumn<TrafficStopLogEntry, String> operatorGenderColumn = new TableColumn<>("Operator Gender");
		operatorGenderColumn.setCellValueFactory(new PropertyValueFactory<>("operatorGender"));
		
		TableColumn<TrafficStopLogEntry, String> operatorDescriptionColumn = new TableColumn<>("Operator Description");
		operatorDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("operatorDescription"));
		
		TableColumn<TrafficStopLogEntry, String> operatorAddressColumn = new TableColumn<>("Operator Address");
		operatorAddressColumn.setCellValueFactory(new PropertyValueFactory<>("operatorAddress"));
		
		ObservableList<TableColumn<TrafficStopLogEntry, ?>> trafficStopColumns = FXCollections.observableArrayList(
				stopNumberColumn, dateColumn, timeColumn, modelColumn, otherInfoColumn, operatorNameColumn,
				operatorAgeColumn, operatorAddressColumn, operatorDescriptionColumn, operatorGenderColumn, nameColumn,
				rankColumn, numberColumn, divisionColumn, agencyColumn, commentsTextAreaColumn, streetColumn,
				countyColumn, areaColumn, plateNumberColumn, colorColumn, typeColumn);
		
		tableview.getColumns().addAll(trafficStopColumns);
		for (TableColumn<TrafficStopLogEntry, ?> column : trafficStopColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(stopNumberColumn);
		setSmallColumnWidth(dateColumn);
		setSmallColumnWidth(timeColumn);
		setSmallColumnWidth(operatorAgeColumn);
		setSmallColumnWidth(operatorGenderColumn);
		setSmallColumnWidth(numberColumn);
		setSmallColumnWidth(plateNumberColumn);
		setSmallColumnWidth(colorColumn);
		setSmallColumnWidth(numberColumn);
		
	}
	
	public static void initializeCalloutColumns(TableView tableview) {
		
		TableColumn<CalloutReport, String> calloutNumberColumn = new TableColumn<>("Callout #");
		calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));
		
		TableColumn<CalloutReport, String> notesTextAreaColumn = new TableColumn<>("Notes");
		notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));
		
		TableColumn<CalloutReport, String> responseGradeColumn = new TableColumn<>("Grade");
		responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));
		
		TableColumn<CalloutReport, String> responseTypeColumn = new TableColumn<>("Type");
		responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseType"));
		
		TableColumn<CalloutReport, String> timeColumn = new TableColumn<>("Time");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
		
		TableColumn<CalloutReport, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
		
		TableColumn<CalloutReport, String> divisionColumn = new TableColumn<>("Division");
		divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));
		
		TableColumn<CalloutReport, String> agencyColumn = new TableColumn<>("Agency");
		agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));
		
		TableColumn<CalloutReport, String> numberColumn = new TableColumn<>("Number");
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
		
		TableColumn<CalloutReport, String> rankColumn = new TableColumn<>("Rank");
		rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));
		
		TableColumn<CalloutReport, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		TableColumn<CalloutReport, String> addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
		
		TableColumn<CalloutReport, String> countyColumn = new TableColumn<>("County");
		countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));
		
		TableColumn<CalloutReport, String> areaColumn = new TableColumn<>("Area");
		areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));
		
		ObservableList<TableColumn<CalloutReport, ?>> columns = FXCollections.observableArrayList(calloutNumberColumn,
		                                                                                          dateColumn,
		                                                                                          timeColumn,
		                                                                                          notesTextAreaColumn,
		                                                                                          responseGradeColumn,
		                                                                                          responseTypeColumn,
		                                                                                          divisionColumn,
		                                                                                          agencyColumn,
		                                                                                          numberColumn,
		                                                                                          rankColumn,
		                                                                                          nameColumn,
		                                                                                          addressColumn,
		                                                                                          countyColumn,
		                                                                                          areaColumn);
		tableview.getColumns().addAll(columns);
		for (TableColumn<CalloutReport, ?> column : columns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(calloutNumberColumn);
		setSmallColumnWidth(dateColumn);
		setSmallColumnWidth(timeColumn);
		setSmallColumnWidth(numberColumn);
		setSmallColumnWidth(responseGradeColumn);
	}
	
}
