package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.controller.BookController;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "controller-test-configuration.xml")
@WebAppConfiguration
public class ValidBookControllerTest {

	@Autowired
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		Mockito.reset(bookService);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		BookController bookController = new BookController();
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).setViewResolvers(viewResolver).build();
		// Due to fact, that We are trying to construct real Bean - Book
		// Controller, we have to use reflection to mock existing field book
		// service
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
	}

	@Test
	public void testAddBookPage() throws Exception {
		// given
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(testBook);

		// TODO: please take a look how we pass @ModelAttribute as a request
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", testBook));
		// then
		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute("newBook", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						BookTo book = (BookTo) argument;
						return null != book && testBook.getTitle().equals(book.getTitle())
								&& testBook.getAuthors().equals(book.getAuthors());
					}
				}));
	}

	@Test
	public void testFindBook() throws Exception {
		// given
		List<BookTo> searchedBook = new ArrayList<>();
		searchedBook.add(new BookTo(1L, "First book", "Jan Kowalski", BookStatus.FREE));
		searchedBook.add(new BookTo(2L, "Second book", "Jan Nowak", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitleAndAuthor(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(searchedBook);
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/find").flashAttr("bookList", searchedBook));
		// then
		resultActions.andExpect(view().name("foundBooks"))
				.andExpect(model().attribute("bookList", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						List<BookTo> bookList = (List<BookTo>) argument;
						return null != bookList && searchedBook.get(0).getTitle().equals(bookList.get(0).getTitle())
								&& searchedBook.get(0).getAuthors().equals(bookList.get(0).getAuthors())
								&& searchedBook.get(1).getTitle().equals(bookList.get(1).getTitle())
								&& searchedBook.get(1).getAuthors().equals(bookList.get(1).getAuthors());
					}
				}));
	}

	/**
	 * Sample method which convert's any object from Java to String
	 */
	// private static String asJsonString(final Object obj) {
	// try {
	// final ObjectMapper mapper = new ObjectMapper();
	// final String jsonContent = mapper.writeValueAsString(obj);
	// return jsonContent;
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }
}
