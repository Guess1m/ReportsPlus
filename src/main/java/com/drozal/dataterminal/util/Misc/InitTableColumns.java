package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReport;
import com.drozal.dataterminal.logs.Death.DeathReport;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReport;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
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
		
		TableColumn<ImpoundLogEntry, String> impoundNumberColumn = new TableColumn<>("Impound #");
		impoundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundNumber"));
		
		TableColumn<ImpoundLogEntry, String> impoundDateColumn = new TableColumn<>("Impound Date");
		impoundDateColumn.setCellValueFactory(new PropertyValueFactory<>("impoundDate"));
		
		TableColumn<ImpoundLogEntry, String> impoundTimeColumn = new TableColumn<>("Impound Time");
		impoundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundTime"));
		
		TableColumn<ImpoundLogEntry, String> ownerNameColumn = new TableColumn<>("Owner Name");
		ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		
		TableColumn<ImpoundLogEntry, String> ownerAgeColumn = new TableColumn<>("Owner Age");
		ownerAgeColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAge"));
		
		TableColumn<ImpoundLogEntry, String> ownerGenderColumn = new TableColumn<>("Owner Gender");
		ownerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("ownerGender"));
		
		TableColumn<ImpoundLogEntry, String> ownerAddressColumn = new TableColumn<>("Owner Address");
		ownerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));
		
		TableColumn<ImpoundLogEntry, String> impoundPlateNumberColumn = new TableColumn<>("Veh. Plate #");
		impoundPlateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundPlateNumber"));
		
		TableColumn<ImpoundLogEntry, String> impoundModelColumn = new TableColumn<>("Veh. Model");
		impoundModelColumn.setCellValueFactory(new PropertyValueFactory<>("impoundModel"));
		
		TableColumn<ImpoundLogEntry, String> impoundTypeColumn = new TableColumn<>("Veh. Type");
		impoundTypeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundType"));
		
		TableColumn<ImpoundLogEntry, String> impoundColorColumn = new TableColumn<>("Veh. Color");
		impoundColorColumn.setCellValueFactory(new PropertyValueFactory<>("impoundColor"));
		
		TableColumn<ImpoundLogEntry, String> impoundCommentsColumn = new TableColumn<>("Comments");
		impoundCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("impoundComments"));
		
		TableColumn<ImpoundLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<ImpoundLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<ImpoundLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<ImpoundLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<ImpoundLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		ObservableList<TableColumn<ImpoundLogEntry, ?>> impoundColumns = FXCollections.observableArrayList(
				impoundNumberColumn, impoundDateColumn, impoundTimeColumn, ownerNameColumn, ownerAgeColumn,
				ownerGenderColumn, ownerAddressColumn, impoundPlateNumberColumn, impoundModelColumn, impoundTypeColumn,
				impoundColorColumn, impoundCommentsColumn, officerRankColumn, officerNameColumn, officerNumberColumn,
				officerDivisionColumn, officerAgencyColumn);
		
		tableview.getColumns().addAll(impoundColumns);
		
		for (TableColumn<ImpoundLogEntry, ?> column : impoundColumns) {
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
		
		TableColumn<TrafficCitationLogEntry, String> citationNumberColumn = new TableColumn<>("Citation #");
		citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));
		
		TableColumn<TrafficCitationLogEntry, String> citationDateColumn = new TableColumn<>("Citation Date");
		citationDateColumn.setCellValueFactory(new PropertyValueFactory<>("citationDate"));
		
		TableColumn<TrafficCitationLogEntry, String> citationTimeColumn = new TableColumn<>("Citation Time");
		citationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("citationTime"));
		
		TableColumn<TrafficCitationLogEntry, String> citationChargesColumn = new TableColumn<>("Charges");
		citationChargesColumn.setCellValueFactory(new PropertyValueFactory<>("citationCharges"));
		
		TableColumn<TrafficCitationLogEntry, String> citationCountyColumn = new TableColumn<>("County");
		citationCountyColumn.setCellValueFactory(new PropertyValueFactory<>("citationCounty"));
		
		TableColumn<TrafficCitationLogEntry, String> citationAreaColumn = new TableColumn<>("Area");
		citationAreaColumn.setCellValueFactory(new PropertyValueFactory<>("citationArea"));
		
		TableColumn<TrafficCitationLogEntry, String> citationStreetColumn = new TableColumn<>("Street");
		citationStreetColumn.setCellValueFactory(new PropertyValueFactory<>("citationStreet"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderNameColumn = new TableColumn<>("Sus. Name");
		offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderGenderColumn = new TableColumn<>("Sus. Gender");
		offenderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("offenderGender"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderAgeColumn = new TableColumn<>("Sus. Age");
		offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderHomeAddressColumn = new TableColumn<>("Sus. Address");
		offenderHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("offenderHomeAddress"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderDescriptionColumn = new TableColumn<>("Sus. Description");
		offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderVehicleModelColumn = new TableColumn<>("Sus. Veh. Model");
		offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderVehicleColorColumn = new TableColumn<>("Sus. Veh. Color");
		offenderVehicleColorColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleColor"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderVehicleTypeColumn = new TableColumn<>("Sus. Veh. Type");
		offenderVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleType"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderVehiclePlateColumn = new TableColumn<>("Sus. Veh. Plate");
		offenderVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehiclePlate"));
		
		TableColumn<TrafficCitationLogEntry, String> offenderVehicleOtherColumn = new TableColumn<>("Sus. Veh. Other");
		offenderVehicleOtherColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleOther"));
		
		TableColumn<TrafficCitationLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<TrafficCitationLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<TrafficCitationLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<TrafficCitationLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<TrafficCitationLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<TrafficCitationLogEntry, String> citationCommentsColumn = new TableColumn<>("Comments");
		citationCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("citationComments"));
		
		ObservableList<TableColumn<TrafficCitationLogEntry, ?>> citationColumns = FXCollections.observableArrayList(
				citationNumberColumn, citationDateColumn, citationTimeColumn, citationChargesColumn,
				citationCountyColumn, citationAreaColumn, citationStreetColumn, offenderNameColumn,
				offenderGenderColumn, offenderAgeColumn, offenderHomeAddressColumn, offenderDescriptionColumn,
				offenderVehicleModelColumn, offenderVehicleColorColumn, offenderVehicleTypeColumn,
				offenderVehiclePlateColumn, offenderVehicleOtherColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn, citationCommentsColumn);
		
		tableview.getColumns().addAll(citationColumns);
		
		for (TableColumn<TrafficCitationLogEntry, ?> column : citationColumns) {
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
		
		TableColumn<ArrestLogEntry, String> arrestNumberColumn = new TableColumn<>("Arrest #");
		arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));
		
		TableColumn<ArrestLogEntry, String> arrestDateColumn = new TableColumn<>("Arrest Date");
		arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));
		
		TableColumn<ArrestLogEntry, String> arrestTimeColumn = new TableColumn<>("Arrest Time");
		arrestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrestTime"));
		
		TableColumn<ArrestLogEntry, String> arrestChargesColumn = new TableColumn<>("Charges");
		arrestChargesColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCharges"));
		
		TableColumn<ArrestLogEntry, String> arrestCountyColumn = new TableColumn<>("County");
		arrestCountyColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCounty"));
		
		TableColumn<ArrestLogEntry, String> arrestAreaColumn = new TableColumn<>("Area");
		arrestAreaColumn.setCellValueFactory(new PropertyValueFactory<>("arrestArea"));
		
		TableColumn<ArrestLogEntry, String> arrestStreetColumn = new TableColumn<>("Street");
		arrestStreetColumn.setCellValueFactory(new PropertyValueFactory<>("arrestStreet"));
		
		TableColumn<ArrestLogEntry, String> arresteeNameColumn = new TableColumn<>("Sus. Name");
		arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));
		
		TableColumn<ArrestLogEntry, String> arresteeAgeColumn = new TableColumn<>("Sus. Age/DOB");
		arresteeAgeColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeAge"));
		
		TableColumn<ArrestLogEntry, String> arresteeGenderColumn = new TableColumn<>("Sus. Gender");
		arresteeGenderColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeGender"));
		
		TableColumn<ArrestLogEntry, String> arresteeDescriptionColumn = new TableColumn<>("Sus. Description");
		arresteeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeDescription"));
		
		TableColumn<ArrestLogEntry, String> ambulanceYesNoColumn = new TableColumn<>("Ambulance (Y/N)");
		ambulanceYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanceYesNo"));
		
		TableColumn<ArrestLogEntry, String> taserYesNoColumn = new TableColumn<>("Taser (Y/N)");
		taserYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("TaserYesNo"));
		
		TableColumn<ArrestLogEntry, String> arresteeMedicalInformationColumn = new TableColumn<>("Med. Info.");
		arresteeMedicalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeMedicalInformation"));
		
		TableColumn<ArrestLogEntry, String> arresteeHomeAddressColumn = new TableColumn<>("Sus. Address");
		arresteeHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeHomeAddress"));
		
		TableColumn<ArrestLogEntry, String> arrestDetailsColumn = new TableColumn<>("Details");
		arrestDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDetails"));
		
		TableColumn<ArrestLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<ArrestLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<ArrestLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<ArrestLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<ArrestLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		ObservableList<TableColumn<ArrestLogEntry, ?>> arrestColumns = FXCollections.observableArrayList(
				arrestNumberColumn, arrestDateColumn, arrestTimeColumn, arrestChargesColumn, arrestCountyColumn,
				arrestAreaColumn, arrestStreetColumn, arresteeNameColumn, arresteeAgeColumn, arresteeGenderColumn,
				arresteeDescriptionColumn, ambulanceYesNoColumn, taserYesNoColumn, arresteeMedicalInformationColumn,
				arresteeHomeAddressColumn, arrestDetailsColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn);
		
		tableview.getColumns().addAll(arrestColumns);
		
		for (TableColumn<ArrestLogEntry, ?> column : arrestColumns) {
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
		
		TableColumn<IncidentLogEntry, String> incidentNumberColumn = new TableColumn<>("Incident #");
		incidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));
		
		TableColumn<IncidentLogEntry, String> incidentDateColumn = new TableColumn<>("Date");
		incidentDateColumn.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));
		
		TableColumn<IncidentLogEntry, String> incidentTimeColumn = new TableColumn<>("Time");
		incidentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("incidentTime"));
		
		TableColumn<IncidentLogEntry, String> incidentStatementColumn = new TableColumn<>("Statement");
		incidentStatementColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStatement"));
		
		TableColumn<IncidentLogEntry, String> incidentWitnessesColumn = new TableColumn<>("Suspects");
		incidentWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("incidentWitnesses"));
		
		TableColumn<IncidentLogEntry, String> incidentVictimsColumn = new TableColumn<>("Victims/Witnesses");
		incidentVictimsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentVictims"));
		
		TableColumn<IncidentLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<IncidentLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<IncidentLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<IncidentLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<IncidentLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<IncidentLogEntry, String> incidentStreetColumn = new TableColumn<>("Street");
		incidentStreetColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStreet"));
		
		TableColumn<IncidentLogEntry, String> incidentAreaColumn = new TableColumn<>("Area");
		incidentAreaColumn.setCellValueFactory(new PropertyValueFactory<>("incidentArea"));
		
		TableColumn<IncidentLogEntry, String> incidentCountyColumn = new TableColumn<>("County");
		incidentCountyColumn.setCellValueFactory(new PropertyValueFactory<>("incidentCounty"));
		
		TableColumn<IncidentLogEntry, String> incidentActionsTakenColumn = new TableColumn<>("Details");
		incidentActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("incidentActionsTaken"));
		
		TableColumn<IncidentLogEntry, String> incidentCommentsColumn = new TableColumn<>("Comments");
		incidentCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentComments"));
		
		ObservableList<TableColumn<IncidentLogEntry, ?>> incidentColumns = FXCollections.observableArrayList(
				incidentNumberColumn, incidentDateColumn, incidentTimeColumn, incidentStatementColumn,
				incidentWitnessesColumn, incidentVictimsColumn, officerNameColumn, officerRankColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, incidentStreetColumn,
				incidentAreaColumn, incidentCountyColumn, incidentActionsTakenColumn, incidentCommentsColumn);
		
		tableview.getColumns().addAll(incidentColumns);
		for (TableColumn<IncidentLogEntry, ?> column : incidentColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(incidentNumberColumn);
		setSmallColumnWidth(incidentDateColumn);
		setSmallColumnWidth(incidentTimeColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public static void initializeSearchColumns(TableView tableview) {
		
		TableColumn<SearchLogEntry, String> searchNumberColumn = new TableColumn<>("Search #");
		searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));
		
		TableColumn<SearchLogEntry, String> searchDateColumn = new TableColumn<>("Date");
		searchDateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));
		
		TableColumn<SearchLogEntry, String> searchTimeColumn = new TableColumn<>("Time");
		searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("searchTime"));
		
		TableColumn<SearchLogEntry, String> searchSeizedItemsColumn = new TableColumn<>("Details/Field Sob.");
		searchSeizedItemsColumn.setCellValueFactory(new PropertyValueFactory<>("searchSeizedItems"));
		
		TableColumn<SearchLogEntry, String> searchGroundsColumn = new TableColumn<>("Grounds");
		searchGroundsColumn.setCellValueFactory(new PropertyValueFactory<>("searchGrounds"));
		
		TableColumn<SearchLogEntry, String> searchTypeColumn = new TableColumn<>("Type");
		searchTypeColumn.setCellValueFactory(new PropertyValueFactory<>("searchType"));
		
		TableColumn<SearchLogEntry, String> searchMethodColumn = new TableColumn<>("Method");
		searchMethodColumn.setCellValueFactory(new PropertyValueFactory<>("searchMethod"));
		
		TableColumn<SearchLogEntry, String> searchWitnessesColumn = new TableColumn<>("Witnesses");
		searchWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("searchWitnesses"));
		
		TableColumn<SearchLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
		officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));
		
		TableColumn<SearchLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		
		TableColumn<SearchLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
		officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));
		
		TableColumn<SearchLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
		officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));
		
		TableColumn<SearchLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
		officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));
		
		TableColumn<SearchLogEntry, String> searchStreetColumn = new TableColumn<>("Street");
		searchStreetColumn.setCellValueFactory(new PropertyValueFactory<>("searchStreet"));
		
		TableColumn<SearchLogEntry, String> searchAreaColumn = new TableColumn<>("Area");
		searchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("searchArea"));
		
		TableColumn<SearchLogEntry, String> searchCountyColumn = new TableColumn<>("County");
		searchCountyColumn.setCellValueFactory(new PropertyValueFactory<>("searchCounty"));
		
		TableColumn<SearchLogEntry, String> searchCommentsColumn = new TableColumn<>("Comments");
		searchCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("searchComments"));
		
		TableColumn<SearchLogEntry, String> searchedPersonsColumn = new TableColumn<>("Sus. Searched");
		searchedPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("searchedPersons"));
		
		TableColumn<SearchLogEntry, String> testsConductedColumn = new TableColumn<>("Test(s) Cond.");
		testsConductedColumn.setCellValueFactory(new PropertyValueFactory<>("testsConducted"));
		
		TableColumn<SearchLogEntry, String> resultsColumn = new TableColumn<>("Result(s)");
		resultsColumn.setCellValueFactory(new PropertyValueFactory<>("testResults"));
		
		TableColumn<SearchLogEntry, String> BACMeasurementColumn = new TableColumn<>("BAC");
		BACMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerBACMeasure"));
		
		ObservableList<TableColumn<SearchLogEntry, ?>> searchColumns = FXCollections.observableArrayList(
				searchNumberColumn, searchDateColumn, searchTimeColumn, searchSeizedItemsColumn, searchGroundsColumn,
				searchTypeColumn, searchMethodColumn, searchWitnessesColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, searchStreetColumn, searchAreaColumn,
				searchCountyColumn, searchCommentsColumn, searchedPersonsColumn, testsConductedColumn, resultsColumn,
				BACMeasurementColumn);
		
		tableview.getColumns().addAll(searchColumns);
		
		for (TableColumn<SearchLogEntry, ?> column : searchColumns) {
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
