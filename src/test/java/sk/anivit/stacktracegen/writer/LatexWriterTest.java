package sk.anivit.stacktracegen.writer;

import static org.junit.Assert.*;

import org.junit.Test;


public class LatexWriterTest {

	@Test
	public void testFormatTime() throws Exception {
		LatexWriter w=new LatexWriter();
		assertEquals("500ms",w.formatTime(500));
		assertEquals("1.65s",w.formatTime(1650));
		assertEquals("1.03h",w.formatTime(3700000));
		
	}

}
