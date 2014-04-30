package test;

public class Controller {
	
	private Model model;
	public Controller(Model model) {
		this.model = model;
		
	}
	public void init() {
		Bean b=Bean.newInstance();
		
		model.setBean(b);
		model.persist();
	}
	public void simpleBeanOperation() {
		Bean b = model.getBean();
		b.simpleBeanOperation();
		model.setBean(b);

		model.persist();
	}
	
	public void longBeanOperation() {
		Bean b = model.getBean();
		b.longBeanOperation();
		model.setBean(b);

		model.persist();
	}
	
	public void recursiveBeanOperation(){
		Bean b = model.getBean();
		b.recursiveBeanOperation();
		model.setBean(b);

		model.persist();
	}
	public void forBeanOperation(){
		Bean b = model.getBean();
		b.forBeanOperation();
		model.setBean(b);
		
	}
	
	
	

}
