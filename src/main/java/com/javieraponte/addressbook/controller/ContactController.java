package com.javieraponte.addressbook.controller;

import com.javieraponte.addressbook.model.Contact;
import com.javieraponte.addressbook.service.AmazonS3Service;
import com.javieraponte.addressbook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;


@Controller
public class ContactController {

    private ContactService contactService;
    private AmazonS3Service amazonS3Service;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setAmazonS3Service(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @GetMapping("/contacts")
    String listContacts(Model model) {

        model.addAttribute("contacts", contactService.findAllContacts());

        return "list";
    }

    @GetMapping("/contacts/create")
    String createContactForm(Model model) {

        model.addAttribute("contact", new Contact());
        return "create";
    }

    @PostMapping("/contacts/create")
    String createContact(@Valid Contact contact, @RequestParam("profilePictureFile") MultipartFile multipart) {

        String filename = UUID.randomUUID().toString();
        contact.setProfilePicture(filename);
        amazonS3Service.uploadFile(filename, multipart);
        contactService.saveContact(contact);
        return "redirect:/contacts/";
    }

    @GetMapping("/contacts/{id}")
    String editContactForm(Model model, @PathVariable("id") Long id) {

        Contact contact = contactService.findContactById(id).orElse(null);
        model.addAttribute("contact", contact);

        return "edit";
    }

    @PostMapping("/contacts/update/{id}")
    String updateContact(@PathVariable("id") Long id, @Valid Contact contact, @RequestParam("profilePictureFile") MultipartFile multipart) {

        String filename = UUID.randomUUID().toString();
        contact.setProfilePicture(filename);
        amazonS3Service.uploadFile(filename, multipart);
        contactService.saveContact(contact);

        return "redirect:/contacts/";
    }

    @GetMapping("/contacts/{id}/delete")
    String deleteContact(@PathVariable("id") Long id) {
        contactService.deleteContactById(id);

        return "redirect:/contacts/";
    }
}
