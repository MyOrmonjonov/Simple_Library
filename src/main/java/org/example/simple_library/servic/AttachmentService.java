package org.example.simple_library.servic;

import org.example.simple_library.entity.Attachment;
import org.example.simple_library.repo.AttachmentRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public Attachment save(Attachment attachment) {
      return attachmentRepository.save(attachment);
    }

    public Optional<Attachment> findById(Integer id) {
      return   attachmentRepository.findById(id);
    }

    public void deleteById(Integer id) {
        attachmentRepository.deleteById(id);
    }
}
