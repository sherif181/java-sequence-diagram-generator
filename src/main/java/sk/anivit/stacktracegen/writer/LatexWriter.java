package sk.anivit.stacktracegen.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javassist.CtClass;
import javassist.NotFoundException;
import sk.anivit.stacktracegen.recording.CalledMethod;
import sk.anivit.stacktracegen.recording.DiagramBean;

public class LatexWriter implements WriterI {
	DecimalFormat df = new DecimalFormat("#.##");
	private File outputDir;
	private File outputFile;
	private BufferedOutputStream writeBuffer;
	

	@Override
	public void write(DiagramBean bean) throws IOException {
		try {
			initOutput(bean);
			write(" \\begin{figure}");
			write(" \\begin{center}");

			ArrayList<ArrayList<CalledMethod>> mListList = new ArrayList<ArrayList<CalledMethod>>();
			ArrayList<CalledMethod> mList = null;

			write(" \\begin{sequencediagram}");

			
			write("\\newthread{" + bean.getEntryPoint().getDeclaringClass().getName().replace("$", "").replace(".", "") + "}{"
					+ bean.getEntryPoint().getDeclaringClass().getSimpleName().replace("$", "-") + "}");

			for (CtClass c : bean.getDiagramClasses()) {
				if(bean.getRecordedClasses().contains(c)){
					write("\\newinst{" + c.getName().replace("$", "").replace(".", "") + "}{"
							+ c.getSimpleName().replace("$", "-") + "}");
				}

			}
			
		
			CalledMethod r = bean.getRoot();
			
			writeMethods(r);
			write(" \\end{sequencediagram}");
			
			if(bean.getCaption()!=null){
				write("\\caption{"+bean.getCaption()+"}");
			}
			if(bean.getLabel()!=null){
				write("\\label{"+bean.getLabel()+"}");
			}
			write("\\end{center}");
			write("\\end{figure}");
			writeBuffer.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void initOutput(DiagramBean bean) throws IOException {
		outputDir=new File(bean.getOutputDir());
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}else{
			if(!outputDir.isDirectory()){
				throw new IOException("Output dir is not a directory");
			}
		}
		
		outputFile=new File(outputDir.getAbsolutePath()+File.separator+bean.getFileName());
		System.out.println(outputFile.getAbsolutePath());
		writeBuffer=new BufferedOutputStream(new FileOutputStream(outputFile));
	}

	private void writeMethods(CalledMethod r) throws NotFoundException, IOException {
		String callerClass;
		if (r.getParrent() == null) {
			
			
			for ( CalledMethod c : r.getChildren()) {
				writeMethods(c);
				
			}
			
			return;
		} else {
			callerClass = r.getParrent().getContainingClass().getName();
		}

		write("\\begin{call}{" + callerClass.replace(".", "") + "}{" + r.getMethod().getName().replace(".", "")
				+ "}{" + r.getContainingClass().getName().replace(".", "") + "}{"
				+ r.getMethod().getReturnType().getSimpleName() + " "
				+ formatTime(r.getRunTime()) + "}");

		ArrayList<CalledMethod> ch = r.getChildren();
		boolean looping = false;
		for (int i = 0; i < ch.size(); i++) {
			if (i + 1 < ch.size()) {
				CalledMethod next = ch.get(i + 1);
				CalledMethod cur = ch.get(i);
				if (next.getMethod().equals(cur.getMethod())) {
					write("\\begin{sdblock}{Loop/multiple}{}");
					writeMethods(ch.get(i));
					while ((i + 1 < ch.size())) {
						if (!cur.getMethod().equals(ch.get(i + 1).getMethod())) {
							break;
						}
						i++;
					}
					write("\\end{sdblock}");
				} else {
					writeMethods(ch.get(i));
				}
			} else {
				writeMethods(ch.get(i));
			}

		}
		write("\\end{call}");
	}

	private void write(String s) throws IOException {
		
		writeBuffer.write((s+"\n").replace("$", "").getBytes());

	}

	protected  String formatTime(long d) {
		if(d>=3600000){//more then hour
			return df.format(d/3600000.0)+"h";
		}
		if(d>=60000){//more then minute
			return df.format(d/60000.0)+"m";
		}
		if(d>=1000){//more then second
			
			return df.format(d/1000.0)+"s";
		}
		return df.format(d)+"ms";
	
	
	}
}
