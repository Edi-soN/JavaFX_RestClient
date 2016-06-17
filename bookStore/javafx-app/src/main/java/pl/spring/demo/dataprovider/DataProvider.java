package pl.spring.demo.dataprovider;

import java.util.List;

import pl.spring.demo.dataprovider.impl.DataProviderImpl;
import pl.spring.demo.to.BookTo;

public interface DataProvider {
	
	DataProvider INSTANCE = new DataProviderImpl();
	
	public List<BookTo> findAllBooks();
	
	public List<BookTo> findBookByTitleAndAuthor(String title, String author);
	
	public void addNewBook(BookTo book);
	
	public void deleteBook(BookTo book);
	
	public void editBook(BookTo book);
	
}
