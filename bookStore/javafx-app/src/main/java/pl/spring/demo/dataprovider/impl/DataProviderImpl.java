package pl.spring.demo.dataprovider.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import pl.spring.demo.dataprovider.DataProvider;
import pl.spring.demo.to.BookTo;

public class DataProviderImpl implements DataProvider {

	@Override
	public List<BookTo> findAllBooks() {
		init();
		HttpResponse<BookTo[]> bookResponse = null;
		try {
			// REV: adres serwera powinien byc pobrany z konfiguracji
			bookResponse = Unirest.get("http://localhost:8080/webstore/rest/books").asObject(BookTo[].class);
		} catch (UnirestException e) {
			// REV: nie mozesz w tym miejscu wyswietlic okna, bo jestes ponizej warstwy UI, poza tym ta metoda wykonuje sie w watku w tle
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Status");
			alert.setContentText("Connection error. Try again later");
			alert.show();
			// REV: zawsze uzywaj loggera
			e.printStackTrace();
		}

		return Arrays.asList(bookResponse.getBody());
	}

	@Override
	public List<BookTo> findBookByTitleAndAuthor(String title, String author) {
		init();
		HttpResponse<BookTo[]> bookResponse = null;
		try {
			// REV: j.w.
			bookResponse = Unirest.get("http://localhost:8080/webstore/rest/book/find").queryString("title", title)
					.queryString("authors", author).asObject(BookTo[].class);
		} catch (UnirestException e) {
			// REV: j.w.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Status");
			alert.setContentText("Connection error. Try again later");
			alert.show();
			// REV: j.w.
			e.printStackTrace();
		}
		return Arrays.asList(bookResponse.getBody());
	}

	@Override
	public void addNewBook(BookTo book) {
		try {
			// REV: j.w.
			Unirest.post("http://localhost:8080/webstore/rest/book").header("accept", "application/json")
					.header("accept", "application/json").header("Content-Type", "application/json").body(book)
					.asJson();
		} catch (UnirestException e) {
			// REV: j.w.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Status");
			alert.setContentText("Connection error. Try again later");
			alert.show();
			// REV: j.w.
			e.printStackTrace();
		}
	}

	@Override
	public void deleteBook(BookTo book) {
		init();
		try {
			// REV: j.w.
			Unirest.delete("http://localhost:8080/webstore/rest/book").queryString("id", book.getId()).asJson();
		} catch (UnirestException e) {
			// REV: j.w.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Status");
			alert.setContentText("Connection error. Try again later");
			alert.show();
			// REV: j.w.
			e.printStackTrace();
		}
	}

	@Override
	public void editBook(BookTo book) {
		init();
		try {
			// REV: j.w.
			Unirest.put("http://localhost:8080/webstore/rest/book").header("accept", "application/json")
					.header("accept", "application/json").header("Content-Type", "application/json").body(book)
					.asJson();
		} catch (UnirestException e) {
			// REV: j.w.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Status");
			alert.setContentText("Connection error. Try again later");
			alert.show();
			// REV: j.w.
			e.printStackTrace();
		}

	}

	private void init() {
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					// REV: polecam zawsze dodawac message do wyjatku
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					// REV: polecam zawsze dodawac message do wyjatku
					throw new RuntimeException(e);
				}
			}
		});
	}

}
