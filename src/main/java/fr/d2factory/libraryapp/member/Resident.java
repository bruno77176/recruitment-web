package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;

public class Resident extends Member {
	
	

	
	public Resident(float wallet) {
		super(wallet);
	}

	@Override
	public void payBook(int numberOfDays) {
		
		float dueFee = 0F;
		
		if (numberOfDays <= Library.RESIDENT_MAX_PERIOD) {
			
			dueFee = numberOfDays * Library.NORMAL_FEE;
			
		} else {
			
			dueFee = Library.RESIDENT_MAX_PERIOD * Library.NORMAL_FEE + (numberOfDays -Library.RESIDENT_MAX_PERIOD)* Library.RESIDENT_DELAY_FEE;

		}
		this.setWallet(this.getWallet() - dueFee); 
		
	}

}
