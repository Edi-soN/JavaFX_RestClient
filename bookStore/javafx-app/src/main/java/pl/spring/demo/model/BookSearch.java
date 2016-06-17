package pl.spring.demo.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import pl.spring.demo.to.BookTo;

public class BookSearch {

	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty author = new SimpleStringProperty();
	private final ObjectProperty<Status> status = new SimpleObjectProperty<>();
	private final ListProperty<BookTo> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final String getTitle() {
		return title.get();
	}

	public final void setTitle(String value) {
		title.set(value);
	}

	public StringProperty titleProperty() {
		return title;
	}

	public final String getAuthor() {
		return author.get();
	}

	public final void setAuthor(String value) {
		author.set(value);
	}

	public StringProperty authorProperty() {
		return author;
	}

	public final Status getStatus() {
		return status.get();
	}

	public final void setStatus(Status value) {
		status.set(value);
	}

	public ObjectProperty<Status> statusProperty() {
		return status;
	}

	public final List<BookTo> getResult() {
		return result.get();
	}

	public final void setResult(List<BookTo> value) {
		result.setAll(value);
	}

	public ListProperty<BookTo> resultProperty() {
		return result;
	}
}
