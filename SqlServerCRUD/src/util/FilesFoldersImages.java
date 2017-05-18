package util;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import view.frmCadastro;

/**
 * 
 * @author CAIO SOUZA
 */
public final class FilesFoldersImages { 
    
    private BufferedImage imagem;
    public String lastImageLoaded;
    
    /**
     * 
     * @return caminho do diretorio do Desktop
     */    
    public String getDirectoryDesktop()
    {        
        FileSystemView filesys = FileSystemView.getFileSystemView();
        File desktop = filesys.getHomeDirectory();        
        return desktop.toString();
        // or
        //String desktopPath =System.getProperty("user.home") + "\\"+"Desktop\\";
        //File desktop = new File(desktopPath);        
    }    
    
    /**
     * 
     * @return caminho do diretorio do JAR
     */    
    public String getDirectoryJAR()
    {         
        try {
            //Class directory
            //String path0 = FilesFoldersImages.class.getResource(FilesFoldersImages.class.getSimpleName() + ".class").getPath();
            
            String path = FilesFoldersImages.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");//resolver problemas com espaçoes e caracteres especiais
            File directoryMyJar = new File(decodedPath);
            return directoryMyJar.toString();
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FilesFoldersImages.class.getName()).log(Level.SEVERE, null, ex);
            return null;        
        }        
    }
    
    /**
     *Lê o arquivo
     * @param f arquivo a ser lido
     * @return conteudo do arquivo
     */
    public String readFileToString(File f)
    {
        try {            
            StringBuilder contentBuilder = new StringBuilder();
            FileReader fr = new FileReader(f);            
            try (BufferedReader br = new BufferedReader(fr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    contentBuilder.append(line);
                    contentBuilder.append(System.getProperty("line.separator"));// new line any OS
                }
                System.out.println("TEXTO LIDO: "+f.toString());
            }
            return contentBuilder.toString();
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
    //Overload
    public String readFileToString(String path)
    {
        return readFileToString(new File(path));
    }
    
    /**
     * Cria o arquivo e Escreve ou Sobrescreve o texto dentro de um arquivo em UTF-8
     * @param file arquivo a ser alterado
     * @param text texto unico a ser inserido ou trocado
     * @param Overwrite true =  sobrescrever o texto, false = juntar com texto atual
     */
    public void writeFile(File file, String text, boolean Overwrite)
    {        
        try {//"C:\\Users\\CAIO\\Desktop\\test\\test2.txt"         
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, !Overwrite), "UTF-8"));
            bf.write(text);
            bf.close();
            
            if(Overwrite)
                System.out.println("TEXTO NOVO: "+file.toString());
            else
                System.out.println("TEXTO ADICIONADO: "+file.toString());
        } catch (IOException ex) {
            Logger.getLogger(FilesFoldersImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Overload
    public void writeFile(String pathTextFile, String text, boolean Overwrite)
    {
        writeFile(new File(pathTextFile), text, Overwrite);
    }
    
    /**
     * cria as pastas, subpastas inexistentes e o arquivo
     * @param file
     */
    public void createFolderAndFile(File file)
    {
        try {            
            //cria as pastas definina no caminho se nao haver
            if(file.getParentFile().mkdirs())
                System.out.println("SUBPASTAS CRIADAS ");
            //se o arquivo foi criado
            if(file.createNewFile())
                System.out.println("ARQUIVO CRIADO: "+file.toString());
            else
                System.out.println("ARQUIVO JÁ EXISTENTE: "+file.toString());
        } catch (IOException ex) {
            Logger.getLogger(FilesFoldersImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Overload
    public void createFolderAndFile(String path)
    {
        createFolderAndFile(new File(path));
    }

    /**
     * Cria pasta e Sub-Pastas vazia
     * @param stringPathOrFile string da pasta ou File
     * @param Overwrite TRUE = Se ja houver a ultima sub pasta, irá deletar e criar novamente. FALSE = so criara as que nao existe
     * @return TRUE = se alguma pasta ou subpasta foi criada ou recriada, FALSE = Provavelmente a pasta ja existe
     */
    public void createFolder(Object stringPathOrFile, Boolean Overwrite)
    {      
        File folder = null;
        String path = null;
        try{
            folder = (File) stringPathOrFile;
        }catch(Exception ex){
            path = stringPathOrFile.toString();
            String decodedPath;
            try {  decodedPath = URLDecoder.decode(path, "UTF-8"); }
            catch (UnsupportedEncodingException ex1) { decodedPath = path; }
            
            folder = new File(decodedPath);
        } 
        
        //se  existe, e quiser substituir
        if(folder.exists() && Overwrite)
            deleteFile(folder);        
        if(folder.mkdirs())
            System.out.println("PASTA(S) CRIADA: "+folder.toString());
        else
            System.out.println("PASTAS JÁ EXISTENTE: "+folder.toString());
    }
    
    public String createFolderInDirectoryJAR(String nameFolder)
    {
        File directoryMyJar = new File(getDirectoryJAR(), "\\"+nameFolder+"\\");            
        createFolder(directoryMyJar, false);
        return directoryMyJar.toString();
    }
    
    public void renameFile(File currentFile, File newRenamedFile)
    {
        if(currentFile.exists())
        {
            currentFile.renameTo(newRenamedFile);
            System.out.println("ARQUIVO RENOMEADO: ");
            System.out.println("    DE  : "+currentFile.toString());
            System.out.println("    PARA: "+newRenamedFile.toString());
        }
    }    
    //Overload
    public void renameFile(File currentFile, String newNameFile)
    {        
        String folder = currentFile.toString().substring(0, currentFile.toString().lastIndexOf("\\"));
        File newFile = new File(folder+"\\"+newNameFile);
        renameFile(currentFile, newFile);
    }
    //Overload
    public void renameFile(String urlFile, String newNameFile)
    {
        String folder = urlFile.substring(0, urlFile.lastIndexOf("\\"));
        File oldFileName = new File(urlFile);        
        File newFileName = new File(folder+"\\"+newNameFile);
        renameFile(oldFileName, newFileName);
    } 
        
    /**
     * Recursivo, Deleta o arquivo ou a pasta
     * @param element
     * @param deleteAllSubDirectories deletar todos sub-diretorios e arquivos que contem dentro
     */
    public void deleteFile(File element, boolean deleteAllSubDirectories) {
        if(element.exists()){
            if (element.isDirectory()) {
                if(deleteAllSubDirectories) {
                    for (File sub : element.listFiles()) {
                        deleteFile(sub, true);
                    }
                }
            }
            if(element.delete())
                System.out.println("EXCLUIDO: "+element.toString());
            else
                System.out.println("NÃO EXCLUIDO: "+element.toString());
        }else{
            System.out.println("Arquivo inexistente");
        }
    /*OBS:
        Java não é capaz de excluir pastas com dados nele. 
        Você deve excluir todos os arquivos antes de excluir a pasta.
    */   
    }
    //Overload
    public void deleteFile(File file)
    {
        if(file == null || file.toString().equals(""))
            return;
        deleteFile(file, true);
    }    
    //Overload
    public void deleteFile(String path)
    {        
        if(path == null || path.equals(""))
            return;        
        deleteFile(new File(path), true);
    }
    //Overload
    public void deleteFile(String path, boolean deleteAllSubDirectories)
    {
        if(path == null || path.equals(""))
            return;
        deleteFile(new File(path), deleteAllSubDirectories); 
    }
    
    //Apenas carrega a imagem pelo caminho especificado
    public void loadImageToLabel(JLabel lblImagem, String pathFile)
    {
        File file = new File(pathFile);
        if(file.exists())
        {
            System.out.println("IMAGEM BUSCADA: "+pathFile);
            ImageIcon myImage = new ImageIcon(pathFile); //.getPath, getAbsolutePath ...jLabelImagem1 = new JLabel(new ImageIcon("src//imagem//logo.jpg"));
            Image img = myImage.getImage();
            Image newImg = img.getScaledInstance(lblImagem.getWidth(), lblImagem.getHeight(), Image.SCALE_SMOOTH);
            lblImagem.setIcon(new ImageIcon(newImg));
            
            lastImageLoaded = pathFile;
        }            
    }
    
    //Abre a caixa de dialogo e carrega a imagem selecionada, e envia para label
    /**
     *
     * @param lblImagem
     * @return dialogo confirmado = true , cancelado = false
     */
    public boolean openDialogAndLoadImageSelectedToLabel(JLabel lblImagem){
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));//setando o diretiorio a iniciar
        jfc.setFileFilter(new FileNameExtensionFilter("Images file", ImageIO.getReaderFileSuffixes())); //bmp jpg jpeg wbmp png gif
        jfc.setAcceptAllFileFilterUsed(true);//removo o filtro de Todos Arquivos

        //Abre a caixa de dialogo        
        if(jfc.showOpenDialog(new javax.swing.JPanel()) == JFileChooser.APPROVE_OPTION)
        {
            //pega o arquivo selecionado
            java.io.File f = jfc.getSelectedFile();            
            
            //FORMA 1 DE JOGAR IMAGEM NA LABEL
            try {      
                //Armazena a imagem original na global
                imagem = ImageIO.read(f);
                //Cria uma nova imagem com o tamanho da label 
                Image newImage = imagem.getScaledInstance(lblImagem.getWidth(), lblImagem.getHeight(), Image.SCALE_SMOOTH);                        
                //joga a imagem na label
                lblImagem.setIcon(new ImageIcon(newImage)); 
                System.out.println("IMAGEM CARREGADA: "+f.toString());
                
                lblImagem.setHorizontalAlignment(javax.swing.JLabel.CENTER);
                return true;                
            } catch (IOException ex) {
                Logger.getLogger(frmCadastro.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else
        {
            return false;
        }
    }
    
    //Recebe a imagem original e retorna com novo tamanho escalonado
    private BufferedImage scaleImage(BufferedImage original, int maxWidth, int maxHeight)
    {
        double ratioX = (double)maxWidth / original.getWidth();
        double ratioY = (double)maxHeight / original.getHeight();
        double ratio = Math.min(ratioX, ratioY);

        int newWidth = (int)(original.getWidth() * ratio);
        int newHeight = (int)(original.getHeight() * ratio);        
        
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
        g.dispose();
        return resized;
    } 
    
    //Salva a imagem que está na memoria
    public void saveImage(String URLtoSave)
    {
        if(imagem == null)
            return;
        try {
            File f = new File(URLtoSave);
            BufferedImage bi = scaleImage(imagem, 200, 200);
            ImageIO.write(bi, "jpg", f);
            System.out.println("IMAGEM SALVA: "+f.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
   //Testes (SHIFT + F6 para Executar), este metodo pode ser removido
    public static void main(String args[]) { 
        //new FilesFoldersImages().deleteFile(new File("C:\\Users\\CAIO\\Desktop\\testeJava"), true);
        //FoldersFileWriteRead cp = new FilesFoldersImages(); 
//        cp.exemploTeste2();
        
//        String myFolder = getStringDirectoryJAR() + "\\Imagens";
//        File directory = new File(myFolder);
      
    
    }
    
    //Teste, Exemplo de criaçao e pastas e arquivos sem os metodos desta classe
    private void exemploTeste()
    {        
        try {
            File file = new File("C:\\Users\\CAIO\\Desktop\\TestFolder\\Test.txt");//new FilesFoldersImages().getFileDirectoryDesktop().toString() + "\\TestFolder\\Test.txt";
            if(file.exists() && file.isDirectory())
                if(!file.delete()){
                    System.out.println("existe uma pasta com este nome, e não foi possivel apagar");
                    return;
                }
            //Cria as pastas que faltam para criar o arquivo
            if(file.getParentFile().mkdirs())
                System.out.println("Subpasta(s) criada(s)");                
            //cria o arquivo
            if(file.createNewFile())
                System.out.println("Novo arquivo criado");
            else
                System.out.println("nao criado, arquivo já existente");
//            }
            FileWriter fw = new FileWriter(file, true);//true = Nao sobrescreve o arquivo quando utilizar o metodo write
            try (BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
                bf.write("Linha 1");
                bf.newLine();
                bf.write("Linha 2");
                bf.close();
            }            
            fw.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage()); 
        } finally {
            
        }
    }
    
    //Teste, Exemplo utilizando metodos dessa classe
    private void exemploTeste2()
    {
        FilesFoldersImages cp = new FilesFoldersImages(); 
        String dirDesktop = cp.getDirectoryDesktop();
        File f = new File(dirDesktop, "TestFolder\\Test.txt");
        
        //criando a pasta
        createFolderAndFile(f);
        //escrevendo
        String texto = "Linha1"+System.lineSeparator()+
                       "Linha2";
        writeFile(f, texto, true);
    }
}
