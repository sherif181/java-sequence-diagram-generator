package sk.anivit.stacktracegen.writer;

import java.io.IOException;

import sk.anivit.stacktracegen.recording.DiagramBean;

public interface WriterI {
public void write(DiagramBean bean) throws IOException;
}
