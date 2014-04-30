package test;

import java.util.Random;

public class Bean {
	private int val=new Random().nextInt();

	public static Bean newInstance() {
		
		return new Bean();
	}


	public void simpleBeanOperation() {
		val++;
		
	}


	public void longBeanOperation() {
		simpleBeanOperation();
		try {
			Thread.sleep(1020);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void recursiveBeanOperation() {
		val=0;
		recurse(5);
		
	}


	private void recurse(int i) {
		val++;
		if(i>0){
			recurse(i-1);
		}
		
	}


	public void forBeanOperation() {
		for (int i = 0; i < 10; i++) {
			simpleBeanOperation();
		}
		
	}

}
