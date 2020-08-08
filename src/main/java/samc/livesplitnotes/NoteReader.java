package samc.livesplitnotes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteReader {

	List<String> notes = new ArrayList<String>();
	int currentNote = 0;
	private boolean isInitialised = false;

	public NoteReader() {
		
	}
	
	public NoteReader(String notes) {
		this.loadNotes(notes, "\r\n");
	}
	
	public NoteReader(String notes, String splitOn) {
		this.loadNotes(notes, splitOn);
	}
	
	public void loadNotes(String notes, String splitOn) {

		try {
			List<String> contents = Files.readAllLines(Paths.get(notes));
			
			StringBuilder builder = new StringBuilder("");
			
			contents.stream().forEach(line -> {
				
				if (line.equals(splitOn)) {
					this.notes.add(builder.toString());
					builder.setLength(0);
				} else {
					builder.append(line + "\r\n");
				}
			});
			
			this.isInitialised = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void next() {
		this.currentNote++;
		
		if (this.currentNote > this.notes.size()) {
			this.currentNote = this.notes.size() - 1;
		}
		
	}
	
	public void previous() {
		this.currentNote--;
		if (this.currentNote < 0) {
			this.currentNote = 0;
		}
	}
	
	public String getCurrentNote() {
		return this.notes.get(currentNote);
	}
	
	public void setNote(int i) {
		//Ensure that the notes can't go beyond the size
		this.currentNote = i < this.notes.size() - 1 ? i : this.notes.size() - 1;
	}
	
	public boolean isInitialised() {
		return isInitialised;
	}

	public void setInitialised(boolean isInitialised) {
		this.isInitialised = isInitialised;
	}
	
	
}
