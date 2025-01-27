package com.Guess.ReportsPlus.util.Other;

import com.Guess.ReportsPlus.logs.Accident.AccidentReport;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReport;
import com.Guess.ReportsPlus.logs.Callout.CalloutReport;
import com.Guess.ReportsPlus.logs.Death.DeathReport;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReport;
import com.Guess.ReportsPlus.logs.Incident.IncidentReport;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReport;
import com.Guess.ReportsPlus.logs.Search.SearchReport;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationReport;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReport;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.Guess.ReportsPlus.util.Other.controllerUtils.setSmallColumnWidth;

public class InitTableColumns {
	public static String commonTableFontSize = "-fx-font-size: 11; -fx-font-weight: bold; -fx-font-family: \"Segoe UI\";";
	
	public static void initializeDeathReportColumns(TableView tableview) {
		TableColumn<DeathReport, String> deathReportNumberColumn = new TableColumn<>("Report #:");
		deathReportNumberColumn.setCellValueFactory(new PropertyValueFactory<>("deathReportNumber"));
		deathReportNumberColumn.setCellFactory(column -> new TableCell<DeathReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<DeathReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<DeathReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<DeathReport, String> decedentColumn = new TableColumn<>("Decedent:");
		decedentColumn.setCellValueFactory(new PropertyValueFactory<>("decedent"));
		decedentColumn.setCellFactory(column -> new TableCell<DeathReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<DeathReport, String> causeOfDeathColumn = new TableColumn<>("Cause of Death:");
		causeOfDeathColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(causeOfDeathColumn, Pos.CENTER_LEFT, "Cause of Death:");
		causeOfDeathColumn.setCellValueFactory(new PropertyValueFactory<>("causeOfDeath"));
		causeOfDeathColumn.setCellFactory(column -> new TableCell<DeathReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<DeathReport, String> modeOfDeathColumn = new TableColumn<>("Mode of Death:");
		modeOfDeathColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(modeOfDeathColumn, Pos.CENTER_LEFT, "Mode of Death:");
		modeOfDeathColumn.setCellValueFactory(new PropertyValueFactory<>("modeOfDeath"));
		modeOfDeathColumn.setCellFactory(column -> new TableCell<DeathReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<DeathReport, ?>> deathReportColumns = FXCollections.observableArrayList(deathReportNumberColumn, statusColumn, decedentColumn, causeOfDeathColumn, modeOfDeathColumn);
		
		tableview.getColumns().addAll(deathReportColumns);
		
		for (TableColumn<DeathReport, ?> column : deathReportColumns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(deathReportNumberColumn);
	}
	
	public static void initializeImpoundColumns(TableView tableview) {
		TableColumn<ImpoundReport, String> impoundNumberColumn = new TableColumn<>("Impound #:");
		impoundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundNumber"));
		impoundNumberColumn.setCellFactory(column -> new TableCell<ImpoundReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ImpoundReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<ImpoundReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ImpoundReport, String> ownerNameColumn = new TableColumn<>("Owner:");
		ownerNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(ownerNameColumn, Pos.CENTER_LEFT, "Owner:");
		ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		ownerNameColumn.setCellFactory(column -> new TableCell<ImpoundReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ImpoundReport, String> impoundPlateNumberColumn = new TableColumn<>("Plate #:");
		impoundPlateNumberColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(impoundPlateNumberColumn, Pos.CENTER_LEFT, "Plate #:");
		impoundPlateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundPlateNumber"));
		impoundPlateNumberColumn.setCellFactory(column -> new TableCell<ImpoundReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<ImpoundReport, ?>> impoundColumns = FXCollections.observableArrayList(impoundNumberColumn, statusColumn, impoundPlateNumberColumn, ownerNameColumn);
		
		tableview.getColumns().addAll(impoundColumns);
		
		for (TableColumn<ImpoundReport, ?> column : impoundColumns) {
			column.setEditable(false);
			column.setReorderable(false);
		}
		
		setSmallColumnWidth(impoundNumberColumn);
		setSmallColumnWidth(statusColumn);
	}
	
	public static void initializePatrolColumns(TableView tableview) {
		TableColumn<PatrolReport, String> patrolNumberColumn = new TableColumn<>("Patrol #:");
		patrolNumberColumn.setCellValueFactory(new PropertyValueFactory<>("patrolNumber"));
		patrolNumberColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<PatrolReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<PatrolReport, String> patrolLengthColumn = new TableColumn<>("Length:");
		patrolLengthColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(patrolLengthColumn, Pos.CENTER_LEFT, "Length:");
		patrolLengthColumn.setCellValueFactory(new PropertyValueFactory<>("patrolLength"));
		patrolLengthColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<PatrolReport, String> patrolStartTimeColumn = new TableColumn<>("Start Time:");
		patrolStartTimeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(patrolStartTimeColumn, Pos.CENTER_LEFT, "Start Time:");
		patrolStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStartTime"));
		patrolStartTimeColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<PatrolReport, String> patrolStopTimeColumn = new TableColumn<>("Stop Time:");
		patrolStopTimeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(patrolStopTimeColumn, Pos.CENTER_LEFT, "Stop Time:");
		patrolStopTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStopTime"));
		patrolStopTimeColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<PatrolReport, String> officerNameColumn = new TableColumn<>("Name:");
		officerNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(officerNameColumn, Pos.CENTER_LEFT, "Name:");
		officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));
		officerNameColumn.setCellFactory(column -> new TableCell<PatrolReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<PatrolReport, ?>> patrolColumns = FXCollections.observableArrayList(patrolNumberColumn, statusColumn, patrolLengthColumn, patrolStartTimeColumn, patrolStopTimeColumn, officerNameColumn);
		
		tableview.getColumns().addAll(patrolColumns);
		
		for (TableColumn<PatrolReport, ?> column : patrolColumns) {
			column.setEditable(false);
			column.setReorderable(false);
		}
		
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(patrolNumberColumn);
	}
	
	public static void initializeCitationColumns(TableView tableview) {
		TableColumn<TrafficCitationReport, String> citationNumberColumn = new TableColumn<>("Citation #:");
		citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));
		citationNumberColumn.setCellFactory(column -> new TableCell<TrafficCitationReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficCitationReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<TrafficCitationReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficCitationReport, String> citationDate = new TableColumn<>("Date:");
		citationDate.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(citationDate, Pos.CENTER_LEFT, "Date:");
		citationDate.setCellValueFactory(new PropertyValueFactory<>("citationDate"));
		citationDate.setCellFactory(column -> new TableCell<TrafficCitationReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficCitationReport, String> offenderNameColumn = new TableColumn<>("Suspect:");
		offenderNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(offenderNameColumn, Pos.CENTER_LEFT, "Suspect:");
		offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));
		offenderNameColumn.setCellFactory(column -> new TableCell<TrafficCitationReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficCitationReport, String> offenderVehicleModelColumn = new TableColumn<>("Model:");
		offenderVehicleModelColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(offenderVehicleModelColumn, Pos.CENTER_LEFT, "Model:");
		offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));
		offenderVehicleModelColumn.setCellFactory(column -> new TableCell<TrafficCitationReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<TrafficCitationReport, ?>> citationColumns = FXCollections.observableArrayList(citationNumberColumn, statusColumn, citationDate, offenderNameColumn, offenderVehicleModelColumn);
		
		tableview.getColumns().addAll(citationColumns);
		
		for (TableColumn<TrafficCitationReport, ?> column : citationColumns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(citationNumberColumn);
	}
	
	public static void initializeArrestColumns(TableView tableview) {
		TableColumn<ArrestReport, String> arrestNumberColumn = new TableColumn<>("Arrest #:");
		arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));
		arrestNumberColumn.setCellFactory(column -> new TableCell<ArrestReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ArrestReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<ArrestReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ArrestReport, String> arrestDateColumn = new TableColumn<>("Date:");
		arrestDateColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(arrestDateColumn, Pos.CENTER_LEFT, "Date:");
		arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));
		arrestDateColumn.setCellFactory(column -> new TableCell<ArrestReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<ArrestReport, String> arresteeNameColumn = new TableColumn<>("Suspect:");
		arresteeNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(arresteeNameColumn, Pos.CENTER_LEFT, "Suspect:");
		arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));
		arresteeNameColumn.setCellFactory(column -> new TableCell<ArrestReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<ArrestReport, ?>> arrestColumns = FXCollections.observableArrayList(arrestNumberColumn, statusColumn, arrestDateColumn, arresteeNameColumn);
		
		tableview.getColumns().addAll(arrestColumns);
		
		for (TableColumn<ArrestReport, ?> column : arrestColumns) {
			column.setEditable(false);
			column.setReorderable(false);
		}
		
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(arrestNumberColumn);
	}
	
	public static void initializeIncidentColumns(TableView tableview) {
		TableColumn<IncidentReport, String> incidentNumberColumn = new TableColumn<>("Incident #:");
		incidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));
		incidentNumberColumn.setCellFactory(column -> new TableCell<IncidentReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<IncidentReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<IncidentReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<IncidentReport, String> incidentWitnessesColumn = new TableColumn<>("Suspects:");
		incidentWitnessesColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(incidentWitnessesColumn, Pos.CENTER_LEFT, "Suspects:");
		incidentWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("incidentWitnesses"));
		incidentWitnessesColumn.setCellFactory(column -> new TableCell<IncidentReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<IncidentReport, String> incidentVictimsColumn = new TableColumn<>("Victims/Witnesses:");
		incidentVictimsColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(incidentVictimsColumn, Pos.CENTER_LEFT, "Victims/Witnesses:");
		incidentVictimsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentVictims"));
		incidentVictimsColumn.setCellFactory(column -> new TableCell<IncidentReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<IncidentReport, ?>> incidentColumns = FXCollections.observableArrayList(incidentNumberColumn, statusColumn, incidentWitnessesColumn, incidentVictimsColumn);
		
		tableview.getColumns().addAll(incidentColumns);
		
		for (TableColumn<IncidentReport, ?> column : incidentColumns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(incidentNumberColumn);
	}
	
	public static void initializeSearchColumns(TableView tableview) {
		TableColumn<SearchReport, String> searchNumberColumn = new TableColumn<>("Search #:");
		searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));
		searchNumberColumn.setCellFactory(column -> new TableCell<SearchReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<SearchReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<SearchReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<SearchReport, String> searchedPersonsColumn = new TableColumn<>("Searched:");
		searchedPersonsColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(searchedPersonsColumn, Pos.CENTER_LEFT, "Searched:");
		searchedPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("searchedPersons"));
		searchedPersonsColumn.setCellFactory(column -> new TableCell<SearchReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<SearchReport, String> searchTypeColumn = new TableColumn<>("Type:");
		searchTypeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(searchTypeColumn, Pos.CENTER_LEFT, "Type:");
		searchTypeColumn.setCellValueFactory(new PropertyValueFactory<>("searchType"));
		searchTypeColumn.setCellFactory(column -> new TableCell<SearchReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<SearchReport, String> searchMethodColumn = new TableColumn<>("Method:");
		searchMethodColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(searchMethodColumn, Pos.CENTER_LEFT, "Method:");
		searchMethodColumn.setCellValueFactory(new PropertyValueFactory<>("searchMethod"));
		searchMethodColumn.setCellFactory(column -> new TableCell<SearchReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<SearchReport, ?>> searchColumns = FXCollections.observableArrayList(searchNumberColumn, statusColumn, searchedPersonsColumn, searchTypeColumn, searchMethodColumn);
		
		tableview.getColumns().addAll(searchColumns);
		
		for (TableColumn<SearchReport, ?> column : searchColumns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(searchNumberColumn);
	}
	
	public static void initializeTrafficStopColumns(TableView tableview) {
		TableColumn<TrafficStopReport, String> stopNumberColumn = new TableColumn<>("Stop #:");
		stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("StopNumber"));
		stopNumberColumn.setCellFactory(column -> new TableCell<TrafficStopReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficStopReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<TrafficStopReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficStopReport, String> plateNumberColumn = new TableColumn<>("Plate #:");
		plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
		plateNumberColumn.setCellFactory(column -> new TableCell<TrafficStopReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficStopReport, String> operatorNameColumn = new TableColumn<>("Operator:");
		operatorNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(operatorNameColumn, Pos.CENTER_LEFT, "Operator Name:");
		operatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("operatorName"));
		operatorNameColumn.setCellFactory(column -> new TableCell<TrafficStopReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<TrafficStopReport, String> modelColumn = new TableColumn<>("Model:");
		modelColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(modelColumn, Pos.CENTER_LEFT, "Model:");
		modelColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseModel"));
		modelColumn.setCellFactory(column -> new TableCell<TrafficStopReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<TrafficStopReport, ?>> trafficStopColumns = FXCollections.observableArrayList(stopNumberColumn, statusColumn, plateNumberColumn, operatorNameColumn, modelColumn);
		
		tableview.getColumns().addAll(trafficStopColumns);
		for (TableColumn<TrafficStopReport, ?> column : trafficStopColumns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		
		setSmallColumnWidth(stopNumberColumn);
		setSmallColumnWidth(plateNumberColumn);
		setSmallColumnWidth(statusColumn);
	}
	
	public static void initializeCalloutColumns(TableView tableview) {
		TableColumn<CalloutReport, String> calloutNumberColumn = new TableColumn<>("Callout #:");
		calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));
		calloutNumberColumn.setCellFactory(column -> new TableCell<CalloutReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<CalloutReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<CalloutReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<CalloutReport, String> responseGradeColumn = new TableColumn<>("Grade:");
		responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));
		responseGradeColumn.setCellFactory(column -> new TableCell<CalloutReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<CalloutReport, String> responseTypeColumn = new TableColumn<>("Type");
		responseTypeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(responseTypeColumn, Pos.CENTER_LEFT, "Type:");
		responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseType"));
		responseTypeColumn.setCellFactory(column -> new TableCell<CalloutReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		TableColumn<CalloutReport, String> areaColumn = new TableColumn<>("Area");
		areaColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(areaColumn, Pos.CENTER_LEFT, "Area:");
		areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));
		areaColumn.setCellFactory(column -> new TableCell<CalloutReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize);
					setGraphic(label);
				}
			}
		});
		
		ObservableList<TableColumn<CalloutReport, ?>> columns = FXCollections.observableArrayList(calloutNumberColumn, statusColumn, responseGradeColumn, responseTypeColumn, areaColumn);
		for (TableColumn<CalloutReport, ?> column : columns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		tableview.getColumns().addAll(columns);
		setSmallColumnWidth(calloutNumberColumn);
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(responseGradeColumn);
	}
	
	public static void initializeAccidentColumns(TableView tableview) {
		TableColumn<AccidentReport, String> accidentNumberColumn = new TableColumn<>("Accident #:");
		accidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("AccidentNumber"));
		
		TableColumn<AccidentReport, String> statusColumn = new TableColumn<>("Status:");
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		statusColumn.setCellFactory(column -> new TableCell<AccidentReport, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";";
					
					if (item.trim().equalsIgnoreCase("In Progress")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Reopened")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Pending")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
					} else if (item.trim().equalsIgnoreCase("Cancelled")) {
						label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
					} else {
						label.setStyle(baseStyle);
					}
					
					setGraphic(label);
				}
			}
		});
		
		TableColumn<AccidentReport, String> plateNumberColumn = new TableColumn<>("Plate #:");
		plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));
		
		TableColumn<AccidentReport, String> ownerNameColumn = new TableColumn<>("Owner:");
		ownerNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(ownerNameColumn, Pos.CENTER_LEFT, "Owner:");
		ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("OwnerName"));
		
		TableColumn<AccidentReport, String> streetColumn = new TableColumn<>("Street");
		streetColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(streetColumn, Pos.CENTER_LEFT, "Street:");
		streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));
		
		TableColumn<AccidentReport, String> roadConditionsColumn = new TableColumn<>("Road Conditions");
		roadConditionsColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(roadConditionsColumn, Pos.CENTER_LEFT, "Conditions:");
		roadConditionsColumn.setCellValueFactory(new PropertyValueFactory<>("RoadConditions"));
		
		ObservableList<TableColumn<AccidentReport, ?>> columns = FXCollections.observableArrayList(accidentNumberColumn, statusColumn, plateNumberColumn, ownerNameColumn, streetColumn, roadConditionsColumn);
		tableview.getColumns().addAll(columns);
		for (TableColumn<AccidentReport, ?> column : columns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		setSmallColumnWidth(accidentNumberColumn);
		setSmallColumnWidth(statusColumn);
		setSmallColumnWidth(plateNumberColumn);
	}
	
}
