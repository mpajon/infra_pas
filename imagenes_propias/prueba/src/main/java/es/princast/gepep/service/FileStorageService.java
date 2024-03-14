package es.princast.gepep.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import es.princast.gepep.web.rest.errors.FileStorageException;

@Service
public class FileStorageService {

	private static final String FOLDER_SEPARATOR = "/";
	private String location;

	public FileStorageService(String location) {

		this.location = location;

		createPath(location);
	}

	private Path createPath(String path) {
		Path fileStoragePath = Paths.get(path).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStoragePath);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
		
		return fileStoragePath;
	}
	
	
	public String storeFile(MultipartFile file, String folder) throws FileStorageException {

		Path fileStorageLocation = createPath(new StringBuilder(location).append(FOLDER_SEPARATOR).append(folder).toString());
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {

			if (fileName.contains("..")) {
				throw new FileStorageException("Filename contains invalid path sequence " + fileName);
			}

			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName, String folder) {
		Path fileStorageLocation = createPath(new StringBuilder(location).append(FOLDER_SEPARATOR).append((folder==null?"":folder)).toString());
		try {
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileStorageException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileStorageException("File not found " + fileName, ex);
		}
	}

	public BufferedImage loadFileAsBufferedImage(String fileName, String folder) throws FileStorageException {
		Path fileStorageLocation = createPath(new StringBuilder(location).append(FOLDER_SEPARATOR).append(folder).toString());
		try {
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return ImageIO.read(resource.getInputStream());
			} else {
				throw new FileStorageException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileStorageException("File not found " + fileName, ex);
		} catch (IOException e) {
			throw new FileStorageException("File not found " + fileName, e);
		}
	}

	public void deleteAll(String folder) {
		Path fileStorageLocation = createPath(new StringBuilder(location).append(FOLDER_SEPARATOR).append(folder).toString());
		FileSystemUtils.deleteRecursively(fileStorageLocation.toFile());
	}

	
	public void deleteFile(String folder, String fileName) {
		if(!fileName.isEmpty()) {
			Path fileStorageLocation = Paths.get(new StringBuilder(location).append(FOLDER_SEPARATOR).append(folder).append(FOLDER_SEPARATOR).append(fileName).toString());
			FileSystemUtils.deleteRecursively(fileStorageLocation.toFile());
		}
	}
	
	
}
