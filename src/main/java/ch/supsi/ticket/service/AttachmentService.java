package ch.supsi.ticket.service;

import ch.supsi.ticket.model.Attachment;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    public Optional<Attachment> getFromId(Long id){
        return attachmentRepository.findById(id);
    }

    public void save(Attachment attachment){
        attachmentRepository.save(attachment);
    }

    public boolean update(Attachment attachment, Long id){
        Optional<Attachment> oldAttachment = attachmentRepository.findById(id);
        if(oldAttachment.isPresent()) {
            Attachment newAttachment = oldAttachment.get();
            newAttachment.setName(attachment.getName());
            newAttachment.setData(attachment.getData());
            newAttachment.setSize(attachment.getSize());
            attachmentRepository.save(newAttachment);
            return true;
        }
        return false;
    }

    public void delete(Long id){
        Optional<Attachment> oldAttachment = attachmentRepository.findById(id);
        oldAttachment.ifPresent(attachment -> attachmentRepository.delete(attachment));
    }
}
