package tn.cloudnine.queute.dto.workspace.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.enums.DocumentType;

@Getter
@Setter
@Builder
public class DocumentRequest {
    String document_name;
    DocumentType doc_type;
    MultipartFile document;
}
