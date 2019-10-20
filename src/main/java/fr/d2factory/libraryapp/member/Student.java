package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;

public class Student extends Member {
	
		
	private boolean isFirstYear;
	
	
	public Student(boolean isFirstYear, float wallet) {
		super(wallet);
		this.isFirstYear = isFirstYear;
	}
	
	

	public boolean isFirstYear() {
		return isFirstYear;
	}



	public void setFirstYear(boolean isFirstYear) {
		this.isFirstYear = isFirstYear;
	}



	@Override
	public void payBook(int numberOfDays) {
		
		float dueFee = 0F;
		int firstYearFreeDays= 0;
		
		if(this.isFirstYear) {
			firstYearFreeDays = Library.FIRST_YEAR_STUDENT_FREE_PERIOD;
		}
		
		if (numberOfDays <= firstYearFreeDays) {
			dueFee = 0F;
		} else if (numberOfDays <= Library.STUDENT_MAX_PERIOD && numberOfDays > firstYearFreeDays) {
			dueFee = (numberOfDays - firstYearFreeDays) * Library.NORMAL_FEE;
		} else {
			dueFee = (Library.STUDENT_MAX_PERIOD - firstYearFreeDays) * Library.NORMAL_FEE + (numberOfDays - Library.STUDENT_MAX_PERIOD) * Library.STUDENT_DELAY_FEE;
		}
		
		this.setWallet(this.getWallet() - dueFee);
		
	}

}
