package org.example.simple_library.controller;




import org.example.simple_library.entity.Attachment;
import org.example.simple_library.entity.AttachmentContent;
import org.example.simple_library.servic.AttachmentContentService;
import org.example.simple_library.servic.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentContentService attachmentContentService;

    @PostMapping
    public ResponseEntity<Integer> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }


        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        Attachment savedAttachment = attachmentService.save(attachment);

        // Save file content
        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setAttachment(savedAttachment);
        attachmentContent.setContent(file.getBytes());
        attachmentContentService.save(attachmentContent);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttachment.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
        Optional<Attachment> attachmentOptional = attachmentService.findById(id);

        if (attachmentOptional.isPresent()) {
            Attachment attachment = attachmentOptional.get();
            Optional<AttachmentContent> contentOptional = attachmentContentService.findByAttachmentId(attachment.getId());

            if (contentOptional.isPresent()) {
                AttachmentContent content = contentOptional.get();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", attachment.getFileName());

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(content.getContent());
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer id) {
        Optional<Attachment> attachmentOptional = attachmentService.findById(id);

        if (attachmentOptional.isPresent()) {
            attachmentContentService.deleteByAttachmentId(id);
            attachmentService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}