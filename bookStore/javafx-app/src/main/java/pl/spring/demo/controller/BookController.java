package pl.spring.demo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pl.spring.demo.dataprovider.DataProvider;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.model.BookSearch;
import pl.spring.demo.model.Status;
import pl.spring.demo.to.BookTo;

public class BookController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane editPane;

	@FXML
	private Label searchStatus;

	@FXML
	private Label addBookInfo;

	@FXML
	private TextField titleSearchField;

	@FXML
	private TextField titleAddField;

	@FXML
	private TextField titleEditField;

	@FXML
	private TextField authorSearchField;

	@FXML
	private TextField authorAddField;

	@FXML
	private TextField authorEditField;

	@FXML
	private ComboBox<Status> statusAddField;

	@FXML
	private ComboBox<Status> statusEditField;

	@FXML
	private Button searchButton;

	@FXML
	private Button addButton;

	@FXML
	private Button saveButton;

	@FXML
	private TableView<BookTo> resultAllTable;

	@FXML
	private TableView<BookTo> resultSearchTable;

	@FXML
	private TableColumn<BookTo, String> titleAllColumn;

	@FXML
	private TableColumn<BookTo, String> titleSearchColumn;

	@FXML
	private TableColumn<BookTo, String> authorAllColumn;

	@FXML
	private TableColumn<BookTo, String> authorSearchColumn;

	@FXML
	private TableColumn<BookTo, String> statusAllColumn;

	@FXML
	private TableColumn<BookTo, String> statusSearchColumn;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final BookSearch findModel = new BookSearch();

	private final BookSearch allModel = new BookSearch();

	private BookTo tempBook;

	@FXML
	private void initialize() {

		initTable();
		initStatusList();

		/*
		 * Bind controls properties to model properties.
		 */
		resultSearchTable.itemsProperty().bind(findModel.resultProperty());
		resultAllTable.itemsProperty().bind(allModel.resultProperty());

		titleSearchField.textProperty().bindBidirectional(findModel.titleProperty());
		authorSearchField.textProperty().bindBidirectional(findModel.authorProperty());

		titleAddField.textProperty().bindBidirectional(allModel.titleProperty());
		authorAddField.textProperty().bindBidirectional(allModel.authorProperty());
		statusAddField.valueProperty().bindBidirectional(allModel.statusProperty());
		statusAddField.getSelectionModel().selectFirst();

		showAllBooks();

		// addButton.disableProperty().bind(authorAddField.textProperty().isEmpty());
		// addButton.disableProperty().bind(titleAddField.textProperty().isEmpty());
		BooleanBinding booleanBind = authorAddField.textProperty().isEmpty().or(titleAddField.textProperty().isEmpty());
		addButton.disableProperty().bind(booleanBind);

		searchButton.setGraphic(new ImageView(new Image("http://gmapsapi.com/examples/018/lupa.png")));

		editPane.setVisible(false);

	}

	private void initTable() {

		/*
		 * Define what properties of PersonVO will be displayed in different
		 * columns.
		 */
		titleAllColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorAllColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));
		statusAllColumn
				.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().toString()));

		ContextMenu contextMenu = new ContextMenu();
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dataProvider.deleteBook(resultAllTable.getSelectionModel().getSelectedItem());
				showAllBooks();
				System.out.println(resultAllTable.getSelectionModel().getSelectedItem().getTitle());
				editPane.setVisible(false);
			}
		});
		edit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				editPane.setVisible(true);
				titleEditField.setText(resultAllTable.getSelectionModel().getSelectedItem().getTitle());
				authorEditField.setText(resultAllTable.getSelectionModel().getSelectedItem().getAuthors());
				statusEditField.getSelectionModel()
						.select(bookStatus2Status(resultAllTable.getSelectionModel().getSelectedItem().getStatus()));
				tempBook = resultAllTable.getSelectionModel().getSelectedItem();
				System.out.println(resultAllTable.getSelectionModel().getSelectedItem().getTitle());
			}
		});
		contextMenu.getItems().addAll(edit, delete);
		resultAllTable.setContextMenu(contextMenu);

	}

	private void initStatusList() {
		statusAddField.getItems().add(Status.FREE);
		statusAddField.getItems().add(Status.LOAN);
		statusAddField.getItems().add(Status.MISSING);

		statusEditField.getItems().add(Status.FREE);
		statusEditField.getItems().add(Status.LOAN);
		statusEditField.getItems().add(Status.MISSING);
	}

	private void showAllBooks() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<BookTo>> backgroundTask = new Task<Collection<BookTo>>() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			protected Collection<BookTo> call() throws Exception {

				/*
				 * Get the data.
				 */
				Collection<BookTo> result = dataProvider.findAllBooks();

				/*
				 * Value returned from this method is stored as a result of task
				 * execution.
				 */
				return result;
			}

			/**
			 * This method will be executed in the JavaFX Application Thread
			 * when the task finishes.
			 */
			@Override
			protected void succeeded() {

				/*
				 * Get result of the task execution.
				 */
				Collection<BookTo> result = getValue();

				/*
				 * Copy the result to model.
				 */
				allModel.setResult(new ArrayList<BookTo>(result));

				/*
				 * Reset sorting in the result table.
				 */
				resultAllTable.getSortOrder().clear();
			}
		};

		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks. You should never create a thread on your
		 * own.
		 */
		new Thread(backgroundTask).start();
	}

	@FXML
	private void searchButtonAction(ActionEvent event) {
		findBookButtonAction();
	}

	private void findBookButtonAction() {

		titleSearchColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorSearchColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));
		statusSearchColumn
				.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().toString()));

		Task<Collection<BookTo>> backgroundTask = new Task<Collection<BookTo>>() {
			@Override
			protected Collection<BookTo> call() throws Exception {
				Collection<BookTo> result = dataProvider.findBookByTitleAndAuthor(titleSearchField.getText(),
						authorSearchField.getText());
				return result;
			}

			@Override
			protected void succeeded() {
				Collection<BookTo> result = getValue();
				findModel.setResult(new ArrayList<BookTo>(result));
				resultSearchTable.getSortOrder().clear();
			}

			@Override
			protected void failed() {
				findModel.setResult(new ArrayList<BookTo>());
				resultSearchTable.getSortOrder().clear();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Status");
				alert.setContentText("No books found");
				alert.show();
			}
		};
		new Thread(backgroundTask).start();
	}

	@FXML
	private void addButtonAction(ActionEvent event) {

		if (!titleAddField.getText().isEmpty() && !authorAddField.getText().isEmpty()
				&& statusAddField.getValue() != null) {
			addBook();
		}

	}

	private void addBook() {
		Task<Collection<BookTo>> backgroundTask = new Task<Collection<BookTo>>() {
			@Override
			protected Collection<BookTo> call() throws Exception {
				BookTo book = new BookTo(null, titleAddField.getText(), authorAddField.getText(),
						status2BookStatus(statusAddField.getValue()));
				dataProvider.addNewBook(book);
				Collection<BookTo> result = dataProvider.findAllBooks();
				return result;
			}

			@Override
			protected void succeeded() {
				Collection<BookTo> result = getValue();
				allModel.setResult(new ArrayList<BookTo>(result));
				resultSearchTable.getSortOrder().clear();
				titleAddField.setText("");
				authorAddField.setText("");
				statusAddField.getSelectionModel().selectFirst();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Status");
				alert.setContentText("Book has been added properly.");
				alert.show();
			}

			@Override
			protected void failed() {
				addBookInfo.setText("Info : failed to add book");
			}
		};
		new Thread(backgroundTask).start();
	}

	@FXML
	private void saveButtonAction(ActionEvent event) {
		tempBook.setTitle(titleEditField.getText());
		tempBook.setAuthors(authorEditField.getText());
		tempBook.setStatus(status2BookStatus(statusEditField.getSelectionModel().getSelectedItem()));
		dataProvider.editBook(tempBook);
		showAllBooks();
		editPane.setVisible(false);
	}

	private BookStatus status2BookStatus(Status status) {
		BookStatus bookStatus = null;

		if (BookStatus.FREE.toString().equals(status.toString())) {
			bookStatus = BookStatus.FREE;
		}
		if (BookStatus.LOAN.toString().equals(status.toString())) {
			bookStatus = BookStatus.LOAN;
		}
		if (BookStatus.MISSING.toString().equals(status.toString())) {
			bookStatus = BookStatus.MISSING;
		}

		return bookStatus;
	}

	private Status bookStatus2Status(BookStatus bookStatus) {
		Status status = null;

		if (Status.FREE.toString().equals(bookStatus.toString())) {
			status = Status.FREE;
		}
		if (Status.LOAN.toString().equals(bookStatus.toString())) {
			status = Status.LOAN;
		}
		if (Status.MISSING.toString().equals(bookStatus.toString())) {
			status = Status.MISSING;
		}

		return status;
	}

}
