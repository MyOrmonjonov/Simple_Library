package org.example.simple_library.servic;

import org.example.simple_library.entity.AttachmentContent;
import org.example.simple_library.repo.AttachmentContentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttachmentContentService {
    private final AttachmentContentRepository attachmentContentRepository;

    public AttachmentContentService(AttachmentContentRepository attachmentContentRepository) {
        this.attachmentContentRepository = attachmentContentRepository;
    }

    public void save(AttachmentContent attachmentContent) {
        attachmentContentRepository.save(attachmentContent);
    }

    public Optional<AttachmentContent> findByAttachmentId(Integer id) {
        return attachmentContentRepository.findById(id);
    }

    public void deleteByAttachmentId(Integer id) {
        attachmentContentRepository.deleteById(id);
    }
}
