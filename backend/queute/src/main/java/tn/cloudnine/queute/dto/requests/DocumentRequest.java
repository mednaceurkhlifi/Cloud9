package tn.cloudnine.queute.dto.requests;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import tn.cloudnine.queute.enums.DocumentType;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class DocumentRequest implements Serializable {
    String document_name;
    DocumentType doc_type;
}