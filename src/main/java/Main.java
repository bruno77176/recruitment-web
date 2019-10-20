import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

public class Main {

	public static void main(String[] args) {
		
		Library library = generateLibrary();
				
		Member firstYearStudent = new Student(true, 3F);		
		Member student = new Student(false, 3F);		
		Member resident = new Resident(3F);
		
		library.seeBooks();
		
		library.borrowBook(46578964513L, firstYearStudent, LocalDate.now());
		library.borrowBook(0000L, student, LocalDate.now());
		library.borrowBook(3326456467846L, resident, LocalDate.of(2019, 9, 10));
				
		library.seeBooks();
		
		library.returnBook(library.seeBorrowedBook(resident), resident);

		
		System.out.println("firstYearStudent.getWallet() : " + firstYearStudent.getWallet());
		System.out.println("student.getWallet() : " + student.getWallet());
		System.out.println("resident.getWallet() : " + resident.getWallet());
		
		library.seeBooks(); 
		
		library.borrowBook(465789453149L, resident, LocalDate.now());
		
		
		
		
	}

	private static Library generateLibrary() {
		
		BookRepository bookRepository = new BookRepository();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<Book> books = new ArrayList<>();
		
		try {
			books = Arrays.asList(mapper.readValue(new File("C:\\Users\\Bruno\\eclipse-workspace\\devoteam\\recruitment-web\\src\\test\\resources\\books.json"), Book[].class));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		bookRepository.addBooks(books);
		
		return new Library(bookRepository);
	}

}
