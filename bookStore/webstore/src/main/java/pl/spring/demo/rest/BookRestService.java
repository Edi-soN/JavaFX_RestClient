package pl.spring.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
@ResponseBody
@RequestMapping("/rest")
public class BookRestService {

	// TODO: Inject properly book service
	@Autowired
	BookService bookService;

	// TODO: implement all necessary CRUD operations as a rest service
	@RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<Void> createBook(@RequestBody BookTo book) {
		if (book.getId() != null && !bookService.findBookById(book.getId()).isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		bookService.saveBook(book);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/book", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateBook(@RequestBody BookTo book) {
		if (book.getId() == null || bookService.findBookById(book.getId()).isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		bookService.saveBook(book);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/book", method = RequestMethod.GET)
	public ResponseEntity<BookTo> retrieveBook(@RequestParam("id") Long id) {
		if (id == null || bookService.findBookById(id).isEmpty()) {
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		BookTo book = bookService.findBookById(id).get(0);
		return new ResponseEntity<BookTo>(book, HttpStatus.OK);
	}

	@RequestMapping(value = "/book", method = RequestMethod.DELETE)
	public ResponseEntity<BookTo> deleteBook(@RequestParam("id") Long id) {
		if (id == null || bookService.findBookById(id).isEmpty()) {
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		bookService.deleteBook(id);
		return new ResponseEntity<BookTo>(HttpStatus.OK);
	}

	// TODO: implement some search methods considering single request parameters
	// / multiple request parameters / array request parameters
	@RequestMapping(value = "/book/find", method = RequestMethod.GET)
	public ResponseEntity<List<BookTo>> retrieveBooks(@RequestParam("title") String title,
			@RequestParam("authors") String authors) {
		if (bookService.findBooksByTitleAndAuthor(title, authors).isEmpty()) {
			return new ResponseEntity<List<BookTo>>(HttpStatus.NOT_FOUND);
		}
		List<BookTo> book = bookService.findBooksByTitleAndAuthor(title, authors);
		return new ResponseEntity<List<BookTo>>(book, HttpStatus.OK);
	}

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookTo>> allBooks() {
		return new ResponseEntity<List<BookTo>>(bookService.findAllBooks(), HttpStatus.OK);
	}
}
