package fr.d2factory.libraryapp.library;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Student;

public class Library implements ILibrary {
	
	public static final float NORMAL_FEE = 0.1F;
	public static final float FIRST_YEAR_STUDENT_FEE = 0;
	public static final float STUDENT_DELAY_FEE = 0.15F;
	public static final float RESIDENT_DELAY_FEE = 0.2F;
	public static final int STUDENT_MAX_PERIOD = 30;
	public static final int RESIDENT_MAX_PERIOD = 60;
	public static final int FIRST_YEAR_STUDENT_FREE_PERIOD = 15;
	
	private BookRepository bookRepository;
	
	private Map<Member, Book> borrowers = new HashMap<>();
	
	public Library(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}


	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		
		int maxDuration;
		if (member.getClass().equals(Student.class)) {
			maxDuration = Library.STUDENT_MAX_PERIOD;
			
		} else {
			maxDuration = Library.RESIDENT_MAX_PERIOD;
		}
		
		if (member.getWallet() < Library.NORMAL_FEE) {
			System.out.println("the member cannot borrow, empty wallet.");
			return null;
		}
		Book book = this.bookRepository.findBook(isbnCode);
		if (book == null) {
			System.out.println("the book is not available.");
			return null;
		}
		
		
		
		if (borrowers.containsKey(member)) {
			Book bookBorrowed = borrowers.get(member);
			LocalDate borrowedAtDay = this.bookRepository.findBorrowedBookDate(bookBorrowed);
			if ((int)Duration.between(borrowedAtDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() > maxDuration) {
				System.out.println("the member is late and cannot borrow until the book is returned");
				throw new HasLateBooksException();
			}
		}
		
		borrowers.put(member, book);		
		this.bookRepository.saveBookBorrow(book, borrowedAt);
		
		return book;
	}

	@Override
	public void returnBook(Book book, Member member) {
		
		LocalDate date = this.bookRepository.findBorrowedBookDate(book);
		
		int numberOfDays = (int)Duration.between(date.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
		member.payBook(numberOfDays);
		
		this.bookRepository.returnBookBorrowed(book);
		

	}
	
	public void seeBooks() {
		this.bookRepository.seeBooks();
	}
	
	public Book seeBorrowedBook(Member member) {
		return this.borrowers.get(member);
	}

}
