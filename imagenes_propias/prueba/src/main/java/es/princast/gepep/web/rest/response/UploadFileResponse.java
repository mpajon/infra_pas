package es.princast.gepep.web.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Data

public class UploadFileResponse {
	
	private String result;
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    

}
