java-sequence-diagram-generator
===============================
Simple latex sequence diagram generator. This project uses annotated junit tests to generate latex sequence diagrams.

SDTEST/doc/test.pdf contains  some rendered examples
First compile the root project, then you can run gradle diagram or gradle latex  in the SDTEST folder

Example output :
![alt tag](https://github.com/sherif181/java-sequence-diagram-generator/raw/master/example.png)

Hello World :
	
	@Test
	@SequenceDiagram({ Controller.class, Model.class })
	public void testMethod() {
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}


SDTEST contains also  other examples



