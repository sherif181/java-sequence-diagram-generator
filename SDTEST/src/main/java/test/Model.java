package test;

public class Model {

	private Bean bean;

	public void setBean(Bean b) {
		this.bean = b;
		
		
	}

	public boolean persist() {
		Persistor.persist(bean);
		return true;
		
	}

	public Bean getBean() {
		return bean;
	}
}
