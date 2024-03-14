package es.princast.gepep.web.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.princast.gepep.service.FileStorageService;
import es.princast.gepep.web.rest.errors.FileStorageException;
import es.princast.gepep.web.rest.response.UploadFileResponse;

@RestController
@RequestMapping("/api/fileStorage")
public class FileStorageController {

	private static final String FOLDER_INFORMACION = "informacion";
	private static final String FOLDER_REPORTS_IMG = "reports/img";
	private static final String RESULT_OK = "OK";
	private static final String RESULT_KO = "KO";

	private static final Logger logger = LoggerFactory.getLogger(FileStorageController.class);

	@Autowired
	private FileStorageService fileStorageService;
	
	
	@Autowired
	private FileStorageService fileStorageServiceInformacion;
	

	@PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
		String resultado = RESULT_OK;
		String fileName = file.getName();
		String fileDownloadUri = "";
		if(folder==null) {
			folder = ".";
		}
		try {
			fileName = fileStorageService.storeFile(file, folder);
			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/fileStorage/downloadFile/").path(fileName).toUriString();

		} catch (FileStorageException e) {
			resultado = RESULT_KO;
		}

		return new UploadFileResponse(resultado, fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping(path = "/uploadFileDeleteOld", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder, @RequestParam("oldFileName") String oldFileName, @RequestParam("oldFolder") String oldFolder) {
		
		if(!oldFolder.isEmpty() && !oldFileName.isEmpty()) {
			fileStorageService.deleteFile(oldFolder, oldFileName);
		}
		
		String resultado = RESULT_OK;
		String fileName = file.getName();
		String fileDownloadUri = "";
		if(folder==null) {
			folder = ".";
		}
		try {
			fileName = fileStorageService.storeFile(file, folder);
			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/fileStorage/downloadFile/").path(fileName).toUriString();

		} catch (FileStorageException e) {
			resultado = RESULT_KO;
		}

		return new UploadFileResponse(resultado, fileName, fileDownloadUri, file.getContentType(), file.getSize());	}

	@PostMapping(path = "/uploadMultipleFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("folder") String folder) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file, folder)).collect(Collectors.toList());
	}

	@GetMapping(path = "/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		Resource resource = null;
		try {
			resource = fileStorageService.loadFileAsResource(fileName, ".");
		}catch(FileStorageException e) {
			return ResponseEntity.notFound().build();
		}
		
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	
	
	@GetMapping(path = "/openFile/{fileName:.+}")
	public ResponseEntity<Resource> openFile(@PathVariable String fileName, HttpServletRequest request) {
		return openFile(fileName, request, null);
	}
	
	
	@GetMapping(path = "/openFile/{folder}/{fileName:.+}")
	public ResponseEntity<Resource> openFileFolder(@PathVariable String folder, @PathVariable String fileName, HttpServletRequest request) {
		return openFile(fileName, request, folder);
	}
	
	
	@GetMapping(path = "/openFileReportsImg/{fileName:.+}")
	public ResponseEntity<Resource> openFileReportsImg(@PathVariable String fileName, HttpServletRequest request) {
		return openFile(fileName, request, FOLDER_REPORTS_IMG);
	}

	private ResponseEntity<Resource> openFile(String fileName, HttpServletRequest request, String folder) {
		Resource resource = null;
		try {
			resource = fileStorageServiceInformacion.loadFileAsResource(fileName, folder);
		}catch(FileStorageException e) {
			return ResponseEntity.notFound().build();
		}
		
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.LINK, "filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	
}
