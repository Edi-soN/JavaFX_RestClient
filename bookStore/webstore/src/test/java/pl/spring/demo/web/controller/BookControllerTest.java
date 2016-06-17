package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
public class BookControllerTest {
	private MockMvc mockMvc;
	@Autowired
	private BookService bookService;

	@Before
	public void setup() {
		Mockito.reset(bookService);
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).setViewResolvers(viewResolver).build();
	}

	@Test
	public void testAddBookPage() throws Exception {
		// given
		ResultActions resultActions = mockMvc.perform(post("/books/add"));
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		// when
		Mockito.when(bookService.saveBook(Mockito.any(BookTo.class))).thenReturn(testBook);
		// then
		resultActions.andExpect(view().name("addBook"))
		.andExpect(model().attribute("newBook" ,new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				BookTo book = (BookTo) argument;
				return null != book && testBook.getTitle().equals(book.getTitle());
			}
		}));

	}
}
