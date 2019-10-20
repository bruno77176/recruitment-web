package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();    
    

    public Map<ISBN, Book> getAvailableBooks() {
		return availableBooks;
	}

	public void setAvailableBooks(Map<ISBN, Book> availableBooks) {
		this.availableBooks = availableBooks;
	}

	public Map<Book, LocalDate> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(Map<Book, LocalDate> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	public void addBooks(List<Book> books){
    	
        for(Book book : books) {
        	availableBooks.put(book.isbn, book);
        }
    }

    public Book findBook(long isbnCode) {
        return availableBooks.get(new ISBN(isbnCode));
    }
    

    public void saveBookBorrow(Book book, LocalDate borrowedAt){
    	
    	availableBooks.remove(book.isbn);
        borrowedBooks.put(book, borrowedAt);
    }
    
    public void returnBookBorrowed(Book book) {
    	this.getBorrowedBooks().remove(book);
		this.getAvailableBooks().put(book.isbn, book);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }

	
	public void seeBooks() {
		
		
		String Newligne=System.getProperty("line.separator");
		
		System.out.println("-----------------------------------");
		
		System.out.println("availableBooks : "+ Newligne);
		
		for(Book book : availableBooks.values()) {
			System.out.println(book);
		}
		
		System.out.println(Newligne+ "borrowedBooks : "+ Newligne);
		
		for (Book book: borrowedBooks.keySet()) {
			System.out.println(book);
		}
		
		System.out.println("-----------------------------------");
	}
    
    
}
