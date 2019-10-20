package fr.d2factory.libraryapp.library;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

public class LibraryTest {
    private ILibrary library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
    	
    	this.bookRepository = new BookRepository();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<Book> books = new ArrayList<>();
		
		try {
			books = Arrays.asList(mapper.readValue(new File("C:\\Users\\Bruno\\eclipse-workspace\\devoteam\\recruitment-web\\src\\test\\resources\\books.json"), Book[].class));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.bookRepository.addBooks(books);
		
		this.library = new Library(bookRepository);
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
    	Member member = new Student(false, 3F);
        Book book = library.borrowBook(46578964513L, member, LocalDate.now());
        assertEquals(book.isbn.getIsbnCode(), 46578964513L);
    }

    @Test
    public void borrowed_book_is_no_longer_available(){
    	Member member1 = new Student(false, 3F);
        library.borrowBook(46578964513L, member1, LocalDate.now());
    	Member member2 = new Student(false, 3F);

        assertEquals(library.borrowBook(46578964513L, member2, LocalDate.now()), null);
    }

	@Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
    	Member member = new Resident(3F);
    	Book book = library.borrowBook(46578964513L, member, LocalDate.now().minusDays(8));
    	library.returnBook(book, member);
    	assertEquals(member.getWallet(), 2.2F, 0.01f);

    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
    	Member member = new Student(false, 3F);
    	Book book = library.borrowBook(46578964513L, member, LocalDate.now().minusDays(8));
    	library.returnBook(book, member);
    	assertEquals(member.getWallet(), 2.2F, 0.01f);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
    	Member member = new Student(true, 3F);
    	Book book = library.borrowBook(46578964513L, member, LocalDate.now().minusDays(8));
    	library.returnBook(book, member);
    	assertEquals(member.getWallet(), 3F, 0.01f);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
    	Member member = new Student(false, 10F);
    	Book book = library.borrowBook(46578964513L, member, LocalDate.now().minusDays(31));
    	library.returnBook(book, member);
    	assertEquals(member.getWallet(), 6.85F, 0.01f);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
    	Member member = new Resident(10F);
    	Book book = library.borrowBook(46578964513L, member, LocalDate.now().minusDays(61));
    	library.returnBook(book, member);
    	assertEquals(member.getWallet(), 3.80F, 0.01f);
    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books(){
    	Member member = new Resident(10F);
    	library.borrowBook(46578964513L, member, LocalDate.now().minusDays(61));
    	library.borrowBook(968787565445L, member, LocalDate.now());
    	
    }
}
