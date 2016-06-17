package pl.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

/**
 * Book controller
 * 
 * @author mmotowid
 *
 */
@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	// @RequestMapping()
	// public String list(Model model) {
	// return ViewNames.BOOKS;
	// }

	/**
	 * Method collects info about all books
	 */
	@RequestMapping()
	public ModelAndView allBooks() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("bookList", bookService.findAllBooks());
		modelAndView.setViewName(ViewNames.BOOKS);
		return modelAndView;
	}

	@RequestMapping(value = "/book")
	public ModelAndView bookDetails(@RequestParam("id") Long id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("book", bookService.findBookById(id).get(0));
		modelAndView.setViewName(ViewNames.BOOK);
		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBookDetails(Model model) {
		model.addAttribute("newBook", new BookTo());
		return ViewNames.ADD_BOOK;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addBookDeatails(@ModelAttribute("newBook") BookTo newBook) {
		ModelAndView modelAndView = new ModelAndView();
		if (newBook.getTitle() != null && newBook.getAuthors() != null) {
			modelAndView.addObject("newBook", bookService.saveBook(newBook));
		}
		modelAndView.setViewName(ViewNames.WELCOME);
		return modelAndView;
	}

	@RequestMapping(value = "/remove")
	public String removeBook(@RequestParam("id") Long id) {
		bookService.deleteBook(id);
		return ViewNames.REMOVE_BOOK;
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String findBook(Model model) {
		model.addAttribute("searchedBook", new BookTo());
		return ViewNames.FIND_BOOK;
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ModelAndView findBook(@ModelAttribute("searchedBook") BookTo searchedBook) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("bookList",
				bookService.findBooksByTitleAndAuthor(searchedBook.getTitle(), searchedBook.getAuthors()));
		modelAndView.setViewName(ViewNames.FOUND_BOOKS);
		return modelAndView;

	}

	/**
	 * Binder initialization
	 */
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}
