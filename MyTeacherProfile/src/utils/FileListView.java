
package utils;


import java.io.File;

public class FileListView extends ItemListView {
    
    private boolean    isDirectory;
    private final File file;
    
    public FileListView( File file, int iconeRid, boolean isDirectory ) {
    
        super(file.getName(), iconeRid);
        this.file = file;
        this.isDirectory = isDirectory;
    }
    
    public boolean isDirectory() {
    
        return isDirectory;
    }
    
    public void setDirectory( boolean isDirectory ) {
    
        this.isDirectory = isDirectory;
    }
    
    public File getFile() {
    
        return file;
    }
    
}
