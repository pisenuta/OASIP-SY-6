package sit.int221.eventsservice.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.eventsservice.advice.FileStorageException;
import sit.int221.eventsservice.advice.MyFileNotFoundException;
import sit.int221.eventsservice.dtos.File.FileStorageProperties;
import sit.int221.eventsservice.entities.Event;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public List<String> listFileName(Integer eventId) {
        File folder = new File(fileStorageLocation.resolve(String.valueOf(eventId)).toUri());
        List<File> listOfFiles = List.of(folder.listFiles());
        List<String> listOfFilenames = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                listOfFilenames.add(file.getName());
                System.out.println(listOfFilenames);
            }
        }
        return listOfFilenames;
    }

    public String storeFile(MultipartFile file, Event event) {
//        String userDir = event.getUser() != null ? event.getUser().getUserId().toString() : "Guest";
        String eventDir = event.getId().toString();
        if (file == null) {
            try {
                Path fileDir = this.fileStorageLocation.resolve(eventDir);
                Path targetLocation = Files.createDirectories(fileDir);
            } catch (Exception ex) {
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }
        if (file != null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            System.out.println("fileName: " + fileName);
            System.out.println("eventDir: " + eventDir);

            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Path fileDir = this.fileStorageLocation.resolve(eventDir);
                System.out.println("fileDir: " + fileDir.toString());
                Path targetLocation = Files.createDirectories(fileDir).resolve(fileName);
                System.out.println("targetLocation: " + targetLocation.toString());
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return eventDir;
    }

    public Path load(Integer eventId, String filename) {
        return fileStorageLocation.resolve(String.valueOf(eventId)).resolve(filename);
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(filePath.toUri());
            System.out.println(resource);
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + "eventNo" + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + "eventNo" + fileName, ex);
        }
    }

    public Resource loadAsResource(Integer eventId, String filename) {
        try {
            Path file = load(eventId, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new MyFileNotFoundException("Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new MyFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.delete(filePath);
        } catch (IOException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public void deleteDir(String dir) {
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException ex) {
            throw new MyFileNotFoundException("Dir not found " + dir, ex);
        }
    }
}
